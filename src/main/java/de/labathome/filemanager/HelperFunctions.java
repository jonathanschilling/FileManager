package de.labathome.filemanager;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * Class which provides helping methods
 * @author Jonathan Schilling & Veronika Polke
 *
 */

abstract public class HelperFunctions {

	/**
	 * method to return byte size in readable format
	 * @param bytes - size in bytes to convert
	 * @return readableSize - size in readable format
	 */

	public static String getReadableSize (long bytes) {

		DecimalFormat df = new DecimalFormat( "0.00" );

		double KB = 1000;
		double MB = KB * 1000;
		double GB = MB * 1000;
		double TB = GB * 1000;

		if (bytes < KB) {
			return df.format(bytes) + " Bytes";
		} else if (bytes < MB) {
			return df.format((double)bytes/KB) + " KB";
		} else if (bytes < GB) {
			return df.format((double)bytes/MB) + " MB";
		} else if (bytes < TB) {
			return df.format((double)bytes/GB) + " GB";
		} else {
			return df.format((double)bytes/TB) + " TB";
		}
	}

	/**
	 * get the rectangle that surrounds a string if painted in a Graphics g
	 * @param g - the Graphics used to compute the size
	 * @param caption - the string to calculate its rectangle
	 * @return the rectangle containing the string
	 */

	public static Rectangle getStringRect(Graphics g, String caption) {
		Graphics2D g2 = (Graphics2D) g;
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D r2d = fm.getStringBounds(caption, g2);

		return (new Rectangle((int)r2d.getX(), (int)r2d.getY(), (int)r2d.getWidth(), (int)r2d.getHeight()));
	}

	/**
	 * which view modes are available
	 */
	public enum ViewMode {
		SymbolView, ListView, DetailedView
	};

	private static Image folderIcon16;
	private static Image genericFileIcon16;
	private static Image textFileIcon16;
	private static Image graphicFileIcon16;
	private static Image audioFileIcon16;
	private static Image archiveFileIcon16;
	private static Image executableIcon16;

	private static Image folderIcon32;
	private static Image genericFileIcon32;
	private static Image textFileIcon32;
	private static Image graphicFileIcon32;
	private static Image audioFileIcon32;
	private static Image archiveFileIcon32;
	private static Image executableIcon32;

	public static Image arrowFolded;
	public static Image arrowUnfolded;

	private static LinkedList<String> textExtensions;
	private static LinkedList<String> graphicExtensions;
	private static LinkedList<String> audioExtensions;
	private static LinkedList<String> archiveExtensions;
	private static LinkedList<String> executableExtensions;

	private final static String iconsBaseFolder = "./src/main/resources/icons/";
	private final static String base16px = iconsBaseFolder + "16px/";
	private final static String base32px = iconsBaseFolder + "32px/";
	private final static String iconsExt = ".png";

	private final static String folderIconName = "folderIcon";
	private final static String genericFileIconName = "genericFileIcon";
	private final static String textFileIconName = "textFileIcon";
	private final static String graphicFileIconName = "graphicFileIcon";
	private final static String audioFileIconName = "audioFileIcon";
	private final static String archiveFileIconName = "archiveFileIcon";
	private final static String executableFileIconName = "executableFileIcon";

	private final static String arrowFoldedIconName = iconsBaseFolder + "arrowFolded16.png";
	private final static String arrowUnfoldedIconName = iconsBaseFolder + "arrowUnfolded16.png";


	/**
	 * load the used icons
	 */
	public static void loadIcons() {

		// create search lists for extensions (= micro database ;-)
		textExtensions       = new LinkedList<String>(Arrays.asList("txt", "log"));
		graphicExtensions    = new LinkedList<String>(Arrays.asList("png", "jpg", "ico", "jpeg", "tif", "tiff", "bmp", "raw"));
		audioExtensions      = new LinkedList<String>(Arrays.asList("wav", "mp3", "wma", "ogg"));
		archiveExtensions    = new LinkedList<String>(Arrays.asList("tar", "gz", "zip", "tgz", "rar", "part"));
		executableExtensions = new LinkedList<String>(Arrays.asList("sh", "run", "awk", "pl", "py"));

		// read icon images
		try {
			folderIcon16      = ImageIO.read(new File(base16px + folderIconName         + "16" + iconsExt));
			genericFileIcon16 = ImageIO.read(new File(base16px + genericFileIconName    + "16" + iconsExt));
			textFileIcon16    = ImageIO.read(new File(base16px + textFileIconName       + "16" + iconsExt));
			graphicFileIcon16 = ImageIO.read(new File(base16px + graphicFileIconName    + "16" + iconsExt));
			audioFileIcon16   = ImageIO.read(new File(base16px + audioFileIconName      + "16" + iconsExt));
			archiveFileIcon16 = ImageIO.read(new File(base16px + archiveFileIconName    + "16" + iconsExt));
			executableIcon16  = ImageIO.read(new File(base16px + executableFileIconName + "16" + iconsExt));

			folderIcon32      = ImageIO.read(new File(base32px + folderIconName         + "32" + iconsExt));
			genericFileIcon32 = ImageIO.read(new File(base32px + genericFileIconName    + "32" + iconsExt));
			textFileIcon32    = ImageIO.read(new File(base32px + textFileIconName       + "32" + iconsExt));
			graphicFileIcon32 = ImageIO.read(new File(base32px + graphicFileIconName    + "32" + iconsExt));
			audioFileIcon32   = ImageIO.read(new File(base32px + audioFileIconName      + "32" + iconsExt));
			archiveFileIcon32 = ImageIO.read(new File(base32px + archiveFileIconName    + "32" + iconsExt));
			executableIcon32  = ImageIO.read(new File(base32px + executableFileIconName + "32" + iconsExt));

			arrowFolded       = ImageIO.read(new File(arrowFoldedIconName));
			arrowUnfolded     = ImageIO.read(new File(arrowUnfoldedIconName));
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"<html><center>Could not load all icon images.<br/>" + e.getMessage() + "</center></html>", "Load Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
	}

	/**
	 * get the appropriate icon for a file or folder
	 * @param ffo - the file or folder whichs icon to get
	 * @param vm - in which viewmode we currently are
	 * @return the appropriate icon
	 */
	public static Image getIcon(FileFolderObject ffo, ViewMode vm) {
		// get Extension from filename
		String filename = ffo.getName();
		String ext = "";
		String parts[];

		if (!ffo.isDirectory() && filename.contains(".")) {
			parts = filename.split(".");
			if (parts.length > 0) {
				ext = parts[parts.length - 1];
			}
		}
		// return matching icon, default icon if file is not in "database" ;-)
		if (vm == ViewMode.ListView || vm == ViewMode.DetailedView ) {
			// 16 px icons


			if (ffo.isDirectory()) {
				return folderIcon16;
			}
			else if (ffo.isExecutable() || executableExtensions.contains(ext)) {
				return executableIcon16;
			}
			else if (graphicExtensions.contains(ext)) {
				return graphicFileIcon16;
			}
			else if (archiveExtensions.contains(ext)) {
				return archiveFileIcon16;
			}
			else if (audioExtensions.contains(ext)) {
				return audioFileIcon16;
			}
			else if (textExtensions.contains(ext)) {
				return textFileIcon16;
			}
			else {
				return genericFileIcon16;
			}
		}
		else if (vm == ViewMode.SymbolView) {
			// 32 px icons

			if (ffo.isDirectory()) {
				return folderIcon32;
			}
			else if (ffo.isExecutable() || executableExtensions.contains(ext)) {
				return executableIcon32;
			}
			else if (graphicExtensions.contains(ext)) {
				return graphicFileIcon32;
			}
			else if (archiveExtensions.contains(ext)) {
				return archiveFileIcon32;
			}
			else if (audioExtensions.contains(ext)) {
				return audioFileIcon32;
			}
			else if (textExtensions.contains(ext)) {
				return textFileIcon32;
			}
			else {
				return genericFileIcon32;
			}
		}
		else {
			// should never happen, but better to have one there than to mess up the display...
			return genericFileIcon16;
		}
	}

	/**
	 * get an array of icons for all contents of a folder
	 * @param name - the folder
	 * @param vm - current viewmode
	 * @return an array of icons
	 */
	public static Image[] getIcons (FileFolderObject name, ViewMode vm) {
		name.updateContents();
		LinkedList<FileFolderObject> conts = name.getContents();
		Image[] icons = new Image[conts.size()];
		for (int i = 0; i < conts.size(); i++) {
			icons[i] = getIcon(conts.get(i), vm);
		}

		return icons;
	}

	/**
	 * get an icon by a string
	 * @param name - the icon's id string
	 * @param size - which icon size is wished
	 * @return the appropriate icon
	 */
	public static Image getIcon (String name, int size) {
		if (size == 16) {
			if (name == "textfile") {
				return textFileIcon16;
			} else if (name == "audiofile") {
				return audioFileIcon16;
			}

		} else if (size == 32) {
			if (name == "textfile") {
				return textFileIcon32;
			} else if (name == "folder") {
				return folderIcon32;
			}
		}
		return genericFileIcon16;

	}
}

