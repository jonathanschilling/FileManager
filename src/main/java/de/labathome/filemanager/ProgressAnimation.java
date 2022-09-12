package de.labathome.filemanager;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * Class to show copy-animation
 * @author Jonathan Schilling & Veronika Polke
 *
 */

public class ProgressAnimation extends JComponent {

	private static final long serialVersionUID = 1L;

	private long msPerFrame;

	private int pos;

	private Thread internalThread;
	private Image folderImage;

	private volatile boolean noStopRequested;
	private int i = 0;
	private Image[] iconImages;

	/**
	 * Constructor method of ProgressAnimation
	 * @param name - FileFolderObject for icon generating
	 */

	public ProgressAnimation (FileFolderObject name) {
		folderImage = HelperFunctions.getIcon("folder", 32);
		iconImages = HelperFunctions.getIcons(name, HelperFunctions.ViewMode.ListView);
		setPreferredSize(new Dimension(300, 50));
		int framesPerSec = 1;
		msPerFrame = 1000L / framesPerSec;

		pos = 0;

		noStopRequested = true;
		Runnable r = new Runnable() {
			public void run() {
				try {
					runWork();
				} catch (Exception x) {
					// in case ANY exception slips through
					x.printStackTrace();
				}
			}
		};

		internalThread = new Thread(r);
		internalThread.start();
	}

	/**
	 * runs the animation and updates icon and its position
	 */

	private void runWork() {
		while (noStopRequested) {
			if (pos > 2) {
				pos = 0;
			}
			if (i >= iconImages.length-1) {
				i = 0;
			}
			repaint();
			if (pos == 2) {
				i++;
			}
			pos++;
			try {
				Thread.sleep(msPerFrame);
			} catch (InterruptedException x) {
				Thread.currentThread().interrupt();
			}
		}
	}
	

	/**
	 * paints icons onto canvas
	 */


	public void paint(Graphics g) {
		g.drawImage(folderImage, 13, 15, this);
		g.drawImage(folderImage, 132, 15, this);
		int x = 30;
		int y = 9;
		switch (pos) {
		case 0:
			x = 30;
			y = 9;
			break;
		case 1:
			x = 80;
			y = 0;
			break;
		case 2:
			x = 137;
			y = 9;
			break;
		}

		g.drawImage(iconImages[i], x, y, this);
	}

}

