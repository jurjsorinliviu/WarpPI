package org.warp.picalculator;

import org.nevec.rjm.Rational;

public class Variable {
	public char symbol = 'X';
	public Rational exponent = Rational.ONE;

	public Variable(char simbolo, Rational esponente) {
		this.symbol = simbolo;
		this.exponent = esponente;
	}

	public Variable(char simbolo, int a, int b) {
		this.symbol = simbolo;
		this.exponent = new Rational(a, b);
	}

	public Variable(char simbolo) {
		this.symbol = simbolo;
		this.exponent = new Rational(1, 1);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Variable) {
			if (this.symbol == ((Variable) o).symbol) {
				if (this.exponent == ((Variable) o).exponent) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Character.getNumericValue(symbol) * 3 + exponent.hashCode();
	}
}
