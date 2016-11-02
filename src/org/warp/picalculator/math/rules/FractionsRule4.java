package org.warp.picalculator.math.rules;

import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Power;

import java.util.ArrayList;

import org.nevec.rjm.NumeroAvanzato;
import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.picalculator.Error;

/**
 * Fractions rule<br>
 * <b>(a / b) ^ -1 = b / a</b>
 * @author Andrea Cavalli
 *
 */
public class FractionsRule4 {

	public static boolean compare(Function f) throws Error {
		if (f instanceof Power) {
			Power fnc = (Power) f;
			if (fnc.getVariable1() instanceof Division && fnc.getVariable2() instanceof Number) {
				Number n2 = (Number) fnc.getVariable2();
				if (n2.equals(new Number(null, "-1"))) {
					return true;
				}
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Power fnc = (Power) f;
		Function a = ((Division)fnc.getVariable1()).getVariable1();
		Function b = ((Division)fnc.getVariable1()).getVariable2();
		Division res = new Division(f.getParent(), b, a);
		result.add(res);
		return result;
	}

}
