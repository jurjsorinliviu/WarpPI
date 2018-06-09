package org.warp.picalculator;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.warp.picalculator.gui.expression.blocks.Block;

public final class PlatformUtils {
	public static final boolean isJavascript = true;
	public static String osName = "JavaScript";
	
	public static void setThreadName(Thread t, String string) {
	}

	public static void setDaemon(Thread kt) {
	}

	public static void setDaemon(Thread kt, boolean val) {
	}

	public static void throwNewExceptionInInitializerError(String string) {
		throw new NullPointerException(string);
	}

	public static String[] stacktraceToString(Error e) {
		return e.getMessage().toUpperCase().replace("\r", "").split("\n");
	}
}
