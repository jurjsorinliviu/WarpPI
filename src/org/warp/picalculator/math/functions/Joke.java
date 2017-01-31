package org.warp.picalculator.math.functions;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.RAWFont;
import org.warp.picalculator.gui.graphicengine.cpu.CPUDisplay;
import org.warp.picalculator.math.Calculator;

public class Joke implements Function {

	public static final byte FISH = 0;
	public static final byte TORNADO = 1;
	public static final byte SHARKNADO = 2;
	private static final String[] jokes = new String[] { "♓", "TORNADO", "SHARKNADO" };
	private static final int[] jokesFont = new int[] { 4, -1, -1 };
	private final byte joke;
	private final Calculator root;

	public Joke(Calculator root, byte joke) {
		this.root = root;
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
		final RAWFont rf = DisplayManager.renderer.getCurrentFont();
		if (jokesFont[joke] >= 0) {
			DisplayManager.renderer.glSetFont(DisplayManager.fonts[jokesFont[joke]]);
		}
		DisplayManager.renderer.glDrawStringLeft(x, y, jokes[joke]);
		if (jokesFont[joke] >= 0) {
			DisplayManager.renderer.glSetFont(rf);
		}
	}

	@Override
	public int getWidth() {
		if (jokesFont[joke] >= 0) {
			return DisplayManager.renderer.glGetStringWidth(DisplayManager.fonts[jokesFont[joke]], jokes[joke]);
		} else {
			return DisplayManager.renderer.glGetStringWidth(Utils.getFont(small), jokes[joke]);
		}
	}

	@Override
	public int getHeight() {
		if (jokesFont[joke] >= 0) {
			return DisplayManager.fonts[jokesFont[joke]].charH;
		} else {
			return Utils.getFont(small).charH;
		}
	}

	@Override
	public int getLine() {
		return getHeight() / 2;
	}

	@Override
	public Calculator getRoot() {
		return root;
	}

	private boolean small = false;

	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

}
