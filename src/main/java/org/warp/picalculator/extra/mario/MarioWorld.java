package org.warp.picalculator.extra.mario;

public class MarioWorld {

	private int[] spawnPoint;
	private int width;
	private int height;
	private byte[] data;
	private MarioEvent[] events;
	private MarioEntity[] entities;
	
	/**
	 * @param width
	 * @param height
	 * @param data
	 * @param events
	 * @param marioEnemies
	 */
	public MarioWorld(int[] spawnPoint, int width, int height, byte[] data, MarioEvent[] events, MarioEntity[] entities) {
		this.spawnPoint = spawnPoint;
		this.width = width;
		this.height = height;
		this.data = data;
		this.events = events;
		this.entities = entities;
	}

	public byte getBlockIdAt(int x, int y) {
		int idx = (height - 1 - y) * width + x;
		if (idx < 0 || idx >= data.length) return 0b0;
		return data[idx];
	}

	public MarioBlock getBlockAt(int x, int y) {
		return new MarioBlock(x, y, getBlockIdAt(x, y));
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void reset() {
		
	}

	public double getSpawnPointX() {
		return spawnPoint[0];
	}

	public double getSpawnPointY() {
		return spawnPoint[1];
	}

	public MarioEntity[] getEntities() {
		return entities;
	}

}
