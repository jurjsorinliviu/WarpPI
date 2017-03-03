package org.warp.picalculator.math.functions;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;

public class Joke implements Function {

	public static final byte FISH = 0;
	public static final byte TORNADO = 1;
	public static final byte SHARKNADO = 2;
	private static final String[] jokes = new String[] { "♓", "TORNADO", "SHARKNADO" };
	private static final int[] jokesFont = new int[] { 4, -1, -1 };
	private final byte joke;
	private final MathContext root;

	public Joke(MathContext root, byte joke) {
		this.root = root;
		this.joke = joke;
	}

	@Override
	public ObjectArrayList<Function> simplify() throws Error {
		return null;
	}

	@Override
	public boolean isSimplified() {
		return true;
	}

	@Override
	public MathContext getMathContext() {
		return root;
	}

	@Override
	public Function clone() {
		return new Joke(root, joke);
	}

	@Override
	public Function setParameter(int index, Function var) throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Function getParameter(int index) throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}

}
