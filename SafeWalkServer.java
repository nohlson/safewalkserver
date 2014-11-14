

import java.io.*;
import java.net.*;

public class SafeWalkServer extends Thread {

	private ServerSocket serverSocket;

	public SafeWalkServer(int port) throws SocketException, IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("Server is bound to port " + port);
		serverSocket.setReuseAddress(true);
		serverSocket.setSoTimeout(10000);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client...");
				Socket server = serverSocket.accept();
				System.out.println("Connected to " + server.getRemoteSocketAddress());
				DataInputStream in =
                  new DataInputStream(server.getInputStream());
	            System.out.println(in.readUTF());
	            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
                out.writeUTF("Welcome to the SafeWalkServer");
                server.close();

				

			} catch (SocketTimeoutException ste) {
				System.out.println("The socket timed out."); 
				break;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		int portNum = Integer.parseInt(args[0]);
		try {			
			Thread t = new SafeWalkServer(portNum);
			t.start();
		} catch (Exception se) {
			se.printStackTrace();
		}
	}
}