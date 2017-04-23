package org.warp.picalculator.math;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.math.functions.equations.Equation;

public interface SolveMethod {
	public static final SolveMethod[] techniques = new SolveMethod[] {};

	public abstract ObjectArrayList<Equation> solve(Equation equation);
}
