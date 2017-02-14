package org.warp.picalculator.math;

import java.util.ArrayList;

import org.warp.picalculator.Error;

public abstract class FunctionSingle implements Function {
	
	/**
	 * Create a new instance of FunctionSingle. The Math Context will be the same of <strong>value</strong>'s.
	 * @throws NullPointerException when value is null.
	 * @param value The parameter of this function.
	 */
	public FunctionSingle(Function value) throws NullPointerException {
		mathContext = value.getMathContext();
		parameter = value;
	}
	
	/**
	 * Create a new instance of FunctionSingle.
	 * @param mathContext Math Context
	 * @param value The parameter of this function.
	 */
	public FunctionSingle(MathContext mathContext, Function value) {
		this.mathContext = mathContext;
		parameter = value;
	}
	
	private final MathContext mathContext;
	
	/**
	 * Function parameter.<br>
	 * <u>MUST NOT BE MODIFIED IF ALREADY SET UP.</u>
	 */
	protected Function parameter;

	/**
	 * 
	 * @return Parameter.
	 */
	public Function getParameter() {
		return parameter;
	}
	/**
	 * 
	 * @param var Parameter.
	 * @return A new instance of this function.
	 */
	public FunctionSingle setParameter(Function value) {
		FunctionSingle s = this.clone();
		s.parameter = value;
		return s;
	}

	@Override
	public FunctionSingle setParameter(int index, Function var) throws IndexOutOfBoundsException {
		if (index == 0) {
			return this.setParameter(var);
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public Function getParameter(int index) throws IndexOutOfBoundsException {
		if (index == 0) {
			return this.getParameter();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public MathContext getMathContext() {
		return mathContext;
	}

	@Override
	public final ArrayList<Function> simplify() throws Error {
		final boolean simplified = parameter.isSimplified();
		ArrayList<Function> result = simplified ? solve() : null;

		if (result == null || result.isEmpty()) {
			result = new ArrayList<>();

			final ArrayList<Function> l1 = new ArrayList<>();
			if (parameter.isSimplified()) {
				l1.add(parameter);
			} else {
				l1.addAll(parameter.simplify());
			}

			for (final Function f : l1) {
				result.add(this.setParameter(f));
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
	public boolean isSimplified() {
		return parameter.isSimplified() ? !isSolvable() : false;
	}

	/**
	 * The current simplification status of this function, assuming that its children are already simplified.
	 * @return <strong>true</strong> if this function can be solved, otherwise <strong>false</strong>.
	 */
	protected abstract boolean isSolvable();

	@Override
	public abstract FunctionSingle clone();
	
	@Override
	public int hashCode() {
		return parameter.hashCode() + 883 * super.hashCode();
	}

	@Override
	public abstract boolean equals(Object o);
}
