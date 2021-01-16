package BKS_aufgabe.bks_package;
import BKS_aufgabe.bks_package.ParallelPart;
import BKS_aufgabe.bks_package.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

	private PrintWriter outputClient;
	private Scanner inputServer;
	public static Scanner inputUser = new Scanner(System.in);

	private static String serverAddress = "localhost";
	private static int port = 50113;

	private Socket socket;

	public Client(String address, int port) throws UnknownHostException, IOException, NoSuchElementException{
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

			while (!serverOutput.equals("\u001a")) { 
				System.out.println(serverOutput);
				serverOutput = inputServer.nextLine();
			}
		}
	}

	public static void main(String[] args) {
		Scanner inputUser = new Scanner(System.in);
		while (true) {
			try {
				Client client = new Client(serverAddress, port);
				System.out.println("Konnte keine Verbindung herstellen.");
				System.out.print("Erneut verbinden? (y/N)");
				String answer = inputUser.nextLine();
				if ((answer.equalsIgnoreCase("n"))) {
					System.out.println("Client wird beendet.");
					break;
				} else if (!(answer.equalsIgnoreCase("y"))) {
					boolean correctAnswer = false;
					while (!correctAnswer) {
						System.out.println("Unbekannte Eingabe.");
						System.out.print("Erneut verbinden? (y/N)");
						answer = inputUser.nextLine();
						if ((answer.equalsIgnoreCase("n"))) {
							System.out.println("Client wird beendet.");
							break;
						} else if (answer.equalsIgnoreCase("y")) {
							correctAnswer = true;
						}
					}
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
			} catch (NoSuchElementException e) {
				System.out.println("Der Server hat die Verbindung unterbrochen.");
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
	}

	public static boolean changeCredentials() {
		System.out.print("Erneut mit anderen Parametern probieren? (y/N) ");
		String answer = inputUser.nextLine();
		if (answer.equals("y")) {
			System.out.print("Neue Hostaddresse (alte Addresse: " + serverAddress + "): ");
			serverAddress = inputUser.nextLine();
			System.out.print("Neuer Port (alter Port: " + port + "): ");
			port = Integer.parseInt(inputUser.nextLine());
			return true;
		} else if (answer.equalsIgnoreCase("n")) {
			System.out.println("Client wird beendet.");
			return false;
		} else {
			System.out.println("Unbekannte Eingabe.");
			return changeCredentials();
		}
	}

}
