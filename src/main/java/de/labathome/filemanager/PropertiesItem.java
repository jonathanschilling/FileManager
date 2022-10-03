package de.labathome.filemanager;

//import java.awt.Color;
import java.awt.Dimension;
//import java.awt.FontMetrics;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * an object for a single column item
 */
public class PropertiesItem extends RectangleWithText {

	private static final long serialVersionUID = 1L;

	private boolean isSelected;

	/**
	 * set the column selected
	 *
	 * @param b - wether to select it or not
	 */
	public void setSelected(boolean b) {
		this.isSelected = b;
	}

	/**
	 * find out if the column is selected
	 *
	 * @return wether the column is selected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	public PropertiesItem() {
		super();
	}

	public PropertiesItem(Dimension d, String caption) {
		super(d, caption);
	}

	public PropertiesItem(int x, int y, int width, int height, String caption) {
		super(x, y, width, height, caption);
	}

	public PropertiesItem(int width, int height, String caption) {
		super(width, height, caption);
	}

	public PropertiesItem(Point p, Dimension d, String caption) {
		super(p, d, caption);
	}

	public PropertiesItem(Point p, String caption) {
		super(p, caption);
	}

	public PropertiesItem(Rectangle r, String caption) {
		super(r, caption);
	}

	public PropertiesItem(String caption) {
		super(caption);
	}

	/**
	 * paint it
	 *
	 * @param g       - Graphics to use
	 * @param myPrefs - central Preferences to fetch column captions from
	 */
	public void paint(Graphics g, Preferences myPrefs) {

		if (isSelected)
			g.setColor(myPrefs.selectedColor);
		else
			g.setColor(myPrefs.unselectedColor);
		g.fillRect(this.x, this.y, this.width, this.height);

		g.setColor(myPrefs.unselectedColor);
		g.drawLine(this.x, 21, this.x, g.getClipBounds().height - 1);

		Rectangle2D r = HelperFunctions.getStringRect(g, caption);

		int textX = (this.width / 2) - (r.getBounds().width / 2) + this.getBounds().x;
		int textY = (this.height / 2) + (r.getBounds().height / 2) - 3 + 21;

		g.setColor(myPrefs.captionColor);
		g.drawString(caption, textX, textY);
	}
}
