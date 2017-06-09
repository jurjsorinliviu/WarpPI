package org.warp.picalculator.math.parser.steps;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.IntegerObj;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.parser.MathParserStep;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Puts the argument of Single Functions inside them
 * @author Andrea Cavalli
 *
 */
public class FixSingleFunctionArgs implements MathParserStep {

	@Override
	public boolean eval(IntegerObj curIndex, Function lastFunction, Function currentFunction,
			ObjectArrayList<Function> functionsList) throws Error {
		if (currentFunction instanceof FunctionSingle) {
			if (((FunctionSingle) currentFunction).getParameter() == null) {
				if (lastFunction == null) {
					throw new Error(Errors.MISSING_ARGUMENTS, "There is a function at the end without any argument specified.");
				} else {
					((FunctionSingle) currentFunction).setParameter(lastFunction);
					functionsList.remove(curIndex.i+1);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean requiresReversedIteration() {
		return true;
	}

	@Override
	public String getStepName() {
		return "Fix Single Function Arguments";
	}

}
