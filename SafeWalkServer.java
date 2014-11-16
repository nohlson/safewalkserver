

import java.io.*;
import java.net.*;
import java.util.*;



public class SafeWalkServer extends Thread {

	private ServerSocket serverSocket;
	private ArrayList<String> names;
	private ArrayList<String> from;
	private ArrayList<String> to;
	private ArrayList<Integer> type;
	private ArrayList<Integer> options;
	private String[] parts;
	private int[] pair;
	


	public SafeWalkServer(int port) throws SocketException, IOException {
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> from = new ArrayList<String>();
		ArrayList<String> to = new ArrayList<String>();
		ArrayList<Integer> type = new ArrayList<Integer>();
		ArrayList<Integer> options = new ArrayList<Integer>();
		
		serverSocket = new ServerSocket(port);
		System.out.println("Server is bound to port " + port);
		serverSocket.setReuseAddress(true);
		serverSocket.setSoTimeout(30000);
	}

	public int getLocalPort() {
		return serverSocket.getLocalPort();
	}

	/* Public void analyzeRequests runs through all of the current requests
	and matches them to people leaving the same place and are either going
	to the same place or going to * location.
	*/

	public int[] analyzeRequests() {
		String holderFrom = "";
		String holderTo = "";
		int [] holder = new int[2];
		options.clear();
		for (int i = 0; i < from.size(); i++) {
			holderFrom = from.get(i);
			holderTo = to.get(i);
			for (int j = 0; j < from.size(); j++) {
				if (j != i) {
					if (from.get(j) == holderFrom) {
						options.add(j);
					}
				}
			}
			if (options.size() > 0) {
				for (int m = 0; m < options.size(); m++) {
					if (to.get(options.get(m)) == holderTo) {
						holder[0] = i;
						holder[1] = options.get(m);
						return holder;
					}
				}
			}

			
		}
		return holder;
	}

	public void run() {
		while (true) {
			try {
				String request = "";
				System.out.println("Waiting for client...");
				Socket server = serverSocket.accept();
				System.out.println("Connected to " + server.getRemoteSocketAddress());
				BufferedReader in =
		          new BufferedReader(new InputStreamReader(server.getInputStream()));
	            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
                out.writeUTF("Welcome to the SafeWalkServer");
                try {
	            	request = in.readLine();
	            	String[] parts = request.split(", ");
	            	names.add(parts[0]);
	            	from.add(parts[1]);
	            	to.add(parts[2]);
	            	type.add(Integer.parseInt(parts[3]));
	            } catch (IndexOutOfBoundsException ie) {
	            	if (request.charAt(0) == ':') {

	            	}
	            }



                out.writeUTF("\n");
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