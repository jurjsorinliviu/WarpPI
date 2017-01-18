package org.warp.picalculator.screens;

import static org.warp.picalculator.device.PIDisplay.colore;
import static org.warp.picalculator.device.PIDisplay.fonts;
import static org.warp.picalculator.device.graphicengine.Display.Render.*;

import org.warp.picalculator.Main;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.device.graphicengine.Screen;

public class KeyboardDebugScreen extends Screen {

	public String key;
	public String keyevent;
	public static int keyX;
	public static int keyY;
	public static String[] log = new String[]{"POWER ON","LOADING","LOADED","DONE","---"};
	public long beforetime;
	
	public KeyboardDebugScreen() {
		super();
		canBeInHistory = false;
	}
	
	@Override
	public void created() throws InterruptedException {
	}

	@Override
	public void init() throws InterruptedException {
	}

	@Override
	public void render() {
		glSetFont(fonts[2]);
		colore(0.75f, 0.0f, 0.0f, 1.0f);
		glDrawStringRight(Main.screenSize[0] - 10, 30, "-" + keyevent.toUpperCase() + "-");
		if (keyevent != "NONE") {
			glSetFont(fonts[2]);
			colore(0.0f, 0.0f, 0.0f, 1.0f);
			glDrawStringLeft(10, 30, "Key position");
			glDrawStringLeft(10, 45, "X: " + keyX + ", Y:" + keyY);
			glDrawStringLeft(10, 65, "Key value");
			glDrawStringLeft(10, 80, key);
		}
		glSetFont(fonts[3]);
		colore(0.0f, 0.0f, 0.0f, 1.0f);
		for (int i = 0; i < 5; i++) {
			if (log[i] != null) {
				glDrawStringLeft(10, 230 + 15*(i+1), log[i].toUpperCase());
			}
		}

		//FROM SERIAL
		colore(0.0f, 0.0f, 0.0f, 1.0f);
		glFillRect(-80+100+200, 90, 5, 5);
		glFillRect(-80+100, 100, 200, 70);
		glSetFont(fonts[2]);
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter(-80+100+200/2, 100+70/2-(currentFont.charH/2), "FROM SERIAL");
		glSetFont(fonts[3]);
		colore(0.0f, 0.0f, 1.0f, 1.0f);
		for (int i = 0; i < 8; i++) {
			if (pinsA[i] == 1) {
				colore(0.0f, 1.0f, 0.0f, 1.0f);
			} else if (pinsA[i] == 2) {
				colore(0.5f, 0.5f, 1.0f, 1.0f);
			} else if (pinsA[i] == -1) {
				colore(0.7f, 0.7f, 0.7f, 1.0f);
			} else if (pinsA[i] == 0) {
				colore(1.0f, 0.0f, 0.0f, 1.0f);
			}
			glFillRect(-80+103+25*(7-i), 80, 20, 20);
			colore(0.0f, 0.0f, 0.0f, 1.0f);
			glDrawStringCenter(-80+113+25*(7-i), 90-(currentFont.charH/2), ""+(i+1));
		}
		for (int i = 15; i >= 8; i--) {
			if (pinsA[i] == 1) {
				colore(0.0f, 1.0f, 0.0f, 1.0f);
			} else if (pinsA[i] == 2) {
				colore(0.5f, 0.5f, 1.0f, 1.0f);
			} else if (pinsA[i] == -1) {
				colore(0.7f, 0.7f, 0.7f, 1.0f);
			} else if (pinsA[i] == 0) {
				colore(1.0f, 0.0f, 0.0f, 1.0f);
			}
			glFillRect(-80+103+25*(i-8), 170, 20, 20);
			colore(0.0f, 0.0f, 0.0f, 1.0f);
			glDrawStringCenter(-80+113+25*(i-8), 180-(currentFont.charH/2), ""+(i+1));
		}
		for (int i = 0; i < 8; i++) {
			if (dataA[i]) {
				colore(0.0f, 1.0f, 0.0f, 1.0f);
			} else {
				colore(1.0f, 0.0f, 0.0f, 1.0f);
			}
			glFillRect(-80+160+10*(i), 150, 8, 8);
		}

		//TO SERIAL
		colore(0.0f, 0.0f, 0.0f, 1.0f);
		glFillRect(150+90, 200, 5, 5);
		glFillRect(150+100, 100, 200, 70);
		glSetFont(fonts[2]);
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter(150+100+200/2, 100+70/2-(currentFont.charH/2), "TO SERIAL");
		glSetFont(fonts[3]);
		colore(0.0f, 0.0f, 1.0f, 1.0f);
		for (int i = 15; i >= 8; i--) {
			if (pinsB[i] == 1) {
				colore(0.0f, 1.0f, 0.0f, 1.0f);
			} else if (pinsB[i] == 2) {
				colore(0.5f, 0.5f, 1.0f, 1.0f);
			} else if (pinsB[i] == -1) {
				colore(0.7f, 0.7f, 0.7f, 1.0f);
			} else if (pinsB[i] == 0) {
				colore(1.0f, 0.0f, 0.0f, 1.0f);
			}
			glFillRect(150+103+25*(15-i), 80, 20, 20);
			colore(0.0f, 0.0f, 0.0f, 1.0f);
			glDrawStringCenter(150+113+25*(15-i), 90-(currentFont.charH/2), ""+(i+1));
		}
		for (int i = 7; i >= 0; i--) {
			if (pinsB[i] == 1) {
				colore(0.0f, 1.0f, 0.0f, 1.0f);
			} else if (pinsB[i] == 2) {
				colore(0.5f, 0.5f, 1.0f, 1.0f);
			} else if (pinsB[i] == -1) {
				colore(0.7f, 0.7f, 0.7f, 1.0f);
			} else if (pinsB[i] == 0) {
				colore(1.0f, 0.0f, 0.0f, 1.0f);
			}
			glFillRect(150+103+25*(i), 170, 20, 20);
			colore(0.0f, 0.0f, 0.0f, 1.0f);
			glDrawStringCenter(150+113+25*(i), 180-(currentFont.charH/2), ""+(i+1));
		}
		for (int i = 0; i < 8; i++) {
			if (dataB[i]) {
				colore(0.0f, 1.0f, 0.0f, 1.0f);
			} else {
				colore(1.0f, 0.0f, 0.0f, 1.0f);
			}
			glFillRect(150+160+10*(i), 150, 8, 8);
		}
		
		//GPIO
		for (int i = 0; i < 40; i++) {
			if (gpio[i] == true) {
				colore(0.0f, 1.0f, 0.0f, 1.0f);
			} else {
				colore(1.0f, 0.0f, 0.0f, 1.0f);
			}
			if (i % 2 == 0) {
				glFillRect(53+15*((i)/2), 50, 5, 5);
				colore(0.0f, 0.0f, 0.0f, 1.0f);
				glDrawStringCenter(55+15*((i)/2), 60, ""+(i+1));
			} else {
				glFillRect(53+15*((i-1)/2), 40, 5, 5);
				colore(0.0f, 0.0f, 0.0f, 1.0f);
				glDrawStringCenter(55+15*((i-1)/2), 35-currentFont.charH, ""+(i+1));
			}
		}
		
		//KEYS
		for (int c = 0; c < 8; c++) {
			for (int r = 0; r < 8; r++) {
				if (ks[c][r]) {
					colore(0.0f, 1.0f, 0.0f, 1.0f);
				} else {
					colore(1.0f, 0.0f, 0.0f, 1.0f);
				}
				glFillRect(250+6*c, 250+6*r, 5, 5);
			}
		}
	}

	@Override
	public void beforeRender(float dt) {
		if (System.currentTimeMillis()-beforetime >= 1000) {
			keyevent = "NONE";
			keyX = 0;
			keyY = 0;
			key = "";
		}
	}

	@Override
	public boolean mustBeRefreshed() {
		return true;
	}

	@Override
	public boolean keyPressed(Key k) {
		beforetime = System.currentTimeMillis();
		keyevent = "PRESSED";
		keyX = 0;
		keyY = 0;
		key = k.toString();
		return false;
	}

	@Override
	public boolean keyReleased(Key k) {
		beforetime = System.currentTimeMillis();
		keyevent = "RELEASED";
		keyX = 0;
		keyY = 0;
		key = k.toString();
		return false;
	}
	

	public static int[] pinsA = new int[]{2, 2, 2, 2, 2, 2, 2, 1, -1, -1, 0, 0, 0, 0, 2, -1};
	public static int[] pinsB = new int[]{0, 0, 2, 2, 2, 2, -1, 1, 0, -1, 2, 2, 2, 2, 0, -1};
	public static boolean[] dataA = new boolean[8];
	public static boolean[] dataB = new boolean[8];
	public static boolean[][] ks = new boolean[8][8];
	public static boolean[] gpio = new boolean[40];
	
	public static void log(String str) {
		String[] newlog = log;
		for (int i = 1; i < 5; i++) {
			newlog[i-1] = newlog[i];
		}
		newlog[4] = "[" + System.currentTimeMillis() + "]" + str;
		log = newlog;
	}

}
