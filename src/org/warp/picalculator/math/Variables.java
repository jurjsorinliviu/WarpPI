package org.warp.picalculator.math;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Vector;

import org.nevec.rjm.BigIntegerMath;
import org.nevec.rjm.Rational;
import org.warp.picalculator.Error;

public class Variables {

	Vector<Variable> variables;

	public Variables() {
		variables = new Vector<Variable>();
	}

	public Variables(Variable[] value) {
		this();
		for (Variable i : value) {
			variables.add(i);
		}
	}

	public Variables(Vector<Variable> value) {
		this();
		variables = value;
	}

	public Variables(Variable value) {
		this();
		variables.add(value);
	}

	public int count() {
		return variables.size();
	}

	public boolean containsSymbol(char simbolo) {
		for (Variable i : variables) {
			if (i.symbol == simbolo) {
				return true;
			}
		}
		return false;
	}

	public Rational getExponentOfSymbol(char simbolo) {
		for (Variable i : variables) {
			if (i.symbol == simbolo) {
				return i.exponent;
			}
		}
		return new Rational(0, 1);
	}

	public void setExponentOfSymbol(char simbolo, Rational esponente) {
		for (Variable i : variables) {
			if (i.symbol == simbolo) {
				i.exponent = esponente;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Variables clone() {
		return new Variables((Vector<Variable>) variables.clone()).normalize();
	}

	public Variables multiply(Variables val) {
		Variables result = new Variables();
		// Passaggio 1: test vari
		// Se il primo gruppo di incognite à nullo allora ritorna il secondo
		// gruppo
		if (this.count() == 0) {
			result = val.clone();
			return result;
		}
		// Se il secondo gruppo di incognite à nullo allora ritorna il primo
		// gruppo
		if (val.count() == 0) {
			result = this.clone();
			return result;
		}

		// Passaggio 2: le incognite doppie vengono raggruppate.
		for (Variable i1 : variables) {
			for (Variable i2 : val.variables) {
				if (i1.symbol == i2.symbol) {
					if (!result.containsSymbol(i1.symbol)) {
						Variable ir = new Variable(i1.symbol, i1.exponent.add(i2.exponent));
						result.variables.add(ir);
					}
				}
			}
		}
		// Passaggio 3: le incognite non ancora presenti vengono aggiunte.
		for (Variable i : variables) {
			if (!result.containsSymbol(i.symbol)) {
				result.variables.add(i);
			}
		}
		for (Variable i : val.variables) {
			if (!result.containsSymbol(i.symbol)) {
				result.variables.add(i);
			}
		}
		return result.normalize();
	}

	public Variables divide(Variables val) {
		Variables result = new Variables();

		// Passaggio 2: le incognite doppie vengono raggruppate.
		for (Variable i1 : variables) {
			for (Variable i2 : val.variables) {
				if (i1.symbol == i2.symbol) {
					if (!result.containsSymbol(i1.symbol)) {
						Variable ir = new Variable(i1.symbol, i1.exponent.add(i2.exponent.multiply(new Rational(-1, 1))));
						result.variables.add(ir);
					}
				}
			}
		}
		// Passaggio 3: le incognite non ancora presenti vengono aggiunte.
		for (Variable i : variables) {
			if (!result.containsSymbol(i.symbol)) {
				result.variables.add(i);
			}
		}
		for (Variable i : val.variables) {
			if (!result.containsSymbol(i.symbol)) {
				result.variables.add(new Variable(i.symbol, i.exponent.multiply(new Rational(-1, 1))));
			}
		}
		return result.normalize();
	}

	public Variables sqrt() {
		Variables result = new Variables();
		for (Variable i1 : variables) {
			Variable ir = null;
			try {
				ir = new Variable(i1.symbol, i1.exponent.divide(new Rational(2, 1)));
			} catch (Error e) {
				e.printStackTrace();
			}
			result.variables.add(ir);
		}
		return result.normalize();
	}

	public Variables normalize() {
		Variables result = new Variables();
		for (Variable i1 : variables) {
			if (i1.exponent.compareTo(Rational.ZERO) != 0) {
				result.variables.add(i1);
			}
		}
		result.variables.sort(new Comparator<Variable>() {
			@Override
			public int compare(Variable o1, Variable o2) {
				int index1 = letterIndex(o1.symbol);
				int index2 = letterIndex(o2.symbol);
				return index1 - index2;
			}
		});
		return result;
	}

	public byte letterIndex(char l) {
		return letterIndex(l, false);
	}

	public static byte letterIndex(char l, boolean reverse) {
		int total = MathematicalSymbols.variables().length - 1;
		for (byte x = 0; x < MathematicalSymbols.variables().length; x++) {
			if (MathematicalSymbols.variables()[x].equals("" + l)) {
				if (reverse) {
					return (byte) (total - x);
				} else {
					return x;
				}
			}
		}

		return -1;
	}

	public boolean compareTo(Variables val) {
		if (this.equals(val))
			return true;
		return false;
	}

	@Override
	public boolean equals(Object val) {
		if (val == null)
			return false;
		if (val instanceof Variables) {
			Variables ii2 = (Variables) val;
			for (Variable i1 : variables) {
				boolean found = false;
				for (Variable i2 : ii2.variables) {
					if (i1.symbol == i2.symbol) {
						if (i1.exponent.compareTo(i2.exponent) != 0)
							return false;
						found = true;
					}
				}
				if (!found) {
					return false;
				}
			}
			for (Variable i1 : ii2.variables) {
				boolean found = false;
				for (Variable i2 : variables) {
					if (i1.symbol == i2.symbol) {
						if (i1.exponent.compareTo(i2.exponent) != 0)
							return false;
						found = true;
					}
				}
				if (!found) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String result = "";
		if (variables.size() != 1) {
			for (Variable i : variables) {
				if (i.exponent.compareTo(Rational.ONE) != 0) {
					result += "(" + i.symbol + "^" + i.exponent + ")";
				} else {
					result += i.symbol;
				}
			}
		} else if (variables.size() == 1) {
			Variable i = variables.get(0);
			if (i.exponent.compareTo(Rational.ONE) != 0) {
				result += "" + i.symbol + "^" + i.exponent + "";
			} else if (i.exponent.compareTo(Rational.ONE) == 0) {
				result += i.symbol;
			}
		}
		return result;
	}

	public static Variables lcm(Variables val1, Variables val2) {
		Variables result = new Variables();
		// Passaggio 1: test vari
		// Se il primo gruppo di incognite à nullo allora ritorna il secondo
		// gruppo
		if (val1.count() == 0) {
			result = val2.clone();
			return result;
		}
		// Se il secondo gruppo di incognite à nullo allora ritorna il primo
		// gruppo
		if (val2.count() == 0) {
			result = val1.clone();
			return result;
		}

		// Passaggio 2: le incognite doppie vengono raggruppate.
		for (Variable i1 : val1.variables) {
			for (Variable i2 : val2.variables) {
				if (i1.symbol == i2.symbol) {
					if (!result.containsSymbol(i1.symbol)) {
						Variable ir = new Variable(i1.symbol);
						if (i1.exponent.compareTo(i2.exponent) > 0) {
							ir.exponent = i1.exponent;
						} else {
							ir.exponent = i2.exponent;
						}
						result.variables.add(ir);
					}
				}
			}
		}
		// Passaggio 3: le incognite non ancora presenti vengono aggiunte.
		for (Variable i : val1.variables) {
			if (!result.containsSymbol(i.symbol)) {
				result.variables.add(i);
			}
		}
		for (Variable i : val2.variables) {
			if (!result.containsSymbol(i.symbol)) {
				result.variables.add(i);
			}
		}
		return result.normalize();
	}

	@SuppressWarnings("unchecked")
	public Vector<Variable> getVariablesList() {
		return (Vector<Variable>) this.variables.clone();
	}

	public static Variables[] normalizeBigSurdVariables(Variables[] incognitetemp) throws Error {
		Variables incognitex = incognitetemp[0].clone();
		Variables incognitey = incognitetemp[1].clone();
		Variables incognitez = incognitetemp[2].clone();

		Variables newincognitex = new Variables();
		for (Variable i : incognitex.variables) {
			Vector<BigInteger> divisori = BigIntegerMath.divisors(i.exponent.divide(2).denom());
			if (divisori.contains(new BigInteger("2"))) {
				newincognitex = newincognitex.multiply(new Variables(i));
			} else {
				incognitey = incognitey.multiply(new Variables(new Variable(i.symbol, i.exponent.divide(2))));
			}
		}
		incognitex = newincognitex;

		for (Variable i : incognitey.variables) {
			if (i.exponent.signum() < 0) {
				incognitey = incognitey.divide(new Variables(i));
				incognitez = incognitez.divide(new Variables(i));
			}
		}
		for (Variable i : incognitez.variables) {
			if (i.exponent.signum() < 0) {
				incognitey = incognitey.divide(new Variables(i));
				incognitez = incognitez.divide(new Variables(i));
			}
		}

		// TODO: SPOSTARE LE Y NEGATIVE SOTTO LA FRAZIONE, DALLA Y ALLA Z

		Variables incogniteyresult = new Variables();
		Variables incognitezresult = new Variables();
		// Le incognite doppie vengono tolte
		for (Variable i1 : incognitey.variables) {
			for (Variable i2 : incognitez.variables) {
				if (i1.symbol == i2.symbol) {
					if (i1.exponent.compareTo(i2.exponent) > 0) {
						incogniteyresult = incogniteyresult.multiply(new Variables(new Variable(i1.symbol, i1.exponent.subtract(i2.exponent))));
					} else if (i2.exponent.compareTo(i1.exponent) > 0) {
						incognitezresult = incognitezresult.multiply(new Variables(new Variable(i1.symbol, i2.exponent.subtract(i1.exponent))));
					}
				}
			}
		}

		// Le altre incognite vengono ri-messe
		for (Variable i : incognitey.variables) {
			if (!incogniteyresult.containsSymbol(i.symbol)) {
				incogniteyresult = incogniteyresult.multiply(new Variables(i));
			}
		}
		for (Variable i : incognitez.variables) {
			if (!incognitezresult.containsSymbol(i.symbol)) {
				incognitezresult = incognitezresult.multiply(new Variables(i));
			}
		}

		incognitey = incogniteyresult;
		incognitez = incognitezresult;

		return new Variables[] { incognitex, incognitey, incognitez };
	}

	public static int priority(Variables ii) {
		double priorità = 0;
		double letterMax = 0;
		for (Variable i : ii.variables) {
			int lettIndex = letterIndex(i.symbol, true);
			if (lettIndex > letterMax) {
				letterMax = lettIndex;
			}
		}
		priorità += letterMax * 100000;

		for (Variable i : ii.variables) {
			int lettIndex = letterIndex(i.symbol, true);
			if (letterMax == lettIndex) {
				priorità += i.exponent.doubleValue() * 100000;
			}
			priorità += +i.exponent.doubleValue();
		}
		return (int) priorità;
	}
}
