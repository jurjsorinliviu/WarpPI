package org.warp.picalculator.extra.mario;

public class MarioBlock {
	private int x, y;
	private byte id;
	
	public MarioBlock(int x, int y, byte b) {
		this.x = x;
		this.y = y;
		id = b;
	}
	
	public boolean isSolid() {
		return id != 0b0;
	}

	public byte getID() {
		return id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
