package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.expression.ExtraMenu;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Variable.V_TYPE;

public class BlockVariable extends Block {

	public static final int CLASS_ID = 0x00000007;

	private final char ch;
	private final VariableMenu menu;
	private V_TYPE type;
	private int color;

	public BlockVariable(char ch) {
		this.ch = ch;
		this.menu = new VariableMenu(this);
		this.type = V_TYPE.UNKNOWN;
		this.color = 0xFF304ffe;
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(color);
		r.glDrawCharLeft(x, y, ch);
	}

	@Override
	public boolean putBlock(Caret caret, Block newBlock) {
		return false;
	}

	@Override
	public boolean delBlock(Caret caret) {
		return false;
	}

	@Override
	public Block getBlock(Caret caret) {
		return null;
	}

	@Override
	public void recomputeDimensions() {
		width = BlockContainer.getDefaultCharWidth(small);
		height = BlockContainer.getDefaultCharHeight(small);
		line = height / 2;
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		recomputeDimensions();
	}

	public char getChar() {
		return ch;
	}

	@Override
	public int getClassID() {
		return CLASS_ID;
	}

	@Override
	public int computeCaretMaxBound() {
		return 0;
	}
	
	@Override
	public ExtraMenu<?> getExtraMenu() {
		return menu;
	}

	public class VariableMenu extends ExtraMenu<BlockVariable> {

		boolean mustRefresh = true;
		String text = "";
		private int color;
		
		public VariableMenu(BlockVariable var) {
			super(var);
		}

		private static final long serialVersionUID = 3941994107852212764L;

		@Override
		public void open() {
			
		}

		@Override
		public void close() {
			
		}

		@Override
		public boolean keyPressed(Key k) {
			switch (k) {
				case LEFT:
				case UP:
					switch (block.type) {
						case UNKNOWN:
							block.type = V_TYPE.SOLUTION;
							break;
						case COEFFICIENT:
							block.type = V_TYPE.UNKNOWN;
							break;
						case SOLUTION:
						default:
							block.type = V_TYPE.COEFFICIENT;
							break;
					}
					break;
				case RIGHT:
				case DOWN:
				case EQUAL:
				case SIMPLIFY:
					switch (block.type) {
						case UNKNOWN:
							block.type = V_TYPE.COEFFICIENT;
							break;
						case COEFFICIENT:
							block.type = V_TYPE.SOLUTION;
							break;
						case SOLUTION:
						default:
							block.type = V_TYPE.UNKNOWN;
							break;
					}
					break;
				default:
					return false;
			}
			
			mustRefresh = true;
			return true;
		}

		@Override
		public boolean keyReleased(Key k) {
			return false;
		}

		@Override
		public boolean beforeRender(float delta, Caret caret) {
			if (super.beforeRender(delta, caret)||mustRefresh) {
				mustRefresh = false;
				switch (block.type) {
					case UNKNOWN:
						color = 0xFF304ffe;
						break;
					case COEFFICIENT:
						color = 0xFFf50057;
						break;
					case SOLUTION:
					default:
						color = 0xFF64dd17;
						break;
				}
				block.color = color;
				text = block.type.toString();
				BinaryFont f = BlockContainer.getDefaultFont(true);
				width = 2+f.getStringWidth(text)+2;
				height = 2+f.getCharacterHeight()+2;
				return true;
			}
			return false;
		}

		@Override
		public void draw(GraphicEngine ge, Renderer r, Caret caret) {
			BlockContainer.getDefaultFont(true).use(ge);
			r.glColor3f(1.0f, 1.0f, 1.0f);
			r.glFillColor(location[0], location[1], width, height);
			r.glColor(color);
			r.glDrawStringCenter(location[0]+width/2, location[1]+2, text);
		}
		
	}
}
