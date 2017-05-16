package org.warp.picalculator.math;

import org.warp.picalculator.math.functions.equations.Equation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public interface SolveMethod {
	public static final SolveMethod[] techniques = new SolveMethod[] {};

	public abstract ObjectArrayList<Equation> solve(Equation equation);
}
