package org.warp.picalculator.math.functions;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;

public class Variable implements Function {

	protected char var;
	protected final MathContext root;
	protected V_TYPE type = V_TYPE.COEFFICIENT;

	public Variable(MathContext root, char val, V_TYPE type) {
		this.root = root;
		var = val;
		this.type = type;
	}

	public Variable(MathContext root, String s, V_TYPE type) throws Error {
		this(root, s.charAt(0), type);
	}

	public char getChar() {
		return var;
	}

	public Variable setChar(char val) {
		return new Variable(root, val, type);
	}

	public V_TYPE getType() {
		return type;
	}

	public Variable setType(V_TYPE typ) {
		return new Variable(root, var, typ);
	}

	@Override
	public String toString() {
		return "" + getChar();
	}

	public static class VariableValue {
		public final Variable v;
		public final Number n;

		public VariableValue(Variable v, Number n) {
			this.v = v;
			this.n = n;
		}
	}

	@Override
	public boolean isSimplified() {
		return true;
	}

	@Override
	public List<Function> simplify() throws Error {
		final List<Function> result = new ObjectArrayList<>();
		result.add(this);
		return result;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Variable) {
			return ((Variable) o).getChar() == var && ((Variable) o).getType() == type;
		}
		return false;
	}

	@Override
	public MathContext getMathContext() {
		return root;
	}

	@Override
	public Variable clone() {
		return new Variable(root, var, type);
	}

	public static enum V_TYPE {
		COEFFICIENT, UNKNOWN, SOLUTION
	}

	@Override
	public Function setParameter(int index, Function var) throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Function getParameter(int index) throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}
}
