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

	private static Socket clientSocket;
	private static ServerSocket server;
	
	private static int port = 50113;

	public static void main(String[] args) {
		
		System.out.println("Server gestartet.");
		System.out.println("Warte auf Verbindungen...");
		
		int amountClients = 1;

		try {
			server = new ServerSocket(port);
			while (true) {
				clientSocket = server.accept();
				new Thread( new ParallelPart(clientSocket, amountClients)).start();
			}
		} catch (IOException e) {
			System.out.println("Es gab ein Problem beim Dateien lesen.");
		}
	}
}
