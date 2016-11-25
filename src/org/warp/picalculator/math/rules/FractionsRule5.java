package org.warp.picalculator.math.rules;

import java.math.BigInteger;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

/**
 * Fractions rule<br>
 * <b>(a / b) ^ -c = (b / a) ^ c</b>
 * @author Andrea Cavalli
 *
 */
public class FractionsRule5 {

	public static boolean compare(Function f) {
		Power fnc = (Power) f;
		if (fnc.getVariable1() instanceof Division && fnc.getVariable2() instanceof Number) {
			Number n2 = (Number) fnc.getVariable2();
			if (n2.getTerm().compareTo(BigInteger.ZERO) < 0) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Power fnc = (Power) f;
		Function a = ((Division)fnc.getVariable1()).getVariable1();
		Function b = ((Division)fnc.getVariable1()).getVariable2();
		Function c = ((Number)fnc.getVariable2()).multiply(new Number(null, "-1"));
		Division dv = new Division(null, b, a);
		Power pow = new Power(f.getParent(), dv, c);
		dv.setParent(pow);
		result.add(pow);
		return result;
	}

}
