package org.warp.picalculator.math.functions;

import java.util.Arrays;
import java.util.List;

import org.warp.picalculator.Error;

import com.rits.cloning.Cloner;

public abstract class FunctionMultipleValues implements Function {
	public FunctionMultipleValues(Function parent) {
		setParent(parent);
		setVariables(new Function[] {});
	}

	public FunctionMultipleValues(Function parent, Function[] values) {
		setParent(parent);
		setVariables(values);
	}

	protected Function parent;
	protected Function[] variables;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Function[] getVariables() {
		return variables;
	}

	public void setVariables(Function[] value) {
		for (Function f : value) {
			f.setParent(this);
		}
		variables = value;
	}

	public void setVariables(final List<Function> value) {
		int vsize = value.size();
		Function[] tmp = new Function[vsize];
		for (int i = 0; i < vsize; i++) {
			tmp[i] = value.get(i);
			tmp[i].setParent(this);
		}
		variables = tmp;
	}

	public Function getVariable(int index) {
		return variables[index];
	}

	public void setVariable(int index, Function value) {
		value.setParent(this);
		variables[index] = value;
	}

	public void addVariableToEnd(Function value) {
		value.setParent(this);
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
	public boolean isSolved() throws Error {
		for (Function variable : variables) {
			if (!variable.isSolved()) {
				return false;
			}
		}
		return !isSolvable();
	}
	
	protected abstract boolean isSolvable() throws Error;


	public Function setParent(Function value) {
		parent = value;
		return this;
	}
	
	public Function getParent() {
		return parent;
	}
	
	@Override
	public abstract void generateGraphics();
	
	@Override
	public String toString() {
//		try {
//			return solve().toString();
			return "TODO: fare una nuova alternativa a solve().toString()";
//		} catch (Error e) {
//			return e.id.toString();
//		}
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
	
	@Override
	public int hashCode() {
		return variables.hashCode()+883*getSymbol().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o.hashCode() == this.hashCode();
	}
}
