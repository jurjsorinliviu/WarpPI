package org.warp.picalculator.gui.graphicengine.cpu;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Skin;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine.CPURenderer;

public class CPUSkin implements Skin {

	public int[] skinData;
	public int[] skinSize;

	CPUSkin(String file) throws IOException {
		load(file);
	}

	@Override
	public void load(String file) throws IOException {
		final BufferedImage img = ImageIO.read(this.getClass().getResource("/" + file));
		skinData = getMatrixOfImage(img);
		skinSize = new int[] { img.getWidth(), img.getHeight() };
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
		((CPURenderer) d.getRenderer()).currentSkin = this;
	}

}
