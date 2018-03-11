package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.parser.features.FeatureChar;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class BlockNumber extends BlockChar {

	public BlockNumber(char ch) {
		super(ch);
	}

}
