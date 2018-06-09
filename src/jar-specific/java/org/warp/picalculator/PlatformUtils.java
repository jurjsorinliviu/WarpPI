package org.warp.picalculator;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class PlatformUtils {
	public static final boolean isJavascript = false;
	public static String osName = System.getProperty("os.name").toLowerCase();
	
	public static void setThreadName(Thread t, String string) {
		t.setName(string);
	}

	public static void setDaemon(Thread kt) {
		kt.setDaemon(true);
	}

	public static void setDaemon(Thread kt, boolean val) {
		kt.setDaemon(val);
	}

	public static void throwNewExceptionInInitializerError(String string) {
		throw new ExceptionInInitializerError(string);
	}

	public static String[] stacktraceToString(Error e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
	}
}
