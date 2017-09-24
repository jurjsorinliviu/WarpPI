package org.warp.picalculator.gui.graphicengine;

import java.io.IOException;

public interface Skin {

	public void load(String file) throws IOException;

	public void initialize(GraphicEngine d);

	public boolean isInitialized();

	public void use(GraphicEngine d);
}
