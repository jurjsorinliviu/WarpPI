package org.warpgate.pi.calculator;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.Rational;

public class Utils {
	
	public static final int scale = 130;

	public static final int scaleMode = BigDecimal.ROUND_HALF_UP;
	public static final RoundingMode scaleMode2 = RoundingMode.HALF_UP;
	
	public static DebugStream debug = new DebugStream();

	public static boolean debugOn;
	
	public static final class DebugStream extends StringWriter {
		
		public void println(String str) {
			if (debugOn) {
				System.err.println(str);
			}
		}
		int before = 0;
		boolean due = false;
		
	}
	
	public static boolean isInArray(String ch, String[] a) {
		boolean contains = false;
		for (String c : a) {
		    if (c.equals(ch)) {
		        contains = true;
		        break;
		    }
		}
		return contains;
	}
	public static String ArrayToRegex(String[] array) {
		String regex = null;
		for(String symbol : array) {
			if (regex != null) {
				regex+="|\\"+symbol;
			} else {
				regex = "\\"+symbol;
			}
		}
		return regex;
	}
	public static String[] concat(String[] a, String[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   String[] c= new String[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
	}
	public static String[] add(String[] a, String b) {
		   int aLen = a.length;
		   String[] c= new String[aLen+1];
		   System.arraycopy(a, 0, c, 0, aLen);
		   c[aLen] = b;
		   return c;
	}
	
	public static boolean ciSonoSoloNumeriESomme(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Termine || fl.get(i) instanceof Somma || fl.get(i) instanceof Parentesi)) {
				return false;
			}
		}
		return true;
	}

	public static boolean ciSonoFunzioniSNnonImpostate(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof FunzioneSingola) {
				if (((FunzioneSingola)fl.get(i)).variable == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean ciSonoAltreFunzioni(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Termine || fl.get(i) instanceof Somma || fl.get(i) instanceof Parentesi || fl.get(i) instanceof FunzioneSingola || fl.get(i) instanceof Moltiplicazione || fl.get(i) instanceof Divisione)) {
				return true;
			}
		}
		return false;
	}

	public static Rational getRational(BigDecimal str) {
		return getRational(str.toString());
	}
	
	public static Rational getRational(String str) {
		try {
			return new Rational(str);
		} catch (NumberFormatException ex) {
			long bits = Double.doubleToLongBits(Double.parseDouble(str));

			long sign = bits >>> 63;
			long exponent = ((bits >>> 52) ^ (sign << 11)) - 1023;
			long fraction = bits << 12; // bits are "reversed" but that's not a problem

			long a = 1L;
			long b = 1L;

			for (int i = 63; i >= 12; i--) {
			    a = a * 2 + ((fraction >>> i) & 1);
			    b *= 2;
			}

			if (exponent > 0)
			    a *= 1 << exponent;
			else
			    b *= 1 << -exponent;

			if (sign == 1)
			    a *= -1;
			
			if (b == 0) {
				a = 0;
				b = 1;
			}

			
			return new Rational(new BigInteger(a+""),new BigInteger(b+""));
		}
	}
	
	public static BigDecimal rationalToIrrationalString(Rational r) {
		return BigDecimalMath.divideRound(new BigDecimal(r.numer()).setScale(Utils.scale, Utils.scaleMode), new BigDecimal(r.denom()).setScale(Utils.scale, Utils.scaleMode));
	}
	public static boolean variabiliUguali(ArrayList<VariabileEdEsponente> variables, ArrayList<VariabileEdEsponente> variables2) {
		if (variables.size() != variables2.size()) {
			return false;
		} else {
			for (VariabileEdEsponente v : variables) {
				if (!variables2.contains(v)) {
					return false;
				}
			}
			return true;
		}
	}
}
