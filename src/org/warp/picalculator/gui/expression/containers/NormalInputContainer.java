package org.warp.picalculator.gui.expression.containers;

import org.warp.picalculator.gui.expression.Block;
import org.warp.picalculator.gui.expression.BlockChar;
import org.warp.picalculator.gui.expression.BlockContainer;
import org.warp.picalculator.gui.expression.BlockDivision;
import org.warp.picalculator.gui.expression.BlockParenthesis;
import org.warp.picalculator.gui.expression.BlockSquareRoot;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.expression.CaretState;
import org.warp.picalculator.math.MathematicalSymbols;

public class NormalInputContainer extends InputContainer {
	
	public NormalInputContainer() {
		super();
	}
	
	public NormalInputContainer(boolean small) {
		super(small);
	}
	
	public NormalInputContainer(boolean small, int minWidth, int minHeight) {
		super(small, minWidth, minHeight);
	}
	
	@Override
	public Block parseChar(char c) {
		switch(c) {
			case MathematicalSymbols.DIVISION:
				return new BlockDivision();
			case MathematicalSymbols.SQUARE_ROOT:
				return new BlockSquareRoot();
			case MathematicalSymbols.PARENTHESIS_OPEN:
			case MathematicalSymbols.PARENTHESIS_CLOSE:
				return new BlockParenthesis();
			case MathematicalSymbols.MULTIPLICATION:
			case MathematicalSymbols.SUM:
			case MathematicalSymbols.SUM_SUBTRACTION:
			case MathematicalSymbols.SUBTRACTION:
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return new BlockChar(c);
			default:
				return new BlockChar(c);
		}
	}
}
