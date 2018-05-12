package org.warp.picalculator.gui.graphicengine.cpu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Skin;

public class CPUSkin implements Skin {

	public int[] skinData;
	public int[] skinSize;
	private final boolean isResource;

	public CPUSkin(String file) throws IOException {
		isResource = !new File(file).exists();
		load(file);
	}

	@Override
	public void load(String file) throws IOException {
		final BufferedImage img = ImageIO.read(isResource ? this.getClass().getResource("/" + file) : new File(file).toURI().toURL());
		if (img == null) {
			skinData = new int[0];
			skinSize = new int[] { 0, 0 };
		} else {
			skinData = getMatrixOfImage(img);
			skinSize = new int[] { img.getWidth(), img.getHeight() };
		}
	}

	public static int[] getMatrixOfImage(BufferedImage bufferedImage) {
		final int width = bufferedImage.getWidth(null);
		final int height = bufferedImage.getHeight(null);
		final int[] pixels = new int[width * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i + j * width] = bufferedImage.getRGB(i, j);
			}
		}

		return pixels;
	}

	@Override
	public void initialize(GraphicEngine d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void use(GraphicEngine d) {
		if (d.getRenderer() instanceof CPURenderer) {
			((CPURenderer) d.getRenderer()).currentSkin = this;
		}
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public int getSkinWidth() {
		return skinSize[0];
	}

	@Override
	public int getSkinHeight() {
		return skinSize[1];
	}

}
