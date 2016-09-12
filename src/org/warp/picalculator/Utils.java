package org.warp.picalculator;

import static org.warp.engine.Display.Render.glDrawLine;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.Rational;

public class Utils {

	public static final int scale = 24;
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
		for (String symbol : array) {
			if (regex != null) {
				regex += "|\\" + symbol;
			} else {
				regex = "\\" + symbol;
			}
		}
		return regex;
	}

	public static String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static String[] add(String[] a, String b) {
		int aLen = a.length;
		String[] c = new String[aLen + 1];
		System.arraycopy(a, 0, c, 0, aLen);
		c[aLen] = b;
		return c;
	}

	public static boolean areThereOnlySettedUpFunctionsSumsEquationsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Sum || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunctionBase) {
					if (((AnteriorFunctionBase) fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValuesBase) {
					if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null || ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsSumsMultiplicationsEquationsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Multiplication || fl.get(i) instanceof PrioritaryMultiplication || fl.get(i) instanceof Sum || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunctionBase) {
					if (((AnteriorFunctionBase) fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValuesBase) {
					if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null || ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsEquationsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunctionBase) {
					if (((AnteriorFunctionBase) fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValuesBase) {
					if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null || ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunctionBase) {
					if (((AnteriorFunctionBase) fl.get(i)).variable == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValuesBase) {
					if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null || ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlyEmptySNFunctions(ArrayList<FunctionBase> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof AnteriorFunctionBase) {
				if (((AnteriorFunctionBase) fl.get(i)).variable == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereOnlyEmptyNSNFunctions(ArrayList<FunctionBase> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof FunctionTwoValuesBase && !(fl.get(i) instanceof Sum) && !(fl.get(i) instanceof Subtraction) && !(fl.get(i) instanceof Multiplication) && !(fl.get(i) instanceof PrioritaryMultiplication) && !(fl.get(i) instanceof Division)) {
				if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null && ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}


	public static boolean areThereEmptyPrioritaryMultiplications(ArrayList<FunctionBase> funzioniOLD) {
		for (int i = 0; i < funzioniOLD.size(); i++) {
			if (funzioniOLD.get(i) instanceof PrioritaryMultiplication) {
				if (((FunctionTwoValuesBase) funzioniOLD.get(i)).variable1 == null && ((FunctionTwoValuesBase) funzioniOLD.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean areThereEmptyMultiplications(ArrayList<FunctionBase> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Multiplication || fl.get(i) instanceof Division) {
				if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null && ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereEmptySums(ArrayList<FunctionBase> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Sum) {
				if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null && ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereEmptySystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof EquationsSystemPart) {
				if (((EquationsSystemPart) fl.get(i)).variable == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereOtherSettedUpFunctions(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Sum || fl.get(i) instanceof Expression || fl.get(i) instanceof AnteriorFunctionBase || fl.get(i) instanceof Multiplication || fl.get(i) instanceof PrioritaryMultiplication || fl.get(i) instanceof Division)) {
				if (fl.get(i) instanceof AnteriorFunctionBase) {
					if (((AnteriorFunctionBase) fl.get(i)).variable == null) {
						return true;
					}
				} else if (fl.get(i) instanceof FunctionTwoValuesBase) {
					if (((FunctionTwoValuesBase) fl.get(i)).variable1 == null || ((FunctionTwoValuesBase) fl.get(i)).variable2 == null) {
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
		try {
			return getRational(str.toString());
		} catch (Error e) {
			//E' IMPOSSIBILE CHE VENGA THROWATO UN ERRORE
			return new Rational("0");
		}
	}

	public static Rational getRational(String str) throws Error {
		try {
			return new Rational(str);
		} catch (NumberFormatException ex) {
			if (new BigDecimal(str).compareTo(new BigDecimal(8000.0)) < 0 && new BigDecimal(str).compareTo(new BigDecimal(-8000.0)) > 0) {
				if (str.equals("-")) {
					str = "-1";
				}
				long bits = Double.doubleToLongBits(Double.parseDouble(str));

				long sign = bits >>> 63;
				long exponent = ((bits >>> 52) ^ (sign << 11)) - 1023;
				long fraction = bits << 12; // bits are "reversed" but that's
											// not a problem

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

				return new Rational(new BigInteger(a + ""), new BigInteger(b + ""));
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

	public static boolean equalsVariables(ArrayList<Variable> variables, ArrayList<Variable> variables2) {
		if (variables.size() != variables2.size()) {
			return false;
		} else {
			for (Variable v : variables) {
				if (!variables2.contains(v)) {
					return false;
				}
			}
			return true;
		}
	}

	public static void writeSquareRoot(Function var, int x, int y, boolean small) {
		var.setSmall(small);
		int w1 = var.getWidth();
		int h1 = var.getHeight();
		int wsegno = 5;
		int hsegno = h1 + 2;
		
		var.draw(x + wsegno, y + (hsegno - h1));

		glDrawLine(x + 1, y + hsegno - 3, x + 3, y + hsegno - 1);
		glDrawLine(x + 3, y + (hsegno - 1) / 2 + 1, x + 3, y + hsegno - 1);
		glDrawLine(x + 4, y, x + 4, y + (hsegno - 1) / 2);
		glDrawLine(x + 4, y, x + 4 + 1 + w1 + 1, y);
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

	public static int getFontHeight(Font font) {
		if (font.getFontName().contains("Big")) {
			return 9;
		} else {
			return 6;
		}
	}
	
	public static byte[] convertStreamToByteArray(InputStream stream, long size) throws IOException {

	    // check to ensure that file size is not larger than Integer.MAX_VALUE.
	    if (size > Integer.MAX_VALUE) {
	        return new byte[0];
	    }

	    byte[] buffer = new byte[(int)size];
	    ByteArrayOutputStream os = new ByteArrayOutputStream();

	    int line = 0;
	    // read bytes from stream, and store them in buffer
	    while ((line = stream.read(buffer)) != -1) {
	        // Writes bytes from byte array (buffer) into output stream.
	        os.write(buffer, 0, line);
	    }
	    stream.close();
	    os.flush();
	    os.close();
	    return os.toByteArray();
	}

	public static int[] realBytes(byte[] bytes) {
		int len = bytes.length;
		int[] realbytes = new int[len];
		for (int i = 0; i < len; i++) {
			realbytes[i] = Byte.toUnsignedInt(bytes[i]);
		}
		return realbytes;
	}
}
