package org.warpgate.pi.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.BigSurd;
import org.nevec.rjm.BigSurdVec;
import org.nevec.rjm.Rational;

public class Termine implements Funzione {

	protected BigSurdVec term = BigSurdVec.ZERO;
	protected ArrayList<VariabileEdEsponente> variables = new ArrayList<VariabileEdEsponente>();

	public Termine(BigSurdVec val) {
		term = val;
	}

	public Termine(String s) {
		term = new BigSurdVec(new BigSurd(Utils.getRational(s), Rational.ONE));
	}

	public Termine(Rational r) {
		term = new BigSurdVec(new BigSurd(r, Rational.ONE));
	}

	public Termine(BigInteger r) {
		term = new BigSurdVec(new BigSurd(new Rational(r, BigInteger.ONE), Rational.ONE));
	}

	public Termine(BigDecimal r) {
		term = new BigSurdVec(new BigSurd(Utils.getRational(r), Rational.ONE));
	}

	public BigSurdVec getTerm() {
		return term;
	}

	public void setTerm(BigSurdVec val) {
		term = val;
	}

	public ArrayList<VariabileEdEsponente> getVariables() {
		return variables;
	}

	public void setVariables(ArrayList<VariabileEdEsponente> variables) {
		this.variables = variables;
	}

	@Override
	public Termine calcola() {
		return this;
	}

	@Override
	public String simbolo() {
		return null;
	}

	public Termine add(Termine f) throws Errore {
		Termine ret = new Termine(getTerm().add(f.getTerm()));
		ret.setVariables(getVariables());
		if (f.getVariables().size() > 0) {
			System.err.println("Moltiplicazioni con variabili non implementato");
			throw new Errore(Errori.NOT_IMPLEMENTED);
		}
		return ret;
	}

	public Termine multiply(Termine f) throws Errore {
		Termine ret = new Termine(getTerm().multiply(f.getTerm()));
		ret.setVariables(getVariables());
		if (f.getVariables().size() > 0) {
			System.err.println("Moltiplicazioni con variabili non implementato");
			throw new Errore(Errori.NOT_IMPLEMENTED);
		}
		return ret;
	}

	public Termine divide(Termine f) throws Errore {
		Termine ret = new Termine(getTerm().divide(f.getTerm()));
		ret.setVariables(getVariables());
		if (f.getVariables().size() > 0) {
			System.err.println("Divisioni con variabili non implementato");
			throw new Errore(Errori.NOT_IMPLEMENTED);
		}
		return ret;
	}

	public Termine pow(Termine f) throws Errore {
		Termine ret = new Termine(BigSurdVec.ONE);
		if (f.getTerm().isBigInteger()) {
			for (BigInteger i = BigInteger.ZERO; i.compareTo(f.getTerm().toBigInteger()) < 0; i = i.add(BigInteger.ONE)) {
				ret = ret.multiply(new Termine(getTerm()));
				ret.setVariables(getVariables());
				if (f.getVariables().size() > 0) {
					System.err.println("Potenze con variabili non implementato");
					throw new Errore(Errori.NOT_IMPLEMENTED);
				}
			}
		} else if (getTerm().isRational() && f.getTerm().isRational() && f.getTerm().toRational().denom().compareTo(BigInteger.ONE) == 0) {
			ret = new Termine(getTerm().toRational().pow(f.getTerm().toRational()));
			ret.setVariables(getVariables());
			if (f.getVariables().size() > 0) {
				System.err.println("Potenze con variabili non implementato");
				throw new Errore(Errori.NOT_IMPLEMENTED);
			}
		} else if (getTerm().isRational() && f.getTerm().isRational() && f.getTerm().toRational().compareTo(Rational.HALF) == 0) {
			//Rational originalExponent = f.getTerm().toRational();
			//Rational rootExponent = new Rational(originalExponent.denom(), originalExponent.numer());
			Rational numberToRoot = getTerm().toRational();
			ret = new Termine(new BigSurdVec(new BigSurd(Rational.ONE, numberToRoot)));
			ret.setVariables(getVariables());
			if (f.getVariables().size() > 0) {
				System.err.println("Potenze con variabili non implementato");
				throw new Errore(Errori.NOT_IMPLEMENTED);
			}
		} else {
			ret = new Termine(BigDecimalMath.pow(getTerm().BigDecimalValue(new MathContext(Utils.scale, Utils.scaleMode2)), f.getTerm().BigDecimalValue(new MathContext(Utils.scale, Utils.scaleMode2))));
			ret.setVariables(getVariables());
			if (f.getVariables().size() > 0) {
				System.err.println("Potenze con variabili non implementato");
				throw new Errore(Errori.NOT_IMPLEMENTED);
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		if (getTerm().isBigInteger()) {
			return getTerm().toBigInteger().toString();
		} else if (getTerm().isRational()) {
			return getTerm().toRational().toString();
		} else {
			return getTerm().toFancyString();
		}
	}
}
