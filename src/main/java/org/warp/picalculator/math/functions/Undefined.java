package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockUndefined;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.rules.Rule;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Undefined implements Function {

	protected final MathContext root;

	public Undefined(MathContext root) {
		this.root = root;
	}

	@Override
	public ObjectArrayList<Function> simplify(Rule rule) throws Error, InterruptedException {
		return rule.execute(this);
	}

	@Override
	public MathContext getMathContext() {
		return root;
	}

	@Override
	public boolean equals(Object o) {
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

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) {
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		result.add(new BlockUndefined());
		return result;
	}
	
	@Override
	public String toString() {
		return "UNDEFINED";
	}

}
