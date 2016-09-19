package org.warp.picalculator.math.functions.equations;

import java.math.BigInteger;

import org.warp.picalculator.math.functions.Number;

public class EquationResult {
	public boolean isAnEquation = false;
	public Number LR = new Number(new BigInteger("0"));

	public EquationResult(Number LR, boolean isAnEquation) {
		this.LR = LR;
		this.isAnEquation = isAnEquation;
	}
}
