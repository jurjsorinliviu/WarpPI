package org.warp.picalculator.device;

import java.awt.event.KeyEvent;

import org.warp.picalculator.Utils;
import org.warp.picalculator.device.chip.ParallelToSerial;
import org.warp.picalculator.device.chip.SerialToParallel;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.GUIErrorMessage;
import org.warp.picalculator.gui.screens.KeyboardDebugScreen;
import org.warp.picalculator.gui.screens.MarioScreen;
import org.warp.picalculator.gui.screens.Screen;

import com.pi4j.wiringpi.Gpio;

public class Keyboard {
	public static volatile boolean alpha = false;
	public static volatile boolean shift = false;

	//From Serial
	private static final int RCK_pin = 35;
	private static final int SCK_and_CLK_pin = 38;
	private static final int SER_pin = 36;

	//To Serial
	private static final int SH_LD_pin = 37;
	private static final int QH_pin = 40;
	private static final int CLK_INH_pin = 33;

	private static volatile boolean[][] precedentStates = new boolean[8][8];
	public static volatile boolean[][] debugKeysDown = new boolean[8][8];
	public static volatile int debugKeyCode = -1;

	private static volatile boolean refreshRequest = false;

	private static KeyboardEventListener additionalListener;

	public synchronized static void startKeyboard() {
		final Thread kt = new Thread(() -> {
			if (Utils.debugOn) {
				try {
					while (true) {
						if (debugKeyCode != -1) {
							debugKeyPressed(debugKeyCode);
							debugKeyCode = -1;
						}
						Thread.sleep(50);
					}
				} catch (final InterruptedException e) {}
			} else {
				Gpio.pinMode(CLK_INH_pin, Gpio.OUTPUT);
				Gpio.pinMode(RCK_pin, Gpio.OUTPUT);
				Gpio.pinMode(SER_pin, Gpio.OUTPUT);
				Gpio.pinMode(SH_LD_pin, Gpio.OUTPUT);
				Gpio.pinMode(SCK_and_CLK_pin, Gpio.OUTPUT);
				Gpio.pinMode(QH_pin, Gpio.INPUT);

				Gpio.digitalWrite(CLK_INH_pin, false);
				Gpio.digitalWrite(RCK_pin, false);
				Gpio.digitalWrite(SER_pin, false);
				Gpio.digitalWrite(SH_LD_pin, false);
				Gpio.digitalWrite(SCK_and_CLK_pin, false);
				Gpio.digitalWrite(QH_pin, false);
				final SerialToParallel chip1 = new SerialToParallel(RCK_pin, SCK_and_CLK_pin /*SCK*/, SER_pin);
				final ParallelToSerial chip2 = new ParallelToSerial(SH_LD_pin, CLK_INH_pin, QH_pin, SCK_and_CLK_pin/*CLK*/);

				KeyboardDebugScreen.log("Started keyboard system");

				while (true) {
					boolean[] data;
					for (int col = 0; col < 8; col++) {
						data = new boolean[8];
						data[col] = true;
						chip1.write(data);

						data = chip2.read();
						KeyboardDebugScreen.ks[col] = data;

						for (int row = 0; row < 8; row++) {
							if (data[row] == true && precedentStates[row][col] == false) {
								System.out.println("Pressed button at " + (row + 1) + ", " + (col + 1));
								KeyboardDebugScreen.log("Pressed button at " + (row + 1) + ", " + (col + 1));
								keyPressedRaw(row + 1, col + 1);
							} else if (data[row] == false && precedentStates[row][col] == true) {
								keyReleasedRaw(row + 1, col + 1);
								KeyboardDebugScreen.log("Released button at " + (row + 1) + ", " + (col + 1));
							}
							precedentStates[row][col] = data[row];
						}
					}
				}
			}
		});
		kt.setName("Keyboard thread");
		kt.setPriority(Thread.MIN_PRIORITY);
		kt.setDaemon(true);
		kt.start();
	}

	private synchronized static void debugKeyPressed(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_ESCAPE:
				Keyboard.keyPressed(Key.POWER);
				break;
			case KeyEvent.VK_S:
				if (Keyboard.shift) {
					Keyboard.keyPressed(Key.ARCSINE);
				} else if (Keyboard.alpha) {
					Keyboard.keyPressed(Key.NONE);
				} else {
					Keyboard.keyPressed(Key.SINE);
				}
				break;
			case KeyEvent.VK_C:
				if (Keyboard.shift) {
					Keyboard.keyPressed(Key.ARCCOSINE);
				} else if (Keyboard.alpha) {
					Keyboard.keyPressed(Key.NONE);
				} else {
					Keyboard.keyPressed(Key.COSINE);
				}
				break;
			case KeyEvent.VK_T:
				if (Keyboard.shift) {
					Keyboard.keyPressed(Key.ARCTANGENT);
				} else if (Keyboard.alpha) {
					Keyboard.keyPressed(Key.NONE);
				} else {
					Keyboard.keyPressed(Key.TANGENT);
				}
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
			case KeyEvent.VK_P:
				if (Keyboard.alpha) {
					Keyboard.keyPressed(Key.PI);
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
			case com.jogamp.newt.event.KeyEvent.VK_ENTER:
			case KeyEvent.VK_ENTER:
				if (Keyboard.shift) {
					Keyboard.keyPressed(Key.STEP);
				} else if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.SIMPLIFY);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				int row = 2;
				int col = 1;
				Keyboard.debugKeysDown[row - 1][col - 1] = true;
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
				} else if (Keyboard.shift) {
					if (Utils.debugOn) {
						Keyboard.keyPressed(Key.DIVIDE);
					}
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
			case com.jogamp.newt.event.KeyEvent.VK_ADD:
			case KeyEvent.VK_ADD:
				if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.PLUS);
				} else if (Keyboard.shift) {
					Keyboard.keyPressed(Key.PLUS_MINUS);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				break;
			case com.jogamp.newt.event.KeyEvent.VK_SUBTRACT:
			case KeyEvent.VK_SUBTRACT:
				if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.MINUS);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				break;
			case com.jogamp.newt.event.KeyEvent.VK_MULTIPLY:
			case KeyEvent.VK_MULTIPLY:
				if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.MULTIPLY);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				break;
			case com.jogamp.newt.event.KeyEvent.VK_DIVIDE:
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
			case com.jogamp.newt.event.KeyEvent.VK_DELETE:
			case KeyEvent.VK_DELETE:
				if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.RESET);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				break;
			case com.jogamp.newt.event.KeyEvent.VK_LEFT:
			case KeyEvent.VK_LEFT:
				//LEFT
				row = 2;
				col = 3;
				Keyboard.debugKeysDown[row - 1][col - 1] = true;
				if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.LEFT);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				break;
			case com.jogamp.newt.event.KeyEvent.VK_RIGHT:
			case KeyEvent.VK_RIGHT:
				//RIGHT
				row = 2;
				col = 5;
				Keyboard.debugKeysDown[row - 1][col - 1] = true;
				if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.RIGHT);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				break;
			case com.jogamp.newt.event.KeyEvent.VK_NUMPAD4:
			case KeyEvent.VK_NUMPAD4:
				if (!Keyboard.shift && !Keyboard.alpha) {
					Keyboard.keyPressed(Key.HISTORY_BACK);
				} else {
					Keyboard.keyPressed(Key.NONE);
				}
				break;
			case com.jogamp.newt.event.KeyEvent.VK_NUMPAD6:
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
			case com.jogamp.newt.event.KeyEvent.VK_SHIFT:
			case KeyEvent.VK_SHIFT:
				Keyboard.keyPressed(Key.SHIFT);
				break;
			case KeyEvent.VK_A:
				Keyboard.keyPressed(Key.ALPHA);
				break;
			case com.jogamp.newt.event.KeyEvent.VK_NUMPAD1:
			case KeyEvent.VK_NUMPAD1:
				Keyboard.keyPressed(Key.SQRT);
				break;
			case com.jogamp.newt.event.KeyEvent.VK_NUMPAD2:
			case KeyEvent.VK_NUMPAD2:
				Keyboard.keyPressed(Key.ROOT);
				break;
			case com.jogamp.newt.event.KeyEvent.VK_NUMPAD3:
			case KeyEvent.VK_NUMPAD3:
				Keyboard.keyPressed(Key.POWER_OF_2);
				break;
			case com.jogamp.newt.event.KeyEvent.VK_NUMPAD5:
			case KeyEvent.VK_NUMPAD5:
				Keyboard.keyPressed(Key.POWER_OF_x);
				break;
		}
	}

	public static boolean isKeyDown(int row, int col) {
		if (Utils.debugOn == false) {
			return precedentStates[row - 1][col - 1];
		} else {
			return debugKeysDown[row - 1][col - 1];
		}
	}

	private synchronized static void keyReleasedRaw(int row, int col) {
		KeyboardDebugScreen.keyX = row;
		KeyboardDebugScreen.keyY = col;
		if (row == 1 && col == 1) {
			//keyReleased(Key.BRIGHTNESS_CYCLE);
		}
	}

	static synchronized void keyPressedRaw(int row, int col) {
		KeyboardDebugScreen.keyX = row;
		KeyboardDebugScreen.keyY = col;
		if (row == 1 && col == 1) {
			keyPressed(Key.SHIFT);
		} else if (row == 1 && col == 2) {
			keyPressed(Key.ALPHA);
		} else if (row == 1 && col == 7) {
			if (shift) {
				keyPressed(Key.BRIGHTNESS_CYCLE_REVERSE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.BRIGHTNESS_CYCLE);
			}
		} else if (row == 1 && col == 8) {
			if (shift) {
				keyPressed(Key.STEP);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.SIMPLIFY);
			}
		} else if (row == 2 && col == 8) {
			if (shift) {
				keyPressed(Key.PI);
			} else if (alpha) {
				keyPressed(Key.DRG_CYCLE);
			} else {
				keyPressed(Key.NONE);
			}
		} else if (row == 3 && col == 2) {
			if (shift) {
				keyPressed(Key.ROOT);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.SQRT);
			}
		} else if (row == 4 && col == 8) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.LETTER_Y);
			} else {
				keyPressed(Key.DOT);
			}
		} else if (row == 5 && col == 8) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.LETTER_X);
			} else {
				keyPressed(Key.NUM0);
			}
		} else if (row == 8 && col == 1) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM1);
			}
		} else if (row == 8 && col == 2) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM2);
			}
		} else if (row == 8 && col == 3) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM3);
			}
		} else if (row == 7 && col == 1) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM4);
			}
		} else if (row == 7 && col == 2) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM5);
			}
		} else if (row == 7 && col == 3) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM6);
			}
		} else if (row == 6 && col == 1) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM7);
			}
		} else if (row == 6 && col == 2) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM8);
			}
		} else if (row == 6 && col == 3) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.NUM9);
			}
		} else if (row == 8 && col == 4) {
			if (shift) {
				keyPressed(Key.PLUS_MINUS);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.PLUS);
			}
		} else if (row == 8 && col == 5) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.MINUS);
			}
		} else if (row == 7 && col == 4) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.MULTIPLY);
			}
		} else if (row == 7 && col == 5) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.DIVIDE);
			}
		} else if (row == 6 && col == 4) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.DELETE);
			}
		} else if (row == 6 && col == 5) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.RESET);
			}
		} else if (row == 1 && col == 4) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.UP);
			}
		} else if (row == 2 && col == 3) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.LEFT);
			}
		} else if (row == 2 && col == 5) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.RIGHT);
			}
		} else if (row == 3 && col == 4) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.DOWN);
			}
		} else if (row == 4 && col == 3) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.POWER_OF_2);
			}
		} else if (row == 4 && col == 4) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.POWER_OF_x);
			}
		} else if (row == 5 && col == 3) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.PARENTHESIS_OPEN);
			}
		} else if (row == 5 && col == 4) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.PARENTHESIS_CLOSE);
			}
		} else if (row == 5 && col == 6) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.SURD_MODE);
			}
		} else if (row == 2 && col == 1) {
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.EQUAL);
			}
		} else if (row == 2 && col == 6) {
			System.out.println("PREMUTO <");
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				System.out.println("PREMUTO <");
				keyPressed(Key.HISTORY_BACK);
			}
		} else if (row == 2 && col == 7) {
			System.out.println("PREMUTO >");
			if (shift) {
				keyPressed(Key.NONE);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				System.out.println("PREMUTO >");
				keyPressed(Key.HISTORY_FORWARD);
			}
		} else {}
	}

	public static void stopKeyboard() {
		if (Utils.debugOn == false) {
			Gpio.digitalWrite(33, false);
			Gpio.digitalWrite(35, false);
			Gpio.digitalWrite(36, false);
			Gpio.digitalWrite(37, false);
			Gpio.digitalWrite(38, false);
			Gpio.digitalWrite(40, false);
		}
	}

	public synchronized static void keyPressed(Key k) {
		boolean done = false;
		if (additionalListener != null) {
			try {
				done = additionalListener.keyPressed(k);
			} catch (final Exception ex) {
				new GUIErrorMessage(ex);
			}
		}
		if (DisplayManager.INSTANCE != null) {
			final Screen scr = DisplayManager.INSTANCE.getScreen();
			boolean refresh = false;
			boolean scrdone = false;
			try {
				scrdone = scr.keyPressed(k);
			} catch (final Exception ex) {
				new GUIErrorMessage(ex);
			}
			if (scr != null && scr.initialized && scrdone) {
				refresh = true;
			} else {
				switch (k) {
					case POWER:
						DisplayManager.engine.destroy();
						break;
					case NONE:
						break;
					case LETTER_X:
						letterPressed('X');
						break;
					case BRIGHTNESS_CYCLE:
						DisplayManager.cycleBrightness(false);
						refresh = true;
						break;
					case BRIGHTNESS_CYCLE_REVERSE:
//						DisplayManager.INSTANCE.setScreen(new MarioScreen()); //TODO: rimuovere: prova
						DisplayManager.cycleBrightness(true);
						refresh = true;
						break;
					case HISTORY_BACK:
						DisplayManager.INSTANCE.goBack();
						refresh = true;
						break;
					case HISTORY_FORWARD:
						DisplayManager.INSTANCE.goForward();
						refresh = true;
						break;
					default:
						break;
				}
			}
			switch (k) {
				case SHIFT:
					Keyboard.shift = !Keyboard.shift;
					refresh = true;
					break;
				case ALPHA:
					Keyboard.alpha = !Keyboard.alpha;
					refresh = true;
					break;
				default:
					break;
			}
			if (Utils.debugOn == false) {
				if (k != Key.SHIFT && Keyboard.shift) {
					Keyboard.shift = false;
					refresh = true;
				} else if (k != Key.ALPHA && Keyboard.alpha) {
					Keyboard.alpha = false;
					refresh = true;
				}
			}
			if (refresh) {
				refreshRequest = true;
			}
		} else if (!done) {
			Utils.debug.println("Key " + k.toString() + " ignored.");
		}
	}

	private static void letterPressed(char L) {

	}

	public synchronized static void keyReleased(Key k) {
		boolean done = false;
		if (additionalListener != null) {
			done = additionalListener.keyReleased(k);
		}
		boolean refresh = false;
		if (DisplayManager.INSTANCE != null) {
			final Screen scr = DisplayManager.INSTANCE.getScreen();
			if (scr != null && scr.initialized && scr.keyReleased(k)) {
				refresh = true;
			} else {
				switch (k) {
					case NONE:
						break;
					default:
						break;
				}
			}
			if (refresh) {
				refreshRequest = true;
			}
		} else if (!done) {
			Utils.debug.println("Key " + k.toString() + " ignored.");
		}
	}

	public static void setAdditionalKeyboardListener(KeyboardEventListener l) {
		additionalListener = l;
	}

	public static enum Key {
		POWER, debug_DEG, debug_RAD, debug_GRA, SHIFT, ALPHA, NONE, HISTORY_BACK, HISTORY_FORWARD, SURD_MODE, DRG_CYCLE, LETTER_X, LETTER_Y, STEP, SIMPLIFY, BRIGHTNESS_CYCLE, BRIGHTNESS_CYCLE_REVERSE, DOT, NUM0, NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9, PARENTHESIS_OPEN, PARENTHESIS_CLOSE, PLUS, MINUS, PLUS_MINUS, MULTIPLY, DIVIDE, EQUAL, DELETE, RESET, LEFT, RIGHT, UP, DOWN, OK, debug1, debug2, debug3, debug4, debug5, SQRT, ROOT, POWER_OF_2, POWER_OF_x, SINE, COSINE, TANGENT, ARCSINE, ARCCOSINE, ARCTANGENT, PI
	}

	public static boolean popRefreshRequest() {
		if (refreshRequest) {
			refreshRequest = false;
			return true;
		}
		return false;
	}

}

/*


-coord-
 NORMAL
 SHIFT
 ALPHA
-------

|1,1---|1,2---|------|1,4---|------|------|1,7---|
|SHIFT |ALPHA |------|  ^   |------|------|+BRIGH|
|SHIFT |ALPHA |------|      |------|------|-BRIGH|
|SHIFT |ALPHA |------|      |------|------|      |
|2,1---|2,2---|2,3---|2,4---|2,5---|2,6---|2,7---|
| =    |      |  <   |  OK  |   >  | Back | Fwd  |
|      |      |      |      |      |      |      |
|      |      |      |      |      |      |      |
|3,1---|3,2---|------|3,4---|------|3,6---|3,7---|
|      | SQRT |------|  v   |------|      |      |
|      | ROOT |------|      |------|      |      |
|      |      |------|      |------|      |      |
|4,1---|4,2---|4,3---|4,4---|4,5---|4,6---|4,7---|
|      |      | POW 2| POW x|      |      |      |
|      |      |      |      |      |      |      |
|      |      |      |      |      |      |      |
|5,1---|5,2---|5,3---|5,4---|5,5---|5,6---|5,7---|
|      |      |      |      |      |S<=>D |      |
|      |      |      |      |      |      |      |
|      |      |      |      |      |      |      |
|6,1---|6,2---|6,3---|6,4---|6,5---|6,6---|6,7---|
| 7    | 8    | 9    | DEL  | RESET              |
|      |      |      |      |                    |
|      |      |      |      |                    |
|7,1---|7,2---|7,3---|7,4---|7,5-----------------|
| 4    | 5    | 6    | *    | /                  |
|      |      |      |      |                    |
|      |      |      |      |                    |
|8,1---|8,2---|8,3---|8,4---|8,5-----------------|
| 1    | 2    | 3    |  +   | -                  |
|      |      |      |      |                    |
|      |      |      |      |                    |
|5,8---|4,8---|3,8---|2,8---|1,8-----------------|
| 0    | .    |      |      | SIMPLIFY           |
|      |      |      |PI    | STEP               |
| X    | Y    | Z    |DRGCYCL|SOLVE FOR [x]      |
|------|------|------|------|--------------------|


*/