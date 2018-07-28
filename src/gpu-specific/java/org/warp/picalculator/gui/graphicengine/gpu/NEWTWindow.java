/**
 * Copyright 2012-2013 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

package org.warp.picalculator.gui.graphicengine.gpu;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.device.HardwareDevice;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.event.Key;
import org.warp.picalculator.event.TouchEndEvent;
import org.warp.picalculator.event.TouchMoveEvent;
import org.warp.picalculator.event.TouchPoint;
import org.warp.picalculator.event.TouchStartEvent;
import org.warp.picalculator.gui.DisplayManager;

import com.jogamp.newt.event.GestureHandler.GestureEvent;
import com.jogamp.newt.event.GestureHandler.GestureListener;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 *
 * @author Xerxes Rånby (xranby)
 * @author Andrea Cavalli (@Cavallium)
 */

class NEWTWindow implements GLEventListener {

	private final GPUEngine disp;
	private final GPURenderer renderer;
	public float windowZoom;
	public int[] realWindowSize;
	public Runnable onInitialized;
	public List<TouchPoint> touches = new ObjectArrayList<>();

	public NEWTWindow(GPUEngine disp) {
		this.disp = disp;
		renderer = disp.getRenderer();
		realWindowSize = new int[] { 1, 1 };
	}

	public GLWindow window;

	public void create() {
		System.out.println("Loading OpenGL...");
		System.out.println(GLProfile.glAvailabilityToString());
		if (!GLProfile.isAvailable(GLProfile.GL2ES1)) {
			System.err.println("Le OpenGL non sono presenti su questo computer!");
			return;
		}
		if (StaticVars.debugOn) {
			System.setProperty("jnlp.newt.window.icons", "res/icons/calculator-016.png res/icons/calculator-018.png res/icons/calculator-256.png");
		}
		final GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2ES1));
		System.out.println("Loaded OpenGL");
		// We may at this point tweak the caps and request a translucent drawable
		caps.setHardwareAccelerated(true);
		caps.setBackgroundOpaque(true); //transparency window
//		caps.setSampleBuffers(true);
//		caps.setNumSamples(4);
		final GLWindow glWindow = GLWindow.create(caps);
		window = glWindow;

		glWindow.setTitle("WarpPI Calculator by Andrea Cavalli (@Cavallium)");

		glWindow.addWindowListener(new WindowListener() {

			@Override
			public void windowDestroyNotify(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDestroyed(WindowEvent e) {
				HardwareDevice.INSTANCE.getDisplayManager().engine.destroy();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowMoved(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowRepaint(WindowUpdateEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowResized(WindowEvent e) {

			}

		});
		glWindow.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				Keyboard.debugKeyCode = arg0.getKeyCode();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case KeyEvent.VK_ESCAPE:
						Keyboard.keyReleased(Key.POWEROFF);
						break;
					case KeyEvent.VK_D:
						Keyboard.keyReleased(Key.debug_DEG);
						break;
					case KeyEvent.VK_R:
						Keyboard.keyReleased(Key.debug_RAD);
						break;
					case KeyEvent.VK_G:
						Keyboard.keyReleased(Key.debug_GRA);
						break;
					case KeyEvent.VK_X:
						if (Keyboard.alpha) {
							Keyboard.keyReleased(Key.LETTER_X);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_P:
						if (Keyboard.alpha) {
							Keyboard.keyReleased(Key.PI);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_B:
						if (Keyboard.shift) {
							Keyboard.keyReleased(Key.BRIGHTNESS_CYCLE_REVERSE);
						} else if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.BRIGHTNESS_CYCLE);
						} else {
							Keyboard.keyReleased(Key.ZOOM_MODE);
						}
						break;
					case KeyEvent.VK_ENTER:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.SIMPLIFY);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						int row = 2;
						int col = 1;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
						break;
					case KeyEvent.VK_1:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug1);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_2:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug2);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_3:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug3);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_4:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug4);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_5:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.debug5);
						} else {
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case 0x15:
					case KeyEvent.VK_SHIFT:
						Keyboard.keyReleased(Key.SHIFT);
						if (Keyboard.shift) {
							Keyboard.keyPressed(Key.SHIFT);
							Keyboard.keyReleased(Key.SHIFT);
						}
						break;
					case KeyEvent.VK_A:
						Keyboard.keyReleased(Key.ALPHA);
						if (Keyboard.alpha) {
							Keyboard.keyPressed(Key.ALPHA);
							Keyboard.keyReleased(Key.ALPHA);
						}
						break;
					case KeyEvent.VK_M:
						Keyboard.keyPressed(Key.SURD_MODE);
						break;
					case KeyEvent.VK_LEFT:
						//LEFT
						row = 2;
						col = 3;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
						break;
					case KeyEvent.VK_RIGHT:
						//RIGHT
						row = 2;
						col = 5;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
						break;
					case KeyEvent.VK_UP:
						//UP
						row = 1;
						col = 4;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
						break;
					case KeyEvent.VK_DOWN:
						//Down
						row = 3;
						col = 4;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
						break;
					case (short) 12:
						//Down
						row = 2;
						col = 4;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
						break;
				}
			}
		});
		glWindow.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				List<TouchPoint> newPoints = new ObjectArrayList<>();
				List<TouchPoint> changedPoints = new ObjectArrayList<>();
				List<TouchPoint> oldPoints = touches;
				int[] xs = e.getAllX();
				int[] ys = e.getAllY();
				float[] ps = e.getAllPressures();
				short[] is = e.getAllPointerIDs();
				for (int i = 0; i < e.getPointerCount(); i++) {
					newPoints.add(new TouchPoint(is[i], xs[i], ys[i], 5, 5, ps[i], 0));
				}
				
				changedPoints.add(newPoints.get(0));
				newPoints.remove(0);
				touches = newPoints;
				HardwareDevice.INSTANCE.getDisplayManager().onTouchStart(new TouchStartEvent(changedPoints, touches));
				HardwareDevice.INSTANCE.getDisplayManager().onTouchEnd(new TouchEndEvent(changedPoints, touches));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				List<TouchPoint> newPoints = new ObjectArrayList<>();
				List<TouchPoint> changedPoints = new ObjectArrayList<>();
				List<TouchPoint> oldPoints = touches;
				int[] xs = e.getAllX();
				int[] ys = e.getAllY();
				float[] ps = e.getAllPressures();
				short[] is = e.getAllPointerIDs();
				for (int i = 0; i < e.getPointerCount(); i++) {
					newPoints.add(new TouchPoint(is[i], xs[i], ys[i], 5, 5, ps[i], 0));
				}
				changedPoints.add(newPoints.get(0));
				touches = newPoints;
				HardwareDevice.INSTANCE.getDisplayManager().onTouchStart(new TouchStartEvent(changedPoints, touches));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				List<TouchPoint> newPoints = new ObjectArrayList<>();
				List<TouchPoint> changedPoints = new ObjectArrayList<>();
				List<TouchPoint> oldPoints = touches;
				int[] xs = e.getAllX();
				int[] ys = e.getAllY();
				float[] ps = e.getAllPressures();
				short[] is = e.getAllPointerIDs();
				for (int i = 0; i < e.getPointerCount(); i++) {
					newPoints.add(new TouchPoint(is[i], xs[i], ys[i], 5, 5, ps[i], 0));
				}
				changedPoints.add(newPoints.get(0));
				newPoints.remove(0);
				touches = newPoints;
				HardwareDevice.INSTANCE.getDisplayManager().onTouchEnd(new TouchEndEvent(changedPoints, touches));
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				List<TouchPoint> newPoints = new ObjectArrayList<>();
				List<TouchPoint> changedPoints = new ObjectArrayList<>();
				List<TouchPoint> oldPoints = touches;
				int[] xs = e.getAllX();
				int[] ys = e.getAllY();
				float[] ps = e.getAllPressures();
				short[] is = e.getAllPointerIDs();
				for (int i = 0; i < e.getPointerCount(); i++) {
					newPoints.add(new TouchPoint(is[i], xs[i], ys[i], 5, 5, ps[i], 0));
				}
				newPoints.forEach((newp) -> {
					oldPoints.forEach((oldp) -> {
						if (newp.getID() == oldp.getID()) {
							if (newp.equals(oldp) == false) {
								changedPoints.add(newp);
							}
						}
					});
				});
				touches = newPoints;
				HardwareDevice.INSTANCE.getDisplayManager().onTouchMove(new TouchMoveEvent(changedPoints, touches));
			}

			@Override
			public void mouseWheelMoved(MouseEvent e) {
				
			}
			
		});
		
		glWindow.addGLEventListener(this /* GLEventListener */);
		final Animator animator = new Animator();
		animator.add(glWindow);
		animator.start();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2ES1 gl = drawable.getGL().getGL2ES1();

		if (StaticVars.debugOn) {
			//Vsync
			gl.setSwapInterval(1);
		} else {
			//Vsync
			gl.setSwapInterval(2);
		}

		//Textures
		gl.glEnable(GL.GL_TEXTURE_2D);

		//Transparency
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glShadeModel(GLLightingFunc.GL_FLAT);

		//Multisampling
		//gl.glEnable(GL.GL_MULTISAMPLE);

		if (onInitialized != null) {
			onInitialized.run();
			onInitialized = null;
		}
		try {
			renderer.currentTex = ((GPUSkin) disp.loadSkin("test.png")).t;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
		System.err.println("INIT GL IS: " + gl.getClass().getName());
		System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
	}

	@Override
	public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
		realWindowSize[0] = width;
		realWindowSize[1] = height;
		disp.size[0] = width;
		disp.size[1] = height;
		final GL2ES1 gl = glad.getGL().getGL2ES1();

		onZoomChanged(gl, true);
	}

	private void onZoomChanged(GL2ES1 gl, boolean sizeChanged) {
		final float precWindowZoom = windowZoom;
		windowZoom = StaticVars.getCurrentZoomValue();

		if (((precWindowZoom % ((int) precWindowZoom)) != 0f) != ((windowZoom % ((int) windowZoom)) != 0f)) {
			final boolean linear = (windowZoom % ((int) windowZoom)) != 0f;

			for (final Texture t : disp.registeredTextures) {
				t.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, linear ? GL.GL_LINEAR : GL.GL_NEAREST);
				t.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			}
		}

		final int width = realWindowSize[0];
		final int height = realWindowSize[1];

		disp.size[0] = (int) (realWindowSize[0] / windowZoom);
		disp.size[1] = (int) (realWindowSize[1] / windowZoom);

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrtho(0.0, disp.size[0], disp.size[1], 0.0, -1, 1);

		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void display(GLAutoDrawable glad) {
		final GL2ES1 gl = glad.getGL().getGL2ES1();

		GPURenderer.gl = gl;

		if (windowZoom != StaticVars.getCurrentZoomValue()) {
			onZoomChanged(gl, false);
		}

		Boolean linear = null;
		while (!disp.unregisteredTextures.isEmpty()) {
			if (linear == null) {
				linear = (windowZoom % ((int) windowZoom)) != 0f;
			}
			final Texture t = disp.unregisteredTextures.pop();
			t.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			t.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			disp.registeredTextures.addLast(t);
		}

		gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);

		renderer.initDrawCycle();

		disp.repaint();

		renderer.endDrawCycle();

		GPURenderer.gl = null;

		gl.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		gl.glDisableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("cleanup");
//		final GL2ES1 gl = drawable.getGL().getGL2ES1();
		System.exit(0);
	}

}