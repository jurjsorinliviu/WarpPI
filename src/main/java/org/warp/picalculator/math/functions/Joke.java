package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Joke implements Function {

	public static final byte FISH = 0;
	public static final byte TORNADO = 1;
	public static final byte SHARKNADO = 2;
	@SuppressWarnings("unused")
	private static final String[] jokes = new String[] { "♓", "TORNADO", "SHARKNADO" };
	@SuppressWarnings("unused")
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
