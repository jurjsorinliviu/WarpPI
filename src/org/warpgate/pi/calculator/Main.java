package org.warpgate.pi.calculator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.gatecraft.game2d.utils.BitmapImage;
import org.nevec.rjm.NumeroAvanzato;
import org.nevec.rjm.NumeroAvanzatoVec;
import org.nevec.rjm.Rational;
import org.warp.engine.ActionInterface;
import org.warp.engine.Display;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Main implements ActionInterface {
	public static final int[] screenSize = new int[]{300, 150};
	public static final int screenScale = 1;
	public volatile static Display d;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Main() {
		Display.start(this, screenSize[0], screenSize[1]);
		d = Display.INSTANCE;
		d.g().setFill(new javafx.scene.paint.Color(0.796875, 0.90234375, 0.828125, 1));
		d.g().fillRect(0, 0, screenSize[0], screenSize[0]);
		Utils.writeLetter(d, "Loading...".toUpperCase(), (screenSize[0]-Utils.getPlainTextWidth("Loading...".toUpperCase()))/2, (screenSize[1]-Utils.getFontHeight(false))/2, false);
		
		try {
			//Parentesi f = new Parentesi("(Ⓐ(2X)*3Y)/(5Z^2)+(Ⓐ(11A)*13B)/(7CZ)=19XZ");
			//PARENTESI CON CALCOLI
			//Funzione f = new Sottrazione(new Somma(new Parentesi("Ⓐ9/2+Ⓐ7/2", "").calcola(), new Termine("3.5")), new Parentesi("3*2√14","").calcola());
			//PARENTESI CON DUE NUMERI FRAZIONALI COMPLETI CON INCOGNITE:
			//Funzione f = new Parentesi("(Ⓐ(2X)*3Y)/(5Z^2)+(Ⓐ(11A)*13B)/(7CZ)", "");
			//PARENTESI CON DUE NUMERI FRAZIONALI DISALLINEATI GRAFICAMENTE:
			//Funzione f = new Parentesi("((5^2-1)/2)/5-5/(5/2)=2", "");
			//TERMINE DI PROVA COMPLETO:
			//Funzione f = new Termine(new NumeroAvanzato(new Rational(3, 2), new Rational(7, 1), new Incognite(new Incognita('X', Rational.ONE)), new Incognite(new Incognita('Y', Rational.ONE)), new Incognite(new Incognita('z', Rational.ONE))));
			//PARENTESI REALISTICA CON INCOGNITE:
			//Funzione f = new Equazione(new Parentesi("X^2+(MX-M+4)^2-4X-4(MX-M+4)^2+7", ""), new Termine("0"));
			//POTENZA:
			//Funzione f = new Parentesi("(MX-M+4)^2", "");
			//NUMERO SEMPLICE LUNGO:
			//Funzione f = new Parentesi("-1219999799999996934.42229", "");
			//:
			//Funzione f = new Parentesi("5Y+XY=2", "")
			equazione = "";
			c = new Calculator(true);
			f = new Parentesi(equazione);
			f2 = f.calcolaSistema();
			
			
			ew1 = f.getWidth();
			ew2 = f2.getWidth();
			eh2 = f2.getHeight(false);
			x1 = 2;
			x2 = 2;
			requiresleep1 = false;
			requiresleep2 = false;
			aftersleep = false;
			autoscroll = false;
			
			final Timeline timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.setAutoReverse(true);
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000/25), new EventHandler() {
				@Override
				public void handle(Event arg0) {
					refresh();
				}
			}));
			timeline.play();
			/*
			long start = System.nanoTime();
			Termine result = c.calcolarisultato("((5^2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21");
			long end = System.nanoTime();
			long timeElapsed = end-start;
			System.out.println("RESULT: " + result);
			System.out.println("DECIMAl RESULT: " + result.getTerm().toBigDecimal());
			System.out.println("Time elapsed: " +  (double) timeElapsed / 1000000000 + "\n");
			
			
			start = System.nanoTime();
			RisultatoEquazione eresult = c.calcolaequazione("((5^2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21=(175*(2√7)+3*(2√13))/105");
			end = System.nanoTime();
			timeElapsed = end-start;
			System.out.println("Is an equation: " + eresult.isAnEquation);
			System.out.println("L-R: " + eresult.LR);
			System.out.println("Time elapsed: " +  (((double) timeElapsed / 1000000000)) + "\n");
			*/
		} catch (Errore e) {
			d.setColor(new Color(204, 231, 212));
			d.fillRect(0, 0, screenSize[0], screenSize[1]);
			
			Utils.writeLetter(d, e.id.toString(), 2, 2, false);
			
			System.err.println(e.id);
		}
	}

	public static volatile String equazione = "";
	public static Calculator c;
	public static Parentesi f;
	public static Funzione f2;
	public static int ew1;
	public static int ew2;
	public static int eh2;
	public static int x1;
	public static int x2;
	public static boolean requiresleep1;
	public static boolean requiresleep2;
	public static boolean aftersleep;
	public static boolean autoscroll;

	protected static void refresh() {
		long delta = System.currentTimeMillis();
		d.setColor(new Color(204, 231, 212));
		d.fillRect(0, 0, screenSize[0], screenSize[1]);

		if (equazione == "") {
			d.setColor(Color.BLACK);
			d.drawString("Scrivi qua sopra un'espressione,".toUpperCase(), 10, 20);
			d.drawString("dopodiche premi il tasto execute".toUpperCase(), 10, 35);
			d.setColor(Color.RED);
			d.drawBoldString( "Leggi ATTENZIONE.txt!".toUpperCase(), 10, 60);
		} else {
			if (ew1 + x1 + 5 > screenSize[0] && !requiresleep1 && autoscroll) {
				x1-=1;
			} else if(autoscroll) {
				requiresleep1 = true;
			}
			
			if (ew2 + x2 + 5 > screenSize[0] && !requiresleep2 && autoscroll) {
				x2-=1;
			} else if(autoscroll) {
				requiresleep2 = true;
			}

			if (requiresleep1 && requiresleep2 && autoscroll) {
				requiresleep1 = false;
				requiresleep2 = false;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				x1 = 2;
				x2 = 2;
				aftersleep = true;
			}
			
			f.draw(x1,2,d, false);
			f2.draw(x2,screenSize[1]-2-eh2,d, false);
		}
		
		if (aftersleep && autoscroll) {
			aftersleep = false;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			delta = System.currentTimeMillis() - delta;
			if (50-delta > 0 && autoscroll) {
				Thread.sleep(50-delta);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Main();
	}
	
	@Override
	public synchronized void refresh(String args) {
		equazione  = args.replace("sqrt", "Ⓐ").replace("^", "Ⓑ");
		c = new Calculator(true);
		try {
			f = new Parentesi(equazione);
		} catch (Errore e) {
			e.printStackTrace();
		}
		try {
			f2 = f.calcolaSistema();
		} catch (Errore e) {
			e.printStackTrace();
		}
		ew1 = f.getWidth();
		ew2 = f2.getWidth();
		eh2 = f2.getHeight(false);
		x1 = 2;
		x2 = 2;
		requiresleep1 = false;
		requiresleep2 = false;
		aftersleep = false;
		autoscroll = false;
	}
}
