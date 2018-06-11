package org.warp.picalculator.gui.graphicengine.html;

import java.io.IOException;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.cpu.CPUSkin;

public class HtmlSkin extends CPUSkin {

	public HtmlSkin(String file) throws IOException {
		super(file);
	}

	@Override
	public void use(GraphicEngine d) {
		if (d instanceof HtmlEngine) {
			((HtmlEngine) d).getRenderer().currentSkin = this;
		}
	}
}
