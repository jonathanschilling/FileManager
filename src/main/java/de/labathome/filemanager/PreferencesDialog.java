package de.labathome.filemanager;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

/**
 * Class to show the Preferences Dialog
 *
 * @author Jonathan Schilling & Veronika Polke
 */
public class PreferencesDialog extends JDialog implements ActionListener {

	/**
	 * Defining local Preferences file
	 */
	private static final String PrefsFileName = "./FileManagerPreferences.prefs";

	/**
	 * Defining new Preferences instance
	 */
	private Preferences myPrefs;

	private static final long serialVersionUID = 1L;

	/**
	 * constructor for Preferences Dialog
	 *
	 * @param title - title of GUI window
	 */
	public PreferencesDialog(String title) {
		super();

		this.myPrefs = new Preferences();
		if (!loadFromFile(PrefsFileName)) {
			this.myPrefs = new Preferences();
			saveToFile(PrefsFileName);
		}

		createGUI(title);
	}

	/**
	 * ui vars
	 */
	JFrame frame;

	JButton saveButton;
	JButton loadButton;
	JButton closeButton;
	JButton bgColorButton;
	JButton fontColorButton;
	JButton selectedColorButton;
	JButton unselectedColorButton;
	JButton captionColorButton;
	JButton selectRectColorButton;

	JTextField nameField;
	JTextField sizeField;
	JTextField ownerField;
	JTextField lastChangeField;
	JTextField permissionsField;

	JCheckBox hiddenFilesBox;

	/**
	 * method to add components to pane
	 *
	 * @param contentPane - Pane to which the components are added
	 */
	public void addComponentsToPane(Container contentPane) {

//        Any number of rows and 2 columns
		contentPane.setLayout(new GridLayout(0, 3, 10, 10));

		// main buttons
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		closeButton = new JButton("Close");

		saveButton.addActionListener(this);
		loadButton.addActionListener(this);
		closeButton.addActionListener(this);

		// color selection buttons
		bgColorButton = new JButton("Choose BG Color");
		fontColorButton = new JButton("Choose Font Color");
		selectedColorButton = new JButton("Choose Selected Color");
		unselectedColorButton = new JButton("Choose Unselected Color");
		captionColorButton = new JButton("Choose Caption Color");
		selectRectColorButton = new JButton("Choose Select Rect Color");

		bgColorButton.addActionListener(this);
		fontColorButton.addActionListener(this);
		selectedColorButton.addActionListener(this);
		unselectedColorButton.addActionListener(this);
		captionColorButton.addActionListener(this);
		selectRectColorButton.addActionListener(this);

		bgColorButton.setBackground(myPrefs.backgroundColor);
		fontColorButton.setBackground(myPrefs.fontColor);
		selectedColorButton.setBackground(myPrefs.selectedColor);
		unselectedColorButton.setBackground(myPrefs.unselectedColor);
		captionColorButton.setBackground(myPrefs.captionColor);
		selectRectColorButton.setBackground(myPrefs.selectRectColor);

		JLabel colorsLabel = new JLabel("<html><u>Colors</u></html>");
		JLabel columnsLabel = new JLabel("<html><u>Columns</u></html>");
		JLabel captionsLabel = new JLabel("<html><u>Captions</u></html>");

		colorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		columnsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		captionsLabel.setHorizontalAlignment(SwingConstants.CENTER);

		nameField = new JTextField(Preferences.columnsCaptions.get(0), 20);
		nameField.addActionListener(this);

		sizeField = new JTextField(Preferences.columnsCaptions.get(1), 20);
		sizeField.addActionListener(this);

		ownerField = new JTextField(Preferences.columnsCaptions.get(2), 20);
		ownerField.addActionListener(this);

		lastChangeField = new JTextField(Preferences.columnsCaptions.get(3));
		lastChangeField.addActionListener(this);

		permissionsField = new JTextField(Preferences.columnsCaptions.get(4));
		permissionsField.addActionListener(this);

		hiddenFilesBox = new JCheckBox(" ", myPrefs.showHiddenFiles);
		hiddenFilesBox.addActionListener(this);

		contentPane.add(colorsLabel);
		contentPane.add(columnsLabel);
		contentPane.add(captionsLabel);

		contentPane.add(bgColorButton);
		contentPane.add(new JLabel("Name"));
		contentPane.add(nameField);

		contentPane.add(fontColorButton);
		contentPane.add(new JLabel("Size"));
		contentPane.add(sizeField);

		contentPane.add(selectedColorButton);
		contentPane.add(new JLabel("Owner"));
		contentPane.add(ownerField);

		contentPane.add(unselectedColorButton);
		contentPane.add(new JLabel("Last Change"));
		contentPane.add(lastChangeField);

		contentPane.add(captionColorButton);
		contentPane.add(new JLabel("Permissions"));
		contentPane.add(permissionsField);

		contentPane.add(selectRectColorButton);
		contentPane.add(new JLabel("Show hidden files and folders"));
		contentPane.add(hiddenFilesBox);

		contentPane.add(saveButton);
		contentPane.add(loadButton);
		contentPane.add(closeButton);

	}

	/**
	 * method to create GUI
	 *
	 * @param title - title of GUI window
	 */
	private void createGUI(String title) {
		frame = new JFrame(title);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Set up the content pane and components in GridLayout
		addComponentsToPane(frame.getContentPane());

		frame.pack();
	}

	/**
	 * method to show dialog
	 */
	public void showMe() {
		frame.setVisible(true);
	}

	/**
	 * method to hide dialog
	 */
	public void hideMe() {
		frame.setVisible(false);
	}

	/**
	 * end ui vars
	 */

	/**
	 * method to save preferences in file
	 *
	 * @param filename - path to preferences file
	 * @return - true if succeeds to save file
	 */
	private boolean saveToFile(String filename) {
		boolean success = false;
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(myPrefs);
			oos.flush();
			oos.close();
			fos.flush();
			fos.close();
			success = true;
		} catch (Exception e) {
			System.out.println("Tried to save preferences file " + filename + ": " + e.toString());
			JOptionPane.showMessageDialog(new JFrame(),
					"<html><center>Could not save preferences file.<br/>" + e.getMessage() + "</center></html>",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		return success;
	}

	/**
	 * method to load preferences from file
	 *
	 * @param filename - path to preferences file
	 * @return - true if succeeds to load file
	 */
	private boolean loadFromFile(String filename) {
		boolean success = false;
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			myPrefs = (Preferences) ois.readObject();
			ois.close();
			fis.close();
			success = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"<html><center>Could not load preferences file.<br/>" + e.getMessage() + "</center></html>",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
			System.out.println("Tried to load preferences file " + filename + ": " + e.getMessage());
		}
		return success;
	}

	/**
	 * ActionListener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// save Button: save Preferences to file (= Serialize)
		if (e.getSource() == saveButton) {
			this.saveToFile(PrefsFileName);
		}

		// load Button: load Preferences from file (=Deserialize)
		else if (e.getSource() == loadButton) {
			this.loadFromFile(PrefsFileName);
		}

		// select background color
		else if (e.getSource() == bgColorButton) {
			myPrefs.backgroundColor = JColorChooser.showDialog(this, "Choose Background Color",
					myPrefs.backgroundColor);
			bgColorButton.setBackground(myPrefs.backgroundColor);
		}

		// select font color
		else if (e.getSource() == fontColorButton) {
			myPrefs.fontColor = JColorChooser.showDialog(this, "Choose Font Color", myPrefs.fontColor);
			fontColorButton.setBackground(myPrefs.fontColor);
		}

		// select background color for selected items
		else if (e.getSource() == selectedColorButton) {
			myPrefs.selectedColor = JColorChooser.showDialog(this, "Choose Selected Color", myPrefs.selectedColor);
			selectedColorButton.setBackground(myPrefs.selectedColor);
		}

		// select background color for unselected items
		else if (e.getSource() == unselectedColorButton) {
			myPrefs.unselectedColor = JColorChooser.showDialog(this, "Choose Unselected Color",
					myPrefs.unselectedColor);
			unselectedColorButton.setBackground(myPrefs.unselectedColor);
		}

		// select color for PropertiesItems' captions
		else if (e.getSource() == captionColorButton) {
			myPrefs.captionColor = JColorChooser.showDialog(this, "Choose Caption Color", myPrefs.captionColor);
			captionColorButton.setBackground(myPrefs.captionColor);
		}

		else if (e.getSource() == selectRectColorButton) {
			myPrefs.selectRectColor = JColorChooser.showDialog(this, "Choose Select Rect Color",
					myPrefs.selectRectColor);
			selectRectColorButton.setBackground(myPrefs.selectRectColor);
		}

		// select caption for Name column
		else if (e.getSource() == nameField) {
			Preferences.columnsCaptions.set(0, e.getActionCommand());
		}

		// select caption for Size column
		else if (e.getSource() == sizeField) {
			Preferences.columnsCaptions.set(1, e.getActionCommand());
		}

		// select caption for Owner column
		else if (e.getSource() == ownerField) {
			Preferences.columnsCaptions.set(2, e.getActionCommand());
		}

		// select caption for Last Change column
		else if (e.getSource() == lastChangeField) {
			Preferences.columnsCaptions.set(3, e.getActionCommand());
		}

		// select caption for Permissions column
		else if (e.getSource() == permissionsField) {
			Preferences.columnsCaptions.set(4, e.getActionCommand());
		}

		// update showHiddenFiles
		else if (e.getSource() == hiddenFilesBox) {
			myPrefs.showHiddenFiles = hiddenFilesBox.isSelected();
		}

		// close (= hide) dialog
		else if (e.getSource() == closeButton) {
			this.hideMe();
		}
	}

	/**
	 * returns Preferences
	 *
	 * @return myPrefs - Preferences
	 */
	public Preferences getPrefs() {
		return myPrefs;
	}

}
