package org.warp.picalculator.math;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;

public abstract class FunctionOperator implements Function {
	
	/**
	 * Create a new instance of FunctionOperator. The Math Context will be the same of <strong>value1</strong>'s.
	 * @throws NullPointerException when value1 is null.
	 * @param value1 The parameter of this function.
	 * @param value2 The parameter of this function.
	 */
	public FunctionOperator(Function value1, Function value2) throws NullPointerException {
		this.mathContext = value1.getMathContext();
		parameter1 = value1;
		parameter2 = value2;
	}
	
	/**
	 * Create a new instance of FunctionOperator.
	 * @param value1 The parameter of this function.
	 * @param value2 The parameter of this function.
	 */
	public FunctionOperator(MathContext mc, Function value1, Function value2) {
		this.mathContext = mc;
		parameter1 = value1;
		parameter2 = value2;
	}

	protected final MathContext mathContext;

	protected Function parameter1 = null;
	protected Function parameter2 = null;

	/**
	 * 
	 * @return First parameter.
	 */
	public Function getParameter1() {
		return parameter1;
	}

	/**
	 * 
	 * @return Second parameter.
	 */
	public Function getParameter2() {
		return parameter2;
	}

	/**
	 * 
	 * @param var First parameter.
	 * @return A new instance of this function.
	 */
	public FunctionOperator setParameter1(Function var) {
		FunctionOperator s = this.clone();
		s.parameter1 = var;
		return s;
	}

	/**
	 * 
	 * @param var Second parameter.
	 * @return A new instance of this function.
	 */
	public FunctionOperator setParameter2(Function var) {
		FunctionOperator s = this.clone();
		s.parameter2 = var;
		return s;
	}
	
	@Override
	public FunctionOperator setParameter(int index, Function var) throws IndexOutOfBoundsException {
		switch(index) {
			case 0:
				return this.setParameter1(var);
			case 1:
				return this.setParameter2(var);
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public Function getParameter(int index) throws IndexOutOfBoundsException {
		switch(index) {
			case 0:
				return this.getParameter1();
			case 1:
				return this.getParameter2();
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public MathContext getMathContext() {
		return mathContext;
	}

	@Override
	public boolean isSimplified() {
		return (parameter1.isSimplified() & parameter2.isSimplified()) ? !isSolvable() : false;
	}
	
	/**
	 * The current simplification status of this function, assuming that its children are already simplified.
	 * @return <strong>true</strong> if this function can be solved, otherwise <strong>false</strong>.
	 */
	protected abstract boolean isSolvable();

	@Override
	public final ArrayList<Function> simplify() throws Error {
		final boolean solved = parameter1.isSimplified() & parameter2.isSimplified();
		ArrayList<Function> result = solved ? solve() : null;;

		if (result == null || result.isEmpty()) {
			result = new ArrayList<>();

			final ArrayList<Function> l1 = new ArrayList<>();
			final ArrayList<Function> l2 = new ArrayList<>();
			if (parameter1.isSimplified()) {
				l1.add(parameter1);
			} else {
				l1.addAll(parameter1.simplify());
			}
			if (parameter2.isSimplified()) {
				l2.add(parameter2);
			} else {
				l2.addAll(parameter2.simplify());
			}

			final Function[][] results = Utils.joinFunctionsResults(l1, l2);

			for (final Function[] f : results) {
				result.add(this.setParameter1(f[0]).setParameter2(f[1]));
			}
		}

		return result;
	}
	
	/**
	 * Solves only this function, assuming that its children are already simplified and it can be solved.
	 * @return The solved function.
	 * @throws Error Errors during computation, like a/0 or similar.
	 */
	protected abstract ArrayList<Function> solve() throws Error;

	@Override
	public abstract FunctionOperator clone();
	
	@Override
	public int hashCode() {
		return parameter1.hashCode() + 7 * parameter2.hashCode() + 883 * super.hashCode();
	}

	@Override
	public abstract boolean equals(Object o);
}
