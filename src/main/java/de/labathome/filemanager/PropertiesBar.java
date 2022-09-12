package de.labathome.filemanager;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * a bar for the detailed view mode which contains some columns like file name, file size, etc.
 */
public class PropertiesBar {

	private LinkedList<PropertiesItem> myProperties;
	private int clickedIndex;
	private Preferences prefs;
	
	private Point topleft;
	private int width;
	private int height;
	
	private int mostRight;
	
	/**
	 * get the bottom line of the bar to align following items underneath it
	 * @return the bottom line of the bar
	 */
	public int bottom() {
		return topleft.y + height;
	}
	
	/**
	 * get the most right line of the bar (used for consecutive painting of the columns)
	 * @param getMostRight - if true, get the most right already painted edge, else get the total will-be right edge
	 * @return see parameter desc; 
	 */
	public int right(boolean getMostRight) {
		if (getMostRight) {
			return mostRight;
		}
		return topleft.x + width;
	}
	
	/**
	 * set the origin of the bar
	 * @param topleft - the origin of the bar
	 */
	public void setTopLeft(Point topleft) {
		this.topleft = topleft;
		this.mostRight = topleft.x;
	}
	
	/**
	 * constructor for a new bar at specific point
	 * @param topleft - the origin for the new bar
	 */
	public PropertiesBar(Point topleft) {
		this.topleft = topleft;
		this.height = 30;
		this.mostRight = 0;
		
		this.myProperties = new LinkedList<PropertiesItem>();
		this.clickedIndex = 0;
	}

	/**
	 * give the bar access to the central preferences
	 * @param p - central preferences
	 */
	public void setPreferences(Preferences p) {
		prefs = p;
		
		int i = 0;
		while (myProperties.size() < prefs.columnsCaptions.size()) {
			myProperties.add(new PropertiesItem(prefs.columnsCaptions.get(i)));
			
			myProperties.getLast().setSelected(prefs.indexToSortBy == i);
			i++;
		}
	}
	
	/**
	 * check if the bar contains a certain point, e.g. from a mouse click
	 * @param p - the questioned point
	 * @return wether the point lies inside the bar or not
	 */
	public boolean contains(Point p) {
		for (int i = 0; i < myProperties.size(); i++) {
			if (myProperties.get(i).contains(p)) {
				this.clickedIndex = i;
				return true;
			}
		}
		return false;
	}

	/**
	 * react on a mouse event: select the column clicked on
	 * @param e - the questioned mouse event
	 */
	public void clickMouse(MouseEvent e) {
		System.out.println("clicked on index " + clickedIndex);
		for (int i = 0; i < myProperties.size(); i++) {
			myProperties.get(i).setSelected(i == clickedIndex);
			
		}
	}
	
	/**
	 * paint a column
	 * @param g - Graphics to use
	 * @param columnIndex - which index the current column has (used to determine the column caption)
	 * @param xPosition - where to paint it
	 * @param xWidth - which width it has to have (align to row item's length)
	 */
	public void paintColumn(Graphics g, int columnIndex, int xPosition, int xWidth) {

		width = g.getClipBounds().width;
			
		myProperties.get(columnIndex).setBounds(xPosition, this.topleft.y, xWidth, height);
		
		this.mostRight = xPosition + xWidth;
		
		myProperties.get(columnIndex).paint(g, prefs);
		
	}
	
	
}
