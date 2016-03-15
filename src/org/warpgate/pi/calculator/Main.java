package org.warpgate.pi.calculator;

public class Main {
	public static void main(String[] args) {
		Calculator c = new Calculator(false);
		try {
			long start = System.nanoTime();
			Termine result = c.calcolarisultato("((5^2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21");
			long end = System.nanoTime();
			long timeElapsed = end-start;
			System.out.println("RESULT: " + result);
			System.out.println("DECIMAl RESULT: " + result.getTerm().toBigDecimal());
			System.out.println("Time elapsed: " +  (double) timeElapsed / 1000000000 + "\n");
			
			
			start = System.nanoTime();
			RisultatoEquazione eresult = c.calcolaequazione("((5^2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21=(175*(2√7)+3*(2√13))/105");
			end = System.nanoTime();
			timeElapsed = end-start;
			System.out.println("Is an equation: " + eresult.isAnEquation);
			System.out.println("L-R: " + eresult.LR);
			System.out.println("Time elapsed: " +  (((double) timeElapsed / 1000000000)) + "\n");
		} catch (Errore e) {
			System.err.println(e.id);
		}
	}
}
