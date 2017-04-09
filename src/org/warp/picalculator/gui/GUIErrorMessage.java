package org.warp.picalculator.gui;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.Error;

public class GUIErrorMessage {

	private String err;
	private long creationTime;

	public GUIErrorMessage(Error e) {
		this.err = e.getLocalizedMessage();
		this.creationTime = System.currentTimeMillis();
	}
	
	public GUIErrorMessage(Exception ex) {
		err = ex.getLocalizedMessage();
		this.creationTime = System.currentTimeMillis();
	}
	
	public void draw(GraphicEngine g, Renderer r, String msg) {
		int scrW = g.getWidth();
		int scrH = g.getHeight();
		int width = 200;
		int height = 20;
		int margin = 4;
		r.glClearSkin();
		r.glColor(0x00000000);
		r.glFillRect(scrW-width-margin, scrH-height-margin, width, height, 0, 0, 0, 0);
	}
	
	public long getCreationTime() {
		return creationTime;
	}
}
