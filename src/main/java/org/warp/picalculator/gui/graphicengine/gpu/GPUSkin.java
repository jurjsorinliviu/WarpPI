package org.warp.picalculator.gui.graphicengine.gpu;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Skin;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;

public class GPUSkin implements Skin {

	public Texture t;
	public int w;
	public int h;

	private String texturePath;
	private boolean initialized = false;

	GPUSkin(String file) throws IOException {
		load(file);
	}

	@Override
	public void load(String file) throws IOException {
		if ((this.getClass().getClassLoader().getResource(file)) == null) {
			throw new IOException("File '" + file + "' not found!");
		}
		texturePath = file;
	}

	@Override
	public void initialize(GraphicEngine d) {
		try {
			final BufferedImage i = GPURenderer.openTexture(texturePath);
			final GL2ES1 gl = GPURenderer.gl;
			t = GPURenderer.importTexture(i);
			w = i.getWidth();
			h = i.getHeight();
			t.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			initialized = true;
		} catch (GLException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void use(GraphicEngine d) {
		if (!initialized) {
			initialize(d);
		}
		final GPURenderer r = (GPURenderer) d.getRenderer();
		r.useTexture(t, w, h);
	}

}
