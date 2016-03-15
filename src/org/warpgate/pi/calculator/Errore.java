package org.warpgate.pi.calculator;

public class Errore extends java.lang.Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1014947815755694651L;
	
	public Errore(Errori IDErrore) {
		id = IDErrore;
	}
	
	public Errori id = Errori.ERROR;
}
