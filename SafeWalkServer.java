import java.io.*;
import java.net.*;

public class SafeWalkServer extends Thread {

	private ServerSocket serverSocket;

	public SafeWalkServer(int port) throws SocketException, IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server is bound to port " + port);
		serverSocket.setReuseAddress(true);
	}

	public SafeWalkServer() throws SocketException, IOException {
		ServerSocket serverSocket = new ServerSocket(0);
		System.out.println("Server is bound to port " + serverSocket.getLocalPort());
		serverSocket.setReuseAddress(true);

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
		int portNum = 0;
		try {
			if (args.length > 0) {
					portNum = Integer.parseInt(args[0]);
					Thread sws = new SafeWalkServer(portNum);
					sws.start();
			} else {	
				Thread sws = new SafeWalkServer();
				sws.start();
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (Exception se) {
			se.printStackTrace();
		}
	}
}