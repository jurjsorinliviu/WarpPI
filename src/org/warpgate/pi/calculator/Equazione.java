package org.warpgate.pi.calculator;

import java.util.Vector;

import com.rits.cloning.Cloner;

public class Equazione extends FunzioneDueValori {

	public Equazione(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.EQUATION;
	}

	@Override
	public Termine calcola() throws Errore {
		return new Sottrazione(getVariable1().calcola(),getVariable2().calcola()).calcola();
	}
	
	public Vector<Equazione> risolviPassaggio(char charIncognita) {
		Vector<Equazione> result = new Vector<Equazione>();
		result.add(this.clone());
		for (Tecnica t : Tecnica.tecniche) {
			Vector<Equazione> newResults = new Vector<Equazione>();
			final int sz = result.size();
			for (int n = 0; n < sz; n++) {
				Vector<Equazione> singleResult = t.risolvi(result.get(n));
				final int sz2 = singleResult.size();
				for (int n2 = 0; n2 < sz2; n2++) {
					
				}
			}
		}
		//TODO: finire
		return result;
	}
	
	public Equazione clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

}