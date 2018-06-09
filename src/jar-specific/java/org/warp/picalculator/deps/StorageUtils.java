package org.warp.picalculator.deps;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageUtils {
	public static final boolean exists(Path f) {
		return Files.exists(f);
	}

	public static Path get(String path) {
		return Paths.get(path);
	}
}
