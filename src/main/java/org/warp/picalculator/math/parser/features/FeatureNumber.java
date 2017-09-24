package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.parser.features.interfaces.FeatureBasic;

public class FeatureNumber implements FeatureBasic {
	private String numberString;

	public FeatureNumber(char c) {
		numberString = c + "";
	}

	public FeatureNumber(String s) {
		numberString = s;
	}

	public FeatureNumber() {
		numberString = "";
	}

	public String getNumberString() {
		return numberString;
	}

	public void append(char ch) {
		numberString += ch;
	}

	@Override
	public Number toFunction(MathContext context) throws Error {
		String nmbstr = getNumberString();
		if (nmbstr.charAt(0) == '.') {
			nmbstr = '0' + nmbstr;
		} else if (nmbstr.charAt(nmbstr.length() - 1) == '.') {
			nmbstr += "0";
		} else if (nmbstr.length() == 1) {
			if (nmbstr.charAt(0) == MathematicalSymbols.MINUS) {
				nmbstr += "1";
			} else if (nmbstr.charAt(0) == MathematicalSymbols.SUBTRACTION) {
				nmbstr += "1";
			}
		}
		return new Number(context, nmbstr);
	}
}
