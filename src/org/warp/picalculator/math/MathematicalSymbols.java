package org.warp.picalculator.math;

import static org.warp.picalculator.Utils.concat;

import org.warp.picalculator.Utils;

public class MathematicalSymbols {
	public static final String SUM = "+";
	public static final String SUM_SUBTRACTION = "±";
	public static final String SUBTRACTION = "-";
	public static final String MULTIPLICATION = "*";
	public static final String PRIORITARY_MULTIPLICATION = "▪";
	public static final String DIVISION = "/";
	public static final String NTH_ROOT = "√";
	public static final String SQUARE_ROOT = "Ⓐ";
	public static final String PARENTHESIS_OPEN = "(";
	public static final String PARENTHESIS_CLOSE = ")";
	public static final String POWER = "Ⓑ";
	public static final String EQUATION = "=";
	public static final String SYSTEM = "{";

	public static final String[] functions() {
		return concat(functionsNSN(), functionsSN());
	}

	public static final String[] functionsNSN() {
		return new String[] { NTH_ROOT, POWER };
	}

	public static final String[] functionsSN() {
		return new String[] { SQUARE_ROOT };
	}

	public static final String[] signums(boolean withMultiplication, boolean withPrioritaryMultiplication) {
		String[] ret = new String[] { SUM, SUM_SUBTRACTION, DIVISION };
		if (withMultiplication) {
			ret = Utils.add(ret, MULTIPLICATION);
		}
		if (withPrioritaryMultiplication) {
			ret = Utils.add(ret, PRIORITARY_MULTIPLICATION);
		}
		return ret;
	}

	public static final String[] parentheses() {
		return new String[] { PARENTHESIS_OPEN, PARENTHESIS_CLOSE };
	}

	public static String[] variables() {
		return new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "♓"};
	}

	public static String[] genericSyntax() {
		return new String[] { SYSTEM, EQUATION };
	}
}
