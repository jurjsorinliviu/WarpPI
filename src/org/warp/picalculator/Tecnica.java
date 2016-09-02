package org.warp.picalculator;

import java.util.ArrayList;

public interface Tecnica {
	public static final Tecnica[] tecniche = new Tecnica[] {};

	public abstract ArrayList<Equazione> risolvi(Equazione equazione);
}
