package org.warp.picalculator;

import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;

public class TestCalcBenchmark {

	public static void main(String[] args) throws Error {
		Utils.debugOn = true;
		int times = 1;

		MathContext mc = new MathContext();
		long time1 = System.nanoTime();
		mc.parseInputString("5WABCDEFGHIWABCDEFGHIWABCDEFGHIWABCDEFGHIWABCDEFGHIWABCDEFGHI");
		long time2 = System.nanoTime();
		for (int i = 0; i < times; i++) {
			if (i == 1) {
				Utils.debugOn = false;
			}
			mc.f2 = mc.solveExpression(mc.f);
		}
		long time3 = System.nanoTime();
//		System.out.println(mc.f2.get(0).toString());
		System.out.println("PARSING\t"+((time2-time1) / 1000000d / ((double) times)) + " milliseconds");
		System.out.println("WORK\t"+((time3-time2) / 1000000d / ((double) times)) + " milliseconds");
		System.out.println("TOTAL\t"+((time3-time1) / 1000000d / ((double) times)) + " milliseconds");
	}
}
