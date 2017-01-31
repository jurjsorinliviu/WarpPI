package org.warp.picalculator.gui.graphicengine;

import java.io.IOException;

public interface RAWSkin {

	public void load(String file) throws IOException;

	public void initialize(Display d);

	public void use(Display d);
}
