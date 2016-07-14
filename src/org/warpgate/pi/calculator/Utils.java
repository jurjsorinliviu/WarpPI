package org.warpgate.pi.calculator;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.Rational;

public class Utils {

	public static final int scale = 130;
	public static final int resultScale = 8;

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

	public static boolean ciSonoSoloFunzioniImpostateSommeEquazioniESistemi(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Termine || fl.get(i) instanceof Somma || fl.get(i) instanceof Equazione || fl.get(i) instanceof ParteSistema || fl.get(i) instanceof Parentesi)) {
				if (fl.get(i) instanceof FunzioneAnteriore) {
					if (((FunzioneAnteriore)fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunzioneDueValori) {
					if (((FunzioneDueValori)fl.get(i)).variable1 == null || ((FunzioneDueValori)fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
	public static boolean ciSonoSoloFunzioniImpostateSommeMoltiplicazioniEquazioniESistemi(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Termine || fl.get(i) instanceof Moltiplicazione || fl.get(i) instanceof Somma || fl.get(i) instanceof Equazione || fl.get(i) instanceof ParteSistema || fl.get(i) instanceof Parentesi)) {
				if (fl.get(i) instanceof FunzioneAnteriore) {
					if (((FunzioneAnteriore)fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunzioneDueValori) {
					if (((FunzioneDueValori)fl.get(i)).variable1 == null || ((FunzioneDueValori)fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean ciSonoSoloFunzioniImpostateEquazioniESistemi(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Termine || fl.get(i) instanceof Equazione || fl.get(i) instanceof ParteSistema || fl.get(i) instanceof Parentesi)) {
				if (fl.get(i) instanceof FunzioneAnteriore) {
					if (((FunzioneAnteriore)fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunzioneDueValori) {
					if (((FunzioneDueValori)fl.get(i)).variable1 == null || ((FunzioneDueValori)fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean ciSonoSoloFunzioniImpostateESistemi(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Termine || fl.get(i) instanceof Equazione || fl.get(i) instanceof ParteSistema || fl.get(i) instanceof Parentesi)) {
				if (fl.get(i) instanceof FunzioneAnteriore) {
					if (((FunzioneAnteriore)fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunzioneDueValori) {
					if (((FunzioneDueValori)fl.get(i)).variable1 == null || ((FunzioneDueValori)fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean ciSonoFunzioniSNnonImpostate(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof FunzioneAnteriore && !(fl.get(i) instanceof ParteSistema)) {
				if (((FunzioneAnteriore)fl.get(i)).variable == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean ciSonoFunzioniNSNnonImpostate(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof FunzioneDueValori && !(fl.get(i) instanceof Equazione) && !(fl.get(i) instanceof Somma) && !(fl.get(i) instanceof Sottrazione) && !(fl.get(i) instanceof Moltiplicazione) && !(fl.get(i) instanceof Divisione)) {
				if (((FunzioneDueValori)fl.get(i)).variable1 == null && ((FunzioneDueValori)fl.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean ciSonoMoltiplicazioniNonImpostate(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Moltiplicazione || fl.get(i) instanceof Divisione) {
				if (((FunzioneDueValori)fl.get(i)).variable1 == null && ((FunzioneDueValori)fl.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean ciSonoSommeNonImpostate(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Somma) {
				if (((FunzioneDueValori)fl.get(i)).variable1 == null && ((FunzioneDueValori)fl.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean ciSonoEquazioniNonImpostate(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Equazione) {
				if (((FunzioneDueValori)fl.get(i)).variable1 == null && ((FunzioneDueValori)fl.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean ciSonoSistemiNonImpostati(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof ParteSistema) {
				if (((ParteSistema)fl.get(i)).variable == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean ciSonoAltreFunzioniImpostate(ArrayList<Funzione> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Termine || fl.get(i) instanceof Somma || fl.get(i) instanceof Parentesi || fl.get(i) instanceof FunzioneAnteriore || fl.get(i) instanceof Moltiplicazione || fl.get(i) instanceof Divisione)) {
				if (fl.get(i) instanceof FunzioneAnteriore) {
					if (((FunzioneAnteriore)fl.get(i)).variable == null) {
						return true;
					}
				} else if (fl.get(i) instanceof FunzioneDueValori) {
					if (((FunzioneDueValori)fl.get(i)).variable1 == null || ((FunzioneDueValori)fl.get(i)).variable2 == null) {
						return true;
					}
				} else {
					return true;
				}
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
			if (new BigDecimal(str).compareTo(new BigDecimal(Double.MAX_VALUE)) < 0 && new BigDecimal(str).compareTo(new BigDecimal(Double.MIN_VALUE)) > 0) {
				if (str.equals("-")) {
					str = "-1";
				}
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
			} else {
				BigDecimal original = new BigDecimal(str);
				
				BigInteger numerator = original.unscaledValue();
				
				BigInteger denominator = BigDecimalMath.pow(BigDecimal.TEN, new BigDecimal(original.scale())).toBigIntegerExact();
				
				return new Rational(numerator, denominator);
			}
		}
	}
	
	public static BigDecimal rationalToIrrationalString(Rational r) {
		return BigDecimalMath.divideRound(new BigDecimal(r.numer()).setScale(Utils.scale, Utils.scaleMode), new BigDecimal(r.denom()).setScale(Utils.scale, Utils.scaleMode));
	}
	public static boolean variabiliUguali(ArrayList<Incognita> variables, ArrayList<Incognita> variables2) {
		if (variables.size() != variables2.size()) {
			return false;
		} else {
			for (Incognita v : variables) {
				if (!variables2.contains(v)) {
					return false;
				}
			}
			return true;
		}
	}
	
	public static void writeLetter(Display d, String text, int x, int y, boolean small) {
		if (small) {
			d.setFont(fontSmall);
		} else {
			d.setFont(fontBig);
		}
		d.setColor(Color.BLACK);
		d.drawString(text, x, y+getFontHeight(small));
	}
	
	public static void writeSquareRoot(Display g, Funzione var, int x, int y, boolean small) {
		if (small) {
			g.setFont(fontSmall);
		} else {
			g.setFont(fontBig);
		}
		g.setColor(Color.BLACK);
		int w1 = var.getWidth();
		int h1 = var.getHeight(small);
		int wsegno = 5;
		int hsegno = h1+2;
		
		var.draw(x+wsegno, y+(hsegno-h1), g, small);

		g.draw45Line(x, y+hsegno-3, x+2, y+hsegno-1, false);
		g.drawOrthoLine(x+2, y+(hsegno-1)/2+1, x+2, y+hsegno-1);
		g.drawOrthoLine(x+3, y, x+3, y+(hsegno-1)/2);
		g.drawOrthoLine(x+3, y, x+3+1+w1+2, y);
	}

	public static final int getFontHeight() {
		return getFontHeight(false);
	}
	
	public static final int getFontHeight(boolean small) {
		if (small) {
			return 6;
		} else {
			return 9;
		}
	}
}
