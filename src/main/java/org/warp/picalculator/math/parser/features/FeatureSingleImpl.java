package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.parser.features.interfaces.FeatureSingle;

public abstract class FeatureSingleImpl implements FeatureSingle {
	private Object child;

	public FeatureSingleImpl(Object child) {
		this.child = child;
	}

	@Override
	public Object getChild() {
		return child;
	}
	
	protected Function getFunction1() {
		return (Function) child;
	}

	@Override
	public void setChild(Object obj) {
		child = obj;
	}
}
