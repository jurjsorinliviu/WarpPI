package org.warp.picalculator.math.functions.equations;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Number;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class EquationsSystem extends FunctionDynamic {
	static final int spacing = 2;

	public EquationsSystem(MathContext root) {
		super(root);
	}

	public EquationsSystem(MathContext root, Function value) {
		super(root, new Function[] { value });
	}

	public EquationsSystem(MathContext root, Function[] value) {
		super(root, value);
	}

	@Override
	protected boolean isSolvable() {
		if (functions.length >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public ObjectArrayList<Function> solve() throws Error {
		final ObjectArrayList<Function> ret = new ObjectArrayList<>();
		if (functions.length == 1) {
			if (functions[0].isSimplified()) {
				ret.add(functions[0]);
				return ret;
			} else {
				final List<Function> l = functions[0].simplify();
				for (final Function f : l) {
					if (f instanceof Number) {
						ret.add(f);
					} else {
						ret.add(new Expression(root, new Function[] { f }));
					}
				}
				return ret;
			}
		} else {
			for (final Function f : functions) {
				if (f.isSimplified() == false) {
					final List<Function> partial = f.simplify();
					for (final Function fnc : partial) {
						ret.add(new Expression(root, new Function[] { fnc }));
					}
				}
			}
			return ret;
		}
	}

	@Override
	public EquationsSystem clone() {
		return new EquationsSystem(root, functions);
	}

}
