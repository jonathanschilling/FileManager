package de.labathome.filemanager;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import de.labathome.filemanager.HelperFunctions.ViewMode;

public class ListViewItem {

	public int x, y, width, height;
	public LinkedList<String> properties;
	public Image icon;
	private Preferences prefs;
	private Point loc;
	private ListView listView;
	String filename = "";

	/**
	 * contructor for an item to be showed in a list view
	 * @param centralPrefs - the preferences of the file mananger
	 * @param lv - in which list view to operate
	 */
	public ListViewItem(Preferences centralPrefs, ListView lv) {
		prefs = centralPrefs;
		listView = lv;
	}



	/**
	 * prepare an item and store the computed rects in the file or folder object
	 * @param g - which Graphics to use
	 * @param ffo - which file or folder object to use
	 * @param p - where to originate the item
	 */
	public void prepare(Graphics g, FileFolderObject ffo, Point p) {

		icon = HelperFunctions.getIcon(ffo, prefs.currentViewMode);
		loc = p;

		int iconSize = 16;
		int textFreeSpace = 0;
		int rowHeight = 16;
		int rowFreeSpace = 5;


		int iconX = 0, iconY = 0, textX = 0, textY = 0;
		filename = ffo.getName();
		filename = filename.split("/")[filename.split("/").length - 1];

		// clip filename to 14 chars to fir inside the grid
		if (prefs.currentViewMode == ViewMode.SymbolView && filename.length() > 14) {
			filename = String.copyValueOf(filename.toCharArray(), 0, 12) + "...";
		}

		Rectangle nameRect = HelperFunctions.getStringRect(g, filename);
		Rectangle iconRect = new Rectangle();
		Rectangle arrowRect = new Rectangle();
		
		switch (prefs.currentViewMode) {
		case SymbolView:

			iconSize = 32;
			textFreeSpace = 5;
			rowHeight = 12; // Font height

			iconX = p.x;
			iconY = p.y;

			textX = p.x + (iconSize / 2) - (int)(nameRect.getWidth() / 2);
			textY = p.y + iconSize + textFreeSpace + rowHeight;

			break;

		case DetailedView:
			iconSize = 16;
			textFreeSpace = 4;
			rowFreeSpace = 5;
			rowHeight = iconSize + rowFreeSpace;

			// first column plus icons plus arrows
			if (ffo.isDirectory()) {
				arrowRect = new Rectangle(p.x, p.y, iconSize, iconSize);

				if (ffo.isUnfolded()) {
					g.drawImage(HelperFunctions.arrowUnfolded, p.x, p.y, listView);
				}
				else {
					g.drawImage(HelperFunctions.arrowFolded, p.x, p.y, listView);
				}
			}

			iconX = p.x + iconSize + textFreeSpace;
			iconY = p.y;

			textX = iconX + iconSize + textFreeSpace;
			textY = iconY + rowHeight - 9;

			break;

		case ListView:
			iconSize = 16;
			textFreeSpace = 4;
			rowHeight = iconSize + rowFreeSpace;

			iconX = p.x + textFreeSpace;
			iconY = p.y;

			textX = iconX + iconSize + textFreeSpace;
			textY = iconY + rowHeight - 9;

			break;

		default:
			break;

		}

		nameRect = HelperFunctions.getStringRect(g, filename);
		nameRect = new Rectangle(textX - 3, textY - (int)nameRect.getHeight() - 1, (int)nameRect.getWidth() + 6, (int)nameRect.getHeight() + 6);
		iconRect = new Rectangle(iconX, iconY, iconSize, iconSize);

		ffo.setIconRect(iconRect);
		ffo.setNameRect(nameRect);
		ffo.setArrowRect(arrowRect);
	}

	/**
	 * get the precomputed rects from the file or folde robject and paint the item accordingly
	 * @param g - which Graphics to use
	 * @param ffo - which file or folder to paint
	 */
	public void paint(Graphics g, FileFolderObject ffo) {

		Rectangle nameRect = ffo.getNameRect();
		Rectangle iconRect = ffo.getIconRect();
		
		filename = ffo.getName().split("/")[ffo.getName().split("/").length - 1];
		
		// clip filename to 14 chars to fir inside the grid
		if (prefs.currentViewMode == ViewMode.SymbolView && filename.length() > 14) {
			filename = String.copyValueOf(filename.toCharArray(), 0, 12) + "...";
		}

		int iconX = iconRect.x;
		int iconY = iconRect.y;

		int textX = nameRect.x + 3;
		int textY = nameRect.y + nameRect.height - 5;

		// draw icon
		g.drawImage(HelperFunctions.getIcon(ffo, prefs.currentViewMode), iconX, iconY, listView);

		// if selected, underly filename with colored rect
		if (ffo.isSelected()) {
			g.setColor(prefs.selectedColor);
			g.fillRect(nameRect.x, nameRect.y, nameRect.width, nameRect.height);
		}

		// draw filename
		g.setColor(prefs.fontColor);
		g.drawString(filename, textX, textY);
	}
}
