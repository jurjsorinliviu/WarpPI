package org.warp.picalculator.gui.graphicengine.headless8;

import java.io.IOException;
import java.util.logging.ConsoleHandler;

import org.fusesource.jansi.AnsiConsole;
import org.warp.picalculator.Main;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;

public class Headless256Engine implements org.warp.picalculator.gui.graphicengine.GraphicEngine {

	private Headless256Renderer r = new Headless256Renderer();
	private boolean stopped = true;
	private RenderingLoop renderLoop;
	protected static final int C_WIDTH = Main.screenSize[0]/2;//;60;
	protected static final int C_HEIGHT = Main.screenSize[1]/3;//;40;
	public static final int C_MUL_X = 2;//8;
	public static final int C_MUL_Y = 3;//8;
	private String title = Main.calculatorName;
	
	@Override
	public int[] getSize() {
		new ConsoleHandler();
		return new int[]{C_WIDTH, C_HEIGHT};
	}

	@Override
	public boolean isInitialized() {
		return !stopped;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setResizable(boolean r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDisplayMode(int ww, int wh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create() {
		AnsiConsole.systemInstall();
		AnsiConsole.out.print(Headless256Renderer.ANSI_PREFIX+"?25l");
		stopped = false;
	}

	@Override
	public boolean wasResized() {
		return false;
	}

	@Override
	public int getWidth() {
		return C_WIDTH*C_MUL_X;
	}

	@Override
	public int getHeight() {
		return C_HEIGHT*C_MUL_Y;
	}

	@Override
	public void destroy() {
		stopped = true;
	}

	@Override
	public void start(RenderingLoop d) {
		this.renderLoop = d;
		final Thread th = new Thread(() -> {
			try {
				double extratime = 0;
				while (!stopped) {
					final long start = System.currentTimeMillis();
					repaint();
					final long end = System.currentTimeMillis();
					final double delta = (end - start) / 1000d;
					final int deltaInt = (int) Math.floor(delta);
					final int extraTimeInt = (int) Math.floor(extratime);
					if (extraTimeInt + deltaInt < 200) {
						Thread.sleep(200 - (extraTimeInt + deltaInt));
						extratime = 0;
					} else {
						extratime += delta - 200d;
					}
				}
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		});
		th.setName("Console rendering thread");
		th.setDaemon(true);
		th.start();
	}

	@Override
	public void repaint() {
		renderLoop.refresh();
		r.curColor = 0x1C;
		r.glDrawStringCenter((C_WIDTH*C_MUL_X)/2, 0, title);
		AnsiConsole.out.print(Headless256Renderer.ANSI_PREFIX+"1;1H");
		for (int y = 0; y < C_HEIGHT; y++) {
			int precBgColor = -1;
			int precFgColor = -1;
			int curBgColor = -1;
			int curFgColor = -1;
			for (int x = 0; x < C_WIDTH; x++) {
				curBgColor = r.bgColorMatrix[x+y*C_WIDTH];
				curFgColor = r.fgColorMatrix[x+y*C_WIDTH];
				if (precBgColor != curBgColor) {
					AnsiConsole.out.print(Headless256Renderer.ANSI_PREFIX+Headless256Renderer.ansiBgColorPrefix+curBgColor+Headless256Renderer.ansiColorSuffix);
				}
				if (precFgColor != curFgColor) {
					AnsiConsole.out.print(Headless256Renderer.ANSI_PREFIX+Headless256Renderer.ansiFgColorPrefix+curFgColor+Headless256Renderer.ansiColorSuffix);
				}
				
				AnsiConsole.out.print(r.charmatrix[x+y*C_WIDTH]);
				
				precBgColor = curBgColor;
				precFgColor = curFgColor;
			}
			
			AnsiConsole.out.println();
		}
	}

	@Override
	public Renderer getRenderer() {
		return r;
	}

	@Override
	public Headless256Font loadFont(String file) throws IOException {
		return new Headless256Font();
	}

	@Override
	public Headless256Skin loadSkin(String file) throws IOException {
		return new Headless256Skin(file);
	}

	@Override
	public void waitUntilExit() {
		try {
			do {
				Thread.sleep(500);
			} while (stopped==false);
		} catch (final InterruptedException e) {

		}
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public boolean doesRefreshPauses() {
		return true;
	}
}
