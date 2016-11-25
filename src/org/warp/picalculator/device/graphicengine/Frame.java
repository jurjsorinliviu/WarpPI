package org.warp.picalculator.device.graphicengine;

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
import org.warp.picalculator.device.PIDisplay;

public class Frame extends JFrame {
	private static final long serialVersionUID = 2945898937634075491L;
	public CustomCanvas c;
	public boolean wasResized = false;

	public Frame() {
		c = new CustomCanvas();
		c.setDoubleBuffered(false);
		this.add(c);
//		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		if (!Utils.debugOn) {
			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
			getContentPane().setCursor(blankCursor);
		}
		
		this.setTitle("Raspberry PI Calculator by XDrake99 (Andrea Cavalli)");
		this.setResizable(Utils.debugOn);
		
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
				Display.destroy();
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
				switch (arg0.getKeyCode()) {
					case KeyEvent.VK_ESCAPE:
						Keyboard.keyPressed(Key.POWER);
						break;
					case KeyEvent.VK_D:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.debug_DEG);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_R:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.debug_RAD);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_G:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.debug_GRA);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_X:
						if (Keyboard.alpha) {
							Keyboard.keyPressed(Key.LETTER_X);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_Y:
						if (Keyboard.alpha) {
							Keyboard.keyPressed(Key.LETTER_Y);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_B:
						if (Keyboard.shift) {
							Keyboard.keyPressed(Key.BRIGHTNESS_CYCLE_REVERSE);
						} else if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.BRIGHTNESS_CYCLE);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_ENTER:
						if (Keyboard.shift) {
							Keyboard.keyPressed(Key.SIMPLIFY);
						} else if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.SOLVE);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						int row = 2;
						int col = 1;
						Keyboard.debugKeysDown[row-1][col-1] = true;
						break;
					case KeyEvent.VK_1:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM1);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_2:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM2);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_3:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM3);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_4:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM4);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_5:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM5);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_6:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM6);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_7:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM7);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_8:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM8);
						} else if (Keyboard.shift) {
							Keyboard.keyPressed(Key.PARENTHESIS_OPEN);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_9:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM9);
						} else if (Keyboard.shift) {
							Keyboard.keyPressed(Key.PARENTHESIS_CLOSE);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_0:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.NUM0);
						} else if (Keyboard.shift) {
							Keyboard.keyPressed(Key.EQUAL);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_S:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.SURD_MODE);
						} else if (Keyboard.shift) {
							Keyboard.keyPressed(Key.NONE);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_ADD:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.PLUS);
						} else if (Keyboard.shift) {
							Keyboard.keyPressed(Key.PLUS_MINUS);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_SUBTRACT:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.MINUS);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_MULTIPLY:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.MULTIPLY);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_DIVIDE:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.DIVIDE);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_BACK_SPACE:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.DELETE);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_DELETE:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.RESET);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_LEFT:
						//LEFT
						row = 2;
						col = 3;
						Keyboard.debugKeysDown[row-1][col-1] = true;
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.LEFT);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_RIGHT:
						//RIGHT
						row = 2;
						col = 5;
						Keyboard.debugKeysDown[row-1][col-1] = true;
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.RIGHT);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_NUMPAD4:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.HISTORY_BACK);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_NUMPAD6:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.HISTORY_FORWARD);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_PERIOD:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyPressed(Key.DOT);
						} else {
							Keyboard.keyPressed(Key.NONE);
						}
						break;
					case KeyEvent.VK_SHIFT:
						Keyboard.keyPressed(Key.SHIFT);
						break;
					case KeyEvent.VK_A:
						Keyboard.keyPressed(Key.ALPHA);
						break;
					case KeyEvent.VK_NUMPAD1:
						Keyboard.keyPressed(Key.SQRT);
						break;
					case KeyEvent.VK_NUMPAD2:
						Keyboard.keyPressed(Key.ROOT);
						break;
					case KeyEvent.VK_NUMPAD3:
						Keyboard.keyPressed(Key.POWER_OF_2);
						break;
					case KeyEvent.VK_NUMPAD5:
						Keyboard.keyPressed(Key.POWER_OF_x);
						break;
				}
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
			PIDisplay.INSTANCE.refresh();

	        final int[] a = ((DataBufferInt) Display.g.getRaster().getDataBuffer()).getData();
//		        System.arraycopy(canvas2d, 0, a, 0, canvas2d.length);
	        Display.canvas2d = a;
			g.clearRect(0, 0, Display.size[0], Display.size[1]);
			g.drawImage(Display.g, 0, 0, null);
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
