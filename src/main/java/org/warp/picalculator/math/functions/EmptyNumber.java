package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
