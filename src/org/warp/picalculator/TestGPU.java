package org.warp.picalculator;

import java.io.IOException;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;
import org.warp.picalculator.gui.expression.BlockChar;
import org.warp.picalculator.gui.expression.BlockContainer;
import org.warp.picalculator.gui.expression.BlockDivision;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.expression.CaretState;
import org.warp.picalculator.gui.expression.containers.NormalInputContainer;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Skin;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.gui.graphicengine.gpu.GPUEngine;
import org.warp.picalculator.gui.graphicengine.gpu.GPURenderer;
import org.warp.picalculator.gui.screens.KeyboardDebugScreen;
import org.warp.picalculator.gui.screens.MarioScreen;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.parser.MathParser;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class TestGPU {

	public static final GraphicEngine d = new GPUEngine();

	public static void main(String[] args) throws IOException, Error {
		Utils.debugOn = true;
		Utils.debugThirdScreen = false;
		d.create();

		final Scene s = new Scene(d);
	}

	private static class Scene implements RenderingLoop {

		private BinaryFont exampleFont;
		private final Skin exampleSkin;

		private final Renderer r;
		private final GraphicEngine d;
		
		private final NormalInputContainer c;
		
		public Scene(GraphicEngine d) throws IOException, Error {
			this.d = d;
			r = d.getRenderer();

			exampleFont = d.loadFont("ex");

			exampleSkin = d.loadSkin("skin.png");

			BlockContainer.initializeFonts(d.loadFont("ex"), d.loadFont("big"));
			

			//New expression framework test
			c = new NormalInputContainer(false, 0, 200);
			c.typeChar(MathematicalSymbols.DIVISION);
			c.typeChar('5');
			c.typeChar(MathematicalSymbols.MULTIPLICATION);
			c.typeChar('2');
			c.moveRight();
			c.typeChar('2');
			c.moveRight();
			c.typeChar(MathematicalSymbols.MULTIPLICATION);
			c.typeChar('2');
			c.typeChar('2');
			c.recomputeDimensions();
			
			Expression expr = MathParser.parseInput(new MathContext(), c.root);
			System.out.println("Parsed input:"+expr.toString());
			
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
			r.glDrawStringLeft(10, 170, "Prova! 123456789 222");
			
			//MSAA TEST
			r.glDrawStringLeft(10f, 190.5f, "Test MSAA");
			exampleSkin.use(d);
			r.glColor3f(1.0f, 1.0f, 1.0f);
			r.glFillRect(162, 2.5f, 160, 160, 0, 0, 16, 16);
			
			//New expression framework test
			c.draw(d, r, 10, 220);
		}

	}
}
