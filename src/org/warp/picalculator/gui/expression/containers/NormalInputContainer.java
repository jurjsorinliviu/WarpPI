package org.warp.picalculator.gui.expression.containers;

import org.warp.picalculator.gui.expression.Block;
import org.warp.picalculator.gui.expression.BlockChar;
import org.warp.picalculator.gui.expression.BlockDivision;
import org.warp.picalculator.gui.expression.BlockSquareRoot;
import org.warp.picalculator.math.MathematicalSymbols;

public class NormalInputContainer extends InputContainer {
	@Override
	public Block parseChar(char c) {
		switch(c) {
			case MathematicalSymbols.DIVISION:
				return new BlockDivision();
			case MathematicalSymbols.SQUARE_ROOT:
				return new BlockSquareRoot();
			case MathematicalSymbols.MULTIPLICATION:
			case MathematicalSymbols.SUM:
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
