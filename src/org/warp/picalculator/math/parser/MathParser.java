package org.warp.picalculator.math.parser;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.gui.expression.Block;
import org.warp.picalculator.gui.expression.BlockChar;
import org.warp.picalculator.gui.expression.BlockContainer;
import org.warp.picalculator.gui.expression.BlockDivision;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.parser.features.FeatureChar;
import org.warp.picalculator.math.parser.features.FeatureDivision;
import org.warp.picalculator.math.parser.features.FeatureMultiplication;
import org.warp.picalculator.math.parser.features.FeatureNumber;
import org.warp.picalculator.math.parser.features.FeatureSum;
import org.warp.picalculator.math.parser.features.interfaces.Feature;
import org.warp.picalculator.math.parser.features.interfaces.FeatureDouble;

import com.sun.org.apache.xpath.internal.functions.Function2Args;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

public class MathParser {
	public static Expression parseInput(MathContext context, BlockContainer root) throws Error {
		Expression result;
		
		Function resultFunction = parseContainer(context, root);
		
		result = new Expression(context, resultFunction);
		return result;
	}
	
	private static Function parseContainer(final MathContext context, final BlockContainer container) throws Error {
		final ObjectArrayList<Block> blocks = container.getContent();
		final ObjectArrayList<Feature> blockFeatures = new ObjectArrayList<>();
		
		for (final Block block : blocks) {
			Feature blockFeature = parseBlock(context, block);
			blockFeatures.add(blockFeature);
		}
		
		Function result = joinFeatures(context, blockFeatures);
		return result;
	}
	
	private static Feature parseBlock(final MathContext context, final Block block) throws Error {
		
		Feature result;
		
		int blockType = block.getClassID();
		switch (blockType) {
			case BlockChar.CLASS_ID:
				result = new FeatureChar(((BlockChar) block).getChar());
				break;
			case BlockDivision.CLASS_ID:
				BlockDivision bd = (BlockDivision) block;
				Function upper = parseContainer(context, bd.getUpperContainer());
				Function lower = parseContainer(context, bd.getLowerContainer());
				result = new FeatureDivision(upper, lower);
				break;
			default:
				throw new Error(Errors.SYNTAX_ERROR);
		}
		
		return result;
	}

	private static Function joinFeatures(final MathContext context, ObjectArrayList<Feature> features) throws Error {
		
		features = fixFeatures(context, features);

		ObjectArrayList<Function> process = makeFunctions(context, features);
		
		fixStack(context, process);
		
		if (process.size() > 1) {
			throw new Error(Errors.UNBALANCED_STACK, "The stack is unbalanced. Not all the functions are nested correctly");
		}
		
		return process.get(0);
	}
	
	private static void fixStack(MathContext context, ObjectArrayList<Function> process) throws Error {

		//Phase 1
		ObjectListIterator<Function> stackIterator = process.listIterator(process.size());
		Function lastElement = null;
		while (stackIterator.hasPrevious()) {
			Function f = stackIterator.previous();
			
			if (f instanceof FunctionSingle) {
				if (((FunctionSingle) f).getParameter() == null) {
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

		//Phase 2
		stackIterator = process.listIterator();
		while (stackIterator.hasNext()) {
			Function f = stackIterator.next();
			final int curIndex = stackIterator.previousIndex();
			
			if (f instanceof Multiplication || f instanceof Division) {
				if (curIndex-1>=0 && stackIterator.hasNext()) {
					Function next = process.get(curIndex+1);
					Function prev = process.get(curIndex-1);
					stackIterator.set(
					f.setParameter(0, prev)
					.setParameter(1, next)
					);
					process.remove(curIndex+1);
					process.remove(curIndex-1);
				} else {
					if (f.getParameter(0) == null || f.getParameter(1) == null) {
						throw new Error(Errors.MISSING_ARGUMENTS, "There is a function at the end without any argument specified.");
					}
				}
			}
		}

		//Phase 3
		stackIterator = process.listIterator();
		while (stackIterator.hasNext()) {
			Function f = stackIterator.next();
			
			if (f instanceof Sum || f instanceof Subtraction || f instanceof SumSubtraction) {
				
			}
		}

		//Phase 4
		stackIterator = process.iterator();
		while (stackIterator.hasNext()) {
			Function f = stackIterator.next();
			
			if (f instanceof Function2Args) {
				
			}
		}
	}

	private static ObjectArrayList<Function> makeFunctions(MathContext context, ObjectArrayList<Feature> features) throws Error {
		ObjectArrayList<Function> process = new ObjectArrayList<>();
		
		for (Feature f : features) {
			if (f instanceof FeatureDivision) {
				process.add(new Division(context, (Function) ((FeatureDouble) f).getChild1(), (Function) ((FeatureDouble) f).getChild2()));
			} else if (f instanceof FeatureMultiplication) {
				process.add(new Multiplication(context, (Function) ((FeatureDouble) f).getChild1(), (Function) ((FeatureDouble) f).getChild2()));
			} else if (f instanceof FeatureNumber) {
				process.add(new Number(context, ((FeatureNumber) f).getNumberString()));
			} else if (f instanceof FeatureChar) {
				//All the char features must have been changed already before
				throw new Error(Errors.SYNTAX_ERROR, "\"" + f.getClass().getSimpleName().toString() + "\" can't be converted into a Function!");
			} else {
				throw new Error(Errors.SYNTAX_ERROR, "\"" + f.getClass().getSimpleName().toString() + "\" can't be converted into a Function!");
			}
		}
		return process;
	}

	private static ObjectArrayList<Feature> fixFeatures(final MathContext context, ObjectArrayList<Feature> features) throws Error {
		
		features = fixMinuses(context, features);
		
		features = makeNumbers(context, features);
		
		features = convertFunctionChars(context, features);
		
		return features;
	}
	
	/**
	 * Create function features from char features
	 * @param context
	 * @param features
	 * @return
	 */
	private static ObjectArrayList<Feature> convertFunctionChars(MathContext context, ObjectArrayList<Feature> features) throws Error {
		ObjectArrayList<Feature> process = new ObjectArrayList<>();
		
		for (Feature f : features) {
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
	 * @param context
	 * @param features
	 * @return
	 */
	private static ObjectArrayList<Feature> makeNumbers(MathContext context, ObjectArrayList<Feature> features) {
		ObjectArrayList<Feature> process = new ObjectArrayList<>();
		
		FeatureNumber numberBuffer = null;
		for (Feature f : features) {
			if (f instanceof FeatureChar) {
				final FeatureChar bcf = (FeatureChar) f;
				final char[] numbers = MathematicalSymbols.numbers;
				boolean isNumber = false;
				for (char n : numbers) {
					if (bcf.ch == n) {
						isNumber = true;
						break;
					}
				}
				if (bcf.ch == '-') {
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
	 * @param context
	 * @param features
	 * @return
	 * @throws Error
	 */
	private static ObjectArrayList<Feature> fixMinuses(final MathContext context, ObjectArrayList<Feature> features) throws Error {
		ObjectArrayList<Feature> process = new ObjectArrayList<>();
		Feature lastFeature = null;
		for (Feature f : features) {
			if (f instanceof FeatureChar && ((FeatureChar)f).ch == MathematicalSymbols.SUBTRACTION) {
				boolean isNegativeOfNumber = false;
				if (lastFeature == null) {
					isNegativeOfNumber = true;
				} else if (lastFeature instanceof FeatureChar) {
					FeatureChar lcf = (FeatureChar) lastFeature;
					final char[] operators = MathematicalSymbols.functionsNSN;
					for (char operator : operators) {
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
