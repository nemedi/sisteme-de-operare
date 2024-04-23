import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.lang.reflect.Modifier;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summarizingLong;

public class Commands {
	
	private static Path root = Paths.get(".").normalize();
	
	public static final PrintStream out = System.out;
	
	public static final Function<String, Void> prompter = delimiter -> {
		out.print(root.toFile().getAbsolutePath() + delimiter);
		return null;
	};
	
	private static final Function<String, Path> resolver = name -> {
		Path path = Paths.get(name);
		if (!path.isAbsolute()) {
			path = Paths.get(root.toFile().getAbsolutePath(), name);
		}
		return path;
	};
	
	public static int pwd() {
		int exit = 0;
		out.println(root.toFile().getAbsolutePath());
		return exit;
	}
	
	public static int cd(String name) {
		int exit = 0;
		if (Paths.get(name).isAbsolute()) {
			Path path = Paths.get(name);
			if (path.toFile().exists()) {
				root = path;
			} else {
				out.println("Folder not found: " + name);
				exit = 1;
			}
		} else {
			Path path = Paths.get(root.toFile().getAbsolutePath(), name).normalize();
			if (path.toFile().exists()) {
				root = path;
			} else {
				out.println("Folder not found: " + name);
				exit = 1;
			}
		}
		return exit;
	}
	
	public static int ls(String name) {
		int exit = 0;
		Path path = resolver.apply(name);
		if (path.toFile().isDirectory()) {
			Stream.of(path.toFile().list()).forEach(out::println);
		} else {
			out.println("Folder not found: " + name);
			exit = 1;
		}
		return exit;
	}
	
	public static int ls() {
		return ls(".");
	}
	
	public static int mkdir(String name) throws IOException {
		int exit = 0;
		Path path = resolver.apply(name);
		if (!path.toFile().exists()) {
			if (!path.getParent().toFile().isDirectory()) {
				path.getParent().toFile().mkdirs();
			}
			Files.createDirectory(path);
		} else {
			out.println("Folder already exists: " + name);
			exit = 1;
		}
		return exit;
	}
	
	public static int rmdir(String name) {
		int exit = 0;
		Path path = resolver.apply(name);
		if (path.toFile().isDirectory()) {
			Stream.of(path.toFile().listFiles())
				.forEach(f -> {
					if (f.isDirectory()) {
						rmdir(f.getAbsolutePath());
					} else {
						f.delete();
					}
				});
			path.toFile().delete();
		} else {
			out.println("Folder not found: " + name);
			exit = 1;
		}
		return exit;
	}
	
	public static int touch(String name) throws IOException {
		int exit = 0;
		Path path = resolver.apply(name);
		if (!path.toFile().exists()) {
			Files.createFile(path);
		} else {
			out.println("File or folder already exists: " + name);
			exit = 2;
		}
		return exit;
	}
	
	public static int ln(String...arguments) throws IOException {
		int exit = 0;
		if (arguments.length == 2) {
			Path source = resolver.apply(arguments[0]);
			if (source.toFile().exists()) {
				Path target = resolver.apply(arguments[1]);
				Files.createLink(target, source);
			} else {
				out.println("File or folder not found: " + source);
				exit = 1;
			}
		} else if (arguments.length == 2 && "-s".equals(arguments[0])) {
			Path source = resolver.apply(arguments[1]);
			if (source.toFile().exists()) {
				Path target = resolver.apply(arguments[2]);
				Files.createSymbolicLink(target, source);
			} else {
				out.println("File or folder not found: " + source);
				exit = 1;
			}
		} else {
			exit = 2;
		}
		return exit;
	}
	
	public static int echo(String...arguments) throws IOException {
		int exit = 0;
		if (arguments.length > 2) {
			if(">".equals(arguments[arguments.length - 2])
				|| ">>".equals(arguments[arguments.length - 2])) {
			Path path = resolver.apply(arguments[arguments.length - 1]);
			String text = Stream.of(Arrays.copyOfRange(arguments, 0, arguments.length - 2))
					.map(s -> s.startsWith("\"")
						? s.substring(1)
						: (s.endsWith("\"")
							? s.substring(0, s.length() - 1)
							: s))
				.collect(joining(" ")) + System.getProperty("line.separator");
			Files.writeString(path, text,
					">>".equals(arguments[arguments.length - 2])
					? StandardOpenOption.APPEND
					: StandardOpenOption.CREATE);
			} else {
				Stream.of(arguments).forEach(argument -> out.print(argument + " "));
				out.println();
			}
		} else {
			out.println(arguments[0]);
		}
		return exit;
	}
	
	public static int cat(String name) throws IOException {
		int exit = 0;
		Path path = resolver.apply(name);
		if (path.toFile().isFile()) {
			Files.readAllLines(path).forEach(out::println);
		} else {
			out.println("File not found: " + name);
			exit = 1;
		}
		return exit;
	}
	
	public static int cp(String source, String target) throws IOException {
		int exit = 0;
		Path sourcePath = resolver.apply(source);
		if (sourcePath.toFile().exists()) {
			Path targetPath = resolver.apply(target);
			if (targetPath.toFile().isDirectory()) {
				targetPath = Paths.get(targetPath.toFile().getAbsolutePath(), sourcePath.toFile().getName());
			}
			if (sourcePath.toFile().isFile()) {
				Files.write(targetPath, Files.readAllBytes(sourcePath), StandardOpenOption.CREATE_NEW);
			} else {
				if (!targetPath.toFile().isDirectory()) {
					targetPath.toFile().mkdirs();
				}
				final String baseTarget = targetPath.toFile().getAbsolutePath();
				Stream.of(sourcePath.toFile().listFiles())
					.forEach(f -> {
							try {
								cp(f.getAbsolutePath(),
										Paths.get(baseTarget, f.getName()).toFile().getAbsolutePath());
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
			}
		} else {
			out.println("File or folder not found: " + source);
			exit = 1;
		}
		return exit;
	}
	
	public static int mv(String source, String target) {
		int exit = 0;
		Path sourcePath = resolver.apply(source);
		if (sourcePath.toFile().exists()) {
			Path targetPath = resolver.apply(target);
			if (targetPath.toFile().isDirectory()) {
				targetPath = Paths.get(targetPath.toFile().getAbsolutePath(),
						sourcePath.toFile().getName());
			}
			if (!targetPath.toFile().exists()) {
				sourcePath.toFile().renameTo(targetPath.toFile());
			} else {
				out.print("File or folder already exists: " + target);
				exit = 1;
			}
		} else {
			out.println("File or folder not found: " + source);
			exit = 2;
		}
		return exit;
	}
	
	public static int rm(String name) {
		int exit = 0;
		Path path = resolver.apply(name);
		if (path.toFile().exists()) {
			if (path.toFile().isDirectory()
					&& path.toFile().list().length > 0) {
				out.println("Folder cannot be deleted because it's not empty (use rmdir instead): " + name);
				exit = 2;
			} else {
				path.toFile().delete();
			}
		} else {
			out.println("File not found: " + name);
			exit = 1;
		}
		return exit;
	}
	
	public static int wc(String...arguments) throws IOException {
		int exit = 0;
		if (arguments.length > 0) {
			Path path = resolver.apply(arguments[arguments.length - 1]);
			if (path.toFile().isFile()) {
				if (arguments.length == 1) {
					long characters = path.toFile().length();
					long lines = Files.readAllLines(path).size();
					long words = Files.readAllLines(path)
							.stream()
							.map(l -> l.split("\\s+").length)
							.collect(summarizingLong(c -> c))
							.getSum();
					out.println(characters + " " + lines + " " + words);
				} else {
					switch (arguments[0]) {
					case "-c":
						long characters = path.toFile().length();
						out.println(characters);
						break;
					case "-l":
						long lines = Files.readAllLines(path).size();
						out.println(lines);
						break;
					case "-w":
						long words = Files.readAllLines(path)
							.stream()
							.map(l -> l.split("\\s+").length)
							.collect(summarizingLong(c -> c))
							.getSum();
						out.println(words);
						break;
					default:
						out.println("Unknown option: " + arguments[0]);
						exit = 2;
					}
				}
			} else {
				out.println("File not found: " + arguments[arguments.length - 1]);
				exit = 1;
			}
		}
		return exit;
	}
	
	public static int head(String...arguments) throws IOException {
		int exit = 0;
		if (arguments.length == 2) {
			Path path = resolver.apply(arguments[arguments.length - 1]);
			if (path.toFile().isFile()) {
				int limit = Integer.parseInt(arguments[0].substring(1));
				Files.readAllLines(path)
					.stream()
					.limit(limit)
					.forEach(out::println);
			} else {
				out.println("File not found: " + arguments[arguments.length - 1]);
				exit = 1;
			}
		} else {
			exit = 2;
		}
		return exit;
	}
	
	public static int tail(String...arguments) throws IOException {
		int exit = 0;
		if (arguments.length == 2) {
			Path path = resolver.apply(arguments[arguments.length - 1]);
			if (path.toFile().isFile()) {
				int limit = Integer.parseInt(arguments[0].substring(1));
				List<String> lines = Files.readAllLines(path);
				long skip = lines.size() - limit;
				if (skip < 0) {
					skip = 0;
				}
				lines.stream()
					.skip(skip)
					.forEach(out::println);
			} else {
				out.println("File not found: " + arguments[arguments.length - 1]);
				exit = 1;
			}
		} else {
			exit = 2;
		}
		return exit;
	}
	
	public static int test(String...arguments) {
		int exit = 0;
		if (arguments.length == 2) {
			final Path path = resolver.apply(arguments[1]);
			switch (arguments[0]) {
			case "-f":
				exit = path.toFile().isFile() ? 0 : 1;
				break;
			case "-d":
				exit = path.toFile().isDirectory() ? 0 : 1;
				break;
			case "-r":
				exit = Files.isReadable(path) ? 0 : 1;
				break;
			case "-w":
				exit = Files.isWritable(path) ? 0 : 1;
				break;
			case "-x":
				exit = Files.isExecutable(path) ? 0 : 1;
				break;
			default:
				out.println("Unsupported option: " + arguments[0]);
				exit = 3;
			}
		} else {
			exit = 3;
		}
		return exit;
	}

	public static int help() {
		out.println("JFile version 1.0 by Iulian ILIE-NEMEDI");
		out.println("Available commands:");
		Stream.of(Commands.class.getDeclaredMethods())
				.filter(m -> Modifier.isStatic(m.getModifiers()) && !m.getName().contains("$"))
				.map(m -> m.getName())
				.sorted((first, second) -> first.compareTo(second))
				.forEach(out::println);
		return 0;
	}
	
}
