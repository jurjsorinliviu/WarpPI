package org.warp.picalculator;

import org.nevec.rjm.Rational;

public class Incognita {
	public char simbolo = 'X';
	public Rational esponente = Rational.ONE;

	public Incognita(char simbolo, Rational esponente) {
		this.simbolo = simbolo;
		this.esponente = esponente;
	}

	public Incognita(char simbolo, int a, int b) {
		this.simbolo = simbolo;
		this.esponente = new Rational(a, b);
	}

	public Incognita(char simbolo) {
		this.simbolo = simbolo;
		this.esponente = new Rational(1, 1);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Incognita) {
			if (this.simbolo == ((Incognita) o).simbolo) {
				if (this.esponente == ((Incognita) o).esponente) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Character.getNumericValue(simbolo) * 3 + esponente.hashCode();
	}
}
