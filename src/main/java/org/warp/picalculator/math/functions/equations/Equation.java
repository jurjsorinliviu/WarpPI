package org.warp.picalculator.math.functions.equations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.SolveMethod;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Equation extends FunctionOperator {

	public Equation(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	public List<Function> solve(char variableCharacter) {
		@SuppressWarnings("unused")
		final ObjectArrayList<Equation> e;
		//TODO: WORK IN PROGRESS.
		//TODO: Finire. Fare in modo che risolva i passaggi fino a che non ce ne sono più
		return null;
	}

	//WORK IN PROGRESS
	public ObjectArrayList<Equation> solveStep(char charIncognita) {
		ObjectArrayList<Equation> result = new ObjectArrayList<>();
		result.add(clone());
		for (final SolveMethod t : SolveMethod.techniques) {
			final ObjectArrayList<Equation> newResults = new ObjectArrayList<>();
			final int sz = result.size();
			for (int n = 0; n < sz; n++) {
				newResults.addAll(t.solve(result.get(n)));
			}
			final Set<Equation> hs = new HashSet<>();
			hs.addAll(newResults);
			newResults.clear();
			newResults.addAll(hs);
			result = newResults;
		}
		// TODO: controllare se è a posto
		return result;
	}

	@Override
	public Equation clone() {
		return new Equation(mathContext, parameter1, parameter2);
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		// TODO Auto-generated method stub
		return null;
	}

}