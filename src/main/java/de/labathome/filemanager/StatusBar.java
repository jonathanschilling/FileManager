package de.labathome.filemanager;

//import java.awt.Color;
import java.awt.Dimension;
//import java.awt.FontMetrics;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * a class to paint a status bar, e.g. to supply the user with such vital information as
 * the current folder he is in or the number of items in the current view
 */
public class StatusBar extends RectangleWithText {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Preferences myPrefs;
	
	/**
	 * give access to the central preferences
	 * @param p - the central preferences
	 */
	public void setPreferences(Preferences p) {
		myPrefs = p;
	}
	
	/**
	 * make a new status bar at specific origin
	 * @param topleft - the origin for the new status bar
	 */
	public StatusBar(Point topleft) {
		super();

		this.x = topleft.x;
		this.y = topleft.y;
		this.height = 20;

	}
	public StatusBar(Dimension d, String caption) {
		super(d, caption);
	}
	public StatusBar(int x, int y, int width, int height, String caption) {
		super(x, y, width, height, caption);
	}
	public StatusBar(int width, int height, String caption) {
		super(width, height, caption);
	}
	public StatusBar(Point p, Dimension d, String caption) {
		super(p, d, caption);
	}
	public StatusBar(Point p, String caption) {
		super(p, caption);
	}
	public StatusBar(Rectangle r, String caption) {
		super(r, caption);
	}
	public StatusBar(String caption) {
		super(caption);
	}

	/**
	 * get the bottom line of the status bar to align following items
	 * @return the bottom line of the status bar
	 */
	public int bottom() {
		return x + height - 1;
	}
	
	/**
	 * react on mouse click, e.g. to change information visible
	 * @param e
	 */
	public void clickMouse(MouseEvent e) {
		System.out.println("StatusBar was clicked at " + e.getPoint());
	}
	
	/**
	 * paint the status bar
	 * @param g - Graphics to use
	 */
	public void paint(Graphics g) {
			
		Rectangle drawRect = g.getClipBounds();
		this.width = drawRect.width;
		
		g.setColor(myPrefs.selectedColor);
		g.fillRect(this.x, this.y, this.width, this.height);
	
		Rectangle2D r = HelperFunctions.getStringRect(g, caption);
		
		int text_x = (this.width / 2) - (r.getBounds().width / 2);
		int text_y = (this.height / 2) + (r.getBounds().height / 2) - 3;
		
		g.setColor(myPrefs.captionColor);
		g.drawString(caption, text_x, text_y);
		
	}
}
