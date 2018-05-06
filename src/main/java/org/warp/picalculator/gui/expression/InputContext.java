package org.warp.picalculator.gui.expression;

import java.util.HashMap;

import org.warp.picalculator.gui.expression.blocks.BlockVariable;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.Variable.V_TYPE;

public class InputContext {
	public final HashMap<Character, V_TYPE> variableTypes;
	public BlockVariable variableTypeDirtyID = null;

	public InputContext() {
		this.variableTypes = new HashMap<>();
		this.variableTypes.put(MathematicalSymbols.PI, V_TYPE.CONSTANT);
		this.variableTypes.put(MathematicalSymbols.EULER_NUMBER, V_TYPE.CONSTANT);
	}

	public InputContext(HashMap<Character, V_TYPE> variableTypes) {
		this.variableTypes = variableTypes;
	}
}
