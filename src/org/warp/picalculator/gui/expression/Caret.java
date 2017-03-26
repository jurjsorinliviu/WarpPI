package org.warp.picalculator.gui.expression;

public class Caret {

	private int pos;
	private int remaining;
	private CaretState state;
	
	public Caret(CaretState state, int pos) {
		this.state = state;
		this.pos = pos;
		this.remaining = pos;
	}
	
	public void skip(int i) {
		remaining-=i;
	}
	
	public int getPosition() {
		return pos;
	}
	
	public int getRemaining() {
		return remaining;
	}
	
	public CaretState getState() {
		return state;
	}
	
	public void flipState() {
		if (this.state == CaretState.VISIBLE_ON) {
			this.state = CaretState.VISIBLE_OFF;
		} else if (this.state == CaretState.VISIBLE_OFF) {
			this.state = CaretState.VISIBLE_ON;
		}
	}

	public void turnOn() {
		if (this.state == CaretState.VISIBLE_OFF) {
			this.state = CaretState.VISIBLE_ON;
		}
	}

	public void setPosition(int i) {
		this.pos = i;
	}

	public void resetRemaining() {
		remaining = pos;
	}
}
