// Wir hatten im Team zwei unterschiedl. Betriebssysteme; in dem einen funktioniert (nur) die Version:
// package bks_package;
// import bks_package.ParallelPart;
// import bks_package.Server;
package BKS_aufgabe.bks_package;
import BKS_aufgabe.bks_package.Server;
import BKS_aufgabe.bks_package.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ParallelPart implements Runnable {
	
	private Socket clientSocket;

	private Scanner clientInput;
	private PrintWriter serverOutput;

	private String directoryPath;
	
	private int clientNumber;
	
	public ParallelPart(Socket clientSocket, int clientNumber, String path) {
		
		this.clientSocket = clientSocket;
		this.clientNumber = clientNumber;
		this.directoryPath = path;
		
	}
	
	/**
	 * Mandatory part of the runnable interface.
	 * Takes client commands, execute it and returns the result.
	 */
	public void run() {

		try {
			
			this.putOnIO();
			
			System.out.println("Client " + this.clientNumber + " verbunden.");
	
			String input;
	
			while ((input = clientInput.nextLine()) != null) {
					
				String[] split = input.split("\\s");
					
				if (split[0].equalsIgnoreCase("QUIT")) {
						
					System.out.println("Client " + this.clientNumber + " trennt die Verbindung.");
					clientSocket.close();
					break;
						
				} else if (split[0].equalsIgnoreCase("LIST")) {
						
					System.out.println("Client " + this.clientNumber + " fragt nach Dateienliste.");
					serverOutput.println(this.listFiles());
						
				} else if (split[0].equalsIgnoreCase("GET") && split.length == 2) {
						
					System.out.println("Client " + this.clientNumber + " fragt nach Inhalt von Datei " + split[1].toString() + ".");
					serverOutput.println(this.getFile(split[1]));
						
				} else {
						
					serverOutput.println("Unbekannter Befehl.");
						
				}
	
				serverOutput.println("\u001a"); // End-of-file escape symbol
				
			}
				
		} catch (IOException e) {
			System.out.println("Es gab ein Problem mit den Dateien.");
		} catch (NoSuchElementException e) {
			System.out.println("Verbindung zum Client " + this.clientNumber + " verloren.");
		}
		
	}
	

	/**
	 * Initialize the writers and scanners for the communication with the server and the user
	 * @throws IOException If there is a problem with the socket
	 */
	public void putOnIO() throws IOException {
		
		this.clientInput = new Scanner(clientSocket.getInputStream()); // Used to get sent strings from client
		this.serverOutput = new PrintWriter(clientSocket.getOutputStream(), true); // Used to send strings to the client
		
	}
	
	
	/**
	 * Gets content of file
	 * @param filename Filename of the file to be outputed
	 * @return Content of given file
	 */
	public String getFile(String filename) {
		
		try {
			
			String completeFileName = directoryPath + "/" + filename;
			File myObj = new File(completeFileName);
			
			String data = "";
			
			Scanner myReader = new Scanner(myObj);
			
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				data = data + "\n" + line;
			}
			
			myReader.close();
			
			return data;
			
		} catch (FileNotFoundException e) {
			return "Datei nicht gefunden.";
		}
	}

	
	/**
	 * Lists all files in the directory path
	 * @return All files separated by "|"
	 */
	public String listFiles() {

		File folder = new File(this.directoryPath);
		
		folder.mkdir(); // Create directory if not exists

		StringBuilder fileNames = new StringBuilder();

		for (final File f : folder.listFiles())
			if (f.isFile())
				fileNames.append(f.getName() + "|"); // "|" functions as delimiter between the filenames

		if (fileNames.length() < 1)
			return "Keine Dateien im Verzeichnis.";
		else
			fileNames.setLength(fileNames.length() - 1); // Delete unnecessary last "|"

		return fileNames.toString();
	}
}
