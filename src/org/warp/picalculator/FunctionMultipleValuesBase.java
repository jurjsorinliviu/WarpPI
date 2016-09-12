package org.warp.picalculator;

import java.util.Arrays;
import java.util.List;

import com.rits.cloning.Cloner;

public abstract class FunctionMultipleValuesBase extends FunctionBase {
	public FunctionMultipleValuesBase() {
		setVariables(new FunctionBase[] {});
	}

	public FunctionMultipleValuesBase(FunctionBase[] values) {
		setVariables(values);
	}

	protected FunctionBase[] variables;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public FunctionBase[] getVariables() {
		return variables;
	}

	public void setVariables(FunctionBase[] value) {
		variables = value;
	}

	public void setVariables(final List<FunctionBase> value) {
		int vsize = value.size();
		FunctionBase[] tmp = new FunctionBase[vsize];
		for (int i = 0; i < vsize; i++) {
			tmp[i] = value.get(i);
		}
		variables = tmp;
	}

	public FunctionBase getVariable(int index) {
		return variables[index];
	}

	public void setVariable(int index, FunctionBase value) {
		variables[index] = value;
	}

	public void addVariableToEnd(FunctionBase value) {
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
	public abstract Number solve() throws Error;

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
	public FunctionMultipleValuesBase clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}
}
