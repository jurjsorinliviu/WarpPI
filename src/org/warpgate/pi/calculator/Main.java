package org.warpgate.pi.calculator;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.warp.engine.lwjgl.Display;
import org.warpgate.pi.calculator.screens.EmptyScreen;
import org.warpgate.pi.calculator.screens.EquationScreen;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Main {
	public static final int[] screenSize = new int[]{480, 320};
	public static final int screenScale = 1;
	public static Display d;
	public static Main instance;
	
	public Main() throws InterruptedException {
		instance = this;
		d = new Display(new EmptyScreen(), screenSize[0], screenSize[1]);
		d.setBackground(0.796875f, 0.90234375f, 0.828125f);
		Thread t = new Thread("Graphic thread"){@Override public void run() {d.run("");}};
		t.start();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Main();
	}
}
