package de.labathome.filemanager;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * central object for all relevant settings of the file manager;
 * also used to distribute info, p.ex. which folder is currently the "root" folder
 * for the file mananger
 */
public class Preferences implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileFolderObject currentDir;

	public HelperFunctions.ViewMode currentViewMode = HelperFunctions.ViewMode.SymbolView;

	public Color backgroundColor;
	public Color fontColor;
	public Color selectedColor;
	public Color unselectedColor;
	public Color captionColor;
	public Color selectRectColor;

	public static LinkedList<String> columnsCaptions;
	public int indexToSortBy;
	
	public boolean showHiddenFiles;

	public Preferences() {

		// set default values
		this.backgroundColor = Color.lightGray;
		this.fontColor = Color.black;
		this.selectedColor = Color.darkGray;
		this.unselectedColor = Color.gray;
		this.captionColor = Color.orange;
		this.selectRectColor = Color.red;
		this.showHiddenFiles = false;

		columnsCaptions = new LinkedList<String>();
		columnsCaptions.addAll(Arrays.asList("Name", "Size", "Owner", "Last Change", "Permissions"));
		this.indexToSortBy = 0;
	}
}
