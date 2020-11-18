package bks_package;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Server {
	
	public String directoryPath = "/home/mafalda/eclipse-workspace/BKS_aufgabe/bks_package/directoryPath";
	//public String directoryPath = "C:\\Users\\maxim\\OneDrive\\Dokumente\\GitHub\\bks-aufgabe1\\BKS_aufgabe\\bks_package\\directoryPath"; //Pfad bei Max
	
	public String getDirectoryPath() {
		return directoryPath;
	}
	
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	
	// Client says GET
	public String getFile(String filename) {
		try {
			String completeFileName = directoryPath + "/" + filename;
			System.out.println(completeFileName);
			String data = "";
		    File myObj = new File(completeFileName);
		    Scanner myReader = new Scanner(myObj);
		    while (myReader.hasNextLine()) {
		        String line = myReader.nextLine();
		        data = data + "\n" + line;
		      }
		    myReader.close();
			return data;
		    } 
		catch (FileNotFoundException e) {
		      return "File not found.";
		    } 
	}
	
	// Client says LIST
	public String listFiles() {

		File folder = new File(this.directoryPath);
		
		StringBuilder fileNames = new StringBuilder();
		
		for(final File f : folder.listFiles()) 
			if (f.isFile()) 
				fileNames.append(f.getName() + "|"); // "|" functions as delimiter between the filenames 
		
		if (fileNames.length() < 1)
			return "No files in directory";
		else
			fileNames.setLength(fileNames.length() - 1); // Delete unnecessary last "|"
		
		return fileNames.toString();
	}

}
