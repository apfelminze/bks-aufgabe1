package bks_package;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerPrototype {
	
	
	public static void main(String[] args) {
		
		final ServerSocket server;
		
		try {
			server = new ServerSocket(50111); //Starts the server
			System.out.println("Server gestartet.");
			System.out.println("Warte auf Verbindungen...");
			Server serverFunctions = new Server(); //For the list and get functions
			
			while(true) {
				Socket clientSocket = server.accept(); //Accepts the client connection
				System.out.println("Client verbunden.");
				Scanner clientInput = new Scanner(clientSocket.getInputStream()); //Used to get sended strings from client
				PrintWriter serverOutput = new PrintWriter(clientSocket.getOutputStream(), true); //Used to send strings to the client
				
				String input;
				
				while((input = clientInput.nextLine()) != null) {
					String[] split = input.split("\\s");
					if(split[0].equalsIgnoreCase("QUIT")) {
						System.out.println("Client quitted");
						clientSocket.close();
						break;
					} else if (split[0].equalsIgnoreCase("LIST")) {
						System.out.println("Client fragt nach Dateienliste.");
						serverOutput.println(serverFunctions.listFiles());
					} else if (split[0].equalsIgnoreCase("GET") && split.length == 2) {
						System.out.println("Client fragt nach Inhalt von Datei "+ split[1].toString() + ".");
						serverOutput.println(serverFunctions.getFile(split[1]));
					}

					serverOutput.println("End");
				}
			}
		}  catch (IOException e) {
			System.out.println("IOFehler");
		} catch (NoSuchElementException e) {
			System.out.println("Anderer Fehler");
		}

	}
}
