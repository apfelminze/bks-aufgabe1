package bks_package;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server {

	private ServerSocket server;
	private Socket clientSocket;

	private Scanner clientInput;
	private PrintWriter serverOutput;

	private String directoryPath;
	
	private int port = 50113;

	public Server() throws IOException {

		this.directoryPath = System.getProperty("user.dir") + "\\Files\\";

		this.startServerSocket();

		System.out.println("Server gestartet.");
		System.out.println("Warte auf Verbindungen...");

		while (true) {
			this.clientSocket = server.accept(); // Accepts the client connection

			this.putOnIO();

			System.out.println("Client verbunden.");

			String input;

			while ((input = clientInput.nextLine()) != null) {
				String[] split = input.split("\\s");
				if (split[0].equalsIgnoreCase("QUIT")) {
					System.out.println("Client trennt die Verbindung.");
					clientSocket.close();
					break;
				} else if (split[0].equalsIgnoreCase("LIST")) {
					System.out.println("Client fragt nach Dateienliste.");
					serverOutput.println(this.listFiles());
				} else if (split[0].equalsIgnoreCase("GET") && split.length == 2) {
					System.out.println("Client fragt nach Inhalt von Datei " + split[1].toString() + ".");
					serverOutput.println(this.getFile(split[1]));
				}

				serverOutput.println("End");
			}
		}
	}

	public static void main(String[] args) {
		try {
			Server server = new Server();
		} catch (IOException e) {
			System.out.println("Es gab ein Problem beim Dateien lesen.");
		}
	}

	public void startServerSocket() throws IOException {
		server = new ServerSocket(port); // Starts the server
	}

	public void putOnIO() throws IOException {
		this.clientInput = new Scanner(clientSocket.getInputStream()); // Used to get sended strings from client
		this.serverOutput = new PrintWriter(clientSocket.getOutputStream(), true); // Used to send strings to the client
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
		} catch (FileNotFoundException e) {
			return "Datei nicht gefunden.";
		}
	}

	// Client says LIST
	public String listFiles() {

		File folder = new File(this.directoryPath);

		StringBuilder fileNames = new StringBuilder();

		for (final File f : folder.listFiles())
			if (f.isFile())
				fileNames.append(f.getName() + "|"); // "|" functions as delimiter between the filenames

		if (fileNames.length() < 1)
			return "Fehlendes Verzeichnis.";
		else
			fileNames.setLength(fileNames.length() - 1); // Delete unnecessary last "|"

		return fileNames.toString();
	}
}
