package org.warp.picalculator.deps;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;

public class DEngine {
	public static GraphicEngine newGPUEngine() {
		try {
			return new org.warp.picalculator.gui.graphicengine.gpu.GPUEngine();
		} catch (NullPointerException ex) {
			return null;
		}
	}
	public static GraphicEngine newHeadless24bitEngine() {
		return null;
	}
	public static GraphicEngine newHeadless256Engine() {
		return null;
	}
	public static GraphicEngine newHeadless8Engine() {
		return null;
	}
	public static GraphicEngine newCPUEngine() {
		return null;
	}
	public static GraphicEngine newFBEngine() {
		return null;
	}
	public static GraphicEngine newHtmlEngine() {
		return null;
	}
}
