package org.warp.picalculator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.rits.cloning.Cloner;

public class Equazione extends FunzioneDueValori {

	public Equazione(Funzione value1, Funzione value2) {
		super(value1,value2);
	}

	@Override
	public String simbolo() {
		return Simboli.EQUATION;
	}

	@Override
	public Funzione calcola() throws Errore {
		return new Equazione(new Sottrazione((FunzioneBase)variable1.calcola(), (FunzioneBase)variable2.calcola()).calcola(), new Termine("0"));
	}
	
	public Funzione calcola(char charIncognita) {
		@SuppressWarnings("unused")
		ArrayList<Equazione> e;
		//TODO: Finire. Fare in modo che risolva i passaggi fino a che non ce ne sono più
		return null;
	}
	
	public ArrayList<Equazione> risolviPassaggio(char charIncognita) {
		ArrayList<Equazione> result = new ArrayList<Equazione>();
		result.add(this.clone());
		for (Tecnica t : Tecnica.tecniche) {
			ArrayList<Equazione> newResults = new ArrayList<Equazione>();
			final int sz = result.size();
			for (int n = 0; n < sz; n++) {
				newResults.addAll(t.risolvi(result.get(n)));
			}
			Set<Equazione> hs = new HashSet<>();
			hs.addAll(newResults);
			newResults.clear();
			newResults.addAll(hs);
			result = newResults;
		}
		// TODO: controllare se è a posto
		return result;
	}

	@Override
	public Equazione clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

}