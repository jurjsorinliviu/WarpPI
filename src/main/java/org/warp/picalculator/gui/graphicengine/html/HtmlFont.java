package org.warp.picalculator.gui.graphicengine.html;

import java.io.IOException;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.cpu.CPUFont;

public class HtmlFont extends CPUFont {

	public HtmlFont(String fontName) throws IOException {
		super(fontName);
	}

	public HtmlFont(String path, String fontName) throws IOException {
		super(path, fontName);
	}

	@Override
	public void use(GraphicEngine d) {
		if (d.getRenderer() instanceof HtmlRenderer) {
			((HtmlRenderer) d.getRenderer()).f = this;
		}
	}

}
