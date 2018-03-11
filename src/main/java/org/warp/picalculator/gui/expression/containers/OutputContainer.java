package org.warp.picalculator.gui.expression.containers;

import java.io.Serializable;

import org.warp.picalculator.gui.GraphicalElement;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.expression.CaretState;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.layouts.OutputLayout;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class OutputContainer implements GraphicalElement, OutputLayout, Serializable {
	private static final long serialVersionUID = -5714825964892683571L;
	public final ObjectArrayList<BlockContainer> roots;
	private final Caret caret = new Caret(CaretState.HIDDEN, 0);

	public OutputContainer() {
		roots = new ObjectArrayList<>();
		roots.add(new BlockContainer());
	}

	public OutputContainer(boolean small) {
		roots = new ObjectArrayList<>();
		roots.add(new BlockContainer(small));
	}

	public OutputContainer(boolean small, int minWidth, int minHeight) {
		roots = new ObjectArrayList<>();
		roots.add(new BlockContainer(small));
	}

	public void setContentAsSingleGroup(ObjectArrayList<Block> blocks) {
		roots.clear();
		BlockContainer bcnt = new BlockContainer();
		for (Block block : blocks) {
			bcnt.appendBlockUnsafe(block);
		}
		roots.add(bcnt);
		recomputeDimensions();
	}

	public void setContentAsMultipleGroups(ObjectArrayList<ObjectArrayList<Block>> roots) {
		this.roots.clear();
		for (ObjectArrayList<Block> blocks : roots) {
			BlockContainer bcnt = new BlockContainer();
			for (Block block : blocks) {
				bcnt.appendBlockUnsafe(block);
			}
			this.roots.add(bcnt);
		}
		recomputeDimensions();
	}

	public void setContentAsMultipleElements(ObjectArrayList<Block> elems) {
		this.roots.clear();
		for (Block block : elems) {
			BlockContainer bcnt = new BlockContainer();
			bcnt.appendBlockUnsafe(block);
			this.roots.add(bcnt);
		}
		recomputeDimensions();
	}

	@Override
	public void recomputeDimensions() {
		for (BlockContainer root : roots) {
			root.recomputeDimensions();
		}
	}

	@Override
	public int getWidth() {
		int maxw = 0;
		for (BlockContainer root : roots) {
			int w = root.getWidth();
			if (w > maxw)
				maxw = w;
		}
		return maxw;
	}

	@Override
	public int getHeight() {
		int h = 0;
		for (BlockContainer root : roots) {
			h += root.getHeight() + 2;
		}
		if (h > 0) {
			return h - 2;
		} else {
			return h;
		}
	}

	@Override
	public int getLine() {
		return 0;
	}

	/**
	 * 
	 * @param delta
	 *            Time, in seconds
	 */
	public void beforeRender(double delta) {

	}

	/**
	 * 
	 * @param ge
	 *            Graphic Engine class.
	 * @param r
	 *            Graphic Renderer class of <b>ge</b>.
	 * @param x
	 *            Position relative to the window.
	 * @param y
	 *            Position relative to the window.
	 */
	public void draw(GraphicEngine ge, Renderer r, int x, int y) {
		int offset = 0;
		for (BlockContainer root : roots) {
			root.draw(ge, r, x, y + offset, caret);
			offset += root.getHeight() + 2;
		}
	}

	public void clear() {
		roots.clear();
		roots.add(new BlockContainer());
		recomputeDimensions();
	}

	public boolean isContentEmpty() {
		for (BlockContainer root : roots) {
			ObjectArrayList<Block> cnt = root.getContent();
			if (cnt != null && !cnt.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
