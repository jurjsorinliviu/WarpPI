package org.warp.picalculator.deps;

import java.io.File;
import java.nio.file.Path;

public class StorageUtils {
	public static final boolean exists(Path f) {
		return f.toFile().exists();
	}

	public static Path get(String path) {
		return new File(path).toPath();
	}
}
