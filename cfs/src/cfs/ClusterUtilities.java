package cfs;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public final class ClusterUtilities {

	public static Iterable<Path> getRootDirectories() {
		return Arrays.stream(File.listRoots())
				.map(file -> file.toPath())
				.collect(toList());
	}
}
