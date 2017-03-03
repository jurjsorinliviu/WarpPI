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
}
