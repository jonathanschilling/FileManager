package de.labathome.filemanager;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JPanel;

import de.labathome.filemanager.HelperFunctions.ViewMode;

public class ListView extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private Preferences prefs;

	LinkedList<String> entry;

	private StatusBar myStatusBar;
	private PropertiesBar myPropertiesBar;

	private Rectangle dragRect;
	public Point mouseDown;
	public Point mouseUp;

	public boolean mousePressed;
	public boolean findMouseClick;

	private BufferedImage bufferedImage = null;

	private int width, height;

	public boolean mouseDragging;
	public boolean drawDragRect;

	private FileFolderObject draggedFfo;
	private FileFolderObject targetFfo;

	private boolean somethingIsDragged = false;
	private boolean dropTargetFound = false;
	private boolean somethingWasUnfolded = true;
//	private boolean mouseJustReleased = false;

	private Rectangle draggedRect;

	/**
	 * constructor for a new list view
	 *
	 * @param newPrefs - the central preferences of the filemanager that contains
	 *                 this list view
	 */
	public ListView(Preferences newPrefs) {

		super();

		// get relevant preferences from Preferences class
		this.prefs = newPrefs;
		this.setBackground(prefs.backgroundColor);

		entry = new LinkedList<String>();

		myStatusBar = new StatusBar(new Point(0, 0));
		myPropertiesBar = new PropertiesBar(new Point(0, myStatusBar.bottom()));

		myStatusBar.setPreferences(prefs);
		myPropertiesBar.setPreferences(prefs);

		dragRect = new Rectangle();
		mouseDown = new Point();
		mouseUp = new Point();

		mousePressed = false;
		findMouseClick = false;
		mouseDragging = false;
		drawDragRect = false;
		somethingWasUnfolded = true;

		// draggedFfos = new LinkedList<FileFolderObject>();

		width = 1024;
		height = 768;

		// listen to mouse movements and button click events
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * paint the listview using double buffering
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		if (bufferedImage == null) {

			int w = this.getWidth();
			int h = this.getHeight();
			bufferedImage = (BufferedImage) this.createImage(w, h);
			Graphics2D gc = bufferedImage.createGraphics();
			gc.setColor(prefs.backgroundColor);
			gc.fillRect(0, 0, w, h);
		}

		g2.drawImage(bufferedImage, null, 0, 0);

		paintFileList(g2);
	}

	/**
	 * paint the actual file list onto the buffer image
	 *
	 * @param g
	 */
	public void paintFileList(Graphics2D g) {

		// draw file list
		if (prefs.currentDir != null) {

			myStatusBar.caption = prefs.currentDir.getName() + ", " + prefs.currentDir.getEntry().get(1) + " Items";
			myStatusBar.paint(g);

			// compute rubberBand
			if (mousePressed) {
				dragRect.setFrameFromDiagonal(mouseDown, mouseUp);
			}

			prefs.currentDir.loadContents();

			// entry = prefs.currentDir.getEntry();

			// compute position of icons and filenames and store them in the ffos
			if (prefs.currentViewMode == HelperFunctions.ViewMode.DetailedView) {
				prepareDetailedView(g);
			}

			else if (prefs.currentViewMode == HelperFunctions.ViewMode.ListView) {
				prepareListView(g);
			}

			else if (prefs.currentViewMode == HelperFunctions.ViewMode.SymbolView) {
				prepareSymbolView(g);
			}

			// now check for intersections with a dragged rectangle or a dragging mouse and
			// select the items
			boolean mouseInsideItemShadow = false;
			// boolean itemInDragRect = false;
			boolean mouseInsideArrowRect = false;

			FileFolderObject ffo;

			for (int i = 0; i < prefs.currentDir.getContents().size(); i++) {

				ffo = prefs.currentDir.getContent(i);

				mouseInsideItemShadow = (ffo.getNameRect().contains(mouseUp) || ffo.getIconRect().contains(mouseUp));
				if (prefs.currentViewMode == ViewMode.DetailedView) {
					mouseInsideArrowRect = ffo.getArrowRect().contains(mouseUp);
				} else {
					mouseInsideArrowRect = false;
				}
				// itemInDragRect = (dragRect.intersects(ffo.getNameRect()) ||
				// dragRect.intersects(ffo.getIconRect()));

				// check where the mousePress event was and select appropriate
				if (findMouseClick) {
					ffo.setSelected(mouseInsideItemShadow);
					if (mouseInsideArrowRect) {
						ffo.setUnfolded(!ffo.isUnfolded());
						somethingWasUnfolded = true;
					}
					if (ffo.isDirectory()) {
						for (int j = 0; j < ffo.getContents().size(); j++) {
							if (ffo.getContent(j).getArrowRect().contains(mouseUp)) {
								ffo.getContent(j).setUnfolded(!ffo.getContent(j).isUnfolded());
								somethingWasUnfolded = true;
							}
						}
					}
				}

				// check if we are furthermore dragging
				if (mouseDragging) {

					if (ffo.isSelected() && !mouseInsideItemShadow && draggedFfo == null) {

						ffo.isDragged = true;

						draggedFfo = ffo;
						somethingIsDragged = true;

					}

					// if something is dragged, find a target for it
					if (somethingIsDragged) {

						if (prefs.currentViewMode == ViewMode.SymbolView) {
							draggedRect = new Rectangle(mouseUp.x - 16, mouseUp.y - 16, 32, 32);
						} else {
							draggedRect = new Rectangle(mouseUp.x - 8, mouseUp.y - 8, 16, 16);
						}

						if (draggedFfo != ffo) {
							ffo.setSelected(mouseInsideItemShadow && ffo.isDirectory());
						}

						if (mouseInsideItemShadow && ffo.isDirectory()) {

							targetFfo = ffo;
							dropTargetFound = true;

						}
					}

				}
			}

			// now that the proper items are selected, paint them
			paintViewMode(g);

			if (somethingIsDragged) {
				g.setColor(prefs.selectedColor);
				g.drawRect(draggedRect.x, draggedRect.y, draggedRect.width, draggedRect.height);
				g.drawRect(draggedRect.x + 1, draggedRect.y + 1, draggedRect.width - 2, draggedRect.height - 2);

			}

			// draw rubberBand above all other items
			if (mousePressed && drawDragRect) {
				g.setColor(prefs.selectRectColor);
				g.drawRect(dragRect.x, dragRect.y, dragRect.width, dragRect.height);
				drawDragRect = false;

			}
		}
		findMouseClick = false;
	}

	/**
	 * actually paint the items in the current view mode
	 *
	 * @param g - in which Graphics to paint
	 */
	private void paintViewMode(Graphics g) {
		ListViewItem item = new ListViewItem(prefs, this);
		FileFolderObject ffo = prefs.currentDir;

		paintRecursiveItem(g, ffo, item);
	}

	private void paintRecursiveItem(Graphics g, FileFolderObject ffo, ListViewItem item) {

		for (int i = 0; i < ffo.getContents().size(); i++) {
			item.paint(g, ffo.getContent(i));

			if (ffo.getContent(i).isUnfolded()) {
				paintRecursiveItem(g, ffo.getContent(i), item);
			}
		}
	}

	private int getExpandedRecursive(FileFolderObject ffo, int current) {
		int result = 0;

		if (ffo.isDirectory()) {
			result = ffo.getContents().size();
			if (ffo.isUnfolded()) {
				for (int i = 0; i < ffo.getContents().size(); i++) {
					result += getExpandedRecursive(ffo.getContent(i), current);
				}
			}
		}

		return result;
	}

	/**
	 * prepare file list in detailed mode this means: compute the position of the
	 * item's: string rect, icon rect and arrow rect
	 *
	 * @param g - Graphics to use
	 */
	private void prepareDetailedView(Graphics2D g) {

		// basic constants for detailed list view
		final int rowStart = 5;
		final int rowFreeSpace = 5;
		final int iconSize = 16;

		// derived constants for detailed list view
		final int rowHeight = iconSize + rowFreeSpace;

		ListViewItem item = new ListViewItem(prefs, this);

		FileFolderObject ffo = prefs.currentDir;
		ffo.setUnfolded(true);

		int y = myPropertiesBar.bottom() + rowStart;

		int numExpanded = getExpandedRecursive(ffo, 0);

		if ((numExpanded) * rowHeight > g.getClipBounds().height) {
			height = (rowHeight * (numExpanded + 1) + 30);
			this.revalidate();
		}

		int xPosition = 0;
		int xWidth = 0;

		myPropertiesBar.setTopLeft(new Point(0, myStatusBar.bottom()));

		prepareRecursive(g, item, y, ffo, 0);

		for (int i = 0; i < Preferences.columnsCaptions.size(); i++) {

			xPosition = myPropertiesBar.right(true);
			xWidth = prefs.currentDir.getWidestContentWidth(g, i) + 50;

			myPropertiesBar.paintColumn(g, i, xPosition, xWidth);
		}
	}

	private int prepareRecursive(Graphics g, ListViewItem item, int currentYPos, FileFolderObject ffo,
			int numAlreadyExpanded) {

		int num = 0;
		final int columnStart = 4;
		final int rowFreeSpace = 5;
		final int textFreeSpace = 4;
		final int iconSize = 16;
		final int rowHeight = iconSize + rowFreeSpace;
		final int indentIncrement = iconSize + textFreeSpace;

		int x = 0;
		int y = currentYPos;

		int result = 0;

		if (ffo.isDirectory()) {

			num = ffo.getContents().size();
			result = num;

			for (int i = 0; i < num; i++) {

				x = (Math.abs(ffo.getLevel())) * indentIncrement + columnStart;

				y += rowHeight;

				Point p = new Point(x, y);

				item.prepare(g, ffo.getContent(i), p);

				if (ffo.getContent(i).isUnfolded()) {
					result = prepareRecursive(g, item, y, ffo.getContent(i), 0);
					y += result * rowHeight;
				}
			}
		}

		return result;
	}

	/**
	 * prepare file list in list mode this means: compute the position of the
	 * item's: string rect and icon rect
	 *
	 * @param g - Graphics to use
	 */
	private void prepareListView(Graphics2D g) {

		// basic constants for detailed list view
		final int columnStart = 3;
		final int rowStart = 5 + myStatusBar.bottom();
		final int rowFreeSpace = 5;
		final int iconSize = 16;

		// derived constants for detailed list view
		final int rowHeight = iconSize + rowFreeSpace;

		// volatile variables (different for each entry
		Point p = new Point();

		ListViewItem item = new ListViewItem(prefs, this);

		// total number of files in current view
		int numFiles = prefs.currentDir.getContents().size();

		// adjust panel height to allow scrolling to all listed items
		if (numFiles * rowHeight > g.getClipBounds().height) {
			height = (rowHeight * (numFiles + 1));
			this.revalidate();
		}

		// paint view items
		for (int i = 0; i < numFiles; i++) {

			p.x = columnStart;
			p.y = rowStart + (i * rowHeight);

			item.prepare(g, prefs.currentDir.getContent(i), p);
		}
	}

	/**
	 * prepare file list in symbol mode this means: compute the position of the
	 * item's: string rect and icon rect
	 *
	 * @param g - Graphics to use
	 */
	private void prepareSymbolView(Graphics2D g) {

		// constants
		final int gridSpace = 20;
		final int textHeight = 12;
		final int textSpace = 5;
		final int iconSize = 32;

		final int gridSizeHorizontal = 3 * iconSize + gridSpace;
		final int gridSizeVertical = iconSize + textSpace + textHeight + gridSpace;

		// total number of files in current view
		int numFiles = prefs.currentDir.getContents().size();

		// number of files per row
		int numHorizontal = (g.getClipBounds().width / gridSizeHorizontal);

		// volatile variables (different for each entry
		Point p = new Point();
		ListViewItem item = new ListViewItem(prefs, this);

		if (numHorizontal > 0) {

			height = (int) (gridSizeVertical * (Math.ceil(numFiles / numHorizontal))) + 100;
			this.revalidate();

			// iterate over entries
			for (int i = 0; i < numFiles; i++) {

				// position of icon in grid
				p.x = (i % numHorizontal) * gridSizeHorizontal + (gridSizeHorizontal / 2);
				p.y = (i / numHorizontal) * gridSizeVertical + gridSpace + myStatusBar.bottom();

				item.prepare(g, prefs.currentDir.getContent(i), p);
			}
		}
	}

	/**
	 * mouse was clicked, check if this means anything to us
	 */
	public void mouseClicked(MouseEvent e) {

		mouseDown = e.getPoint();
		mouseUp = mouseDown;

		// mousePressed = false;

		// find out where the user clicked and tell the corresponding widget
		// about it
		if (myStatusBar.contains(mouseDown)) {
			myStatusBar.clickMouse(e);

		}

		else if (prefs.currentViewMode == HelperFunctions.ViewMode.DetailedView
				&& myPropertiesBar.contains(mouseDown)) {
			myPropertiesBar.clickMouse(e);

		}

		else {
			// in the paint routines, find out if mouseDown pointed on something important
			// findMouseClick = true;
		}

		// this.revalidate();
		this.repaint();
	}

	/**
	 * mouse is pressed, check if this means anything to us
	 */
	public void mousePressed(MouseEvent arg0) {

		mousePressed = true;
		findMouseClick = true;
//		mouseJustReleased = false;

		mouseDown = arg0.getPoint();
		mouseUp = mouseDown;

		repaint();
	}

	/**
	 * mouse was dragged, maybe drag a file
	 */
	public void mouseDragged(MouseEvent arg0) {

		mouseDragging = true;
		mouseUp = arg0.getPoint();

		repaint();
	}

	/**
	 * mouse was released, so get up and do something if the user has p.ex. dragged
	 * and dropped some poor file or folder
	 */
	public void mouseReleased(MouseEvent arg0) {

		mousePressed = false;
		mouseDragging = false;
		somethingIsDragged = false;

//		mouseJustReleased = true;

		mouseUp = arg0.getPoint();

		repaint();

		if (dropTargetFound || somethingWasUnfolded) {

			if (dropTargetFound) {

				for (int i = 0; i < prefs.currentDir.getContents().size(); i++) {
					prefs.currentDir.getContent(i).setSelected(false);
					prefs.currentDir.getContent(i).isDragged = false;

				}

				String fileName = draggedFfo.getName().split("/")[draggedFfo.getName().split("/").length - 1];

				CopyStatus cps = new CopyStatus();
				draggedFfo.copyFileFolder(false, cps, draggedFfo.getName(),
						targetFfo.getName() + File.separator + fileName);

				prefs.currentDir.updateContents();
			} else if (somethingWasUnfolded) {
				loadContentsRecursive(prefs.currentDir);
			}

			draggedFfo = null;
			targetFfo = null;
			dropTargetFound = false;
			somethingWasUnfolded = false;

			repaint();
		}
	}

	private void loadContentsRecursive(FileFolderObject ffo) {
		for (int i = 0; i < ffo.getContents().size(); i++) {
			if (ffo.getContent(i).isUnfolded()) {
				ffo.getContent(i).loadContents();
				loadContentsRecursive(ffo.getContent(i));
			}
		}
	}

	/**
	 * unused
	 */
	public void mouseMoved(MouseEvent arg0) {
	}

	/**
	 * unused
	 */
	public void mouseEntered(MouseEvent arg0) {
	}

	/**
	 * unused
	 */
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * get the preferred size according to number of listed files and view mode
	 * return so that all files are scrollable
	 */
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}