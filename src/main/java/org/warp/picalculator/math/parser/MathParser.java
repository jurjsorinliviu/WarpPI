package org.warp.picalculator.math.parser;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.blocks.BlockDivision;
import org.warp.picalculator.gui.expression.blocks.BlockExponentialNotation;
import org.warp.picalculator.gui.expression.blocks.BlockParenthesis;
import org.warp.picalculator.gui.expression.blocks.BlockPower;
import org.warp.picalculator.gui.expression.blocks.BlockSquareRoot;
import org.warp.picalculator.gui.expression.containers.InputContainer;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.RootSquare;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Variable.V_TYPE;
import org.warp.picalculator.math.parser.features.FeatureChar;
import org.warp.picalculator.math.parser.features.FeatureDivision;
import org.warp.picalculator.math.parser.features.FeatureMultiplication;
import org.warp.picalculator.math.parser.features.FeatureNumber;
import org.warp.picalculator.math.parser.features.FeatureParenthesis;
import org.warp.picalculator.math.parser.features.FeaturePower;
import org.warp.picalculator.math.parser.features.FeatureSquareRoot;
import org.warp.picalculator.math.parser.features.FeatureSum;
import org.warp.picalculator.math.parser.features.FeatureVariable;
import org.warp.picalculator.math.parser.features.interfaces.Feature;
import org.warp.picalculator.math.parser.features.interfaces.FeatureDouble;
import org.warp.picalculator.math.parser.features.interfaces.FeatureSingle;

import com.sun.org.apache.xpath.internal.functions.Function2Args;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

public class MathParser {
	public static Expression parseInput(MathContext context, InputContainer c) throws Error {
		Expression result;

		final Function resultFunction = parseContainer(context, c.getContent());

		result = new Expression(context, resultFunction);
		return result;
	}

	public static ObjectArrayList<Block> parseOutput(MathContext context, ObjectArrayList<Function> expr) throws Error {
		final ObjectArrayList<Block> resultBlocks = new ObjectArrayList<>();
		
		for (Function f : expr) {
			resultBlocks.addAll(parseFunction(context, f));
		}
		
		return resultBlocks;
	}
	
	private static ObjectArrayList<Block> parseFunction(MathContext context, Function func) throws Error {
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		if (func instanceof FunctionOperator) {
			ObjectArrayList<Block> sub1 = parseFunction(context, func.getParameter(0));
			ObjectArrayList<Block> sub2 = parseFunction(context, func.getParameter(1));
			if (func instanceof Sum) {
				result.addAll(sub1);
				result.add(new BlockChar(MathematicalSymbols.SUM));
				result.addAll(sub2);
				return result;
			}
			if (func instanceof Subtraction) {
				result.addAll(sub1);
				result.add(new BlockChar(MathematicalSymbols.SUBTRACTION));
				result.addAll(sub2);
				return result;
			}
			if (func instanceof SumSubtraction) {
				result.addAll(sub1);
				result.add(new BlockChar(MathematicalSymbols.SUM_SUBTRACTION));
				result.addAll(sub2);
				return result;
			}
			if (func instanceof Multiplication) {
				Block nearLeft = sub1.get(sub1.size()-1);
				Block nearRight = sub2.get(0);
				
				result.addAll(sub1);
				if (nearLeft instanceof BlockChar && nearRight instanceof BlockChar) {
					
				} else {
					result.add(new BlockChar(MathematicalSymbols.MULTIPLICATION));
				}
				result.addAll(sub2);
				return result;
			}
			if (func instanceof Division) {
				BlockDivision bd = new BlockDivision();
				BlockContainer uc = bd.getUpperContainer();
				BlockContainer lc = bd.getLowerContainer();
				for (Block b : sub1) {
					uc.appendBlockUnsafe(b);
				}
				for (Block b : sub2) {
					lc.appendBlockUnsafe(b);
				}
				uc.recomputeDimensions();
				lc.recomputeDimensions();
				bd.recomputeDimensions();
				result.add(bd);
				return result;
			}
			if (func instanceof Power) {
				BlockPower bp = new BlockPower();
				BlockContainer ec = bp.getExponentContainer();
				result.addAll(sub1);
				for (Block b : sub2) {
					ec.appendBlockUnsafe(b);
				}
				ec.recomputeDimensions();
				bp.recomputeDimensions();
				result.add(bp);
				return result;
			}
		}
		if (func instanceof FunctionSingle) {
			ObjectArrayList<Block> sub = parseFunction(context, func.getParameter(0));
			if (func instanceof RootSquare) {
				BlockSquareRoot bsqr = new BlockSquareRoot();
				BlockContainer bsqrc = bsqr.getNumberContainer();
				for (Block b : sub) {
					bsqrc.appendBlockUnsafe(b);
				}
				bsqrc.recomputeDimensions();
				bsqr.recomputeDimensions();
				result.add((bsqr));
				return result;
			}
			
		}
		if (func instanceof Expression) {
			ObjectArrayList<Block> sub = parseFunction(context, ((Expression) func).getParameter(0));
			BlockParenthesis bp = new BlockParenthesis();
			BlockContainer bpc = bp.getNumberContainer();
			for (Block b : sub) {
				bpc.appendBlockUnsafe(b);
			}
			bpc.recomputeDimensions();
			bp.recomputeDimensions();
			result.add(bp);
			return result;
		}
		if (func instanceof Number) {
			Number numb = (Number) func;
			String numberString = numb.toString();
			if (numberString.contains("ℯ℮")) {
				String[] numberParts = numberString.split("ℯ℮", 2);
				BlockPower bp = new BlockExponentialNotation();
				BlockContainer bpec = bp.getExponentContainer();
				for (char c : numberParts[0].toCharArray()) {
					result.add(new BlockChar(c));
				}
				for (char c : numberParts[1].toCharArray()) {
					bpec.appendBlockUnsafe(new BlockChar(c));
				};
				bpec.recomputeDimensions();
				bp.recomputeDimensions();
				result.add(bp);
				return result;
			} else {
				for (char c : numberString.toCharArray()) {
					result.add(new BlockChar(c));
				}
			}
			return result;
		}
		if (func instanceof Variable) {
			//TODO: Temporary solution. In near future Variables will be distint objects and they will have a color. So they will be no longer a BlockChar/FeatureChar
			result.add(new BlockChar(((Variable) func).getChar()));
			return result;
		}
		throw new Error(Errors.NOT_IMPLEMENTED, "Unknown function " + func.getClass().getSimpleName());
	}

	private static Function parseContainer(final MathContext context, final Iterable<Block> blocks) throws Error {
		final ObjectArrayList<Feature> blockFeatures = new ObjectArrayList<>();

		for (final Block block : blocks) {
			final Feature blockFeature = parseBlock(context, block);
			blockFeatures.add(blockFeature);
		}

		final Function result = joinFeatures(context, blockFeatures);
		return result;
	}

	private static Feature parseBlock(final MathContext context, final Block block) throws Error {

		Feature result;

		final int blockType = block.getClassID();
		switch (blockType) {
			case BlockChar.CLASS_ID:
				result = new FeatureChar(((BlockChar) block).getChar());
				break;
			case BlockDivision.CLASS_ID:
				final BlockDivision bd = (BlockDivision) block;
				final Function upper = parseContainer(context, bd.getUpperContainer().getContent());
				final Function lower = parseContainer(context, bd.getLowerContainer().getContent());
				result = new FeatureDivision(upper, lower);
				break;
			case BlockSquareRoot.CLASS_ID:
				final BlockSquareRoot bsqr = (BlockSquareRoot) block;
				final Function contnt = parseContainer(context, bsqr.getNumberContainer().getContent());
				result = new FeatureSquareRoot(contnt);
				break;
			case BlockParenthesis.CLASS_ID:
				final BlockParenthesis bp = (BlockParenthesis) block;
				final Function cont = parseContainer(context, bp.getNumberContainer().getContent());
				result = new FeatureParenthesis(cont);
				break;
			case BlockPower.CLASS_ID:
				final BlockPower blp = (BlockPower) block;
				final Function exp = parseContainer(context, blp.getExponentContainer().getContent());
				result = new FeaturePower(exp);
				break;
			default:
				throw new Error(Errors.NOT_IMPLEMENTED, "The block " + block.getClass().getSimpleName() + " isn't a known BLock");
		}

		return result;
	}

	private static Function joinFeatures(final MathContext context, ObjectArrayList<Feature> features) throws Error {

		features = fixFeatures(context, features);

		final ObjectArrayList<Function> process = makeFunctions(context, features);

		fixStack(context, process);

		if (process.size() > 1) {
			throw new Error(Errors.UNBALANCED_STACK, "The stack is unbalanced. Not all the functions are nested correctly");
		}

		return process.get(0);
	}

	private static void fixStack(MathContext context, ObjectArrayList<Function> process) throws Error {

		boolean lastLoopDidSomething;
		
		ObjectListIterator<Function> stackIterator;
		
		//Phase 0: join number and variables ([2][x] => [[2]*[x]])
		do {
			lastLoopDidSomething = false;
			stackIterator = process.listIterator(process.size());
			Function lastElement = null;
			while (stackIterator.hasPrevious()) {
				final Function f = stackIterator.previous();
				final int curIndex = stackIterator.nextIndex();
	
				if (f instanceof Number | f instanceof Variable) {
					if (lastElement instanceof Variable) {
						lastLoopDidSomething = true;
						final Function var = process.get(curIndex + 1);
						final Function numb = process.get(curIndex);
						stackIterator.set(new Multiplication(context, numb, var));
						process.remove(curIndex + 1);
					}
				}
				lastElement = f;
			}
		} while (lastLoopDidSomething);
		
		//Phase 1
		do {
			lastLoopDidSomething = false;
			stackIterator = process.listIterator(process.size());
			Function lastElement = null;
			while (stackIterator.hasPrevious()) {
				final Function f = stackIterator.previous();

				if (f instanceof FunctionSingle) {
					if (((FunctionSingle) f).getParameter() == null) {
						lastLoopDidSomething = true;
						if (lastElement == null) {
							throw new Error(Errors.MISSING_ARGUMENTS, "There is a function at the end without any argument specified.");
						} else {
							((FunctionSingle) f).setParameter(lastElement);
							process.remove(stackIterator.nextIndex());
						}
					}
				}
				lastElement = f;
			}
		} while (lastLoopDidSomething);

		//Phase 2
		do {
			lastLoopDidSomething = false;
			stackIterator = process.listIterator();
			while (stackIterator.hasNext()) {
				final Function f = stackIterator.next();
				final int curIndex = stackIterator.previousIndex();

				if (f instanceof Multiplication || f instanceof Division) {
					if (curIndex - 1 >= 0 && stackIterator.hasNext()) {
						lastLoopDidSomething = true;
						final Function next = process.get(curIndex + 1);
						final Function prev = process.get(curIndex - 1);
						stackIterator.set(f.setParameter(0, prev).setParameter(1, next));
						process.remove(curIndex + 1);
						process.remove(curIndex - 1);
					} else {
						if (f.getParameter(0) == null || f.getParameter(1) == null) {
							throw new Error(Errors.MISSING_ARGUMENTS, "There is a function at the end without any argument specified.");
						}
					}
				}
			}
		} while (lastLoopDidSomething);

		//Phase 3
		do {
			lastLoopDidSomething = false;
			stackIterator = process.listIterator();
			while (stackIterator.hasNext()) {
				final Function f = stackIterator.next();
				final int curIndex = stackIterator.previousIndex();

				if (f instanceof Sum || f instanceof Subtraction || f instanceof SumSubtraction) {
					if (curIndex - 1 >= 0 && stackIterator.hasNext()) {
						lastLoopDidSomething = true;
						final Function next = process.get(curIndex + 1);
						final Function prev = process.get(curIndex - 1);
						stackIterator.set(f.setParameter(0, prev).setParameter(1, next));
						process.remove(curIndex + 1);
						process.remove(curIndex - 1);
					} else {
						if (f.getParameter(0) == null || f.getParameter(1) == null) {
							throw new Error(Errors.MISSING_ARGUMENTS, "There is a function at the end without any argument specified.");
						}
					}
				}
			}
		} while (lastLoopDidSomething);

		//Phase 4
		do {
			lastLoopDidSomething = false;
			stackIterator = process.iterator();
			while (stackIterator.hasNext()) {
				final Function f = stackIterator.next();

				if (f instanceof Function2Args) {

				}
			}
		} while (lastLoopDidSomething);
	}

	private static ObjectArrayList<Function> makeFunctions(MathContext context, ObjectArrayList<Feature> features)
			throws Error {
		final ObjectArrayList<Function> process = new ObjectArrayList<>();
		
		for (final Feature f : features) {
			if (f instanceof FeatureDivision) {
				process.add(new Division(context, (Function) ((FeatureDouble) f).getChild1(), (Function) ((FeatureDouble) f).getChild2()));
			} else if (f instanceof FeatureMultiplication) {
				process.add(new Multiplication(context, (Function) ((FeatureDouble) f).getChild1(), (Function) ((FeatureDouble) f).getChild2()));
			} else if (f instanceof FeatureVariable) {
				process.add(new Variable(context, ((FeatureVariable) f).ch, ((FeatureVariable) f).varType));
			} else if (f instanceof FeatureSum) {
				process.add(new Sum(context, (Function) ((FeatureDouble) f).getChild1(), (Function) ((FeatureDouble) f).getChild2()));
			} else if (f instanceof FeaturePower) {
				if (process.isEmpty()) {
					throw new Error(Errors.SYNTAX_ERROR, "There is a power at the beginning of the expression!");
				} else {
					Function prec = process.remove(process.size()-1);
					process.add(new Power(context, prec, (Function) ((FeatureSingle) f).getChild()));
				}
			} else if (f instanceof FeatureSquareRoot) {
				process.add(new RootSquare(context, (Function) ((FeatureSingle) f).getChild()));
			} else if (f instanceof FeatureParenthesis) {
				process.add(new Expression(context, (Function) ((FeatureSingle) f).getChild()));
//			}  else if (f instanceof FeatureSubtraction) {
//				process.add(new Subtraction(context, (Function) ((FeatureDouble) f).getChild1(), (Function) ((FeatureDouble) f).getChild2()));
//			}  else if (f instanceof FeatureSumSubtraction) {
//				process.add(new SumSubtraction(context, (Function) ((FeatureDouble) f).getChild1(), (Function) ((FeatureDouble) f).getChild2()));
			} else if (f instanceof FeatureNumber) {
				process.add(new Number(context, ((FeatureNumber) f).getNumberString()));
			} else if (f instanceof FeatureChar) {
				//All the char features must have been changed already before
				throw new Error(Errors.SYNTAX_ERROR, "\"" + f.getClass().getSimpleName() + "\" can't be converted into a Function!");
			} else {
				throw new Error(Errors.SYNTAX_ERROR, "\"" + f.getClass().getSimpleName() + "\" can't be converted into a Function!");
			}
		}
		return process;
	}

	private static ObjectArrayList<Feature> fixFeatures(final MathContext context, ObjectArrayList<Feature> features)
			throws Error {

		features = fixMinuses(context, features);

		features = makeNumbers(context, features);

		features = convertFunctionChars(context, features);

		return features;
	}

	/**
	 * Create function features from char features
	 * 
	 * @param context
	 * @param features
	 * @return
	 */
	private static ObjectArrayList<Feature> convertFunctionChars(MathContext context, ObjectArrayList<Feature> features)
			throws Error {
		final ObjectArrayList<Feature> process = new ObjectArrayList<>();

		for (final Feature f : features) {
			if (f instanceof FeatureChar) {
				final char featureChar = ((FeatureChar) f).ch;
				Feature result = null;
				switch (featureChar) {
					case MathematicalSymbols.SUM:
						result = new FeatureSum(null, null);
						break;
					case MathematicalSymbols.MULTIPLICATION:
						result = new FeatureMultiplication(null, null);
						break;
				}
				
				//TODO: Temporary solution. In near future Variables will be distint objects and they will have a color. So they will be no longer a BlockChar/FeatureChar
				for (char var : MathematicalSymbols.variables) {
					if ( featureChar == var) {
						result = new FeatureVariable(featureChar, V_TYPE.UNKNOWN);
						break;
					}
				}

				if (result == null) {
					throw new Error(Errors.SYNTAX_ERROR, "Char " + featureChar + " isn't a known feature");
				}

				process.add(result);
			} else {
				process.add(f);
			}
		}

		return process;
	}

	/**
	 * Make numbers [-][1][2][+][-][3] => [-12]
	 * 
	 * @param context
	 * @param features
	 * @return
	 */
	private static ObjectArrayList<Feature> makeNumbers(MathContext context, ObjectArrayList<Feature> features) {
		final ObjectArrayList<Feature> process = new ObjectArrayList<>();

		FeatureNumber numberBuffer = null;
		for (final Feature f : features) {
			if (f instanceof FeatureChar) {
				final FeatureChar bcf = (FeatureChar) f;
				final char[] numbers = MathematicalSymbols.numbers;
				boolean isNumber = false;
				for (final char n : numbers) {
					if (bcf.ch == n) {
						isNumber = true;
						break;
					}
				}
				if (bcf.ch == '-' || bcf.ch == '.') {
					isNumber = true;
				}
				if (isNumber) {
					if (numberBuffer == null) {
						numberBuffer = new FeatureNumber(bcf.ch);
						process.add(numberBuffer);
					} else {
						numberBuffer.append(bcf.ch);
					}
				} else {
					if (numberBuffer != null) {
						numberBuffer = null;
					}
					process.add(f);
				}
			} else {
				process.add(f);
			}
		}

		return process;
	}

	/**
	 * Fix minuses [-][1][2][+][-][3][-][2] => [-][12][+][-][3][—][2]
	 * 
	 * @param context
	 * @param features
	 * @return
	 * @throws Error
	 */
	private static ObjectArrayList<Feature> fixMinuses(final MathContext context, ObjectArrayList<Feature> features)
			throws Error {
		final ObjectArrayList<Feature> process = new ObjectArrayList<>();
		Feature lastFeature = null;
		for (final Feature f : features) {
			if (f instanceof FeatureChar && ((FeatureChar) f).ch == MathematicalSymbols.SUBTRACTION) {
				boolean isNegativeOfNumber = false;
				if (lastFeature == null) {
					isNegativeOfNumber = true;
				} else if (lastFeature instanceof FeatureChar) {
					final FeatureChar lcf = (FeatureChar) lastFeature;
					final char[] operators = MathematicalSymbols.functionsNSN;
					for (final char operator : operators) {
						if (lcf.ch == operator) {
							isNegativeOfNumber = true;
							break;
						}
					}
				}
				if (isNegativeOfNumber) {
					process.add(new FeatureChar(MathematicalSymbols.MINUS));
				} else {
					process.add(new FeatureChar(MathematicalSymbols.SUBTRACTION));
				}
			} else {
				process.add(f);
			}
			lastFeature = f;
		}
		return process;
	}
}
