package de.labathome.filemanager;


public class MainClass {
	
	static FileManager theFileManager;
	
	public static void main(String args[]) {
		
		System.out.println("MainClass::main starting...");
		
		theFileManager = new FileManager("filemanager");
		
		System.out.println("MainClass::main finished!");
//		HelperFunctions.loadIcons();
//		FileFolderObject test = new FileFolderObject("/home/vroni/", 0);
//		CopyStatus status = new CopyStatus();
//		test.copyFileFolder(false, status, "/home/vroni/Downtest", "/home/vroni/Internet/Downtest");
	}
}
