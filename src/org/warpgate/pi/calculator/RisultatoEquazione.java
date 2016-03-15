package org.warpgate.pi.calculator;

public class RisultatoEquazione {
	public boolean isAnEquation = false;
	public Termine LR = new Termine("0");
	
	public RisultatoEquazione(Termine LR, boolean isAnEquation) {
		this.LR = LR;
		this.isAnEquation = isAnEquation;
	}
}
