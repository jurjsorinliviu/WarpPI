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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;

import com.jogamp.opengl.util.*;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.Display;

/**
 * <pre>
 *   __ __|_  ___________________________________________________________________________  ___|__ __
 *  //    /\                                           _                                  /\    \\
 * //____/  \__     __ _____ _____ _____ _____ _____  | |     __ _____ _____ __        __/  \____\\
 *  \    \  / /  __|  |     |   __|  _  |     |  _  | | |  __|  |     |   __|  |      /\ \  /    /
 *   \____\/_/  |  |  |  |  |  |  |     | | | |   __| | | |  |  |  |  |  |  |  |__   "  \_\/____/
 *  /\    \     |_____|_____|_____|__|__|_|_|_|__|    | | |_____|_____|_____|_____|  _  /    /\
 * /  \____\                       http://jogamp.org  |_|                              /____/  \
 * \  /   "' _________________________________________________________________________ `"   \  /
 *  \/____.                                                                             .____\/
 * </pre>
 *
 * <p>
 * JOGL2 OpenGL ES 2 demo to expose and learn what the RAW OpenGL ES 2 API looks
 * like.
 *
 * Compile, run and enjoy:
 * wget
 * http://jogamp.org/deployment/jogamp-current/archive/jogamp-all-platforms.7z
 * 7z x jogamp-all-platforms.7z
 * cd jogamp-all-platforms
 * mkdir -p demos/es2
 * cd demos/es2
 * wget
 * https://raw.github.com/xranby/jogl-demos/master/src/demos/es2/RawGL2ES1demo.java
 * cd ../..
 * javac -cp jar/jogl-all.jar:jar/gluegen-rt.jar demos/es2/RawGL2ES1demo.java
 * java -cp jar/jogl-all.jar:jar/gluegen-rt.jar:. demos.es2.RawGL2ES1demo
 * </p>
 *
 *
 * @author Xerxes Rånby (xranby)
 */

public class NEWTWindow implements GLEventListener {

	private final GPUDisplay disp;
	private final GPURenderer renderer;

	public NEWTWindow(GPUDisplay disp) {
		this.disp = disp;
		renderer = disp.getRenderer();
	}

	public GLWindow window;

	public void create() {
		/* This demo are based on the GL2ES1 GLProfile that uses common hardware acceleration
		 * functionality of desktop OpenGL 3, 2 and mobile OpenGL ES 2 devices.
		 * JogAmp JOGL will probe all the installed libGL.so, libEGL.so and libGLESv2.so librarys on
		 * the system to find which one provide hardware acceleration for your GPU device.
		 * Its common to find more than one version of these librarys installed on a system.
		 * For example on a ARM Linux system JOGL may find
		 * Hardware accelerated Nvidia tegra GPU drivers in: /usr/lib/nvidia-tegra/libEGL.so
		 * Software rendered Mesa Gallium driver in: /usr/lib/arm-linux-gnueabi/mesa-egl/libEGL.so.1
		 * Software rendered Mesa X11 in: /usr/lib/arm-linux-gnueabi/mesa/libGL.so
		 * Good news!: JOGL does all this probing for you all you have to do are to ask for
		 * the GLProfile you want to use.
		 */

		System.out.println("Loading OpenGL...");
		System.out.println(GLProfile.glAvailabilityToString());
		if (GLProfile.isAnyAvailable()) {
			System.err.println("Le OpenGL non sono presenti su questo computer!");
		}
		final GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2ES1));
		System.out.println("Loaded OpenGL");
		// We may at this point tweak the caps and request a translucent drawable
		caps.setBackgroundOpaque(true); //transparency window
		final GLWindow glWindow = GLWindow.create(caps);
		window = glWindow;

		glWindow.setTitle("Algebraic Calculator for Raspberry PI by Andrea Cavalli (XDrake99)");
		
		glWindow.addWindowListener(new WindowListener() {

			@Override
			public void windowDestroyNotify(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDestroyed(WindowEvent e) {
				DisplayManager.display.destroy();
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
						Keyboard.keyReleased(Key.POWER);
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
							Keyboard.keyReleased(Key.NONE);
						}
						break;
					case KeyEvent.VK_ENTER:
						if (!Keyboard.shift && !Keyboard.alpha) {
							Keyboard.keyReleased(Key.SOLVE);
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
						break;
					case KeyEvent.VK_A:
						Keyboard.keyReleased(Key.ALPHA);
						break;
					case KeyEvent.VK_M:
						Keyboard.keyPressed(Key.SURD_MODE);
						break;
					case KeyEvent.VK_LEFT:
						//LEFT
						row = 2;
						col = 3;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
					case KeyEvent.VK_RIGHT:
						//RIGHT
						row = 2;
						col = 5;
						Keyboard.debugKeysDown[row - 1][col - 1] = false;
				}
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

		//Vsync
		gl.setSwapInterval(2);
		
		//Textures
		gl.glEnable(GL.GL_TEXTURE_2D);

		//Transparency
		gl.glEnable(GL2ES1.GL_BLEND);
		gl.glBlendFunc(GL2ES1.GL_SRC_ALPHA, GL2ES1.GL_ONE_MINUS_SRC_ALPHA);
		
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
		disp.size[0] = width;
		disp.size[1] = height;
		final GL2ES1 gl = glad.getGL().getGL2ES1();
		float max_wh, min_wh;
		if (width == 0) {
			width = 1;
		}
		if (height == 0) {
			height = 1;
		}
		if (width > height) {
			max_wh = width;
			min_wh = height;
		} else {
			max_wh = height;
			min_wh = width;
		}

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrtho(0.0, width, height, 0.0, -1, 1);

		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void display(GLAutoDrawable glad) {
		final GL2ES1 gl = glad.getGL().getGL2ES1();

		renderer.gl = gl;

		gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);

		renderer.startDrawCycle();
		
		disp.repaint();

		renderer.endDrawCycle();
		
		renderer.gl = null;

		gl.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		gl.glDisableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("cleanup");
		final GL2ES1 gl = drawable.getGL().getGL2ES1();
		System.exit(0);
	}

}