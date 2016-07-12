package org.warpgate.pi.calculator;

import java.util.Arrays;
import java.util.List;

import com.rits.cloning.Cloner;

public abstract class FunzioneMultipla implements Funzione {
	public FunzioneMultipla() {
		setVariables(new Funzione[]{});
	}
	public FunzioneMultipla(Funzione[] values) {
		setVariables(values);
	}
	protected Funzione[] variables;
	public Funzione[] getVariables() {
		return variables;
	}
	public void setVariables(Funzione[] value) {
		variables = value;
	}
	public void setVariables(final List<Funzione> value) {
		int vsize = value.size();
		Funzione[] tmp = new Funzione[vsize];
		for (int i = 0; i < vsize; i++) {
			tmp[i] = value.get(i);
		}
		variables = tmp;
	}
	
	public Funzione getVariable(int index) {
		return variables[index];
	}
	public void setVariable(int index, Funzione value) {
		variables[index] = value;
	}
	
	public void addVariableToEnd(Funzione value) {
		int index = variables.length;
		setVariablesLength(index+1);
		variables[index] = value;
	}

	public int getVariablesLength() {
		return variables.length;
	}
	
	public void setVariablesLength(int length) {
		variables = Arrays.copyOf(variables, length);
	}
	
	@Override
	public abstract String simbolo();
	@Override
	public abstract Termine calcola() throws Errore;
	
	@Override
	public String toString() {
		try {
			return calcola().toString();
		} catch (Errore e) {
			return e.id.toString();
		}
	}

	@Override
	public Funzione clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
}
