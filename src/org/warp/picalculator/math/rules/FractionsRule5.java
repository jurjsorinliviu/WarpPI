package org.warp.picalculator.math.rules;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

/**
 * Fractions rule<br>
 * <b>(a / b) ^ -c = (b / a) ^ c</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule5 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		if (fnc.getVariable1() instanceof Division && fnc.getVariable2() instanceof Number) {
			final Number n2 = (Number) fnc.getVariable2();
			if (n2.getTerm().compareTo(BigDecimal.ZERO) < 0) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final Calculator root = f.getRoot();
		final ArrayList<Function> result = new ArrayList<>();
		final Power fnc = (Power) f;
		final Function a = ((Division) fnc.getVariable1()).getVariable1();
		final Function b = ((Division) fnc.getVariable1()).getVariable2();
		final Function c = ((Number) fnc.getVariable2()).multiply(new Number(root, "-1"));
		final Division dv = new Division(root, b, a);
		final Power pow = new Power(root, dv, c);
		result.add(pow);
		return result;
	}

}
