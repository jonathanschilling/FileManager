package de.labathome.filemanager;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.geom.Rectangle2D;
//import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class FileManager extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// FileFolderObject currentFileFolder;
	// ViewMode currentViewMode;

	PreferencesDialog prefsDialog;

	/**
	 * constructor - makes a new FileManager with title string given
	 *
	 * @param mainTitle
	 */
	public FileManager(String mainTitle) {

		super(mainTitle);

		this.mainTitle = mainTitle;

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.setupUi();
		this.setup();
	}

	// ui vars

	ListView listView;

	ImageIcon folderIcon;

	JMenuBar mainMenuBar;

	JMenu fileMenu;
	JMenuItem fileOpenItem;
	JMenuItem fileQuitItem;

	JMenu editMenu;
	JMenuItem editPreferencesItem;

	JMenu viewMenu;
	JMenuItem viewListModeItem;
	JMenuItem viewSymbolModeItem;
	JMenuItem viewDetailedModeItem;

	String mainTitle;

	JScrollPane scrollPane;

	/**
	 * generate the user interface
	 */
	void setupUi() {

		this.setTitle(mainTitle);

		this.setSize(getPreferredSize());

		// menu items
		fileOpenItem = new JMenuItem("Open");
		fileOpenItem.addActionListener(this);

		fileQuitItem = new JMenuItem("Quit");
		fileQuitItem.addActionListener(this);

		editPreferencesItem = new JMenuItem("Preferences");
		editPreferencesItem.addActionListener(this);

		viewListModeItem = new JMenuItem("List Mode");
		viewListModeItem.addActionListener(this);

		viewSymbolModeItem = new JMenuItem("Symbol Mode");
		viewSymbolModeItem.addActionListener(this);

		viewDetailedModeItem = new JMenuItem("Detailed Mode");
		viewDetailedModeItem.addActionListener(this);

		// menues
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		viewMenu = new JMenu("View");

		fileMenu.add(fileOpenItem);
		fileMenu.add(fileQuitItem);

		editMenu.add(editPreferencesItem);

		viewMenu.add(viewSymbolModeItem);
		viewMenu.add(viewListModeItem);
		viewMenu.add(viewDetailedModeItem);

		mainMenuBar = new JMenuBar();

		mainMenuBar.add(fileMenu);
		mainMenuBar.add(editMenu);
		mainMenuBar.add(viewMenu);

		this.setJMenuBar(mainMenuBar);

		prefsDialog = new PreferencesDialog("Preferences");

		listView = new ListView(prefsDialog.getPrefs());

		Container content = this.getContentPane();
		scrollPane = new JScrollPane(listView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// scrollPane.setMouseWheelIncrement(20); // scroll more than 1 pixel per
		// mouseWheelEvent
		content.add(scrollPane, BorderLayout.CENTER);

		this.pack();

		this.setVisible(true);

		System.out.println("FileManager::setupUi() finished.");
	}

	// end ui vars

	/**
	 * load other things than the user interface, f.ex. the used icons
	 */
	void setup() {

		HelperFunctions.loadIcons();

		System.out.println("FileManager::setup() finished.");
	}

	/**
	 * listen to ui actions
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == fileOpenItem) {

			// open new directory as "root dir" for this FileManager
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (fc.showDialog(this, "Select Dir") == JFileChooser.APPROVE_OPTION) {

				prefsDialog.getPrefs().currentDir = new FileFolderObject(fc.getSelectedFile().getPath(), 0);
			}
		}

		else if (e.getSource() == fileQuitItem) {

			// quit FileManager
			System.out.println("FileManager now exits.");
			this.dispose();
		}

		else if (e.getSource() == editPreferencesItem) {

			// open Preferences Dialog
			prefsDialog.showMe();
		}

		else if (e.getSource() == viewSymbolModeItem) {
			prefsDialog.getPrefs().currentViewMode = HelperFunctions.ViewMode.SymbolView;
		}

		else if (e.getSource() == viewListModeItem) {
			prefsDialog.getPrefs().currentViewMode = HelperFunctions.ViewMode.ListView;
		}

		else if (e.getSource() == viewDetailedModeItem) {
			prefsDialog.getPrefs().currentViewMode = HelperFunctions.ViewMode.DetailedView;
		}

		updateListView();
	}

	/**
	 * update the list view
	 */
	void updateListView() {
		listView.repaint();
	}

	/**
	 * get the preferred size of the file mananger
	 */
	public Dimension getPreferredSize() {
		return new Dimension(1024, 768);
	}

}
