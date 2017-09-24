package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.parser.features.FeatureSine;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class BlockSine extends BlockParenthesisAbstract {
	public BlockSine() {
		super("SIN");
	}

	@Override
	public Feature toFeature(MathContext context) throws Error {
		final Function cont = getNumberContainer().toFunction(context);
		return new FeatureSine(cont);
	}
}
