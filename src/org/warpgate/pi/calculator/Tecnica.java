package org.warpgate.pi.calculator;

import java.util.Vector;

public interface Tecnica {
	public static final Tecnica[] tecniche = new Tecnica[] {};

	public abstract Vector<Equazione> risolvi(Equazione equazione);
}
