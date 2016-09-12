package org.warp.picalculator;

import java.util.Arrays;
import java.util.List;

import com.rits.cloning.Cloner;

public abstract class FunctionMultipleValues implements Function {
	public FunctionMultipleValues() {
		setVariables(new Function[] {});
	}

	public FunctionMultipleValues(Function[] values) {
		setVariables(values);
	}

	protected Function[] variables;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Function[] getVariables() {
		return variables;
	}

	public void setVariables(Function[] value) {
		variables = value;
	}

	public void setVariables(final List<Function> value) {
		int vsize = value.size();
		Function[] tmp = new Function[vsize];
		for (int i = 0; i < vsize; i++) {
			tmp[i] = value.get(i);
		}
		variables = tmp;
	}

	public Function getVariable(int index) {
		return variables[index];
	}

	public void setVariable(int index, Function value) {
		variables[index] = value;
	}

	public void addVariableToEnd(Function value) {
		int index = variables.length;
		setVariablesLength(index + 1);
		variables[index] = value;
	}

	public int getVariablesLength() {
		return variables.length;
	}

	public void setVariablesLength(int length) {
		variables = Arrays.copyOf(variables, length);
	}

	@Override
	public abstract String getSymbol();

	@Override
	public abstract Function solve() throws Error;

	@Override
	public abstract void generateGraphics();
	
	@Override
	public String toString() {
		try {
			return solve().toString();
		} catch (Error e) {
			return e.id.toString();
		}
	}

	@Override
	public Function clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}
}
