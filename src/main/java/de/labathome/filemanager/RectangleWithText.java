package de.labathome.filemanager;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class RectangleWithText extends Rectangle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String caption;
	public RectangleWithText() {
		super();
		this.caption = "<noname>";
	}
	public RectangleWithText(String caption) {
		super();
		this.caption = caption;
	}
	public RectangleWithText(Dimension d, String caption) {
		super(d);
		this.caption = caption;
	}
	public RectangleWithText(int x, int y, int width, int height, String caption) {
		super(x, y, width, height);
		this.caption = caption;
	}
	public RectangleWithText(int width, int height, String caption) {
		super(width, height);
		this.caption = caption;
	}
	public RectangleWithText(Point p, Dimension d, String caption) {
		super(p, d);
		this.caption = caption;
	}
	public RectangleWithText(Point p, String caption) {
		super(p);
		this.caption = caption;
	}
	public RectangleWithText(Rectangle r, String caption) {
		super(r);
		this.caption = caption;
	}
}
