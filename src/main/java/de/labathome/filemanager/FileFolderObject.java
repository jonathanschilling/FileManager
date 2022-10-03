package de.labathome.filemanager;

import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.*;
import java.util.LinkedList;
import java.nio.channels.FileChannel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Class which provides methods to manage files
 *
 * @author Jonathan Schilling & Veronika Polke
 *
 */
public class FileFolderObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * absolute path of file
	 */
	private String name = "";

	/**
	 * false if file, true if folder
	 */
	private boolean isDirectory = false;

	/**
	 * size of file or count of contained objects if folder
	 */
	private long size = 0;

	/**
	 * last change of file or folder
	 */
	private Date lastChange;

	private String owner = "";
	private boolean isExecutable = false;
	private String permissions = "";
	private boolean isHidden = false;

	/**
	 * contains contents of folder
	 */
	private LinkedList<FileFolderObject> contents = new LinkedList<FileFolderObject>();

	/**
	 * contains level
	 */
	private int level = 0;

	/**
	 * wether the ffo is selected by a mouse-dragged rectangle
	 */
	private boolean isSelected = false;

	/**
	 * determine wether to fill the contents list (if folder)
	 */
	private boolean isUnfolded = false;

	private boolean contentsLoaded = false;

	private File javaFile;

	private Rectangle iconRect;
	private Rectangle nameRect;
	private Rectangle arrowRect;

	CopyProgress prog;
	boolean showDialog = true;

	public Rectangle getIconRect() {
		return iconRect;
	}

	public void setIconRect(Rectangle r) {
		iconRect = r;
	}

	public Rectangle getNameRect() {
		return nameRect;
	}

	public void setNameRect(Rectangle r) {
		nameRect = r;
	}

	public Rectangle getArrowRect() {
		return arrowRect;
	}

	public void setArrowRect(Rectangle r) {
		arrowRect = r;
	}

	public boolean isDragged = false;

	/**
	 * constructor of FileFolderObject
	 *
	 * @param name  - path to directory
	 * @param level - level
	 */
	public FileFolderObject(String name, int level) {

		// file relevant info
		this.name = name;

		javaFile = new File(this.name);

		this.isDirectory = javaFile.isDirectory();

		if (this.isDirectory) {
			size = javaFile.listFiles().length;
		} else {
			size = javaFile.length();
		}

		this.lastChange = new Date(javaFile.lastModified());

		owner = javaFile.toString();

		isExecutable = javaFile.canExecute();

		permissions = "drwxrwxrwx";

		isHidden = javaFile.isHidden();

		// view relevant info
		this.level = level;
		this.isSelected = false;
		this.isUnfolded = false;
		this.isDragged = false;

	}

	public void loadContents() {
		if (isDirectory && !contentsLoaded) {

			contents.clear();

			File[] javaFileContents = javaFile.listFiles();

			if (javaFileContents != null) {
				for (int i = 0; i < javaFileContents.length; i++) {
					contents.add(new FileFolderObject(javaFileContents[i].getAbsolutePath(), level - 1));
				}
			}
			contentsLoaded = true;
		}
	}

	public void updateContents() {
		contentsLoaded = false;
		loadContents();
	}

	/**
	 * getter method for name
	 *
	 * @return name of file or folder
	 */
	public String getName() {
		return name;
	}

	/**
	 * getter method for isFileOrFolder
	 *
	 * @return if file or folder
	 */
	public boolean isDirectory() {
		return isDirectory;
	}

	/**
	 * getter method for size
	 *
	 * @return size of current file or folder
	 */
	public long getSize() {
		return size;
	}

	/**
	 * getter method for last change
	 *
	 * @return last change of file or folder
	 */
	public Date getLastChange() {
		return lastChange;
	}

	public String getOwner() {
		return owner;
	}

	/**
	 * getter method for isExecutable
	 *
	 * @return if file is executable
	 */
	public boolean isExecutable() {
		return isExecutable;
	}

	public String getPermissions() {
		return permissions;
	}

	public boolean isHidden() {
		return isHidden;
	}

	// getters/setters for view properties
	/**
	 * getter method for isSelected
	 *
	 * @return value of isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean select) {
		isSelected = select;
	}

	public boolean isUnfolded() {
		return isUnfolded;
	}

	public void setUnfolded(boolean unfolded) {
		isUnfolded = unfolded;
	}

	public int getLevel() {
		return level;
	}

	public boolean exists() {
		return javaFile.exists();
	}

	/**
	 * getter method for contents of folder
	 *
	 * @return linked list of contained files and folders
	 */
	public LinkedList<FileFolderObject> getContents() {
		return contents;
	}

	/**
	 * getter method for specific file or folder
	 *
	 * @param index - index of file or folder
	 * @return file or folder as FileFolderObject
	 */
	public FileFolderObject getContent(int index) {
		return contents.get(index);
	}

	/**
	 * toString method for current folder
	 */
	public String toString() {
		return this.name + " objects: " + Math.abs(this.size) + " Level: " + this.level + "\n";
	}

	/**
	 * method to get entry of current file or folder
	 *
	 * @return linked list of entries
	 */
	public LinkedList<String> getEntry() {

		LinkedList<String> result = new LinkedList<String>();

		String entryName = name.split("/")[name.split("/").length - 1];
		String entrySize = isDirectory ? String.valueOf(size) : HelperFunctions.getReadableSize(size);
		String entryOwner = owner;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		String entryLastChange = sdf.format(lastChange);
		String entryPermissions = permissions;

		result.addAll(Arrays.asList(entryName, entrySize, entryOwner, entryLastChange, entryPermissions));

		return result;
	}

	public String getLongestContentName() {
		String result = name;
		if (isDirectory) {

			if (!contentsLoaded) {
				loadContents();
			}
			for (int i = 0; i < contents.size(); i++) {
				if (contents.get(i).getName().length() > result.length()) {
					result = contents.get(i).getName();
				}
			}
		}
		return result;
	}

	public int getWidestContentWidth(Graphics g, int column) {
		int result = 0, temp = 0;
		if (isDirectory) {

			if (!contentsLoaded) {
				loadContents();
			}
			for (int i = 0; i <= contents.size(); i++) {
				if (i < contents.size()) {
					temp = (int) HelperFunctions.getStringRect(g, contents.get(i).getName()).getWidth();
				} else {
					temp = HelperFunctions.getStringRect(g, Preferences.columnsCaptions.get(column)).width;
				}
				if (temp > result) {
					result = temp;
				}
			}
		}
		return result;
	}

	/**
	 * getter of size for specific file or folder
	 *
	 * @param name - path to file or folder
	 * @return size of file or folder in bytes
	 */
	public static long getSize(String name) {
		long size = 0;
		File thisFile = new File(name);
		if (thisFile.isDirectory()) {
			File[] content = thisFile.listFiles();
			for (int i = 0; i < content.length; i++) {
				size += getSize(content[i].getAbsolutePath());
			}
		} else {
			size += thisFile.length();
		}
		return size;
	}

	public static boolean newFileFolder(boolean FileFolder, String name) {
		boolean success = true;
		if (FileFolder) {
			File newFolder = new File(name);
			try {
				newFolder.mkdir();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"<html><center>Could not create new folder.<br/>" + e.getMessage() + "</center></html>",
						"Error", JOptionPane.ERROR_MESSAGE);
				System.out.println(e.getMessage());
				success = false;
			}
		} else {
			File newFile = new File(name);
			try {
				newFile.createNewFile();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"<html><center>Could not create new file.<br/>" + e.getMessage() + "</center></html>", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.out.println(e.getMessage());
				success = false;
			}
		}
		return success;
	}

	/**
	 * deletes file or folder
	 *
	 * @param name - path to file or folder
	 * @return true if deleting succeeded
	 */
	public boolean delFileFolder(String name) {
		boolean success = true;
		File delFile = new File(name);

		try {
			if (delFile.isDirectory()) {
				File[] content = delFile.listFiles();
				for (int i = 0; i < content.length; i++) {
					delFileFolder(content[i].getAbsolutePath());
				}
			}
			delFile.delete();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"<html><center>Could not delete file or folder.<br/>" + e.getMessage() + "</center></html>",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
			;
			success = false;
		}
		return success;
	}

	/**
	 * moves file or folder
	 *
	 * @param name   - path of existing file or folder
	 * @param target - path to new file or folder
	 * @return true if moving succeeded
	 */
	public boolean moveFileFolder(String name, String target) {
		boolean success = true;
		// File moveFile = new File(name);
		// File targetFile = new File(target);
		try {

			// moveFile.renameTo(targetFile);
			CopyStatus status = new CopyStatus();
			copyFileFolder(false, status, name, target);
			delFileFolder(name);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"<html><center>Could move files.<br/>" + e.getMessage() + "</center></html>", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
			success = false;
		}
		return success;
	}

	/**
	 * copies file or folder
	 *
	 * @param rec    - true if method is called recursively
	 * @param status - CopyStatus to print status of copying
	 * @param name   - path to existing file or folder
	 * @param target - path to new file or folder
	 * @return true if copying suceeded
	 */
	public boolean copyFileFolder(final boolean rec, final CopyStatus status, final String name, final String target) {

		boolean success = true;

		try {
			Thread copy = new Thread(new Runnable() {
				public void run() {

					final File copyFile = new File(name);

					boolean isDir = copyFile.isDirectory();
					newFileFolder(isDir, target);

					File targetFile = new File(target);
					long completesize = getSize(name);
					long freespace = targetFile.getFreeSpace();

					if (freespace > completesize) {
						if (!rec) {
							System.out.println("Starting copying of " + completesize + " Bytes..");
							status.makeStatus(completesize);
							if (completesize < 1000 * 1000) {
								showDialog = false;
							}
							if (showDialog) {
								prog = new CopyProgress();
								prog.createComponents(new FileFolderObject(name, 0));
							}

						}
						if (isDir) {
							File[] content = copyFile.listFiles();
							for (int i = 0; i < content.length; i++) {
								copyFileFolder(true, status, name + File.separator + content[i].getName(),
										target + "/" + content[i].getName());
							}
						} else {
							try {
								FileInputStream inStream = new FileInputStream(copyFile);
								FileOutputStream outStream = new FileOutputStream(targetFile);

								FileChannel inChannel = inStream.getChannel();
								FileChannel outChannel = outStream.getChannel();
								inChannel.transferTo(0, inChannel.size(), outChannel);

								outStream.close();
								inStream.close();
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
							long thissize = 0;
							if (!copyFile.isDirectory()) {
								thissize = copyFile.length();
							}
							status.sendStatus(thissize);
							String stat = status.askStatus();
							if (stat != null) {
								System.out.println(stat);
								if (showDialog) {
									prog.updateProgressBar(stat);
								}
							}
						}
						if (!rec) {
							System.out.println("Done.");
							if (showDialog) {
								prog.killDialog();
							}
						}
					} else {
						 throw new RuntimeException("Not enough free space!");
					}

				}
			});
			copy.start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"<html><center>Could not copy files.<br/>" + e.getMessage() + "</center></html>", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
			success = false;
		}

		return success;
	}

	public boolean copyFile(LinkedList<FileFolderObject> source, FileFolderObject target) {
		boolean success;
		try {

			CopyStatus status = new CopyStatus();
			if (source.size() == 0) {

				// success = copyFileFolder(false, status, this.getName(), target.getName());

			} else {
				if (!target.isDirectory()) {

					if (source.size() == 1 && !source.get(0).isDirectory()) {

						success = copyFileFolder(false, status, source.get(0).getName(), target.getName());
					} else {
						throw (new Exception("Mission Impossible: cannot copy dir into file!"));
					}

				} else {

					if (source.size() == 1) {
						success = copyFileFolder(false, status, source.get(0).getName(), target.getName());
					} else {
						int conts = source.size();
						for (int i = 0; i < conts; i++) {
							success = copyFileFolder(false, status, source.get(i).getName(), target.getName());
						}
					}

				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"<html><center>Could not copy files.<br/>" + e.getMessage() + "</center></html>", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
			success = false;
		}
		success = false;
		return success;

	}
}
