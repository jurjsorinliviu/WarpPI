package org.nevec.rjm;

import java.math.MathContext;
import java.math.RoundingMode;

import org.warp.picalculator.Utils;

public final class SafeMathContext {
	
	public static MathContext newMathContext(int precision) {
		if (precision <= 0) {
			Utils.out.print(Utils.OUTPUTLEVEL_DEBUG_MIN, "Warning! MathContext precision is <= 0 ("+precision+")");
			precision = 1;
		}
		return new MathContext(precision);
	}

	public static MathContext newMathContext(int precision, RoundingMode roundingMode) {
		if (precision <= 0) {
			Utils.out.print(Utils.OUTPUTLEVEL_DEBUG_MIN, "Warning! MathContext precision is <= 0 ("+precision+")");
			precision = 1;
		}
		return new MathContext(precision, roundingMode);
	}

}
