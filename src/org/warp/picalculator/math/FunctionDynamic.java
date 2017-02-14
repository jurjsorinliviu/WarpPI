package org.warp.picalculator.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;

import com.rits.cloning.Cloner;

public abstract class FunctionDynamic implements Function {
	public FunctionDynamic(MathContext root) {
		this.root = root;
		functions = new Function[] {};
	}

	public FunctionDynamic(Function[] values) {
		if (values.length > 0) {
			root = values[0].getMathContext();
		} else {
			throw new NullPointerException("Nessun elemento nell'array. Impossibile ricavare il nodo root");
		}
		functions = values;
	}

	public FunctionDynamic(MathContext root, Function[] values) {
		this.root = root;
		functions = values;
	}

	protected final MathContext root;
	protected Function[] functions;

	public Function[] getParameters() {
		return Arrays.copyOf(functions, functions.length);
	}

	public FunctionDynamic setParameters(final List<Function> value) {
		FunctionDynamic f = this.clone();
		final int vsize = value.size();
		final Function[] tmp = new Function[vsize];
		for (int i = 0; i < vsize; i++) {
			tmp[i] = value.get(i);
		}
		f.functions = tmp;
		return f;
	}

	public FunctionDynamic setParameters(final Function[] value) {
		FunctionDynamic f = this.clone();
		f.functions = value;
		return f;
	}
	
	@Override
	public Function getParameter(int index) {
		return functions[index];
	}

	@Override
	public FunctionDynamic setParameter(int index, Function value) {
		FunctionDynamic f = this.clone();
		f.functions[index] = value;
		return f;
	}

	public FunctionDynamic appendParameter(Function value) {
		FunctionDynamic f = this.clone();
		f.functions = Arrays.copyOf(f.functions, f.functions.length + 1);
		f.functions[f.functions.length - 1] = value;
		return f;
	}

	/**
	 * Retrieve the current number of parameters.
	 * @return The number of parameters.
	 */
	public int getParametersLength() {
		return functions.length;
	}

	public FunctionDynamic setParametersLength(int length) {
		FunctionDynamic f = this.clone();
		f.functions = Arrays.copyOf(functions, length);
		return f;
	}
	
	@Override
	public boolean isSimplified() {
		for (final Function variable : functions) {
			if (variable.isSimplified() == false) {
				return false;
			}
		}
		return !isSolvable();
	}
	
	/**
	 * The current simplification status of this function, assuming that its children are already simplified.
	 * @return <strong>true</strong> if this function can be solved, otherwise <strong>false</strong>.
	 */
	protected abstract boolean isSolvable();

	@Override
	public final ArrayList<Function> simplify() throws Error {
		boolean solved = true;
		Function[] fncs = getParameters();
		for (Function f : fncs) {
			if (f.isSimplified() == false) {
				solved = false;
				break;
			}
		}
		ArrayList<Function> result = solved ? solve() : null;

		if (result == null || result.isEmpty()) {
			result = new ArrayList<>();

			final ArrayList<ArrayList<Function>> ln = new ArrayList<>();
			for (int i = 0; i < fncs.length; i++) {
				ArrayList<Function> l = new ArrayList<>();
				if (fncs[i].isSimplified()) {
					l.add(fncs[i]);
				} else {
					l.addAll(fncs[i].simplify());
				}
				ln.add(l);
			}

			final Function[][] results = Utils.joinFunctionsResults(ln);

			for (final Function[] f : results) {
				result.add(this.setParameters(f));
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
	public MathContext getMathContext() {
		return root;
	}

	@Override
	public abstract FunctionDynamic clone();

	@Override
	public int hashCode() {
		return functions.hashCode() + 883 * super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}
}
