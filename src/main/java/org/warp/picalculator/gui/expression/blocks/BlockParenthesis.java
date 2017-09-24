package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.parser.features.FeatureParenthesis;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class BlockParenthesis extends BlockParenthesisAbstract {
	public BlockParenthesis() {
		super();
	}

	@Override
	public Feature toFeature(MathContext context) throws Error {
		final Function cont = getNumberContainer().toFunction(context);
		return new FeatureParenthesis(cont);
	}

}
