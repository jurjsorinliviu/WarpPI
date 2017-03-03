package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.math.parser.features.interfaces.FeatureBasic;

public class FeatureNumber implements FeatureBasic {
	private String numberString;
	
	public FeatureNumber(char c){
		numberString = c+"";
	}
	
	public FeatureNumber(String s) {
		numberString = s;
	}
	
	public FeatureNumber() {
		numberString = "";
	}
	
	public String getNumberString() {
		return numberString;
	}

	public void append(char ch) {
		numberString+=ch;
	}
}
