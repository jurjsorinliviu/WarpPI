package org.warp.picalculator;

public class Error extends java.lang.Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1014947815755694651L;

	public Error(Errors ErrorID) {
		id = ErrorID;
	}

	public Errors id = Errors.ERROR;
}
