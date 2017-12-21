package org.warp.picalculator.math;

import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MathSolver {
	
	private final MathContext mathContext;
	private final Function initialFunction;
	private int stepState = 0;
	private int currentStepStateN = 0;
	private enum StepState {
		_1_CALCULATION,
		_2_REDUCTION,
		_3_CALCULATION,
		_4_EXPANSION
	}
	
	public MathSolver(MathContext mathContext, Function initialFunction) {
		this.mathContext = mathContext;
		this.initialFunction = initialFunction;
	}
	
	public ObjectArrayList<ObjectArrayList<Function>> solveAllSteps() {
		ObjectArrayList<ObjectArrayList<Function>> steps = new ObjectArrayList<>();
		ObjectArrayList<Function> lastFnc = null, currFnc = new ObjectArrayList<>();
		currFnc.add(initialFunction);
		do {
			lastFnc = currFnc;
			currFnc = new ObjectArrayList<>();
			for (Function fnc : lastFnc) {
				currFnc.addAll(solveStep(fnc));
			}
			steps.add(currFnc);
		} while(checkEquals(currFnc, lastFnc));
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
	
	private ObjectArrayList<Function> solveStep(Function fnc) {
		RuleType currentAcceptedRules;
		switch(StepState.values()[stepState]) {
			case _1_CALCULATION: {
				currentAcceptedRules = RuleType.CALCULATION;
				break;
			}
			case _2_REDUCTION: {
				currentAcceptedRules = RuleType.REDUCTION;
				break;
			}
			case _3_CALCULATION: {
				currentAcceptedRules = RuleType.CALCULATION;
				break;
			}
			case _4_EXPANSION: {
				currentAcceptedRules = RuleType.EXPANSION;
				break;
			}
			default:
				System.err.println("Unknown Step State");
				throw new NotImplementedException();
		}
		ObjectArrayList<Rule> rules = mathContext.getAcceptableRules(currentAcceptedRules);
		ObjectArrayList<Function> results = null;
		for (Rule rule : rules) {
			ObjectArrayList<Function> ruleResults = rule.execute(fnc);
			if (ruleResults != null && !ruleResults.isEmpty()) {
				results = ruleResults;
				break;
			}
		}
		switch(StepState.values()[stepState]) {
			case _1_CALCULATION: {
				if (results == null) {
					stepState++;
					currentStepStateN = 0;
				} else {
					currentStepStateN++;
					return results;
				}
				break;
			}
			case _2_REDUCTION: {
				if (results == null) {
					if (currentStepStateN == 0) {
						stepState += 2;
					} else {
						stepState++;
					}
					currentStepStateN = 0;
				} else {
					currentStepStateN++;
					return results;
				}
				break;
			}
			case _3_CALCULATION: {
				if (results == null) {
					stepState++;
					currentStepStateN = 0;
				} else {
					currentStepStateN++;
					return results;
				}
				break;
			}
			case _4_EXPANSION: {
				if (results == null) {
					stepState++;
					currentStepStateN = 0;
				} else {
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
}
