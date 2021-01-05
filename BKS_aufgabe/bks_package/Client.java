package bks_package;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private PrintWriter outputClient;
	private Scanner inputServer;
	public static Scanner inputUser = new Scanner(System.in);

	private static String serverAddress = "localhost";
	private static int port = 50113;

	private Socket socket;

	public Client(String address, int port) throws UnknownHostException, IOException {
		this.connectToServer(address, port);

		this.putOnIO();

		System.out.println("Zum Server verbunden.");

		while (true) {
			System.out.print("Befehl: ");
			String command = this.inputUser.nextLine();

			if (command.equalsIgnoreCase("QUIT")) {
				this.quitConnection();
				break;
			} else {
				this.outputClient.println(command); // Sends command to server
			}

			String serverOutput = inputServer.nextLine();

			while (!serverOutput.equals("End")) { // Writes what the server sended to console; Problem: If in file is
													// written "End" the client stopps transmitting the rest of the data
				System.out.println(serverOutput);
				serverOutput = inputServer.nextLine();
			}
		}
	}

	public static void main(String[] args) {
		while (true) {
			try {
				Client client = new Client(serverAddress, port);
				System.out.println("Konnte keine Verbindung herstellen.");
				System.out.print("Erneut verbinden? (y/N)");
				Scanner inputUser = new Scanner(System.in);
				String answer = inputUser.nextLine();
				inputUser.close();
				if (!(answer.equals("y"))) {
					System.out.println("Client wird beendet.");
					break;
				}
			} catch (UnknownHostException e) {
				System.out.println("Der Host ist unbekannt.");
				if (!changeCredentials()) {
					break;
				}

			} catch (IOException e) {
				System.out.println("Konnte keine Verbindung herstellen.");
				if (!changeCredentials()) {
					break;
				}
			}
		}
		inputUser.close();
	}

	public void connectToServer(String serverAddress, int Port) throws UnknownHostException, IOException {
		this.socket = new Socket(serverAddress, Port);
	}

	public void putOnIO() throws IOException {
		this.outputClient = new PrintWriter(this.socket.getOutputStream(), true);
		this.inputServer = new Scanner(this.socket.getInputStream());
		this.inputUser = new Scanner(System.in);
	}

	public void quitConnection() throws IOException {
		System.out.println("Trenne Verbindung");
		outputClient.println("QUIT");
		this.socket.close();
		this.inputUser.close();
	}

	public static boolean changeCredentials() {
		String answer = "N";
		System.out.print("Erneut mit anderen Parametern probieren? (y/N) ");
		answer = inputUser.nextLine();
		if (answer.equals("y")) {
			System.out.print("Neue Hostaddresse (alte Addresse: " + serverAddress + "): ");
			serverAddress = inputUser.nextLine();
			System.out.print("Neuer Port (alter Port: " + port + "): ");
			port = Integer.parseInt(inputUser.nextLine());
			return true;
		} else {
			System.out.println("Client wird beendet.");
			return false;
		}
	}

}
