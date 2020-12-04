package bks_package;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientPrototype {

	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 50111); //Tries to connect to the server
			
			PrintWriter outputClient = new PrintWriter(socket.getOutputStream(), true); //Used to send strings to the server
			Scanner inputServer = new Scanner(socket.getInputStream()); //Used to get sended strings from server
			Scanner input = new Scanner(System.in);
			
			System.out.println("Zum Server verbunden.");
			
			while (true) {
				System.out.print("Eingabe: ");
				String userInput = input.nextLine();
				
				if (userInput.equalsIgnoreCase("QUIT")) {
					System.out.println("Trenne Verbindung");
					outputClient.println("QUIT");
					socket.close();
					input.close();
					break;
				} else {
					outputClient.println(userInput); //Sends command to server
				}
				
				
				String serverOutput = inputServer.nextLine();
				
				while(!serverOutput.equals("End")) { //Writes what the server sended to console; Problem: If in file is written "End" the client stopps transmitting the rest of the data
					System.out.println(serverOutput);
					serverOutput = inputServer.nextLine();
				}
			}
			
			
		} catch (UnknownHostException e) {
			System.out.println("Unbekannter Host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOFehler");
		}
	}
}
