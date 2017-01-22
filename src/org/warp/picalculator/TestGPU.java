package org.warp.picalculator;

import org.warp.picalculator.gui.PIDisplay;
import org.warp.picalculator.gui.graphicengine.Display;
import org.warp.picalculator.gui.graphicengine.Drawable;
import org.warp.picalculator.gui.graphicengine.RAWFont;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.gpu.GPUDisplay;
import org.warp.picalculator.gui.graphicengine.gpu.GPURenderer;
import org.warp.picalculator.gui.screens.LoadingScreen;

public class TestGPU {

	public static final GPUDisplay d = new GPUDisplay();
	
	public static void main(String[] args) {
		Utils.debugOn = true;
		Utils.debugThirdScreen = false;
		d.create();
		
		Scene s = new Scene(d);
	}
	
	private static class Scene implements Drawable {

		private RAWFont[] fonts;
		private int[] textures;
		
		private GPURenderer r;
		private Display d;
		
		public Scene(Display d) {
			this.d = d;
			this.r = (GPURenderer) d.getRenderer();
			d.start(this);

//			fonts = new RAWFont[1];
//			textures = new int[100];
//			fonts[0] = new RAWFont();
//			fonts[0].create("big");
			new Thread(()->{
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			}).start();
		}

		@Override
		public void refresh() {
		}
		
	}
}
