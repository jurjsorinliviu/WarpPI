package org.warp.engine.lwjgl;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.warpgate.pi.calculator.Calculator;
import org.warpgate.pi.calculator.Keyboard;
import org.warpgate.pi.calculator.Main;
import org.warpgate.pi.calculator.Utils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.warp.engine.lwjgl.GLFWUtil.*;
import static org.warp.engine.lwjgl.IOUtil.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * STB Truetype oversampling demo.
 *
 * <p>This is a Java port of <a href="https://github.com/nothings/stb/blob/master/tests/oversample/main.c">https://github
 * .com/nothings/stb/blob/master/tests/oversample/main.c</a>.</p>
 */
public final class Display {

	private static final int BITMAP_W = 5*128;
	private static final int BITMAP_H = 9*256;

	private static final float[] scale = {
		30.0f,
		15.0f
	};

	// ----

	private final STBTTAlignedQuad q  = STBTTAlignedQuad.malloc();
	private final FloatBuffer      xb = memAllocFloat(1);
	private final FloatBuffer      yb = memAllocFloat(1);

	private long window;

	// ----

	private int ww = 480;
	private int wh = 320;

	private int fbw = ww;
	private int fbh = wh;

	private int font_tex;
	private int skin_tex;
	private int skin_w;
	private int skin_h;
	private int skin_comp;
	private ByteBuffer skin;

	private STBTTPackedchar.Buffer chardata;

	private boolean black_on_white;
	private boolean integer_align;
	private boolean translating;
	private boolean rotating;

	private boolean supportsSRGB;
	private boolean srgb;

	private float rotate_t, translate_t;

	private boolean show_tex;

	private int font = 0;

	private final int maxCharIndex = 9500;
	private float[] background = new float[]{0f,0f,0f};
	public boolean loading = true;
	public String error = null;
	public String[] errorStackTrace = null;
	public final int[] glyphsHeight = new int[]{9, 6};
	public float translation = 0.0f;
	public boolean translation_top_to_bottom = true;
	public static float brightness = 1.0f;
	
	private Screen screen;
	
	public Display(Screen screen, int ww, int wh) {
		this.ww = ww;
		this.wh = wh;
		setScreen(screen);
	}
/*
	private void load_skin() {
		try {
			skin_tex = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, skin_tex);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

			InputStream in = new FileInputStream("res/skin.png");
			PNGDecoder decoder = new PNGDecoder(in);

			System.out.println("width="+decoder.getWidth());
			System.out.println("height="+decoder.getHeight());

			ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
			buf.flip();
			
			skin = buf;
			skin_w = decoder.getWidth();
			skin_h = decoder.getHeight();
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, skin_w,
				    skin_h, 0, GL_RGBA, GL_UNSIGNED_BYTE, skin);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}*/

	
	private void setScreen(Screen screen) {
		if (screen.initialized == false) {
			if (screen.canBeInHistory) {
				Calculator.currentSession = 0;
				for (int i = 1; i < Calculator.sessions.length; i++) {
					Calculator.sessions[i] = Calculator.sessions[i-1];
				}
				Calculator.sessions[0] = this.screen;
			}
		}
		screen.d = this;
		try {
			screen.initialize();
			this.screen = screen;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean canGoBack() {
		if (this.screen != Calculator.sessions[Calculator.currentSession]) {
			
		} else if (Calculator.currentSession+1 < Calculator.sessions.length) {
			if (Calculator.sessions[Calculator.currentSession + 1] != null) {
				
			} else {
				return false;
			}
		} else {
			return false;
		}
		if (Calculator.sessions[Calculator.currentSession] != null) {
			return true;
		}
		return false;
	}
	
	private void goBack() {
		if (canGoBack()) {
			if (this.screen != Calculator.sessions[Calculator.currentSession]) {
				
			} else {
				Calculator.currentSession += 1;
			}
			this.screen = Calculator.sessions[Calculator.currentSession];
		}
	}

	
	private boolean canGoForward() {
		if (this.screen != Calculator.sessions[Calculator.currentSession]) {
			
		} else if (Calculator.currentSession > 0) {
			if (Calculator.sessions[Calculator.currentSession - 1] != null) {
				
			} else {
				return false;
			}
		} else {
			return false;
		}
		if (Calculator.sessions[Calculator.currentSession] != null) {
			return true;
		}
		return false;
	}
	
	private void goForward() {
		if (canGoForward()) {
			if (this.screen != Calculator.sessions[Calculator.currentSession]) {
				
			} else {
				Calculator.currentSession -= 1;
			}
			this.screen = Calculator.sessions[Calculator.currentSession];
		}
	}
	
	private Screen getScreen() {
		return this.screen;
	}


	private void load_skin() {
		ByteBuffer imageBuffer;
		try {
			imageBuffer = ioResourceToByteBuffer("skin.png", 8 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		skin_tex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, skin_tex);
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		
		skin = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
		
		if ( skin == null )
			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
		skin_w = w.get(0);
		skin_h = h.get(0);
		skin_comp = comp.get(0);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		if ( skin_comp == 3 ) {
			if ( (skin_w & 3) != 0 )
				glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (skin_w & 1));
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, skin_w, skin_h, 0, GL_RGB, GL_UNSIGNED_BYTE, skin);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, skin_w, skin_h, 0, GL_RGBA, GL_UNSIGNED_BYTE, skin);
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}

		glBindTexture(GL_TEXTURE_2D, 0);

		stbi_image_free(skin);
	}

	private void load_fonts() {
		font_tex = glGenTextures();
		chardata = STBTTPackedchar.mallocBuffer(6*maxCharIndex);
		glBindTexture(GL_TEXTURE_2D, font_tex);

		
		try {
			STBTTPackContext pc = STBTTPackContext.malloc();
			ByteBuffer ttfBig = ioResourceToByteBuffer("font_big.ttf", 18000);
			ByteBuffer ttfSmall = ioResourceToByteBuffer("font_small.ttf", 18000);

			ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);

			stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, null);
			chardata.limit(maxCharIndex);
			chardata.position(0);
			stbtt_PackSetOversampling(pc, 1, 1);
			stbtt_PackFontRange(pc, ttfBig, 0, 15 /* Font size */, 0, chardata);
			chardata.clear();
			chardata.limit(maxCharIndex*2);
			chardata.position(maxCharIndex);
			stbtt_PackSetOversampling(pc, 1, 1);
			stbtt_PackFontRange(pc, ttfSmall, 0, 15 /* Font size */, 0, chardata);
			chardata.clear();
			stbtt_PackEnd(pc);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void draw_init() {
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		
		glViewport(0, 0, fbw, fbh);
		clearColor(background[0], background[1], background[2], 0.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0, ww, wh, 0.0, -1.0, 1.0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	private static void drawBoxTC(float x0, float y0, float x1, float y1, float s0, float t0, float s1, float t1) {
		glTexCoord2f(s0, t0);
		glVertex2f(x0, y0);
		glTexCoord2f(s1, t0);
		glVertex2f(x1, y0);
		glTexCoord2f(s1, t1);
		glVertex2f(x1, y1);
		glTexCoord2f(s0, t1);
		glVertex2f(x0, y1);
	}

	private void print(float x, float y, int font, String text) {
		xb.put(0, x);
		yb.put(0, y+glyphsHeight[font]);

		chardata.position(font * maxCharIndex);

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, font_tex);

		glBegin(GL_QUADS);
		for ( int i = 0; i < text.length(); i++ ) {
			stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H, text.charAt(i), xb, yb, q, (font == 0 && integer_align)?1:0);
			drawBoxTC(
				q.x0(), q.y0(), q.x1(), q.y1(),
				q.s0(), q.t0(), q.s1(), q.t1()
			);
		}
		glEnd();
		glDisable( GL_TEXTURE_2D );
	}

	private int[] textSize(int font, String text) {
		float[] size = new float[]{0,0};
		xb.put(0, 0);
		yb.put(0, 0);

		chardata.position(font * maxCharIndex);
		
		for ( int i = 0; i < text.length(); i++ ) {
			stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H, text.charAt(i), xb, yb, q, (font == 0 && integer_align)?1:0);
			size[0]=q.x1();
			size[1]=q.y1();
		}
		return new int[]{(int) size[0], (int) size[1]};
	}
	
	private void drawSkinPart(int[] posOnScreen, float[] posOnSkin) {
		glEnable(GL_TEXTURE_2D);
		colore(1, 1, 1, 1);
		glBindTexture(GL_TEXTURE_2D, skin_tex);
		
		glBegin(GL_QUADS);
		drawBoxTC(posOnScreen[0],posOnScreen[1],posOnScreen[2], posOnScreen[3], posOnSkin[0]/1000, posOnSkin[1]/100, posOnSkin[2]/1000, posOnSkin[3]/100);
		glEnd();
		glDisable( GL_TEXTURE_2D );
	}
	
	private void drawLine(int[]... pos) {
	    glLineWidth(1.0f);
	    glBegin(GL_LINE_STRIP);
	    
	    for (int[] single_pos : pos) {
		    glVertex2d(single_pos[0], single_pos[1]);
		    glVertex2d(single_pos[2], single_pos[3]);
	    }
	    glEnd();
	}
	
	private void drawRect(int[]... pos) {
	    glLineWidth(1.0f);
	    glBegin(GL_QUADS);
	    
	    for (int[] single_pos : pos) {
		    glVertex2d(single_pos[0], single_pos[1]);
		    glVertex2d(single_pos[2], single_pos[1]);
		    glVertex2d(single_pos[2], single_pos[3]);
		    glVertex2d(single_pos[0], single_pos[3]);
	    }
	    glEnd();
	}


	private void draw_status() {
		colore(background[0],background[1],background[2]);
		drawRect(new int[]{0,0,ww,20});
		colore(0,0,0);
		drawLine(new int[]{0,20,ww,20});
		colore(0,0,0);
		if (Keyboard.shift) {
			drawSkinPart(new int[]{2+18*0,2,2+16+18*0,2+16}, new float[]{16*2,16*0,16+16*2,16+16*0});
		} else {
			drawSkinPart(new int[]{2+18*0,2,2+16+18*0,2+16}, new float[]{16*3,16*0,16+16*3,16+16*0});
		}
		if (Keyboard.alpha) {
			drawSkinPart(new int[]{2+18*1,2,2+16+18*1,2+16}, new float[]{16*0,16*0,16+16*0,16+16*0});
		} else {
			drawSkinPart(new int[]{2+18*1,2,2+16+18*1,2+16}, new float[]{16*1,16*0,16+16*1,16+16*0});
		}
		if (Calculator.angleMode == "deg") {
			drawSkinPart(new int[]{8+18*2,2,8+16+18*2,2+16}, new float[]{16*4,16*0,16+16*4,16+16*0});
			drawSkinPart(new int[]{8+18*3,2,8+16+18*3,2+16}, new float[]{16*7,16*0,16+16*7,16+16*0});
			drawSkinPart(new int[]{8+18*4,2,8+16+18*4,2+16}, new float[]{16*9,16*0,16+16*9,16+16*0});
		} else if (Calculator.angleMode == "rad") {
			drawSkinPart(new int[]{8+18*2,2,8+16+18*2,2+16}, new float[]{16*5,16*0,16+16*5,16+16*0});
			drawSkinPart(new int[]{8+18*3,2,8+16+18*3,2+16}, new float[]{16*6,16*0,16+16*6,16+16*0});
			drawSkinPart(new int[]{8+18*4,2,8+16+18*4,2+16}, new float[]{16*9,16*0,16+16*9,16+16*0});
		} else if (Calculator.angleMode == "gra") {
			drawSkinPart(new int[]{8+18*2,2,8+16+18*2,2+16}, new float[]{16*5,16*0,16+16*5,16+16*0});
			drawSkinPart(new int[]{8+18*3,2,8+16+18*3,2+16}, new float[]{16*7,16*0,16+16*7,16+16*0});
			drawSkinPart(new int[]{8+18*4,2,8+16+18*4,2+16}, new float[]{16*8,16*0,16+16*8,16+16*0});
		} else {
			drawSkinPart(new int[]{8+18*2,2,8+16+18*2,2+16}, new float[]{16*5,16*0,16+16*5,16+16*0});
			drawSkinPart(new int[]{8+18*3,2,8+16+18*3,2+16}, new float[]{16*7,16*0,16+16*7,16+16*0});
			drawSkinPart(new int[]{8+18*4,2,8+16+18*4,2+16}, new float[]{16*9,16*0,16+16*9,16+16*0});
		}
		
		int padding = 2;
		
		int brightness = (int)(Display.brightness * 4);
		if (brightness == 1) {
			drawSkinPart(new int[]{ww-(padding+18*0),2,ww-(padding+16+18*0),2+16}, new float[]{16*10,16*0,16+16*10,16+16*0});
		} else if (brightness == 2) {
			drawSkinPart(new int[]{ww-(padding+18*0),2,ww-(padding+16+18*0),2+16}, new float[]{16*11,16*0,16+16*11,16+16*0});
		} else if (brightness == 3) {
			drawSkinPart(new int[]{ww-(padding+18*0),2,ww-(padding+16+18*0),2+16}, new float[]{16*12,16*0,16+16*12,16+16*0});
		} else if (brightness == 4) {
			drawSkinPart(new int[]{ww-(padding+18*0),2,ww-(padding+16+18*0),2+16}, new float[]{16*13,16*0,16+16*13,16+16*0});
		}
		
		padding += 18+6;
		
		boolean canGoBack = canGoBack();
		boolean canGoForward = canGoForward();

		
		if (Calculator.haxMode) {
			drawSkinPart(new int[]{ww-(padding+16),2,ww-padding,2+16}, new float[]{16*18,16*0,16+16*18,16+16*0});
			padding += 18+6;
		}
		
		if (canGoBack && canGoForward) {
			drawSkinPart(new int[]{ww-(padding+16),2,ww-padding,2+16}, new float[]{16*14,16*0,16+16*14,16+16*0});
		} else if (canGoBack) {
			drawSkinPart(new int[]{ww-(padding+16),2,ww-padding,2+16}, new float[]{16*15,16*0,16+16*15,16+16*0});
		} else if (canGoForward) {
			drawSkinPart(new int[]{ww-(padding+16),2,ww-padding,2+16}, new float[]{16*16,16*0,16+16*16,16+16*0});
		} else {
			drawSkinPart(new int[]{ww-(padding+16),2,ww-padding,2+16}, new float[]{16*17,16*0,16+16*17,16+16*0});
		}
		
		padding += 18;

	}


	private void draw_screen() {
		screen.render();
	}


	private void draw_bottom() {
		
	}
	
	private void draw_world() {
		float x = 20;
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		colore(1.0f, 1.0f, 1.0f);
		
		if (error != null) {
			colore(0.0f, 0.0f, 0.0f, 0.75f);
			print(ww-textSize(1, "ANDREA CAVALLI'S CALCULATOR")[0]-2, wh-this.glyphsHeight[1]-2, 1, "ANDREA CAVALLI'S CALCULATOR");
			colore(0.0f, 0.0f, 0.0f, 0.75f);
			print((ww/2)-(textSize(0, "UNEXPECTED EXCEPTION")[0]/2), 11, 0, "UNEXPECTED EXCEPTION");
			colore(0.0f, 0.0f, 0.0f, 0.5f);
			print((ww/2)-(textSize(1, error)[0]/2), 22, 1, error);
			colore(0.0f, 0.0f, 0.0f, 0.4f);
			int i = 22;
			for (String stackPart : errorStackTrace) {
				print(2, 22+i, 1, stackPart);
				i+=11;
			}
		} else if (loading) {
			colore(1.0f, 1.0f, 1.0f, 1.0f);
			int titlew = textSize(0, "ANDREA CAVALLI'S CALCULATOR")[0];
			print((ww/2)-(titlew/2)-1, (wh/2)-25+translation, 0, "ANDREA CAVALLI'S CALCULATOR");
			colore(1.0f, 1.0f, 1.0f, 1.0f);
			print((ww/2)-(titlew/2)+1, (wh/2)-25+translation, 0, "ANDREA CAVALLI'S CALCULATOR");
			colore(1.0f, 1.0f, 1.0f, 1.0f);
			print((ww/2)-(titlew/2), (wh/2)-25-1+translation, 0, "ANDREA CAVALLI'S CALCULATOR");
			colore(1.0f, 1.0f, 1.0f, 1.0f);
			print((ww/2)-(titlew/2), (wh/2)-25+1+translation, 0, "ANDREA CAVALLI'S CALCULATOR");
			colore(1.0f, 0.5f, 0.0f, 1.0f);
			print((ww/2)-(titlew/2), (wh/2)-25+translation, 0, "ANDREA CAVALLI'S CALCULATOR");
			colore(0.0f, 0.0f, 0.0f, 0.75f);
			print((ww/2)-(textSize(0, "LOADING")[0]/2), (wh/2)+11, 0, "LOADING");
			colore(0.0f, 0.0f, 0.0f, 0.5f);
			print((ww/2)-(textSize(1, "PLEASE WAIT...")[0]/2), (wh/2)+22, 1, "PLEASE WAIT...");
		} else {
			draw_status();
			draw_screen();
			draw_bottom();
		}
	}

	private void draw() {
		draw_init();
		draw_world();
		glfwSwapBuffers(window);
	}

	private void loopmode(float dt) {

		rotate_t += dt;
		translate_t += dt;
		
		/*
		 * Calcoli 
		 */

		if (translation >= 10.0f) {
			translation = 10.0f;
			translation_top_to_bottom = false;
		} else if (translation <= -10.0f) {
			translation = -10.0f;
			translation_top_to_bottom = true;
		}
		
		if (translation_top_to_bottom) {
			translation += dt*15;
		} else {
			translation -= dt*15;
		}
		
		screen.beforeRender(dt);

		draw();
	}
	
	private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if ( action == GLFW_RELEASE )
				return;
			switch ( key ) {
				case GLFW_KEY_ESCAPE:
					glfwSetWindowShouldClose(window, GLFW_TRUE);
					break;
				case GLFW_KEY_F:
					font = 1;
					break;
				case GLFW_KEY_R:
					rotating = !rotating;
					rotate_t = 0.0f;
					break;
				case GLFW_KEY_P:
					integer_align = !integer_align;
					break;
				case GLFW_KEY_G:
					font = 0;
					break;
				case GLFW_KEY_V:
					show_tex = !show_tex;
					break;
				case GLFW_KEY_B:
					black_on_white = !black_on_white;
					break;
			}
		}	
	};

	private GLFWWindowSizeCallback sizecallback = new GLFWWindowSizeCallback() {
		@Override
		public void invoke(long window, int width, int height) {
			Display.this.ww = width;
			Display.this.wh = height;
		}
	};

	private GLFWFramebufferSizeCallback fbsizecallback = new GLFWFramebufferSizeCallback() {
		@Override
		public void invoke(long window, int width, int height) {
			Display.this.fbw = width;
			Display.this.fbh = height;
		}
	};
	
	private void createWindow(String title) {
		GLFWErrorCallback.createPrint().set();
		if ( glfwInit() == GLFW_FALSE )
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		this.window = glfwCreateWindow(ww, wh, title, NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetWindowSizeCallback(window, sizecallback);
		glfwSetFramebufferSizeCallback(window, fbsizecallback);
		glfwSetKeyCallback(window, keyCallback);

		// Center window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(
			window,
			(vidmode.width() - ww) / 2,
			(vidmode.height() - wh) / 2
		);

		// Create context
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwSwapInterval(1);
		glfwShowWindow(window);

		glfwInvoke(window, sizecallback, fbsizecallback);

		// Detect sRGB support
		GLCapabilities caps = GL.getCapabilities();
		supportsSRGB = caps.OpenGL30 || caps.GL_ARB_framebuffer_sRGB || caps.GL_EXT_framebuffer_sRGB;
	}

	public void run(String title) {
		try {
			createWindow(title);
			load_skin();
			load_fonts();

			long time = System.nanoTime();
			while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
				glfwPollEvents();

				long t = System.nanoTime();
				float dt = (float)((t - time) / 1000000000.0);
				time = t;

				loopmode(dt);
			}
		} finally {
			try {
				destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void destroy() {
		chardata.clear();
		
		glfwTerminate();
		glfwSetErrorCallback(null);

		memFree(yb);
		memFree(xb);

		q.free();
	}

	public void setBackground(float d, float e, float f) {
		background = new float[]{d,e,f};
	}
	
	public void colore(float f1, float f2, float f3) {
		glColor3f(f1*brightness, f2*brightness, f3*brightness);
	}
	
	public void colore(float f1, float f2, float f3, float f4) {
		glColor4f(f1*brightness, f2*brightness, f3*brightness, f4);
	}
	
	public void clearColor(float f1, float f2, float f3, float f4) {
		glClearColor(f1*brightness,f2*brightness,f3*brightness,f4);
	}
}