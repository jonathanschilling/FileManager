package de.labathome.filemanager;



import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * Class to show copy progress dialog
 * @author Jonathan Schilling & Veronika Polke
 *
 */

public class CopyProgress extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	private JFrame progressFrame;
	private Container pane;
	private JProgressBar progressBar;
	private int iterations = 100;
	
	private JLabel progressText;
	private ProgressAnimation animated;
	
	/**
	 * creates the dialog
	 * @param ffo - FileFolderObject to generate icons in animation
	 */
	
	public void createComponents (final FileFolderObject ffo) {
		
	      SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	          //Create all components
	          progressFrame = new JFrame("Copying.. 0%");
	          progressFrame.setSize(300, 150);
	          progressText = new JLabel("Copied 0 Bytes/0 Bytes 0%");
	          Thread ani = new Thread (new Runnable() {
	        	  public void run() {
	        		  
	          animated = new ProgressAnimation (ffo);
	          pane = progressFrame.getContentPane();
	          pane.setLayout(new FlowLayout());
	          progressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	          progressBar = new JProgressBar(0, iterations);
	          //Add components to pane
	          pane.add(animated);
	          pane.add(progressBar);
	          pane.add(progressText);

	          //Make frame visible
	          progressFrame.setVisible(true);
	        	  }
	          });
	          ani.start();
	       }
	      });
		

	    }
	
	/**
	 * updates Progress Bar with percentage and copy status
	 * @param stat - String that contains current percentage and copy status as connected String
	 */
	     public void updateProgressBar(final String stat) {
	           SwingUtilities.invokeLater(new Runnable() {
	                public void run() {
	                   String[] stats;
	                   stats = stat.split("\\#");
	                   int i = Integer.parseInt(stats[0]);
	                   progressBar.setValue(i);
	                   progressText.setText(stats[1]);
	                   progressFrame.setTitle("Copying.. " + stats[0] + "%");
	                   
	                   if (i == 100) {
	                	   killDialog();
	                   }
	                   
	                }
	           });
	    }
	     
	     /**
	      * kills the dialog
	      */

	    public void killDialog() {
	           SwingUtilities.invokeLater(new Runnable() {
	                public void run() {
	                    progressFrame.setVisible(false);
	                }
	            });
	    } 
	
}