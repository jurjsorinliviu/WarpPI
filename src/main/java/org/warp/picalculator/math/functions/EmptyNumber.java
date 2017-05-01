package org.warp.picalculator.math.functions;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;

public class EmptyNumber implements Function {

	public EmptyNumber(MathContext root) {
		this.root = root;
	}

	private final MathContext root;

	@Override
	public ObjectArrayList<Function> simplify() throws Error {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSimplified() {
		return false;
	}

	@Override
	public MathContext getMathContext() {
		return root;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof EmptyNumber;
	}

	@Override
	public Function clone() {
		return new EmptyNumber(root);
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
