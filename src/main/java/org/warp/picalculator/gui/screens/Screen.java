package org.warp.picalculator.gui.screens;

import org.warp.picalculator.device.KeyboardEventListener;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;

public abstract class Screen implements KeyboardEventListener {
	public DisplayManager d;
	public boolean created = false;
	public boolean initialized = false;
	public boolean canBeInHistory = false;

	public Screen() {}

	public void initialize() throws InterruptedException {
		if (!initialized) {
			initialized = true;
			init();
		}
	}

	public void create() throws InterruptedException {
		if (!created) {
			created = true;
			created();
		}
	}

	public abstract void created() throws InterruptedException;

	public abstract void init() throws InterruptedException;

	public abstract void render();

	public void renderStatusbar() {

	}

	public abstract void beforeRender(float dt);

	public abstract boolean mustBeRefreshed();
}
