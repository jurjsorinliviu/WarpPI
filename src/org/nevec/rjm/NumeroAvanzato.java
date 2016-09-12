package org.nevec.rjm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.security.ProviderException;

import org.warp.picalculator.Error;
import org.warp.picalculator.Variables;

/**
 * Square roots on the real line. These represent numbers which are a product of
 * a (signed) fraction by a square root of a non-negative fraction. This might
 * be extended to values on the imaginary axis by allowing negative values
 * underneath the square root, but this is not yet implemented.
 * 
 * @since 2011-02-12
 * @author Richard J. Mathar
 */
public class NumeroAvanzato implements Cloneable {
	/**
	 * The value of zero.
	 */
	public static final NumeroAvanzato ZERO = new NumeroAvanzato();

	/**
	 * The value of one.
	 */
	public static final NumeroAvanzato ONE = new NumeroAvanzato(Rational.ONE, Rational.ONE);
	/**
	 * Prefactor
	 */
	Rational pref;

	private Variables variablex;

	private Variables variabley;

	private Variables variablez;

	/**
	 * The number underneath the square root, always non-negative. The
	 * mathematical object has the value pref*sqrt(disc).
	 */
	Rational disc;

	/**
	 * Default ctor, which represents the zero.
	 * 
	 * @since 2011-02-12
	 */
	public NumeroAvanzato() {
		pref = Rational.ZERO;
		disc = Rational.ZERO;
		variablex = new Variables();
		variabley = new Variables();
		variablez = new Variables();
	}

	/**
	 * ctor given the prefactor and the basis of the root. This creates an
	 * object of value a*sqrt(b).
	 * 
	 * @param a
	 *            the prefactor.
	 * @param b
	 *            the discriminant.
	 * @throws Error
	 * @since 2011-02-12
	 */
	public NumeroAvanzato(Rational a, Rational b) {
		this.pref = a;
		/*
		 * reject attempts to use a negative b
		 */
		if (b.signum() < 0)
			throw new ProviderException("Not implemented: imaginary surds");
		this.disc = b;
		variablex = new Variables();
		variabley = new Variables();
		variablez = new Variables();
		try {
			normalize();
			normalizeG();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	public NumeroAvanzato(Rational a, Rational b, Variables x, Variables y, Variables z) {
		this.pref = a;
		/*
		 * reject attempts to use a negative b
		 */
		if (b.signum() < 0)
			throw new ProviderException("Not implemented: imaginary surds");
		this.disc = b;
		variablex = x;
		variabley = y;
		variablez = z;
		try {
			normalize();
			normalizeG();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	/**
	 * ctor given the numerator and denominator of the root. This creates an
	 * object of value sqrt(a/b).
	 * 
	 * @param a
	 *            the numerator
	 * @param b
	 *            the denominator.
	 * @since 2011-02-12
	 */
	public NumeroAvanzato(int a, int b) {
		this(Rational.ONE, new Rational(a, b));
	}

	/**
	 * ctor given the value under the root. This creates an object of value
	 * sqrt(a).
	 * 
	 * @param a
	 *            the discriminant.
	 * @since 2011-02-12
	 */
	public NumeroAvanzato(BigInteger a) {
		this(Rational.ONE, new Rational(a, BigInteger.ONE));
	}

	public NumeroAvanzato(Rational a) {
		this(Rational.ONE, a);
	}

	/**
	 * Create a deep copy.
	 * 
	 * @since 2011-02-12
	 */
	@Override
	public NumeroAvanzato clone() {
		Rational fclon = pref.clone();
		Rational dclon = disc.clone();
		Variables incognitexb = variablex;
		Variables incogniteyb = variabley;
		Variables incognitezb = variablez;
		/*
		 * the main intent here is to bypass any attempt to reduce the
		 * discriminant by figuring out the square-free part in normalize(),
		 * which has already done in the current copy of the number.
		 */
		NumeroAvanzato cl = new NumeroAvanzato();
		cl.pref = fclon;
		cl.disc = dclon;
		cl.variablex = incognitexb;
		cl.variabley = incogniteyb;
		cl.variablez = incognitezb;
		return cl;
	} /* NumeroAvanzato.clone */

	/**
	 * Add two surds of compatible discriminant.
	 * 
	 * @param val
	 *            The value to be added to this.
	 */

	public NumeroAvanzatoVec add(final NumeroAvanzato val) {
		// zero plus somethings yields something
		if (signum() == 0)
			return new NumeroAvanzatoVec(val);
		else if (val.signum() == 0)
			return new NumeroAvanzatoVec(this);
		else
			// let the ctor of NumeroAvanzatoVec to the work
			return new NumeroAvanzatoVec(this, val);
	} /* NumeroAvanzato.add */

	/**
	 * Multiply by another square root.
	 * 
	 * @param val
	 *            a second number of this type.
	 * @return the product of this with the val.
	 * @since 2011-02-12
	 */
	public NumeroAvanzato multiply(final NumeroAvanzato val) {
		return new NumeroAvanzato(pref.multiply(val.pref), disc.multiply(val.disc), variablex.multiply(val.variablex), variabley.multiply(val.variabley), variablez.multiply(val.variablez));
	} /* NumeroAvanzato.multiply */

	/**
	 * Multiply by a rational number.
	 * 
	 * @param val
	 *            the factor.
	 * @return the product of this with the val.
	 * @since 2011-02-15
	 */
	public NumeroAvanzato multiply(final Rational val) {
		return new NumeroAvanzato(pref.multiply(val), disc, variablex, variabley, variablez);
	} /* NumeroAvanzato.multiply */

	/**
	 * Multiply by a BigInteger.
	 * 
	 * @param val
	 *            a second number.
	 * @return the product of this with the value.
	 * @since 2011-02-12
	 */
	public NumeroAvanzato multiply(final BigInteger val) {
		return new NumeroAvanzato(pref.multiply(val), disc, variablex, variabley, variablez);
	} /* NumeroAvanzato.multiply */

	/**
	 * Multiply by an integer.
	 * 
	 * @param val
	 *            a second number.
	 * @return the product of this with the value.
	 * @since 2011-02-12
	 */
	public NumeroAvanzato multiply(final int val) {
		BigInteger tmp = new BigInteger("" + val);
		return multiply(tmp);
	} /* NumeroAvanzato.multiply */

	/**
	 * Compute the square.
	 * 
	 * @return this value squared.
	 * @throws Error
	 * @since 2011-02-12
	 */
	public NumeroAvanzato pow2() throws Error {
		NumeroAvanzato res = new NumeroAvanzato();
		BigInteger a = pref.a;
		BigInteger b = pref.b;
		BigInteger c = disc.a;
		BigInteger d = disc.b;
		res.pref = new Rational(a.pow(2).multiply(c).multiply(d), b.pow(2).multiply(d));
		res.disc = new Rational(0, 1);
		res.normalize();
		res.variablex = variablex;
		res.variabley = variabley.multiply(variabley);
		res.variablez = variablez.multiply(variablez);
		return res;
	} /* NumeroAvanzato.sqr */

	/**
	 * Divide by another square root.
	 * 
	 * @param val
	 *            A second number of this type.
	 * @return The value of this/val
	 * @throws Error
	 * @since 2011-02-12
	 */
	public NumeroAvanzato divide(final NumeroAvanzato val) throws Error {
		if (val.signum() == 0)
			throw new ArithmeticException("Dividing " + toFancyString() + " through zero.");
		NumeroAvanzato result = new NumeroAvanzato(pref.divide(val.pref), disc.divide(val.disc));
		result.variablex = variablex.divide(val.variablex);
		result.variabley = variabley.divide(val.variabley);
		result.variablez = variablez.divide(val.variablez);
		result.normalize();
		return result;
	} /* NumeroAvanzato.divide */

	private String toFancyString() throws Error {
		return new NumeroAvanzatoVec(this).toFancyString();
	}

	/**
	 * Divide by an integer.
	 * 
	 * @param val
	 *            a second number.
	 * @return the value of this/val
	 * @throws Error
	 * @since 2011-02-12
	 */
	public NumeroAvanzato divide(final BigInteger val) throws Error {
		if (val.signum() == 0)
			throw new ArithmeticException("Dividing " + toFancyString() + " through zero.");
		return new NumeroAvanzato(pref.divide(val), disc);
	} /* NumeroAvanzato.divide */

	/**
	 * Divide by an integer.
	 * 
	 * @param val
	 *            A second number.
	 * @return The value of this/val
	 * @throws Error
	 * @since 2011-02-12
	 */
	public NumeroAvanzato divide(int val) throws Error {
		if (val == 0)
			throw new ArithmeticException("Dividing " + toFancyString() + " through zero.");
		return new NumeroAvanzato(pref.divide(val), disc);
	} /* NumeroAvanzato.divide */

	/**
	 * Compute the negative.
	 * 
	 * @return -this.
	 * @since 2011-02-12
	 */
	public NumeroAvanzato negate() {
		/*
		 * This is trying to be quick, avoiding normalize(), by toggling the
		 * sign in a clone()
		 */
		NumeroAvanzato n = clone();
		n.pref = n.pref.negate();
		return n;
	} /* NumeroAvanzato.negate */

	/**
	 * Absolute value.
	 * 
	 * @return The absolute (non-negative) value of this.
	 * @since 2011-02-12
	 */
	public NumeroAvanzato abs() {
		return new NumeroAvanzato(pref.abs(), disc);
	}

	/**
	 * Compares the value of this with another constant.
	 * 
	 * @param val
	 *            the other constant to compare with
	 * @return -1, 0 or 1 if this number is numerically less than, equal to, or
	 *         greater than val.
	 * @throws Error
	 * @since 2011-02-12
	 */
	public int signumComparedTo(final NumeroAvanzato val) throws Error {
		/*
		 * Since we keep the discriminant positive, the rough estimate comes
		 * from comparing the signs of the prefactors.
		 */
		final int sig = signum();
		final int sigv = val.signum();
		if (sig < 0 && sigv >= 0)
			return -1;
		if (sig > 0 && sigv <= 0)
			return 1;
		if (sig == 0 && sigv == 0)
			return 0;
		if (sig == 0 && sigv > 0)
			return -1;
		if (sig == 0 && sigv < 0)
			return 1;

		/*
		 * Work out the cases of equal sign. Compare absolute values by
		 * comparison of the squares which is forwarded to the comparison of the
		 * Rational class.
		 */
		final Rational this2 = pow2().pref;
		final Rational val2 = val.pow2().pref;
		final int c2 = this2.compareTo(val2);
		if (c2 == 0)
			return 0;
		/*
		 * If both values have negative sign, the one with the smaller square is
		 * the larger number.
		 */
		else if (sig > 0 && c2 > 0 || sig < 0 && c2 < 0)
			return 1;
		else
			return -1;
	} /* NumeroAvanzato.compareTo */

	/**
	 * Return a string in the format (number/denom)*()^(1/2). If the
	 * discriminant equals 1, print just the prefactor.
	 * 
	 * @return the human-readable version in base 10
	 * @since 2011-02-12
	 */
	@Override
	public String toString() {
		try {
			return toFancyString();
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * if (disc.compareTo(Rational.ONE) != 0 &&
		 * disc.compareTo(Rational.ZERO) != 0)
		 * return ("(" + pref.toString() + ")*(" + disc.toString() + ")^(1/2)");
		 * else
		 * return pref.toString();
		 */
		return "err";
	} /* NumeroAvanzato.toString */

	/**
	 * Return a double value representation.
	 * 
	 * @return The value with double precision.
	 * @since 2011-02-12
	 */
	public double doubleValue() {
		/*
		 * First compute the square to prevent overflows if the two pieces of
		 * the prefactor and the discriminant are of very different magnitude.
		 */
		Rational p2 = pref.pow(2).multiply(disc);
		System.out.println("dv sq " + p2.toString());
		double res = p2.doubleValue();
		System.out.println("dv sq " + res);
		return (pref.signum() >= 0) ? Math.sqrt(res) : -Math.sqrt(res);
	} /* NumeroAvanzato.doubleValue */

	/**
	 * Return a float value representation.
	 * 
	 * @return The value with single precision.
	 * @since 2011-02-12
	 */
	public float floatValue() {
		return (float) (doubleValue());
	} /* NumeroAvanzato.floatValue */

	/**
	 * True if the value is integer. Equivalent to the indication whether a
	 * conversion to an integer can be exact.
	 * 
	 * @param hasBigIntegerVariables
	 * 
	 * @since 2011-02-12
	 */
	public boolean isBigInteger(boolean hasBigIntegerVariables) {
		if (pref.isBigInteger() && (disc.signum() == 0 || disc.compareTo(Rational.ONE) == 0)) {
			if (disc.signum() != 0 && variablex.count() > 0) {
				return false;
			}
			if (hasBigIntegerVariables == false && variabley.count() > 0) {
				return false;
			}
			if (pref.b.compareTo(BigInteger.ZERO) != 0 && disc.b.compareTo(BigInteger.ZERO) != 0 && variablez.count() > 0) {
				return false;
			}
			return true;
		}
		return false;
	} /* NumeroAvanzato.isBigInteger */

	public boolean isRational(boolean hasRationalVariables) {
		if (disc.signum() == 0 || disc.compareTo(new Rational(1, 1)) == 0) {
			if (variablex.count() > 0) {
				return false;
			} else if (hasRationalVariables == false) {
				if (variabley.count() > 0 || variablez.count() > 0) {
					return false;
				}
			}

			if (this.pref.b.compareTo(new BigInteger("10000")) > 0) {
				return false;
			}
			return true;
		}
		return false;

	} /* NumeroAvanzato.isRational */

	public boolean isTooPreciseRational(boolean hasRationalVariables) {
		if (disc.signum() == 0 || disc.compareTo(new Rational(1, 1)) == 0) {
			if (variablex.count() > 0) {
				return false;
			} else if (hasRationalVariables == false) {
				if (variabley.count() > 0 || variablez.count() > 0) {
					return false;
				}
			}

			if (this.pref.b.compareTo(new BigInteger("10000")) > 0) {
				return true;
			}
			return false;
		}
		return false;
	} /* NumeroAvanzato.isRational */

	/**
	 * Convert to a rational value if possible
	 * 
	 * @throws Error
	 * 
	 * @since 2012-02-15
	 */
	public Rational toRational(boolean hasRationalVariables) throws Error {
		if (isRational(hasRationalVariables) || isTooPreciseRational(hasRationalVariables))
			return pref;
		else
			throw new ArithmeticException("Undefined conversion " + toFancyString() + " to Rational.");
	} /* NumeroAvanzato.toRational */

	public Rational toRational() throws Error {
		if (isRational(true))
			return pref;
		else
			throw new ArithmeticException("Undefined conversion " + toFancyString() + " to Rational.");
	}

	/**
	 * The sign: 1 if the number is >0, 0 if ==0, -1 if <0
	 * 
	 * @return the signum of the value.
	 * @since 2011-02-12
	 */
	public int signum() {
		/*
		 * Since the disc is kept positive, this is the same as the sign of the
		 * prefactor. This works because a zero discriminant is always copied
		 * over to the prefactor, not hidden.
		 */
		return pref.signum();
	} /* NumeroAvanzato.signum */

	/**
	 * Normalize to squarefree discriminant.
	 * 
	 * @throws Error
	 * 
	 * @since 2011-02-12
	 */
	protected void normalize() throws Error {
		/*
		 * Move squares out of the numerator and denominator of the discriminant
		 */
		if (disc.signum() != 0) {
			/*
			 * square-free part of the numerator: numer = numC*some^2
			 */
			BigInteger numC = BigIntegerMath.core(disc.numer());
			/*
			 * extract the perfect square of the numerator
			 */
			BigInteger sq = disc.numer().divide(numC);
			/*
			 * extract the associated square root
			 */
			BigInteger sqf = BigIntegerMath.isqrt(sq);

			/*
			 * move sqf over to the pre-factor
			 */
			pref = pref.multiply(sqf);

			BigInteger denC = BigIntegerMath.core(disc.denom());
			sq = disc.denom().divide(denC);
			sqf = BigIntegerMath.isqrt(sq);
			try {
				pref = pref.divide(sqf);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			disc = new Rational(numC, denC);

			if (disc.b.compareTo(BigInteger.ZERO) == 0 || disc.a.compareTo(BigInteger.ZERO) == 0 || pref.a.compareTo(BigInteger.ZERO) == 0) {
				variablex = new Variables();
				variabley = new Variables();
			}

			if (disc.b.compareTo(BigInteger.ZERO) == 0 || pref.b.compareTo(BigInteger.ZERO) == 0) {
				variablez = new Variables();
			}

			if (variabley.compareTo(variablez)) {
				variabley = new Variables();
				variablez = new Variables();
			}

			/**/
			Variables[] incognitetemp = new Variables[] { variablex, variabley, variablez };
			incognitetemp = Variables.normalizeBigSurdVariables(incognitetemp);
			variablex = incognitetemp[0];
			variabley = incognitetemp[1];
			variablez = incognitetemp[2];
			/**/
		} else {
			pref = Rational.ZERO;
			variablex = new Variables();
			variabley = new Variables();
			variablez = new Variables();
		}
	} /* NumeroAvanzato.normalize */

	/**
	 * Normalize to coprime numerator and denominator in prefactor and
	 * discriminant
	 * 
	 * @throws Error
	 * 
	 * @since 2011-02-12
	 */
	protected void normalizeG() throws Error {
		/*
		 * Is there a common factor between the numerator of the prefactor and
		 * the denominator of the discriminant ?
		 */
		BigInteger d = pref.numer().abs().gcd(disc.denom());
		if (d.compareTo(BigInteger.ONE) > 0) {
			try {
				pref = pref.divide(d);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * instead of multiplying with the square of d, using two steps
			 * offers a change to recognize the common factor..
			 */
			disc = disc.multiply(d);
			disc = disc.multiply(d);
		}
		/*
		 * Is there a common factor between the denominator of the prefactor and
		 * the numerator of the discriminant ?
		 */
		d = pref.denom().gcd(disc.numer());
		if (d.compareTo(BigInteger.ONE) > 0) {
			pref = pref.multiply(d);
			/*
			 * instead of dividing through the square of d, using two steps
			 * offers a change to recognize the common factor..
			 */
			disc = disc.divide(d);
			disc = disc.divide(d);
		}
	} /* NumeroAvanzato.normalizeG */

	/**
	 * Return the approximate floating point representation.
	 * 
	 * @param mc
	 *            Description of the accuracy needed.
	 * @return A representation with digits valid as described by mc
	 * @since 2012-02-15
	 */
	public BigDecimal BigDecimalValue(MathContext mc) {
		/*
		 * the relative error of the result equals the relative error of the
		 * prefactor plus half of the relative error of the discriminant. So
		 * adding 3 digits temporarily is sufficient.
		 */
		final MathContext locmc = new MathContext(mc.getPrecision() + 3, mc.getRoundingMode());
		/*
		 * first the square root of the discriminant
		 */
		BigDecimal sqrdis = BigDecimalMath.sqrt(disc.BigDecimalValue(locmc), locmc);
		/*
		 * Then multiply by the prefactor. If sqrdis is a terminating decimal
		 * fraction, we prevent early truncation of the result by truncating
		 * later.
		 */
		BigDecimal res = sqrdis.multiply(pref.BigDecimalValue(mc));
		return BigDecimalMath.scalePrec(res, mc);
	} /* BigDecimalValue */

	@Override
	public boolean equals(Object val) {
		if (val instanceof NumeroAvanzato) {
			NumeroAvanzato na = (NumeroAvanzato) val;
			try {
				if (pow2().pref == na.pow2().pref) {
					if (pow2().pref == Rational.ZERO) {
						return true;
					} else {
						if (variablex == na.variablex) {
							if (variabley == na.variabley) {
								if (variablez == na.variablez) {
									return true;
								}
							}
						}
					}
				}
			} catch (Error e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Variables getVariableX() {
		if (variablex == null) {
			return variablex;
		}
		return variablex.clone();
	}

	public NumeroAvanzato setVariableX(Variables x) {
		NumeroAvanzato result = this.clone();
		result.variablex = x;
		return result;
	}

	public Variables getVariableY() {
		if (variabley == null) {
			return variabley;
		}
		return variabley.clone();
	}

	public NumeroAvanzato setVariableY(Variables y) {
		NumeroAvanzato result = this.clone();
		result.variabley = y;
		return result;
	}

	public Variables getVariableZ() {
		if (variablez == null) {
			return variablez;
		}
		return variablez.clone();
	}

	public NumeroAvanzato setVariableZ(Variables z) {
		NumeroAvanzato result = this.clone();
		result.variablez = z;
		return result;
	}

	public NumeroAvanzato divideUnsafe(BigInteger denominator) {
		try {
			return divide(denominator);
		} catch (Error e) {
			e.printStackTrace();
			return new NumeroAvanzato();
		}
	}

} /* NumeroAvanzato */
