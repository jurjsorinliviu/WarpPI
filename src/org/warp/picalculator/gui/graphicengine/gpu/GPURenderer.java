package org.warp.picalculator.gui.graphicengine.gpu;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import org.warp.picalculator.gui.graphicengine.RAWFont;
import org.warp.picalculator.gui.graphicengine.Renderer;

import com.jogamp.opengl.GL2ES2;

public class GPURenderer implements Renderer {

	public GL2ES2 gl;

	@Override
	public void glColor3i(int r, int gg, int b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glColor(int c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int glGetClearColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void glClearColor(int c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glColor4i(int red, int green, int blue, int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glClearColor(int red, int green, int blue, int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glClear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glDrawSkin(int skinwidth, int[] skin, int x0, int y0, int s0, int t0, int s1, int t1,
			boolean transparent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glDrawLine(int x0, int y0, int x1, int y1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glFillRect(int x0, int y0, int w1, int h1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getMatrixOfImage(BufferedImage bufferedImage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void glDrawStringLeft(int x, int y, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glDrawStringCenter(int x, int y, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glDrawStringRight(int x, int y, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glSetFont(RAWFont font) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int glGetStringWidth(RAWFont rf, String text) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int glGetFontWidth(FontMetrics fm, String text) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RAWFont getCurrentFont() {
		// TODO Auto-generated method stub
		return null;
	}

}
