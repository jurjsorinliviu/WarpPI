package org.warpgate.pi.calculator;

import java.math.BigInteger;

public class VariabileEdEsponente {
	public char simbolo = 'X';
	public BigInteger esponente = BigInteger.ONE;
	
	public VariabileEdEsponente(char simbolo, BigInteger esponente) {
		this.simbolo = simbolo;
		this.esponente = esponente;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof VariabileEdEsponente) {
			if (this.simbolo == ((VariabileEdEsponente) o).simbolo) {
				if (this.esponente == ((VariabileEdEsponente) o).esponente) {
					return true;
				}
			}
		}
		return false;
	}
}
