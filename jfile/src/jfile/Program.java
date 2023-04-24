package jfile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class Program {
	
	public static void main(String[] args) {
		int exit = 0;
		Commands.prompter.apply(">");
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				if (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					line = line.replace("$?", Integer.toString(exit));
					String[] segments = line.split("\\s+");
					if ("exit".equals(segments[0])) {
						break;
					}
					Optional<Method> method = Stream.of(Commands.class.getDeclaredMethods())
							.filter(m -> segments[0].equals(m.getName())
									&& Modifier.isStatic(m.getModifiers())
									&& (m.getParameterCount() == segments.length - 1
									|| m.getParameterCount() == 1 && m.getParameterTypes()[0].isArray()))
							.findAny();
					if (method.isPresent()) {
						try {
							Object[] arguments = Arrays.copyOfRange(segments, 1, segments.length);
							if (method.get().getParameterCount() == 1
									&& method.get().getParameterTypes()[0].isArray()) {
								arguments = new Object[] {arguments};
							}
							exit = (int) method.get().invoke(null, arguments);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Commands.out.println("Unknown command: " + segments[0]);
					}
					Commands.prompter.apply(">");
				}
			}
		}
	}

}
