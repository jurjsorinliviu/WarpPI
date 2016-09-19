package org.warp.picalculator.math;

import java.util.ArrayList;

import org.warp.picalculator.math.functions.equations.Equation;

public interface SolveMethod {
	public static final SolveMethod[] techniques = new SolveMethod[] {};

	public abstract ArrayList<Equation> solve(Equation equation);
}
