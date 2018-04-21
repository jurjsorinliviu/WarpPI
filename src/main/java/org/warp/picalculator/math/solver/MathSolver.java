package org.warp.picalculator.math.solver;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
	private AtomicInteger stepState = new AtomicInteger(0);
	private int stepStateRepetitions = 0;
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
		Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", "Solving all steps. Input: " + initialFunction.toString());
		currFnc.add(initialFunction);
		long stepNumber = 0;
		int initStepState = 0, endStepState = 0;
		AtomicInteger stepState = new AtomicInteger(0);
		do {
			final String stepName = "Step " + stepNumber;
			for (int i = initStepState; i < endStepState; i++) {
				lastFunctions[i] = currFnc;
			}
			lastFnc = currFnc;
			initStepState = stepState.get();
			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", stepName, "Starting step " + stepStates[initStepState] + ". Input: " + currFnc);
			ObjectArrayList<Function> stepResult = solveStep(lastFnc, stepState);
			if (stepResult != null) {
				for (Function result : stepResult) {
					Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, result.toString());
				}
				currFnc = stepResult;
				steps.add(currFnc);
			}
			endStepState = stepState.get();
			stepNumber++;

			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", stepName, "Step result: " + stepResult);
			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", stepName, "Step result details: Consecutive steps that did nothing: " + consecutiveNullSteps + ", this step did " + stepStateRepetitions + " simplifications.");
			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", stepName, "Next step state: " + stepStates[endStepState]);
		} while(consecutiveNullSteps < stepStates.length && !checkEquals(currFnc, lastFunctions[endStepState]));
		if (consecutiveNullSteps >= stepStates.length) {
			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", "Loop ended because " + consecutiveNullSteps + " >= " + stepStates.length);
		} else {
			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", "Loop ended because " + currFnc + " is equals to " + lastFunctions[endStepState]);
		}
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
		return solveStep(fncs, stepState);
	}
	
	private ObjectArrayList<Function> solveStep(ObjectArrayList<Function> fncs, AtomicInteger stepState) throws InterruptedException, Error {
		ObjectArrayList<Function> processedFncs = applyRules(fncs, RuleType.EXISTENCE); // Apply existence rules before everything
		if (processedFncs != null) {
			fncs = processedFncs;
		}
		RuleType currentAcceptedRules;
		switch(stepStates[stepState.get()]) {
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
		switch(stepStates[stepState.get()]) {
			case _1_CALCULATION: {
				if (results == null) {
					stepState.incrementAndGet();
					consecutiveNullSteps++;
					stepStateRepetitions = 0;
				} else {
					consecutiveNullSteps = 0;
					stepStateRepetitions++;
					return results;
				}
				break;
			}
			case _2_EXPANSION: {
				if (results == null) {
					if (stepStateRepetitions == 0) {
						stepState.addAndGet(2);
						consecutiveNullSteps += 2;
					} else {
						stepState.incrementAndGet();
						consecutiveNullSteps++;
					}
					stepStateRepetitions = 0;
				} else {
					consecutiveNullSteps = 0;
					stepStateRepetitions++;
					return results;
				}
				break;
			}
			case _3_CALCULATION: {
				if (results == null) {
					stepState.incrementAndGet();
					consecutiveNullSteps++;
					stepStateRepetitions = 0;
				} else {
					consecutiveNullSteps = 0;
					stepStateRepetitions++;
					return results;
				}
				break;
			}
			case _4_REDUCTION: {
				if (results == null) {
					stepState.set(1);
					consecutiveNullSteps++;
					stepStateRepetitions = 0;
				} else {
					stepState.set(0);
					consecutiveNullSteps = 0;
					stepStateRepetitions++;
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
		out: {
			for (Function fnc : fncs) {
				for (Rule rule : rules) {
					List<Function> ruleResults = fnc.simplify(rule);
					if (ruleResults != null && !ruleResults.isEmpty()) {
						if (results == null) results = new ObjectArrayList<Function>();
						results.addAll(ruleResults);
						appliedRule = rule;
						break out; //TODO: prima era un break normale, controllare se bisogna uscire da tutti e due i for oppure soltanto dall'ultimo.
					}
				}
			}
		}
		if (StaticVars.debugOn & results != null & appliedRule != null) {
			Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_VERBOSE, "Math Solver", currentAcceptedRules.toString(), "Applied rule " + appliedRule.getRuleName());
		}
		return results;
	}
}
