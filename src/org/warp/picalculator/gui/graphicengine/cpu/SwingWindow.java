package org.warp.picalculator.gui.graphicengine.cpu;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.warp.picalculator.Utils;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.PIDisplay;
import org.warp.picalculator.gui.graphicengine.Drawable;

public class SwingWindow extends JFrame {
	private static final long serialVersionUID = 2945898937634075491L;
	public CustomCanvas c;
	private static Drawable d;
	public boolean wasResized = false;

	public SwingWindow(Drawable d) {
		SwingWindow.d = d;
		c = new CustomCanvas();
		c.setDoubleBuffered(false);
		this.add(c);
//		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		if (Utils.debugOn) {
			if (Utils.debugThirdScreen) {
				this.setLocation(2880, 900);
				this.setResizable(false);
				this.setAlwaysOnTop(true);
			}
		} else {
			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
			getContentPane().setCursor(blankCursor);
			
			this.setResizable(false);
		}
		
		this.setTitle("Raspberry PI Calculator by XDrake99 (Andrea Cavalli)");
		
		
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
				PIDisplay.display.destroy();
			}

			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentResized(ComponentEvent e) {
				wasResized = true;
			}

			@Override
			public void componentShown(ComponentEvent e) {}
		});
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				Keyboard.debugKeyEvent = arg0;
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case KeyEvent.VK_ESCAPE:
						Keyboard.keyReleased(Key.POWER);
						break;
					case KeyEvent.VK_D:
						Keyboard.keyReleased(Key.debug_DEG);
						break;
					case KeyEvent.VK_R:
						Keyboard.keyReleased(Key.debug_RAD);
						break;
					case KeyEvent.VK_G:
						Keyboard.keyReleased(Key.debug_GRA);
						break;
					case KeyEvent.VK_X:
						if (Keyboard.alpha) {
							Keyboard.keyReleased(Key.LETTER_X);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_P:
						if (Keyboard.alpha) {
							Keyboard.keyReleased(Key.PI);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_B:
						if (Keyboard.shift) {
							Keyboard.keyReleased(Key.BRIGHTNESS_CYCLE_REVERSE);
						} else if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.BRIGHTNESS_CYCLE);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_ENTER:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.SOLVE);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						int row = 2;
						int col = 1;
						Keyboard.debugKeysDown[row-1][col-1] = false;
						break;
					case KeyEvent.VK_1:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug1);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_2:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug2);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_3:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug3);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_4:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug4);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_5:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug5);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_SHIFT:
						Keyboard.keyReleased(Key.SHIFT);
						break;
					case KeyEvent.VK_A:
						Keyboard.keyReleased(Key.ALPHA);
						break;
					case KeyEvent.VK_M:
						Keyboard.keyPressed(Key.SURD_MODE);
						break;
					case KeyEvent.VK_LEFT:
						//LEFT
						row = 2;
						col = 3;
						Keyboard.debugKeysDown[row-1][col-1] = false;
					case KeyEvent.VK_RIGHT:
						//RIGHT
						row = 2;
						col = 5;
						Keyboard.debugKeysDown[row-1][col-1] = false;
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void setSize(int width, int height) {
		c.setSize(width, height);
		super.getContentPane().setPreferredSize(new Dimension(width, height));
		super.pack();
	}

	@Override
	public Dimension getSize() {
		return c.getSize();
	}

	@Override
	public int getWidth() {
		return c.getWidth();
	}

	@Override
	public int getHeight() {
		return c.getHeight();
	}

//	private static ArrayList<Double> mediaValori = new ArrayList<Double>();

	public static class CustomCanvas extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 605243927485370885L;

		@Override
		public void paintComponent(Graphics g) {
//			long time1 = System.nanoTime();
			d.refresh();

	        final int[] a = ((DataBufferInt) CPUDisplay.g.getRaster().getDataBuffer()).getData();
//		        System.arraycopy(canvas2d, 0, a, 0, canvas2d.length);
	        CPUDisplay.canvas2d = a;
			g.clearRect(0, 0, CPUDisplay.size[0], CPUDisplay.size[1]);
			g.drawImage(CPUDisplay.g, 0, 0, null);
//			long time2 = System.nanoTime();
//			double timeDelta = ((double)(time2-time1))/1000000000d;
//			double mediaAttuale = timeDelta;
//			mediaValori.add(mediaAttuale);
//			double somma = 0;
//			for (Double val : mediaValori) {
//				somma+=val;
//			}
//			System.out.println(somma/((double)mediaValori.size()));
		}
	}
}
