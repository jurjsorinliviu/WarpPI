package org.warp.picalculator.gui.graphicengine.headless8;

import java.io.IOException;

import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;

public class Headless8Font implements BinaryFont {

	@Override
	public void load(String file) throws IOException {
		
	}

	@Override
	public void initialize(GraphicEngine d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use(GraphicEngine d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStringWidth(String text) {
		return 5*text.length();
	}

	@Override
	public int getCharacterWidth() {
		return 5;
	}

	@Override
	public int getCharacterHeight() {
		return 5;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

}
