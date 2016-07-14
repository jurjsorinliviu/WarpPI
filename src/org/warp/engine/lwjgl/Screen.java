package org.warp.engine.lwjgl;

public abstract class Screen {
	public Display d;
	public boolean initialized = false;
	public boolean canBeInHistory = false;
	
	public Screen() {
	}
	
	public void initialize() throws InterruptedException {
		if (!initialized) {
			initialized = true;
			init();
		}
	}
	
	public abstract void init() throws InterruptedException;
	
	public abstract void render();

	public abstract void beforeRender(float dt);
}
