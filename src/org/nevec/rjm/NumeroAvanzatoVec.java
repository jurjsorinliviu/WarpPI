package org.nevec.rjm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Comparator;
import java.util.Vector;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.Variables;

/**
 * A NumeroAvanzatoVec represents an algebraic sum or differences of values
 * which each
 * term an instance of NumeroAvanzato. This mainly means that sums or
 * differences of
 * two NumeroAvanzato (or two NumeroAvanzatoVec) can be represented (exactly) as
 * a NumeroAvanzatoVec.
 * 
 * @since 2012-02-15
 * @author Richard J. Mathar
 */
public class NumeroAvanzatoVec implements Comparable<NumeroAvanzatoVec> {
	/**
	 * The value of zero.
	 */
	public static final NumeroAvanzatoVec ZERO = new NumeroAvanzatoVec();

	/**
	 * The value of one.
	 */
	public static final NumeroAvanzatoVec ONE = new NumeroAvanzatoVec(NumeroAvanzato.ONE);

	/**
	 * Internal representation: Each term as a single NumeroAvanzato. The value
	 * zero is
	 * represented by an empty vector.
	 */
	Vector<NumeroAvanzato> terms;

	/**
	 * Default ctor, which represents the zero.
	 * 
	 * @since 2012-02-15
	 */
	public NumeroAvanzatoVec() {
		terms = new Vector<NumeroAvanzato>();
	} /* ctor */

	/**
	 * ctor given the value of a NumeroAvanzato.
	 * 
	 * @param a
	 *            The value to be represented by this vector.
	 * @since 2012-02-15
	 */
	public NumeroAvanzatoVec(NumeroAvanzato a) {
		terms = new Vector<NumeroAvanzato>(1);
		terms.add(a);
	} /* ctor */

	/**
	 * ctor given two values, which (when added) represent this number a+b.
	 * 
	 * @param a
	 *            The value to be represented by the first term of the vector.
	 * @param b
	 *            The value to be represented by the second term of the vector.
	 * @since 2012-02-15
	 */
	public NumeroAvanzatoVec(NumeroAvanzato a, NumeroAvanzato b) {
		terms = new Vector<NumeroAvanzato>(2);
		terms.add(a);
		terms.add(b);
		try {
			normalize();
		} catch (Error e) {
			e.printStackTrace();
		}
	} /* ctor */

	/**
	 * Combine terms that can be written as a single surd. This unites for
	 * example the terms sqrt(90) and sqrt(10) to 4*sqrt(10).
	 * 
	 * @throws Error
	 * 
	 * @since 2012-02-15
	 */
	protected void normalize() throws Error {
		/*
		 * nothing to be done if at most one term
		 */
		if (terms.size() <= 1)
			return;

		Vector<NumeroAvanzato> newter = new Vector<NumeroAvanzato>();
		newter.add(terms.firstElement());
		/*
		 * add j-th element to the existing vector and combine were possible
		 */
		for (int j = 1; j < terms.size(); j++) {
			NumeroAvanzato todo = terms.elementAt(j);
			boolean merged = false;
			for (int ex = 0; ex < newter.size(); ex++) {
				NumeroAvanzato v = newter.elementAt(ex);
				/*
				 * try to merge terms[j] and newter[ex]. todo = r * v with r a
				 * rational number is needed. Replaces v with v+todo = v*(1+r)
				 * if this reduction works.
				 */
				NumeroAvanzato r = todo.divide(v);
				if ((r.isRational(true) || r.isTooPreciseRational(true)) && todo.getVariableX().compareTo(v.getVariableX()) && todo.getVariableY().compareTo(v.getVariableY()) && todo.getVariableZ().compareTo(v.getVariableZ())) {
					/* compute r+1 */
					Rational newpref = r.toRational(true).add(1);
					/*
					 * eliminate accidental zeros; overwrite with v*(1+r).
					 */
					if (newpref.compareTo(Rational.ZERO) == 0)
						newter.removeElementAt(ex);
					else {
						v = v.multiply(newpref);
						newter.setElementAt(v, ex);
					}
					merged = true;
					break;
				}
			}
			/*
			 * append if none of the existing elements matched
			 */
			if (!merged)
				newter.add(todo);
		}

		newter.sort(new Comparator<NumeroAvanzato>() {
			@Override
			public int compare(NumeroAvanzato o1, NumeroAvanzato o2) {
				int index1 = Variables.priority(o1.getVariableX().sqrt().multiply(o1.getVariableY()).divide(o1.getVariableZ()));
				int index2 = Variables.priority(o2.getVariableX().sqrt().multiply(o2.getVariableY()).divide(o2.getVariableZ()));
				return index2 - index1;
			}
		});

		/* overwrite old version */
		terms = newter;
	} /* normalize */

	/**
	 * Compare algebraic value with oth. Returns -1, 0 or +1 depending on
	 * whether this is smaller, equal to or larger than oth.
	 * 
	 * @param oth
	 *            The value with which this is to be compared.
	 * @return 0 or +-1.
	 * @since 2012-02-15
	 */
	@Override
	public int compareTo(NumeroAvanzatoVec oth) {
		NumeroAvanzatoVec diff;
		try {
			diff = this.subtract(oth);
			return diff.signum();
		} catch (Error e) {
			e.printStackTrace();
			return 0;
		}
	} /* compareTo */

	/**
	 * Sign function. Returns -1, 0 or +1 depending on whether this is smaller,
	 * equal to or larger than zero.
	 * 
	 * @return 0 or +-1.
	 * @throws Error
	 * @since 2012-02-15
	 */
	public int signum() throws Error {
		/*
		 * the case of zero is unique, because no (reduced) vector of surds
		 * other than the one element 0 itself can add/subtract to zero.
		 */
		if (terms.size() == 0)
			return 0;

		/*
		 * if there is one term: forward to the signum function of
		 * NumeroAvanzato
		 */
		if (terms.size() == 1)
			return terms.firstElement().signum();

		/*
		 * if all terms have a common sign: take that one offsig is the index of
		 * the first "offending" term in the sense that its sign doese not agree
		 * with the term[0].
		 */
		int sig0 = terms.elementAt(0).signum();
		int offsig = 1;
		for (; offsig < terms.size(); offsig++)
			if (terms.elementAt(offsig).signum() != sig0)
				break;
		if (offsig >= terms.size())
			return sig0;

		/*
		 * if there are two terms (now known to have different sign): forward to
		 * the comparison of the two elements as NumeroAvanzatos
		 */
		if (terms.size() == 2)
			return terms.elementAt(0).signumComparedTo(terms.elementAt(1).negate());

		/*
		 * if there are three terms, move the one with the offending sign to the
		 * other side and square both sides (which looses the sign) to remove
		 * all but one surds. The difference of the squared sides contains at
		 * most two terms, which reduces to the case above. t(0)+t(offbar) <>
		 * -t(offs)
		 */
		if (terms.size() == 3) {
			NumeroAvanzatoVec lhs;
			if (offsig == 2)
				lhs = new NumeroAvanzatoVec(terms.elementAt(0), terms.elementAt(1));
			else
				lhs = new NumeroAvanzatoVec(terms.elementAt(0), terms.elementAt(2));
			lhs = lhs.sqr();
			/*
			 * Strange line: this line isn't used, but it's present in this
			 * code!
			 * 
			 * 
			 * 
			 * NumeroAvanzato rhs = new
			 * NumeroAvanzato(terms.elementAt(offsig).sqr(), Rational.ONE);
			 * 
			 * 
			 * 
			 */
			if (lhs.compareTo(lhs) > 0)
				/*
				 * dominating sign was t(0)+t(offbar)
				 */
				return terms.elementAt(0).signum();
			else
				return terms.elementAt(offsig).signum();
		}

		/*
		 * for a larger number of terms: take a floating point representation
		 * with a small but correct number of digits, and resume with the sign
		 * of that one.
		 */
		return (floatValue() > 0.) ? 1 : -1;

	} /* signum */

	/**
	 * Construct an approximate floating point representation
	 * 
	 * @param mc
	 *            The intended accuracy of the result.
	 * @return A truncated version with the precision described by mc
	 */
	public BigDecimal BigDecimalValue(MathContext mc) {
		/*
		 * simple cases with one term forwarded to the NumeroAvanzato class
		 */
		if (terms.size() == 0)
			return BigDecimal.ZERO;
		else if (terms.size() == 1) {
			return terms.firstElement().BigDecimalValue(mc);
		}

		/*
		 * To reduce cancellation errors, loop over increasing local precision
		 * until we are stable to the required result. Keep the old (less
		 * precise) estimate in res[0], and the newer, more precise in res[1].
		 */
		BigDecimal[] res = new BigDecimal[2];
		res[0] = BigDecimal.ZERO;
		for (int addpr = 1;; addpr += 3) {
			MathContext locmc = new MathContext(mc.getPrecision() + addpr, mc.getRoundingMode());
			res[1] = BigDecimal.ZERO;
			for (NumeroAvanzato j : terms)
				res[1] = BigDecimalMath.addRound(res[1], j.BigDecimalValue(locmc));
			if (addpr > 1) {
				BigDecimal err = res[1].subtract(res[0]).abs();
				int prec = BigDecimalMath.err2prec(res[1], err);
				if (prec == Integer.MIN_VALUE) {
					break;
				}
				if (prec > mc.getPrecision())
					break;
			}
			res[0] = res[1];
		}
		return BigDecimalMath.scalePrec(res[1], mc);

	} /* BigDecimalValue */

	/**
	 * Construct an approximate floating point representation
	 * 
	 * @return A truncated version with the precision described by mc
	 */
	public double doubleValue() {
		BigDecimal bd = BigDecimalValue(MathContext.DECIMAL128);
		return bd.doubleValue();
	} /* doubleValue */

	/**
	 * Construct an approximate floating point representation
	 * 
	 * @return A truncated version with the precision described by mc
	 */
	public double floatValue() {
		BigDecimal bd = BigDecimalValue(MathContext.DECIMAL64);
		return bd.floatValue();
	} /* floatValue */

	/**
	 * Add two vectors algebraically.
	 * 
	 * @param val
	 *            The value to be added to this.
	 * @return The new value representing this+val.
	 * @throws Error
	 */
	public NumeroAvanzatoVec add(final NumeroAvanzatoVec val) throws Error {
		NumeroAvanzatoVec sum = new NumeroAvanzatoVec();
		/*
		 * concatenate the vectors and eliminate common overlaps
		 */
		for (NumeroAvanzato term : terms) {
			if (term.signumComparedTo(NumeroAvanzato.ZERO) != 0) {
				sum.terms.add(term);
			}
		}
		for (NumeroAvanzato term : val.terms) {
			if (term.signumComparedTo(NumeroAvanzato.ZERO) != 0) {
				sum.terms.add(term);
			}
		}
		sum.normalize();
		return sum;
	} /* add */

	/**
	 * Add two vectors algebraically.
	 * 
	 * @param val
	 *            The value to be added to this.
	 * @return The new value representing this+val.
	 * @throws Error
	 */
	public NumeroAvanzatoVec add(final NumeroAvanzato val) throws Error {
		NumeroAvanzatoVec sum = new NumeroAvanzatoVec();
		/*
		 * concatenate the vectors and eliminate common overlaps
		 */
		sum.terms.addAll(terms);
		sum.terms.add(val);
		sum.normalize();
		return sum;
	} /* add */

	/**
	 * Subtract another number.
	 * 
	 * @param val
	 *            The value to be subtracted from this.
	 * @return The new value representing this-val.
	 * @throws Error
	 */
	public NumeroAvanzatoVec subtract(final NumeroAvanzatoVec val) throws Error {
		NumeroAvanzatoVec sum = new NumeroAvanzatoVec();
		/*
		 * concatenate the vectors and eliminate common overlaps
		 */
		sum.terms.addAll(terms);
		for (NumeroAvanzato s : val.terms)
			sum.terms.add(s.negate());
		sum.normalize();
		return sum;
	} /* subtract */

	/**
	 * Subtract another number.
	 * 
	 * @param val
	 *            The value to be subtracted from this.
	 * @return The new value representing this-val.
	 * @throws Error
	 */
	public NumeroAvanzatoVec subtract(final NumeroAvanzato val) throws Error {
		NumeroAvanzatoVec sum = new NumeroAvanzatoVec();
		/*
		 * concatenate the vectors and eliminate common overlaps
		 */
		sum.terms.addAll(terms);
		sum.terms.add(val.negate());
		sum.normalize();
		return sum;
	} /* subtract */

	/**
	 * Compute the negative.
	 * 
	 * @return -this.
	 * @since 2012-02-15
	 */
	public NumeroAvanzatoVec negate() {
		/*
		 * accumulate the negated elements of term one by one
		 */
		NumeroAvanzatoVec resul = new NumeroAvanzatoVec();
		for (NumeroAvanzato s : terms)
			resul.terms.add(s.negate());
		/*
		 * no normalization step here, because the negation of all terms does
		 * not introduce new common factors
		 */
		return resul;
	} /* negate */

	/**
	 * Compute the square.
	 * 
	 * @return this value squared.
	 * @throws Error
	 * @since 2012-02-15
	 */
	public NumeroAvanzatoVec sqr() throws Error {
		/*
		 * Binomial expansion. First the sum of the terms squared, then 2 times
		 * the mixed products.
		 */
		NumeroAvanzatoVec resul = new NumeroAvanzatoVec();
		for (int i = 0; i < terms.size(); i++)
			resul.terms.add(terms.elementAt(i).pow2());
		for (int i = 0; i < terms.size() - 1; i++)
			for (int j = i + 1; j < terms.size(); j++)
				resul.terms.add(terms.elementAt(i).multiply(terms.elementAt(j)).multiply(2));
		resul.normalize();
		return resul;
	} /* sqr */

	/**
	 * Multiply by another square root.
	 * 
	 * @param val
	 *            a second number of this type.
	 * @return the product of this with the val.
	 * @throws Error
	 * @since 2011-02-12
	 */
	public NumeroAvanzatoVec multiply(final NumeroAvanzato val) throws Error {
		NumeroAvanzatoVec resul = new NumeroAvanzatoVec();
		for (NumeroAvanzato s : terms)
			resul.terms.add(s.multiply(val));
		resul.normalize();
		return resul;
	} /* multiply */

	public NumeroAvanzatoVec multiply(final NumeroAvanzatoVec val) throws Error {
		NumeroAvanzatoVec resul = new NumeroAvanzatoVec();
		for (NumeroAvanzato s : terms) {
			for (NumeroAvanzato s2 : val.terms) {
				resul = resul.add(s.multiply(s2));
			}
		}
		return resul;
	} /* multiply */

	public NumeroAvanzatoVec divide(final NumeroAvanzato val) throws Error {
		NumeroAvanzatoVec resul = new NumeroAvanzatoVec();
		for (NumeroAvanzato s : terms) {
			resul.terms.add(s.divide(val));
		}
		resul.normalize();
		return resul;
	} /* divide */

	public NumeroAvanzatoVec divide(final NumeroAvanzatoVec val) throws Error {
		NumeroAvanzatoVec resul = new NumeroAvanzatoVec();
		resul.terms = this.terms;
		for (NumeroAvanzato s : val.terms) {
			resul = resul.divide(s);
		}
		return resul;
	} /* divide */

	/**
	 * True if the value is rational. Equivalent to the indication whether a
	 * conversion to a Rational can be exact.
	 * 
	 * @since 2011-02-12
	 */
	public boolean isRational(boolean hasRationalVariables) {
		boolean val = false;
		for (NumeroAvanzato s : terms) {
			val = s.isRational(hasRationalVariables);
			if (val == false) {
				break;
			}
		}
		return val;
	} /* NumeroAvanzatoVec.isRational */

	public boolean isNumeroAvanzato() {
		return (this.terms.size() <= 1);
	}



	public boolean hasVariables() {
		for (NumeroAvanzato s : terms) {
			if ((s.getVariableX().count() | s.getVariableY().count() | s.getVariableZ().count()) != 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * True if the value is BigInteger. Equivalent to the indication whether a
	 * conversion to a BigInteger can be exact.
	 * 
	 * @since 2011-02-12
	 */
	public boolean isBigInteger(boolean hasBigIntegerVariables) {
		boolean val = false;
		for (NumeroAvanzato s : terms) {
			val = s.isBigInteger(hasBigIntegerVariables);
			if (val == true) {
				if (s.getVariableX().count() > 0 || s.getVariableZ().count() > 0) {
					val = false;
				}
			}
			if (val == false) {
				break;
			}
		}
		return val;
	} /* NumeroAvanzatoVec.isRational */

	/**
	 * Convert to a rational value if possible
	 * 
	 * @since 2012-02-15
	 */
	public Rational toRational(boolean hasRationalVariables) {
		Rational rat = Rational.ZERO;
		if (isRational(hasRationalVariables) == false)
			throw new ArithmeticException("Undefined conversion " + toString() + " to Rational.");
		for (NumeroAvanzato s : terms) {
			rat = rat.add(s.pref);
		}
		return rat;
	} /* NumeroAvanzato.toRational */

	/**
	 * Convert to a BigInteger value if possible
	 * 
	 * @since 2012-02-15
	 */
	public BigInteger toBigInteger(boolean hasBigIntegerVariables) {
		BigDecimal tmp = BigDecimal.ZERO.setScale(Utils.scale, Utils.scaleMode);
		if (isBigInteger(hasBigIntegerVariables) == false)
			throw new ArithmeticException("Undefined conversion " + toString() + " to BigInteger.");
		for (NumeroAvanzato s : terms) {
			tmp = BigDecimalMath.addRound(tmp, s.pref.BigDecimalValue(new MathContext(Utils.scale, Utils.scaleMode2)));
		}
		return tmp.toBigInteger();
	} /* NumeroAvanzato.toRational */

	/**
	 * Convert to a BigDecimal value if possible
	 * 
	 * @since 2012-02-15
	 */
	public BigDecimal toBigDecimal() {
		BigDecimal tmp = BigDecimal.ZERO.setScale(Utils.scale, Utils.scaleMode);
		for (NumeroAvanzato s : terms) {
			tmp = BigDecimalMath.addRound(tmp, s.BigDecimalValue(new MathContext(Utils.scale, Utils.scaleMode2)));
		}
		return tmp;
	} /* NumeroAvanzato.toBigDecimal */

	public NumeroAvanzato toNumeroAvanzato() {
		if (this.terms.size() == 0) {
			return NumeroAvanzato.ZERO;
		}
		return this.terms.get(0);
	}

	/**
	 * Return a string in the format (number/denom)*()^(1/2). If the
	 * discriminant equals 1, print just the prefactor.
	 * 
	 * @return the human-readable version in base 10
	 * @since 2012-02-16
	 */
	@Override
	public String toString() {
		/*
		 * simple cases with one term forwarded to the NumeroAvanzato class
		 */
		return toFancyString();
		/*
		 * if (terms.size() == 0)
		 * return new String("0");
		 * else {
		 * String s = new String();
		 * for (int t = 0; t < terms.size(); t++) {
		 * NumeroAvanzato bs = terms.elementAt(t);
		 * if (bs.signum() > 0)
		 * s += "+";
		 * s += bs.toString();
		 * }
		 * return s;
		 * }
		 */
	} /* toString */

	public String toFancyString() {
		if (terms.size() == 0) {
			return new String("0");
		} else if (terms.size() == 1 && terms.elementAt(0).isTooPreciseRational(true)) {
			String s = "";
			NumeroAvanzato bs = terms.elementAt(0).clone();
			String num = bs.BigDecimalValue(new MathContext(Utils.resultScale, Utils.scaleMode2)).toEngineeringString();
			if (num.contains("E")) {
				s += "(" + num.replace("E+", "*10^") + ")";
			} else {
				s += num;
			}
			s += bs.getVariableY().toString();
			return s;
		} else {
			BigInteger denominator = BigInteger.ONE;
			Variables incognitedenom = new Variables();
			for (int i = 0; i < terms.size(); i++) {
				denominator = BigIntegerMath.lcm(denominator, terms.elementAt(i).pref.b);
				// denominator =
				// denominator.multiply(terms.elementAt(i).pref.b);
				Variables iz = terms.elementAt(i).getVariableZ();
				incognitedenom = Variables.lcm(incognitedenom, iz);
			}
			String s = "";

			if (denominator.abs().compareTo(new BigInteger("10000")) > 0) {
				for (int i = 0; i < terms.size(); i++) {
					NumeroAvanzato bs = terms.elementAt(i).clone();
					String num = bs.BigDecimalValue(new MathContext(Utils.resultScale, Utils.scaleMode2)).toEngineeringString().replaceAll("\\.0+$", "").replaceAll("\\.0+E", "E");
					if (num.contains("E")) {
						if (bs.signum() > 0) {
							s += "+";
						}
						num = num.replace("E+", "*10^") + ")";
						if (num.contains("*10^1)")) {
							num = num.replace("*10^1)", ")");
						} else {
							num = "(" + num;
						}
						s += num;
					} else {
						if (bs.signum() > 0) {
							s += "+";
						}
						s += num;
					}
					s += bs.getVariableY().toString();
				}
				return s;
			}

			if (denominator.compareTo(BigInteger.ONE) != 0 || incognitedenom.count() > 0) {
				if (terms.size() == 1 && terms.get(0).multiply(denominator).isBigInteger(true) == false) {
					s += "(";
				}
			}
			for (int t = 0; t < terms.size(); t++) {
				NumeroAvanzato bs = terms.elementAt(t).clone();

				bs = bs.setVariableY(bs.getVariableY().divide(bs.getVariableZ()));
				bs = bs.setVariableY(bs.getVariableY().multiply(incognitedenom));
				bs = bs.multiply(denominator);
				bs = bs.setVariableZ(incognitedenom);

				bs.pref = new Rational(bs.pref.a, BigInteger.ONE);
				if (bs.signum() > 0 && t > 0)
					s += "+";
				if (bs.isBigInteger(true)) {
					String numb;
					try {
						numb = bs.toRational(true).a.toString();
					} catch (Error e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						numb = "";
					}
					String incognite = bs.getVariableY().toString();
					if (((numb.equals("1") || numb.equals("-1")) == false && incognite.length() > 0) || incognite.length() == 0) {
						s += numb;
					} else if (numb.equals("-1")) {
						s += "-";
					}
					s += incognite;
				} else if (bs.isRational(true) || bs.isTooPreciseRational(true)) {
					try {
						s += bs.toRational(true).toString();
					} catch (Error e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					s += bs.getVariableY().toString();
				} else {
					BigInteger numerator = bs.pref.numer();
					if ((numerator.compareTo(BigInteger.ONE) != 0 || bs.getVariableY().count() > 0) && (bs.disc.compareTo(BigInteger.ONE) != 0 || bs.getVariableX().count() > 0)) {
						if (((bs.getVariableY().count() > 0) || (bs.getVariableY().count() == 0 && numerator.toString().length() > 0))) {
							if ((bs.getVariableY().count() > 0 && (numerator.toString().equals("1") || numerator.toString().equals("-1")) == false) || bs.getVariableY().count() == 0) {
								s += numerator.toString();
							} else if (numerator.toString().equals("-1")) {
								s += "-";
							}
						}
						// s += "(";
					}
					if (bs.disc.isInteger() && bs.getVariableX().count() == 0) {
						s += "Ⓐ(";
						s += bs.disc.toString();
						s += ")";
					} else if ((bs.disc.toString().equals("1") || bs.disc.toString().equals("-1")) && bs.getVariableX().count() > 0) {
						s += "Ⓐ(";
						if (bs.disc.toString().equals("-1")) {
							s += "-";
						}
						s += bs.getVariableX().toString();
						s += ")";
					} else {
						s += "Ⓐ(" + bs.disc.toString() + bs.getVariableX().toString() + ")";
					}
					if ((numerator.compareTo(BigInteger.ONE) != 0 || bs.getVariableY().count() > 0) && (bs.disc.compareTo(BigInteger.ONE) != 0 || bs.getVariableX().count() > 0)) {
						if (((bs.getVariableY().count() > 0) || (bs.getVariableY().count() == 0 && numerator.toString().length() > 0))) {
							s += bs.getVariableY().toString();
						}
						// s += "(";
					}
					if ((numerator.compareTo(BigInteger.ONE) != 0 || bs.getVariableY().count() > 0) && (bs.disc.compareTo(BigInteger.ONE) != 0 || bs.getVariableX().count() > 0)) {
						// s += ")";
					}
				}
			}
			if (denominator.compareTo(BigInteger.ONE) != 0 || incognitedenom.count() > 0) {
				if (terms.size() == 1 && terms.get(0).multiply(denominator).isBigInteger(true) == false) {
					s += ")";
				}
				s += "/";
				if ((incognitedenom.count() > 0 && (denominator.toString().equals("1") || denominator.toString().equals("-1")) == false) || incognitedenom.count() == 0) {
					s += denominator;
				} else if (denominator.toString().equals("-1")) {
					s += "-";
				}
				s += incognitedenom.toString();
			}
			return s;
		}
	}

} /* NumeroAvanzatoVec */
