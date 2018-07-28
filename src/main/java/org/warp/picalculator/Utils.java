package org.warp.picalculator;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.Rational;
import org.warp.picalculator.deps.StorageUtils;
import org.warp.picalculator.device.HardwareDevice;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.functions.*;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.equations.Equation;
import org.warp.picalculator.math.functions.equations.EquationsSystemPart;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Utils {

	public static final int scale = 24;
	public static final int displayScale = 8;
	public static final BigInteger maxFactor = BigInteger.valueOf(1000000L);

	public static final int scaleMode = BigDecimal.ROUND_HALF_UP;
	public static final RoundingMode scaleMode2 = RoundingMode.HALF_UP;

	public static boolean debugThirdScreen;
	public static boolean headlessOverride = false;
	public static String forceEngine;
	public static boolean msDosMode;
	public static boolean debugCache;
	public static boolean newtMode = true;

	public static boolean isInArray(String ch, String[] a) {
		boolean contains = false;
		for (final String c : a) {
			if (c.equals(ch)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public static boolean isInArray(char ch, char[] a) {
		boolean contains = false;
		for (final char c : a) {
			if (c == ch) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	private static final String[] regexNormalSymbols = new String[] { "\\", ".", "[", "]", "{", "}", "(", ")", "*", "+", "-", "?", "^", "$", "|" };

	public static String ArrayToRegex(String[] array) {
		String regex = null;
		for (final String symbol : array) {
			boolean contained = false;
			for (final String smb : regexNormalSymbols) {
				if (smb.equals(symbol)) {
					contained = true;
					break;
				}
			}
			if (contained) {
				if (regex != null) {
					regex += "|\\" + symbol;
				} else {
					regex = "\\" + symbol;
				}
			} else {
				if (regex != null) {
					regex += "|" + symbol;
				} else {
					regex = symbol;
				}
			}
		}
		return regex;
	}

	public static String ArrayToRegex(char[] array) {
		String regex = null;
		for (final char symbol : array) {
			boolean contained = false;
			for (final String smb : regexNormalSymbols) {
				if ((smb).equals(symbol + "")) {
					contained = true;
					break;
				}
			}
			if (contained) {
				if (regex != null) {
					regex += "|\\" + symbol;
				} else {
					regex = "\\" + symbol;
				}
			} else {
				if (regex != null) {
					regex += "|" + symbol;
				} else {
					regex = symbol + "";
				}
			}
		}
		return regex;
	}

	public static String[] concat(String[] a, String[] b) {
		final int aLen = a.length;
		final int bLen = b.length;
		final String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static char[] concat(char[] a, char[] b) {
		final int aLen = a.length;
		final int bLen = b.length;
		final char[] c = new char[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static String[] add(String[] a, String b) {
		final int aLen = a.length;
		final String[] c = new String[aLen + 1];
		System.arraycopy(a, 0, c, 0, aLen);
		c[aLen] = b;
		return c;
	}

	public static char[] add(char[] a, char b) {
		final int aLen = a.length;
		final char[] c = new char[aLen + 1];
		System.arraycopy(a, 0, c, 0, aLen);
		c[aLen] = b;
		return c;
	}

	public static boolean areThereOnlySettedUpFunctionsSumsEquationsAndSystems(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Sum || fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Subtraction || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof FunctionSingle) {
					if (((FunctionSingle) fl.get(i)).getParameter() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionOperator) {
					if (((FunctionOperator) fl.get(i)).getParameter1() == null || ((FunctionOperator) fl.get(i)).getParameter2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsSumsMultiplicationsEquationsAndSystems(
			ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Multiplication || fl.get(i) instanceof Sum || fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Subtraction || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof FunctionSingle) {
					if (((FunctionSingle) fl.get(i)).getParameter() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionOperator) {
					if (((FunctionOperator) fl.get(i)).getParameter1() == null || ((FunctionOperator) fl.get(i)).getParameter2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsEquationsAndSystems(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof FunctionSingle) {
					if (((FunctionSingle) fl.get(i)).getParameter() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionOperator) {
					if (((FunctionOperator) fl.get(i)).getParameter1() == null || ((FunctionOperator) fl.get(i)).getParameter2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsAndSystems(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof FunctionSingle) {
					if (((FunctionSingle) fl.get(i)).getParameter() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionOperator) {
					if (((FunctionOperator) fl.get(i)).getParameter1() == null || ((FunctionOperator) fl.get(i)).getParameter2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlyEmptySNFunctions(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof FunctionSingle) {
				if (((FunctionSingle) fl.get(i)).getParameter() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereOnlyEmptyNSNFunctions(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof FunctionOperator && !(fl.get(i) instanceof Sum) && !(fl.get(i) instanceof SumSubtraction) && !(fl.get(i) instanceof Subtraction) && !(fl.get(i) instanceof Multiplication) && !(fl.get(i) instanceof Division)) {
				if (((FunctionOperator) fl.get(i)).getParameter1() == null && ((FunctionOperator) fl.get(i)).getParameter2() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereEmptyMultiplications(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Multiplication || fl.get(i) instanceof Division) {
				if (((FunctionOperator) fl.get(i)).getParameter1() == null && ((FunctionOperator) fl.get(i)).getParameter2() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereEmptySums(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Sum || fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Subtraction) {
				if (((FunctionOperator) fl.get(i)).getParameter1() == null && ((FunctionOperator) fl.get(i)).getParameter2() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereEmptySystems(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof EquationsSystemPart) {
				if (((EquationsSystemPart) fl.get(i)).getParameter() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereOtherSettedUpFunctions(ObjectArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Sum || fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Expression || fl.get(i) instanceof FunctionSingle || fl.get(i) instanceof Multiplication || fl.get(i) instanceof Division)) {
				if (fl.get(i) instanceof FunctionSingle) {
					if (((FunctionSingle) fl.get(i)).getParameter() == null) {
						return true;
					}
				} else if (fl.get(i) instanceof FunctionOperator) {
					if (((FunctionOperator) fl.get(i)).getParameter1() == null || ((FunctionOperator) fl.get(i)).getParameter2() == null) {
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
		} catch (final Error e) {
			//E' IMPOSSIBILE CHE VENGA THROWATO UN ERRORE
			return new Rational("0");
		}
	}

	public static Rational getRational(String str) throws Error {
		try {
			return new Rational(str);
		} catch (final NumberFormatException ex) {
			if (new BigDecimal(str).compareTo(new BigDecimal(8000.0)) < 0 && new BigDecimal(str).compareTo(new BigDecimal(-8000.0)) > 0) {
				if (str.equals("-")) {
					str = "-1";
				}
				final long bits = Double.doubleToLongBits(Double.parseDouble(str));

				final long sign = bits >>> 63;
				final long exponent = ((bits >>> 52) ^ (sign << 11)) - 1023;
				final long fraction = bits << 12; // bits are "reversed" but that's
				// not a problem

				long a = 1L;
				long b = 1L;

				for (int i = 63; i >= 12; i--) {
					a = a * 2 + ((fraction >>> i) & 1);
					b *= 2;
				}

				if (exponent > 0) {
					a *= 1 << exponent;
				} else {
					b *= 1 << -exponent;
				}

				if (sign == 1) {
					a *= -1;
				}

				if (b == 0) {
					a = 0;
					b = 1;
				}

				return new Rational(new BigInteger(a + ""), new BigInteger(b + ""));
			} else {
				final BigDecimal original = new BigDecimal(str);

				final BigInteger numerator = original.unscaledValue();

				final BigInteger denominator = BigDecimalMath.pow(BigDecimal.TEN, new BigDecimal(original.scale())).toBigIntegerExact();

				return new Rational(numerator, denominator);
			}
		}
	}

	public static BigDecimal rationalToIrrationalString(Rational r) {
		return BigDecimalMath.divideRound(new BigDecimal(r.numer()).setScale(Utils.scale, Utils.scaleMode), new BigDecimal(r.denom()).setScale(Utils.scale, Utils.scaleMode));
	}

	public static boolean equalsVariables(ObjectArrayList<Variable> variables, ObjectArrayList<Variable> variables2) {
		if (variables.size() != variables2.size()) {
			return false;
		} else {
			for (final Variable v : variables) {
				if (!variables2.contains(v)) {
					return false;
				}
			}
			return true;
		}
	}

	@Deprecated
	public static void writeSquareRoot(Function var, int x, int y, boolean small) {
//		var.setSmall(small);
//		final int w1 = var.getWidth();
//		final int h1 = var.getHeight();
//		final int wsegno = 5;
//		final int hsegno = h1 + 2;
//
//		var.draw(x + wsegno, y + (hsegno - h1), null, null);
//
//		HardwareDevice.INSTANCE.getDisplayManager().renderer.glDrawLine(x + 1, y + hsegno - 3, x + 1, y + hsegno - 3);
//		HardwareDevice.INSTANCE.getDisplayManager().renderer.glDrawLine(x + 2, y + hsegno - 2, x + 2, y + hsegno - 2);
//		HardwareDevice.INSTANCE.getDisplayManager().renderer.glDrawLine(x + 3, y + hsegno - 1, x + 3, y + hsegno - 1);
//		HardwareDevice.INSTANCE.getDisplayManager().renderer.glDrawLine(x + 3, y + (hsegno - 1) / 2 + 1, x + 3, y + hsegno - 1);
//		HardwareDevice.INSTANCE.getDisplayManager().renderer.glDrawLine(x + 4, y, x + 4, y + (hsegno - 1) / 2);
//		HardwareDevice.INSTANCE.getDisplayManager().renderer.glDrawLine(x + 4, y, x + 4 + 1 + w1 + 1, y);
	}

	public static final int getFontHeight() {
		return getFontHeight(false);
	}

	public static final BinaryFont getFont(boolean small) {
		return getFont(small, StaticVars.zoomed);
	}

	public static final BinaryFont getFont(boolean small, boolean zoomed) {
		return HardwareDevice.INSTANCE.getDisplayManager().fonts[getFontIndex(small, zoomed)];
	}

	public static final int getFontIndex(boolean small, boolean zoomed) {
		if (small) {
			if (zoomed) {
				return 3;
			} else {
				return 1;
			}
		} else {
			if (zoomed) {
				return 2;
			} else {
				return 0;
			}
		}
	}

	public static final int getFontHeight(boolean small) {
		return getFontHeight(small, StaticVars.zoomed);
	}

	public static final int getFontHeight(boolean small, boolean zoomed) {
		if (small) {
			if (zoomed) {
				return HardwareDevice.INSTANCE.getDisplayManager().glyphsHeight[3];
			} else {
				return HardwareDevice.INSTANCE.getDisplayManager().glyphsHeight[1];
			}
		} else {
			if (zoomed) {
				return HardwareDevice.INSTANCE.getDisplayManager().glyphsHeight[2];
			} else {
				return HardwareDevice.INSTANCE.getDisplayManager().glyphsHeight[0];
			}
		}
	}

	
	public static byte[] convertStreamToByteArray(InputStream stream, long size) throws IOException {

		// check to ensure that file size is not larger than Integer.MAX_VALUE.
		if (size > Integer.MAX_VALUE) {
			return new byte[0];
		}

		final byte[] buffer = new byte[(int) size];
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

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
		final int len = bytes.length;
		final int[] realbytes = new int[len];
		for (int i = 0; i < len; i++) {
			realbytes[i] = bytes[i] & 0xFF;
		}
		return realbytes;
	}

	public static Function[][] joinFunctionsResults(List<Function> l1, List<Function> l2) {
		final int size1 = l1.size();
		final int size2 = l2.size();
		int cur1 = 0;
		int cur2 = 0;
		final int total = size1 * size2;
		final Function[][] results = new Function[total][2];
		for (int i = 0; i < total; i++) {
			results[i] = new Function[] { l1.get(cur1), l2.get(cur2) };
			if (i % size2 == 0) {
				cur1 += 1;
			}
			if (i % size1 == 0) {
				cur2 += 1;
			}
			if (cur1 >= size1) {
				cur1 = 0;
			}
			if (cur2 >= size2) {
				cur2 = 0;
			}
		}
		return results;
	}

	public static Function[][] joinFunctionsResults(ObjectArrayList<ObjectArrayList<Function>> ln) {
		final int[] sizes = new int[ln.size()];
		for (int i = 0; i < ln.size(); i++) {
			sizes[i] = ln.get(i).size();
		}
		final int[] curs = new int[sizes.length];
		int total = 0;
		for (int i = 0; i < ln.size(); i++) {
			if (i == 0) {
				total = sizes[i];
			} else {
				total *= sizes[i];
			}
		}
		final Function[][] results = new Function[total][sizes.length];
		for (int i = 0; i < total; i++) {
			results[i] = new Function[sizes.length];
			for (int j = 0; j < sizes.length; j++) {
				results[i][j] = ln.get(j).get(curs[j]);
			}
			for (int k = 0; k < sizes.length; k++) {
				if (i % sizes[k] == 0) {
					for (int l = 0; l < sizes.length; l++) {
						if (l != k) {
							curs[l] += 1;
						}
					}
				}
			}
			for (int k = 0; k < sizes.length; k++) {
				if (curs[k] >= sizes[k]) {
					curs[k] = 0;
				}
			}
		}
		return results;
	}

	public static boolean isNegative(Function b) {
		if (b instanceof Negative) {
			return true;
		} else if (b instanceof Number && ((Number) b).getTerm().compareTo(BigDecimal.ZERO) < 0) {
			return true;
		}
		return false;
	}

	public static CharSequence multipleChars(String string, int i) {
		String result = "";
		for (int j = 0; j < i; j++) {
			result += string;
		}
		return result;
	}

	public static boolean isIntegerValue(BigDecimal bd) {
		return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
	}

	@SafeVarargs
	public static <T> String arrayToString(T... data) {
		String sdata = "";
		for (final T o : data) {
			sdata += "," + o;
		}
		return sdata.substring(1);
	}

	public static String arrayToString(boolean... data) {
		String sdata = "";
		for (final boolean o : data) {
			sdata += (o) ? 1 : 0;
		}
		return sdata;
	}

	public static boolean isRunningOnRaspberry() {
		if (PlatformUtils.osName.equals("Linux")) {
			final File file = new File("/etc", "os-release");
			try (FileInputStream fis = new FileInputStream(file); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis))) {
				String string;
				while ((string = bufferedReader.readLine()) != null) {
					if (string.toLowerCase().contains("raspbian")) {
						if (string.toLowerCase().contains("name")) {
							return true;
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean isWindows() {
		return (PlatformUtils.osName.indexOf("win") >= 0);
	}

	public static <T> ObjectArrayList<T> newArrayList(T o) {
		final ObjectArrayList<T> t = new ObjectArrayList<>();
		t.add(o);
		return t;
	}

	public static InputStream getResourceStreamSafe(String string) throws IOException, URISyntaxException {
		try {
			return StorageUtils.getResourceStream(string);
		} catch (final Exception ex) {
			return null;
		}
	}

	public static Path getJarDirectory() {
		return Paths.get("").toAbsolutePath();
	}
}
