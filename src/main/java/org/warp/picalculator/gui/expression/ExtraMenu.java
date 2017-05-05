package org.warp.picalculator.gui.expression;

import java.io.Serializable;

import org.warp.picalculator.device.KeyboardEventListener;
import org.warp.picalculator.gui.GraphicalElement;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

public abstract class ExtraMenu<T extends Block> implements Serializable, KeyboardEventListener {
	
	private static final long serialVersionUID = -6944683477814944299L;

	public ExtraMenu(T block) {
		this.block = block;
		this.location = new int[]{0,0};
		this.width = 0;
		this.height = 0;
	}
	
	public final T block;
	protected int width;
	protected int height;
	protected int[] location;
	
	public abstract void draw(GraphicEngine ge, Renderer r, Caret caret);

	public abstract void open();
	
	public abstract void close();

	public boolean beforeRender(float delta, Caret caret) {
		int[] l = caret.getLastLocation();
		int[] cs = caret.getLastSize();
		location[0]=l[0]-block.getWidth()/2-width/2;
		location[1]=l[1]+cs[1];
		return false;
	}

}
