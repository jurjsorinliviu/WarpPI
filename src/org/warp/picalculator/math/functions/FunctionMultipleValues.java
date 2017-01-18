package org.warp.picalculator.math.functions;

import java.util.Arrays;
import java.util.List;

import org.warp.picalculator.math.Calculator;

import com.rits.cloning.Cloner;

public abstract class FunctionMultipleValues implements Function {
	public FunctionMultipleValues(Calculator root) {
		this.root = root;
		functions = new Function[] {};
	}

	public FunctionMultipleValues(Function[] values) {
		if (values.length > 0) {
			this.root = values[0].getRoot();
		} else {
			throw new NullPointerException("Nessun elemento nell'array. Impossibile ricavare il nodo root");
		}
		functions = values;
	}

	public FunctionMultipleValues(Calculator root, Function[] values) {
		this.root = root;
		functions = values;
	}

	protected final Calculator root;
	protected Function[] functions;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Function[] getVariables() {
		return functions;
	}

	public void setVariables(final List<Function> value) {
		int vsize = value.size();
		Function[] tmp = new Function[vsize];
		for (int i = 0; i < vsize; i++) {
			tmp[i] = value.get(i);
		}
		functions = tmp;
	}

	public void setVariables(final Function[] value) {
		functions = value;
	}

	public Function getVariable(int index) {
		return functions[index];
	}

	public void setVariable(int index, Function value) {
		functions[index] = value;
	}

	public void addFunctionToEnd(Function value) {
		int index = functions.length;
		setVariablesLength(index + 1);
		functions[index] = value;
	}

	public int getVariablesLength() {
		return functions.length;
	}

	public void setVariablesLength(int length) {
		functions = Arrays.copyOf(functions, length);
	}

	@Override
	public abstract String getSymbol();
	
	@Override
	public boolean isSolved() {
		for (Function variable : functions) {
			if (!variable.isSolved()) {
				return false;
			}
		}
		return !isSolvable();
	}
	
	protected abstract boolean isSolvable();

	@Override
	public Calculator getRoot() {
		return root;
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
		return functions.hashCode()+883*getSymbol().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return false;
	}
}
