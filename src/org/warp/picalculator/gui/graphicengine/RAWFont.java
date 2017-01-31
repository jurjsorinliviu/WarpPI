package org.warp.picalculator.gui.graphicengine;

/**
 *
 * @author andreacv
 */
public interface RAWFont extends RAWSkin {

	public int getStringWidth(String text);

	public int getCharacterWidth();

	public int getCharacterHeight();
}
