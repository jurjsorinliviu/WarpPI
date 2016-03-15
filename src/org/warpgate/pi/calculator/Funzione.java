package org.warpgate.pi.calculator;

public interface Funzione {
	public String simbolo();
	public Termine calcola() throws Errore;
}
