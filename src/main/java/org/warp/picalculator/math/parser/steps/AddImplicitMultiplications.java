package org.warp.picalculator.math.parser.steps;

import org.warp.picalculator.IntegerObj;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.parser.MathParserStep;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class AddImplicitMultiplications implements MathParserStep {

	private MathContext context;
	
	public AddImplicitMultiplications(MathContext context) {
		this.context = context;
	}
	
	@Override
	public boolean eval(IntegerObj curIndex, Function lastFunction, Function currentFunction, ObjectArrayList<Function> functionsList) {
		if (currentFunction instanceof FunctionSingle) {
			if (lastFunction instanceof Function) {
				functionsList.set(curIndex.i, new Multiplication(context, currentFunction, lastFunction));
				functionsList.remove(curIndex.i + 1);
				return true;
			}
		} else if (currentFunction instanceof Function) {
			if (lastFunction instanceof FunctionSingle) {
				functionsList.set(curIndex.i, new Multiplication(context, currentFunction, lastFunction));
				functionsList.remove(curIndex.i + 1);
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
		return "Add implicit multiplications before and after Single Functions";
	}

}
