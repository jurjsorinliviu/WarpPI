package org.warp.picalculator;

import java.util.Arrays;
import java.util.List;

import com.rits.cloning.Cloner;

public abstract class FunzioneMultiplaBase extends FunzioneBase {
	public FunzioneMultiplaBase() {
		setVariables(new FunzioneBase[] {});
	}

	public FunzioneMultiplaBase(FunzioneBase[] values) {
		setVariables(values);
	}

	protected FunzioneBase[] variables;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public FunzioneBase[] getVariables() {
		return variables;
	}

	public void setVariables(FunzioneBase[] value) {
		variables = value;
	}

	public void setVariables(final List<FunzioneBase> value) {
		int vsize = value.size();
		FunzioneBase[] tmp = new FunzioneBase[vsize];
		for (int i = 0; i < vsize; i++) {
			tmp[i] = value.get(i);
		}
		variables = tmp;
	}

	public FunzioneBase getVariable(int index) {
		return variables[index];
	}

	public void setVariable(int index, FunzioneBase value) {
		variables[index] = value;
	}

	public void addVariableToEnd(FunzioneBase value) {
		int index = variables.length;
		setVariablesLength(index + 1);
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
	public abstract void calcolaGrafica();
	
	@Override
	public String toString() {
		try {
			return calcola().toString();
		} catch (Errore e) {
			return e.id.toString();
		}
	}

	@Override
	public FunzioneMultiplaBase clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}
}
