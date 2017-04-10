package org.warp.picalculator.math.functions;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;

public class Undefined implements Function {

	protected final MathContext root;

	public Undefined(MathContext root) {
		this.root = root;
	}

	@Override
	public List<Function> simplify() throws Error {
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
	public boolean equals(Object o) {
		if (o instanceof Undefined) {
			return true;
		}
		return false;
	}

	@Override
	public Undefined clone() {
		return new Undefined(root);
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
