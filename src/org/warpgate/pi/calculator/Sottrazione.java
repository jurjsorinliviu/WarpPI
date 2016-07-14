package org.warpgate.pi.calculator;

import java.awt.Color;
import java.awt.Graphics;

public class Sottrazione extends FunzioneDueValori {

	public Sottrazione(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.SUBTRACTION;
	}

	@Override
	public Termine calcola() throws Errore {
		return getVariable1().calcola().add(getVariable2().calcola().multiply(new Termine("-1")));
	}

}