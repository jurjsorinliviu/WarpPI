package org.warp.picalculator.math.rules;

import org.warp.picalculator.math.Function;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;

/**
 * Rule interface
 * @author Andrea Cavalli
 *
 */
public interface Rule {
	/**
	 * Get rule name
	 * @return
	 */
	public default String getRuleName() {
		return "UnnamedRule1";
	}

	/**
	 * Get rule type
	 * @return
	 */
	@SpecializedFunction
	public RuleType getRuleType();
	
	/**
	 * 
	 * @param func
	 * @return <ul><li><code>null</code> if it's not executable on the function <b>func</b></li><li>An <code>ObjectArrayList&lt;Function&gt;</code> if it did something</li></ul>
	 */
	public ObjectArrayList<Function> execute(Function func);
}