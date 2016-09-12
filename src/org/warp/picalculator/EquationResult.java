package org.warp.picalculator;

import java.math.BigInteger;

public class EquationResult {
	public boolean isAnEquation = false;
	public Number LR = new Number(new BigInteger("0"));

	public EquationResult(Number LR, boolean isAnEquation) {
		this.LR = LR;
		this.isAnEquation = isAnEquation;
	}
}
