package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.math.functions.Variable.V_TYPE;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class FeatureVariable extends FeatureChar {

	public V_TYPE varType;

	public FeatureVariable(char ch, V_TYPE varType) {
		super(ch);
		this.varType = varType;
	}

}
