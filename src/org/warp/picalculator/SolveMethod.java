package org.warp.picalculator;

import java.util.ArrayList;

public interface SolveMethod {
	public static final SolveMethod[] techniques = new SolveMethod[] {};

	public abstract ArrayList<Equation> solve(Equation equation);
}
