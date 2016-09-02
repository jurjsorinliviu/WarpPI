package org.warp.picalculator;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Vector;

import org.nevec.rjm.BigIntegerMath;
import org.nevec.rjm.Rational;

public class Incognite {

	Vector<Incognita> incognite;

	public Incognite() {
		incognite = new Vector<Incognita>();
	}

	public Incognite(Incognita[] value) {
		this();
		for (Incognita i : value) {
			incognite.add(i);
		}
	}

	public Incognite(Vector<Incognita> value) {
		this();
		incognite = value;
	}

	public Incognite(Incognita value) {
		this();
		incognite.add(value);
	}

	public int count() {
		return incognite.size();
	}

	public boolean contieneSimbolo(char simbolo) {
		for (Incognita i : incognite) {
			if (i.simbolo == simbolo) {
				return true;
			}
		}
		return false;
	}

	public Rational prendiEsponenteSimbolo(char simbolo) {
		for (Incognita i : incognite) {
			if (i.simbolo == simbolo) {
				return i.esponente;
			}
		}
		return new Rational(0, 1);
	}

	public void impostaEsponenteSimbolo(char simbolo, Rational esponente) {
		for (Incognita i : incognite) {
			if (i.simbolo == simbolo) {
				i.esponente = esponente;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Incognite clone() {
		return new Incognite((Vector<Incognita>) incognite.clone()).normalize();
	}

	public Incognite multiply(Incognite val) {
		Incognite result = new Incognite();
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
		for (Incognita i1 : incognite) {
			for (Incognita i2 : val.incognite) {
				if (i1.simbolo == i2.simbolo) {
					if (!result.contieneSimbolo(i1.simbolo)) {
						Incognita ir = new Incognita(i1.simbolo, i1.esponente.add(i2.esponente));
						result.incognite.add(ir);
					}
				}
			}
		}
		// Passaggio 3: le incognite non ancora presenti vengono aggiunte.
		for (Incognita i : incognite) {
			if (!result.contieneSimbolo(i.simbolo)) {
				result.incognite.add(i);
			}
		}
		for (Incognita i : val.incognite) {
			if (!result.contieneSimbolo(i.simbolo)) {
				result.incognite.add(i);
			}
		}
		return result.normalize();
	}

	public Incognite divide(Incognite val) {
		Incognite result = new Incognite();

		// Passaggio 2: le incognite doppie vengono raggruppate.
		for (Incognita i1 : incognite) {
			for (Incognita i2 : val.incognite) {
				if (i1.simbolo == i2.simbolo) {
					if (!result.contieneSimbolo(i1.simbolo)) {
						Incognita ir = new Incognita(i1.simbolo, i1.esponente.add(i2.esponente.multiply(new Rational(-1, 1))));
						result.incognite.add(ir);
					}
				}
			}
		}
		// Passaggio 3: le incognite non ancora presenti vengono aggiunte.
		for (Incognita i : incognite) {
			if (!result.contieneSimbolo(i.simbolo)) {
				result.incognite.add(i);
			}
		}
		for (Incognita i : val.incognite) {
			if (!result.contieneSimbolo(i.simbolo)) {
				result.incognite.add(new Incognita(i.simbolo, i.esponente.multiply(new Rational(-1, 1))));
			}
		}
		return result.normalize();
	}

	public Incognite sqrt() {
		Incognite result = new Incognite();
		for (Incognita i1 : incognite) {
			Incognita ir = null;
			try {
				ir = new Incognita(i1.simbolo, i1.esponente.divide(new Rational(2, 1)));
			} catch (Errore e) {
				e.printStackTrace();
			}
			result.incognite.add(ir);
		}
		return result.normalize();
	}

	public Incognite normalize() {
		Incognite result = new Incognite();
		for (Incognita i1 : incognite) {
			if (i1.esponente.compareTo(Rational.ZERO) != 0) {
				result.incognite.add(i1);
			}
		}
		result.incognite.sort(new Comparator<Incognita>() {
			@Override
			public int compare(Incognita o1, Incognita o2) {
				int index1 = letterIndex(o1.simbolo);
				int index2 = letterIndex(o2.simbolo);
				return index1 - index2;
			}
		});
		return result;
	}

	public byte letterIndex(char l) {
		return letterIndex(l, false);
	}

	public static byte letterIndex(char l, boolean reverse) {
		int total = Simboli.incognite().length - 1;
		for (byte x = 0; x < Simboli.incognite().length; x++) {
			if (Simboli.incognite()[x].equals("" + l)) {
				if (reverse) {
					return (byte) (total - x);
				} else {
					return x;
				}
			}
		}

		return -1;
	}

	public boolean compareTo(Incognite val) {
		if (this.equals(val))
			return true;
		return false;
	}

	@Override
	public boolean equals(Object val) {
		if (val == null)
			return false;
		if (val instanceof Incognite) {
			Incognite ii2 = (Incognite) val;
			for (Incognita i1 : incognite) {
				boolean found = false;
				for (Incognita i2 : ii2.incognite) {
					if (i1.simbolo == i2.simbolo) {
						if (i1.esponente.compareTo(i2.esponente) != 0)
							return false;
						found = true;
					}
				}
				if (!found) {
					return false;
				}
			}
			for (Incognita i1 : ii2.incognite) {
				boolean found = false;
				for (Incognita i2 : incognite) {
					if (i1.simbolo == i2.simbolo) {
						if (i1.esponente.compareTo(i2.esponente) != 0)
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
		if (incognite.size() != 1) {
			for (Incognita i : incognite) {
				if (i.esponente.compareTo(Rational.ONE) != 0) {
					result += "(" + i.simbolo + "^" + i.esponente + ")";
				} else {
					result += i.simbolo;
				}
			}
		} else if (incognite.size() == 1) {
			Incognita i = incognite.get(0);
			if (i.esponente.compareTo(Rational.ONE) != 0) {
				result += "" + i.simbolo + "^" + i.esponente + "";
			} else if (i.esponente.compareTo(Rational.ONE) == 0) {
				result += i.simbolo;
			}
		}
		return result;
	}

	public static Incognite lcm(Incognite val1, Incognite val2) {
		Incognite result = new Incognite();
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
		for (Incognita i1 : val1.incognite) {
			for (Incognita i2 : val2.incognite) {
				if (i1.simbolo == i2.simbolo) {
					if (!result.contieneSimbolo(i1.simbolo)) {
						Incognita ir = new Incognita(i1.simbolo);
						if (i1.esponente.compareTo(i2.esponente) > 0) {
							ir.esponente = i1.esponente;
						} else {
							ir.esponente = i2.esponente;
						}
						result.incognite.add(ir);
					}
				}
			}
		}
		// Passaggio 3: le incognite non ancora presenti vengono aggiunte.
		for (Incognita i : val1.incognite) {
			if (!result.contieneSimbolo(i.simbolo)) {
				result.incognite.add(i);
			}
		}
		for (Incognita i : val2.incognite) {
			if (!result.contieneSimbolo(i.simbolo)) {
				result.incognite.add(i);
			}
		}
		return result.normalize();
	}

	@SuppressWarnings("unchecked")
	public Vector<Incognita> listaIncognite() {
		return (Vector<Incognita>) this.incognite.clone();
	}

	public static Incognite[] normalizeBigSurdVariables(Incognite[] incognitetemp) throws Errore {
		Incognite incognitex = incognitetemp[0].clone();
		Incognite incognitey = incognitetemp[1].clone();
		Incognite incognitez = incognitetemp[2].clone();

		Incognite newincognitex = new Incognite();
		for (Incognita i : incognitex.incognite) {
			Vector<BigInteger> divisori = BigIntegerMath.divisors(i.esponente.divide(2).denom());
			if (divisori.contains(new BigInteger("2"))) {
				newincognitex = newincognitex.multiply(new Incognite(i));
			} else {
				incognitey = incognitey.multiply(new Incognite(new Incognita(i.simbolo, i.esponente.divide(2))));
			}
		}
		incognitex = newincognitex;

		for (Incognita i : incognitey.incognite) {
			if (i.esponente.signum() < 0) {
				incognitey = incognitey.divide(new Incognite(i));
				incognitez = incognitez.divide(new Incognite(i));
			}
		}
		for (Incognita i : incognitez.incognite) {
			if (i.esponente.signum() < 0) {
				incognitey = incognitey.divide(new Incognite(i));
				incognitez = incognitez.divide(new Incognite(i));
			}
		}

		// TODO: SPOSTARE LE Y NEGATIVE SOTTO LA FRAZIONE, DALLA Y ALLA Z

		Incognite incogniteyresult = new Incognite();
		Incognite incognitezresult = new Incognite();
		// Le incognite doppie vengono tolte
		for (Incognita i1 : incognitey.incognite) {
			for (Incognita i2 : incognitez.incognite) {
				if (i1.simbolo == i2.simbolo) {
					if (i1.esponente.compareTo(i2.esponente) > 0) {
						incogniteyresult = incogniteyresult.multiply(new Incognite(new Incognita(i1.simbolo, i1.esponente.subtract(i2.esponente))));
					} else if (i2.esponente.compareTo(i1.esponente) > 0) {
						incognitezresult = incognitezresult.multiply(new Incognite(new Incognita(i1.simbolo, i2.esponente.subtract(i1.esponente))));
					}
				}
			}
		}

		// Le altre incognite vengono ri-messe
		for (Incognita i : incognitey.incognite) {
			if (!incogniteyresult.contieneSimbolo(i.simbolo)) {
				incogniteyresult = incogniteyresult.multiply(new Incognite(i));
			}
		}
		for (Incognita i : incognitez.incognite) {
			if (!incognitezresult.contieneSimbolo(i.simbolo)) {
				incognitezresult = incognitezresult.multiply(new Incognite(i));
			}
		}

		incognitey = incogniteyresult;
		incognitez = incognitezresult;

		return new Incognite[] { incognitex, incognitey, incognitez };
	}

	public static int priorità(Incognite ii) {
		double priorità = 0;
		double letterMax = 0;
		for (Incognita i : ii.incognite) {
			int lettIndex = letterIndex(i.simbolo, true);
			if (lettIndex > letterMax) {
				letterMax = lettIndex;
			}
		}
		priorità += letterMax * 100000;

		for (Incognita i : ii.incognite) {
			int lettIndex = letterIndex(i.simbolo, true);
			if (letterMax == lettIndex) {
				priorità += i.esponente.doubleValue() * 100000;
			}
			priorità += +i.esponente.doubleValue();
		}
		return (int) priorità;
	}
}
