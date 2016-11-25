package org.warp.picalculator.math.functions;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.device.graphicengine.RAWFont;

public class Joke implements Function {

	public static final byte FISH = 0;
	public static final byte TORNADO = 1;
	public static final byte SHARKNADO = 2;
	private static final String[] jokes =  new String[]{"♓", "TORNADO", "SHARKNADO"};
	private static final int[] jokesFont =  new int[]{4, -1, -1};
	private final byte joke;
	
	public Joke(byte joke) {
		this.joke = joke;
	}
	
	@Override
	public String getSymbol() {
		return "joke";
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		return null;
	}

	@Override
	public boolean isSolved() {
		return true;
	}

	@Override
	public void generateGraphics() {
		
	}

	@Override
	public void draw(int x, int y) {
		RAWFont rf = Display.Render.currentFont;
		if (jokesFont[joke] >= 0) {
			Display.Render.glSetFont(PIDisplay.fonts[jokesFont[joke]]);
		}
		Display.Render.glDrawStringLeft(x, y, jokes[joke]);
		if (jokesFont[joke] >= 0) {
			Display.Render.glSetFont(rf);
		}
	}

	@Override
	public int getWidth() {
		if (jokesFont[joke] >= 0) {
			return Display.Render.glGetStringWidth(PIDisplay.fonts[jokesFont[joke]], jokes[joke]);
		} else {
			return Display.Render.glGetStringWidth(Utils.getFont(small), jokes[joke]);
		}
	}

	@Override
	public int getHeight() {
		if (jokesFont[joke] >= 0) {
			return PIDisplay.fonts[jokesFont[joke]].charH;
		} else {
			return Utils.getFont(small).charH;
		}
	}

	@Override
	public int getLine() {
		return getHeight()/2;
	}
	
	@Override
	public Function setParent(Function value) {
		return this;
	}

	@Override
	public Function getParent() {
		return null;
	}

	private boolean small = false;
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

}
