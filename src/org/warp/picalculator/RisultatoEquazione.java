package org.warp.picalculator;

import java.math.BigInteger;

public class RisultatoEquazione {
	public boolean isAnEquation = false;
	public Termine LR = new Termine(new BigInteger("0"));

	public RisultatoEquazione(Termine LR, boolean isAnEquation) {
		this.LR = LR;
		this.isAnEquation = isAnEquation;
	}
}
