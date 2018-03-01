package org.warp.picalculator.math.solver;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MathSolver {
	
	private final Function initialFunction;
	private int stepState = 0;
	private int currentStepStateN = 0;
	private int consecutiveNullSteps = 0;
	private enum StepState {
		_1_CALCULATION,
		_2_EXPANSION,
		_3_CALCULATION,
		_4_REDUCTION
	}
	private final StepState[] stepStates = StepState.values();
	@SuppressWarnings("unchecked")
	private final ObjectArrayList<Function>[] lastFunctions = new ObjectArrayList[stepStates.length];
	
	public MathSolver(Function initialFunction) {
		this.initialFunction = initialFunction;
	}
	
	public ObjectArrayList<ObjectArrayList<Function>> solveAllSteps() throws InterruptedException, Error {
		ObjectArrayList<ObjectArrayList<Function>> steps = new ObjectArrayList<>();
		ObjectArrayList<Function> lastFnc = null, currFnc = new ObjectArrayList<>();
		currFnc.add(initialFunction);
		int stepBefore = 0, stepAfter = 0;
		do {
			for (int i = stepBefore; i <= stepAfter; i++) {
				lastFnc = lastFunctions[i] = currFnc;
			}
			stepBefore = stepState;
			ObjectArrayList<Function> stepResult = solveStep(lastFnc);
			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Step state: "+stepStates[stepState]+", Consecutive null steps: " + consecutiveNullSteps + ", currentStepStateN: " + currentStepStateN + ", result: ");
			for (Function result : stepResult) {
				Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, result.toString());
			}
			if (stepResult == null) {
				currFnc = lastFnc;
			} else {
				currFnc = stepResult;
				steps.add(currFnc);
			}
			stepAfter = stepState;
		} while(consecutiveNullSteps < stepStates.length && !checkEquals(currFnc, lastFunctions[stepState]));
		return steps;
	}

	private boolean checkEquals(ObjectArrayList<Function> a, ObjectArrayList<Function> b) {
		if (a == null && b == null) {
			return true;
		} else if (a != null && b != null) {
			if (a.isEmpty() == b.isEmpty()) {
				int size;
				if ((size = a.size()) == b.size()) {
					for (int i = 0; i < size; i++) {
						if (a.get(i).equals(b.get(i)) == false) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private ObjectArrayList<Function> solveStep(ObjectArrayList<Function> fncs) throws InterruptedException, Error {
		ObjectArrayList<Function> processedFncs = applyRules(fncs, RuleType.EXISTENCE); // Apply existence rules before everything
		if (processedFncs != null) {
			fncs = processedFncs;
		}
		RuleType currentAcceptedRules;
		switch(stepStates[stepState]) {
			case _1_CALCULATION: {
				currentAcceptedRules = RuleType.CALCULATION;
				break;
			}
			case _2_EXPANSION: {
				currentAcceptedRules = RuleType.EXPANSION;
				break;
			}
			case _3_CALCULATION: {
				currentAcceptedRules = RuleType.CALCULATION;
				break;
			}
			case _4_REDUCTION: {
				currentAcceptedRules = RuleType.REDUCTION;
				break;
			}
			default:
				System.err.println("Unknown Step State");
				throw new NotImplementedException();
		}
		ObjectArrayList<Function> results = applyRules(fncs, currentAcceptedRules);
		switch(stepStates[stepState]) {
			case _1_CALCULATION: {
				if (results == null) {
					stepState++;
					consecutiveNullSteps++;
					currentStepStateN = 0;
				} else {
					consecutiveNullSteps = 0;
					currentStepStateN++;
					return results;
				}
				break;
			}
			case _2_EXPANSION: {
				if (results == null) {
					if (currentStepStateN == 0) {
						stepState += 2;
						consecutiveNullSteps += 2;
					} else {
						stepState++;
						consecutiveNullSteps++;
					}
					currentStepStateN = 0;
				} else {
					consecutiveNullSteps = 0;
					currentStepStateN++;
					return results;
				}
				break;
			}
			case _3_CALCULATION: {
				if (results == null) {
					stepState++;
					consecutiveNullSteps++;
					currentStepStateN = 0;
				} else {
					consecutiveNullSteps = 0;
					currentStepStateN++;
					return results;
				}
				break;
			}
			case _4_REDUCTION: {
				if (results == null) {
					stepState = 1;
					consecutiveNullSteps++;
					currentStepStateN = 0;
				} else {
					stepState = 0;
					consecutiveNullSteps = 0;
					currentStepStateN++;
					return results;
				}
				break;
			}
			default:
				System.err.println("Unknown Step State");
				throw new NotImplementedException();
		}
		return null;
	}
	
	private ObjectArrayList<Function> applyRules(ObjectArrayList<Function> fncs, RuleType currentAcceptedRules) throws InterruptedException, Error {
		ObjectArrayList<Rule> rules = initialFunction.getMathContext().getAcceptableRules(currentAcceptedRules);
		ObjectArrayList<Function> results = null;
		Rule appliedRule = null;
		for (Function fnc : fncs) {
			for (Rule rule : rules) {
				List<Function> ruleResults = fnc.simplify(rule);
				if (ruleResults != null && !ruleResults.isEmpty()) {
					if (results == null) results = new ObjectArrayList<Function>();
					results.addAll(ruleResults);
					appliedRule = rule;
					break;
				}
			}
		}
		if (StaticVars.debugOn & results != null & appliedRule != null) {
			Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, stepStates[stepState].toString().substring(3) + ": " + appliedRule.getRuleName());
		}
		return results;
	}
}
