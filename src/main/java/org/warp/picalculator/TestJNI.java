package org.warp.picalculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.warp.picalculator.MmapByteBuffer;


public class TestJNI {
	public TestJNI() {
		
	}
	
	static {
		System.load("/boot/libpicalc.so");
	}

	private native MmapByteBuffer getDisplayBuffer();

	private native void disposeDisplayBuffer();

	public MmapByteBuffer retrieveBuffer() {
		return getDisplayBuffer();
	}
	
	public void deleteBuffer() {
		disposeDisplayBuffer();
	}
}