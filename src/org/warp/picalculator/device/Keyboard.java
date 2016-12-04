package org.warp.picalculator.device;

import org.warp.picalculator.Utils;
import org.warp.picalculator.device.chip.ParallelToSerial;
import org.warp.picalculator.device.chip.SerialToParallel;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.device.graphicengine.Screen;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.screens.MarioScreen;

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
	
	public static void startKeyboard() {
		if (Utils.debugOn == false) {
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
			Thread kt = new Thread(()->{
				SerialToParallel chip1 = new SerialToParallel(RCK_pin, SCK_and_CLK_pin /*SCK*/, SER_pin);
				ParallelToSerial chip2 = new ParallelToSerial(SH_LD_pin, CLK_INH_pin, QH_pin, SCK_and_CLK_pin/*CLK*/);
				while(true) {
					boolean[] data;
					for (int col = 0; col < 8; col++) {
						data = new boolean[8];
						data[col] = true;
						chip1.write(data);
						
						data = new boolean[8];
						data = chip2.read();
						
						for (int row = 0; row < 8; row++) {
							if (data[row] == true && precedentStates[row][col] == false) {
								System.out.println("Pressed button at "+(row+1) +", "+(col+1));
								keyPressedRaw(row+1, col+1);
							} else if (data[row] == false && precedentStates[row][col] == true) {
								keyReleasedRaw(row+1, col+1);
							}
							precedentStates[row][col] = data[row];
						}
					}
				}
			});
			kt.setName("Keyboard thread");
			kt.setPriority(Thread.MIN_PRIORITY);
			kt.setDaemon(true);
			kt.start();
		}
	}

	public static boolean isKeyDown(int row, int col) {
		if (Utils.debugOn == false) {
			return precedentStates[row-1][col-1];
		} else {
			return debugKeysDown[row-1][col-1];
		}
	}
	
	private static void keyReleasedRaw(int row, int col) {
		if (row == 1 && col == 1) {
			keyReleased(Key.BRIGHTNESS_CYCLE);
		}
	}

	static void keyPressedRaw(int row, int col) {
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
				keyPressed(Key.SIMPLIFY);
			} else if (alpha) {
				keyPressed(Key.NONE);
			} else {
				keyPressed(Key.SOLVE);
			}
		} else if (row == 2 && col == 8) {
			if (shift) {
				keyPressed(Key.NONE);
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
		} else if (row ==2 && col == 5) {
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
		} else {
		}
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

	public static void keyPressed(Key k) {
		if (PIDisplay.INSTANCE != null) {
			Screen scr = PIDisplay.INSTANCE.getScreen();
			boolean refresh = false;
			if(scr != null && scr.initialized && scr.keyPressed(k)) {
				refresh = true;
			} else {
				switch (k) {
					case POWER:
						Display.destroy();
						break;
					case NONE:
						break;
					case debug_DEG:
						if (Calculator.angleMode.equals("deg") == false) {
							refresh = true;
						}
						Calculator.angleMode = "deg";
						break;
					case debug_RAD:
						if (Calculator.angleMode.equals("rad") == false) {
							refresh = true;
						}
						Calculator.angleMode = "rad";
						break;
					case debug_GRA:
						if (Calculator.angleMode.equals("gra") == false) {
							refresh = true;
						}
						Calculator.angleMode = "gra";
						break;
					case DRG_CYCLE:
						if (Calculator.angleMode.equals("deg") == true) {
							Calculator.angleMode = "rad";
						} else if (Calculator.angleMode.equals("rad") == true) {
							Calculator.angleMode = "gra";
						} else {
							Calculator.angleMode = "deg";
						}
						refresh = true;
						break;
					case LETTER_X:
						letterPressed('X');
						break;
					case BRIGHTNESS_CYCLE:
						PIDisplay.cycleBrightness(false);
						refresh = true;
						break;
					case BRIGHTNESS_CYCLE_REVERSE:
						PIDisplay.INSTANCE.setScreen(new MarioScreen()); //TODO: rimuovere: prova
						PIDisplay.cycleBrightness(true);
						refresh = true;
						break;
					case HISTORY_BACK:
						PIDisplay.INSTANCE.goBack();
						refresh = true;
						break;
					case HISTORY_FORWARD:
						PIDisplay.INSTANCE.goForward();
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
			if (k != Key.SHIFT && Keyboard.shift) {
				Keyboard.shift = false;
				refresh = true;
			} else if (k != Key.ALPHA && Keyboard.alpha) {
				Keyboard.alpha = false;
				refresh = true;
			}
			if (refresh) {
				Display.repaint();
			}
		}
	}

	private static void letterPressed(char L) {
		
	}

	public static void keyReleased(Key k) {
		boolean refresh = false;
		if (PIDisplay.INSTANCE != null) {
			Screen scr = PIDisplay.INSTANCE.getScreen();
			if(scr != null && scr.initialized && scr.keyReleased(k)) {
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
				Display.repaint();
			}
		}
	}

	public static enum Key {
		POWER, debug_DEG, debug_RAD, debug_GRA, SHIFT, ALPHA, NONE,
		HISTORY_BACK, HISTORY_FORWARD, SURD_MODE, DRG_CYCLE,
		LETTER_X, LETTER_Y, SIMPLIFY, SOLVE, BRIGHTNESS_CYCLE,
		BRIGHTNESS_CYCLE_REVERSE, DOT, NUM0, NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9,
		PARENTHESIS_OPEN, PARENTHESIS_CLOSE, PLUS, MINUS, PLUS_MINUS, MULTIPLY, DIVIDE, EQUAL,
		DELETE, RESET, LEFT, RIGHT, UP, DOWN, OK, debug1, debug2, debug3, debug4, debug5,
		SQRT, ROOT, POWER_OF_2, POWER_OF_x,
		SINE, COSINE, TANGENT, ARCSINE, ARCCOSINE, ARCTANGENT
	}
}



/*

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
| 0    | .    |      |      | SOLVE              |
|      |      |      |      | SIMPLIFY           |
| X    | Y    | Z    |DRGCYCL|                   |
|------|------|------|------|--------------------|


*/