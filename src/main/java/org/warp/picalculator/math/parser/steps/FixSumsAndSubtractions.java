package org.warp.picalculator.math.parser.steps;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.IntegerObj;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.parser.MathParserStep;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class FixSumsAndSubtractions implements MathParserStep {

	@Override
	public boolean eval(IntegerObj curIndex, Function lastFunction, Function currentFunction,
			ObjectArrayList<Function> functionsList) throws Error {
		if (currentFunction instanceof Sum || currentFunction instanceof Subtraction || currentFunction instanceof SumSubtraction) {
			if (currentFunction.getParameter(0) == null && currentFunction.getParameter(1) == null) {
				if (curIndex.i - 1 >= 0 && curIndex.i + 1 < functionsList.size()) {
					final Function next = functionsList.get(curIndex.i + 1);
					final Function prev = functionsList.get(curIndex.i - 1);
					functionsList.set(curIndex.i, currentFunction.setParameter(0, prev).setParameter(1, next));
					functionsList.remove(curIndex.i + 1);
					functionsList.remove(curIndex.i - 1);
					curIndex.i--;
					return true;
				} else {
					if (currentFunction.getParameter(0) == null || currentFunction.getParameter(1) == null) {
						throw new Error(Errors.MISSING_ARGUMENTS, "There is a function at the end without any argument specified.");
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean requiresReversedIteration() {
		return false;
	}

	@Override
	public String getStepName() {
		return "Fix Sums and Subtractions";
	}

}
