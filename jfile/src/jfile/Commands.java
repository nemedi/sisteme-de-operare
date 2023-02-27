package jfile;

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
	
	public static void pwd() {
		out.println(root.toFile().getAbsolutePath());
	}
	
	public static void cd(String name) {
		if (Paths.get(name).isAbsolute()) {
			root = Paths.get(name);
		} else {
			root = Paths.get(root.toFile().getAbsolutePath(), name).normalize();
		}
	}
	
	public static void ls(String name) {
		Path path = resolver.apply(name);
		if (path.toFile().isDirectory()) {
			Stream.of(path.toFile().list()).forEach(out::println);
		} else {
			out.println("Folder not found: " + name);
		}
	}
	
	public static void ls() {
		ls(".");
	}
	
	public static void mkdir(String name) throws IOException {
		Path path = resolver.apply(name);
		if (!path.toFile().exists()) {
			if (!path.getParent().toFile().isDirectory()) {
				path.getParent().toFile().mkdirs();
			}
			Files.createDirectory(path);
		} else {
			out.println("Folder already exists: " + name);
		}
	}
	
	public static void rmdir(String name) {
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
		}
	}
	
	public static void touch(String name) throws IOException {
		Path path = resolver.apply(name);
		if (!path.toFile().exists()) {
			Files.createFile(path);
		} else {
			out.println("File or folder already exists: " + name);
		}
	}
	
	public static void echo(String...arguments) throws IOException {
		if (arguments.length > 2
				&& (">".equals(arguments[arguments.length - 2])
					|| ">>".equals(arguments[arguments.length - 2]))) {
			Path path = resolver.apply(arguments[arguments.length - 1]);
			String text = Stream.of(Arrays.copyOfRange(arguments, 0, arguments.length - 2))
				.collect(joining(" ")) + System.getProperty("line.separator");
			Files.writeString(path, text,
					">>".equals(arguments[arguments.length - 2])
					? StandardOpenOption.APPEND
					: StandardOpenOption.CREATE);
		}
	}
	
	public static void cat(String name) throws IOException {
		Path path = resolver.apply(name);
		if (path.toFile().isFile()) {
			Files.readAllLines(path).forEach(out::println);
		} else {
			out.println("File not found: " + name);
		}
	}
	
	public static void cp(String source, String target) throws IOException {
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
		}
	}
	
	public static void mv(String source, String target) {
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
			}
		} else {
			out.println("File or folder not found: " + source);
		}
	}
	
	public static void rm(String name) {
		Path path = resolver.apply(name);
		if (path.toFile().exists()) {
			if (path.toFile().isDirectory()
					&& path.toFile().list().length > 0) {
				out.println("Folder cannot be deleted because it's not empty (use rmdir instead): " + name);
			} else {
				path.toFile().delete();
			}
		} else {
			out.println("File not found: " + name);
		}
	}
	
	public static void wc(String...arguments) throws IOException {
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
					}
				}
			} else {
				out.println("File not found: " + arguments[arguments.length - 1]);
			}
		}
	}
	
	public static void head(String...arguments) throws IOException {
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
			}
		}
	}
	
	public static void tail(String...arguments) throws IOException {
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
			}
		}
	}
	
}
