package de.labathome.filemanager;

import java.text.DecimalFormat;

/**
 * Class to save and return percental status of File Copy
 *
 * @author Jonathan Schilling & Veronika Polke
 *
 */
public class CopyStatus {

	/**
	 * Complete size of files in bytes
	 */
	public long completeSize;

	/**
	 * Already copied size in bytes
	 */
	public long currentSize;

	public DecimalFormat df = new DecimalFormat("0.00");

	/**
	 * method to save complete size of files
	 *
	 * @param size - size of files in bytes
	 */
	public void makeStatus(long size) {
		this.completeSize = size;
	}

	/**
	 * method to add size of current file to copy
	 *
	 * @param size - size of current file in bytes
	 */
	public void sendStatus(long size) {
		this.currentSize += size;
	}

	/**
	 * method to print current status
	 */
	public String askStatus() {

		double percent = ((double) this.currentSize / (double) this.completeSize) * (double) 100;
		String perCent = df.format(percent);
		String status = (int) percent + "#Copied " + HelperFunctions.getReadableSize(this.currentSize) + "/"
				+ HelperFunctions.getReadableSize(this.completeSize) + " " + perCent + "%";
		return status;
	}
}
