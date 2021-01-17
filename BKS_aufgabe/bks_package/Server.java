package BKS_aufgabe.bks_package;
import BKS_aufgabe.bks_package.ParallelPart;
import BKS_aufgabe.bks_package.Client;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	private static Socket clientSocket;
	private static ServerSocket server;
	
	private static int port = 50113;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Server gestartet.");
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Bitte geben Sie den Pfad des Verzeichnisses an, auf das der Server zugreifen soll:");
		String path = scanner.nextLine(); 
		
		while(!(new File(path).exists())) { // Check for valid path
			
			System.out.println("Ungültiger Pfad, bitte neuen angeben:");
			path = scanner.nextLine();
			
		}
		
		scanner.close();
		System.out.println("Warte auf Verbindungen...");
		
		int amountClients = 1;

		try {
			server = new ServerSocket(port);
			while (true) {
				clientSocket = server.accept();
				new Thread(new ParallelPart(clientSocket, amountClients, path)).start();
				amountClients++;
			}
		} catch (IOException e) {
			System.out.println("Es gab ein Problem beim Dateien lesen.");
		}
	}
	
}
