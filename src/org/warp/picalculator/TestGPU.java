package org.warp.picalculator;

import java.io.IOException;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Skin;
import org.warp.picalculator.gui.graphicengine.gpu.GPUEngine;
import org.warp.picalculator.gui.graphicengine.gpu.GPURenderer;
import org.warp.picalculator.gui.screens.KeyboardDebugScreen;
import org.warp.picalculator.gui.screens.MarioScreen;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class TestGPU {

	public static final GPUEngine d = new GPUEngine();

	public static void main(String[] args) throws IOException {
		Utils.debugOn = true;
		Utils.debugThirdScreen = false;
		d.create();

		final Scene s = new Scene(d);
	}

	private static class Scene implements RenderingLoop {

		private BinaryFont exampleFont;
		private final Skin exampleSkin;

		private final GPURenderer r;
		private final GraphicEngine d;

		public Scene(GraphicEngine d) throws IOException {
			this.d = d;
			r = (GPURenderer) d.getRenderer();

			exampleFont = d.loadFont("ex");

			exampleSkin = d.loadSkin("skin.png");

			d.start(this);

//			fonts = new RAWFont[1];
//			textures = new int[100];
//			fonts[0] = new RAWFont();
//			fonts[0].create("big");
			new Thread(() -> {
				try {
					for (int i = 0; i < 12; i++) {
						Utils.printSystemResourcesUsage();
						Thread.sleep(5000);
					}
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			}).start();
			
			d.waitUntilExit();
		}

		@Override
		public void refresh() {
			exampleSkin.use(d);
			r.glClearColor(0xFFD0FDCF);
			r.glClear(d.getWidth(), d.getHeight());
			r.glColor3f(1.0f, 1.0f, 1.0f);
			r.glFillRect(2, 2, 160, 160, 0, 0, 16, 16);
			exampleFont.use(d);
			r.glColor3f(1, 0, 0);
			r.glDrawStringLeft(10, 170, "Prova! 123456789");
			
			//MSAA TEST
			r.glDrawStringLeft(10f, 190.5f, "Test MSAA");
			exampleSkin.use(d);
			r.glColor3f(1.0f, 1.0f, 1.0f);
			r.glFillRect(162, 2.5f, 160, 160, 0, 0, 16, 16);
		}

	}
}
