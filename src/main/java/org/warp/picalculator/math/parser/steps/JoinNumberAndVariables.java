package org.warp.picalculator.math.parser.steps;

import org.warp.picalculator.IntegerObj;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.parser.MathParserStep;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class JoinNumberAndVariables implements MathParserStep {

	private MathContext context;
	
	public JoinNumberAndVariables(MathContext context) {
		this.context = context;
	}
	
	@Override
	public boolean eval(IntegerObj curIndex, Function lastFunction, Function currentFunction, ObjectArrayList<Function> functionsList) {
		if (currentFunction instanceof Number | currentFunction instanceof Variable | currentFunction instanceof Division) {
			if (lastFunction instanceof Variable | lastFunction instanceof Number | (lastFunction instanceof Multiplication && ((Multiplication)lastFunction).getParameter2() != null)) {
				final Function a = currentFunction;
				final Function b = lastFunction;
				functionsList.set(curIndex.i, new Multiplication(context, a, b));
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
		return "Join number and variables together";
	}

}
