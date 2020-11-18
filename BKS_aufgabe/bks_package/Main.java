package bks_package;

public class Main {
	public static void main(String[] args) {
		Main main = new Main();
		String data = main.readClientRequest("LIST"); // to be replaced with real client request
		System.out.println(data);
	}
	
	public String readClientRequest(String request) {
		String[] requestArray = request.split(" ");
		String firstPart = requestArray[0];
		System.out.println(firstPart); // only for testing
		String filename = "";
		if(requestArray.length > 1) {
			filename = requestArray[1];
			System.out.println(filename); // only for testing
		}
		// Client says GET
		if(firstPart.equals("GET")) {
			Server server = new Server();
			String data = "Requested file content: \n" + server.getFile(filename);
			return data;
		}
		// Client says LIST
		if(firstPart.equals("LIST")) {
			Server server = new Server();
			String data = "Files in directory: \n" + server.listFiles();
			return data;
		}
		// Client says CLOSE
		if(firstPart.equals("CLOSE")) {
			return ""; // to be filled with reference to Socket function
		}
		else {
			return "no valid request";
		}
	}
}
