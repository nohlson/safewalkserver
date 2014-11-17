

import java.io.*;
import java.net.*;
import java.util.*;



public class SafeWalkServer extends Thread {

	//sets the scope of these objects to the entire class
	private ServerSocket serverSocket;
	private ArrayList<String> names;
	private ArrayList<String> from;
	private ArrayList<String> to;
	private ArrayList<Integer> type;
	private ArrayList<Integer> options;
	private String[] parts;
	private int[] pair;
	

	/*public SafeWalkServer(int port) constructor takes an int arguement
	for the port number to bind the server to.
	*/
	public SafeWalkServer(int port) throws SocketException, IOException {
		//sets up the array lists for the pending entries in the server
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> from = new ArrayList<String>();
		ArrayList<String> to = new ArrayList<String>();
		ArrayList<Integer> type = new ArrayList<Integer>();
		ArrayList<Integer> options = new ArrayList<Integer>();
		
		serverSocket = new ServerSocket(port); //creates a new ServerSocket object at port port
		System.out.println("Server is bound to port " + port);
		serverSocket.setReuseAddress(true);
		serverSocket.setSoTimeout(30000);
	}

	public int getLocalPort() {
		return serverSocket.getLocalPort();
	}

	/* Public void analyzeRequests runs through all of the current requests
	and matches them to people leaving the same place and are either going
	to the same place or going to * location. it returns an int[] that has two fields
	that are two int that are the indexes that match. analyze Requests will return the 
	first match and only the first match.
	*/

	public int[] analyzeRequests() {
		String holderFrom = "";
		String holderTo = "";
		int [] holder = new int[2];
		options.clear();

		/*iterate through the requests already in the server database
		and first checks to see if another request is departing the
		same location
		*/
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
			/* After finding matches for requests with similar 
			departing location, analyzeRequests
			will test the requests for requests that match arrival location
			or * location.
			*/
			if (options.size() > 0) {
				for (int m = 0; m < options.size(); m++) {
					if (to.get(options.get(m)) == holderTo || to.get(options.get(m)) == "*") {
						holder[0] = i;               //sets the first field to first sampled index
						holder[1] = options.get(m);  //sets the second field to first match found
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
				//set up new server with serverSocket.accept()
				String request = "";
				System.out.println("Waiting for client...");
				Socket server = serverSocket.accept();
				System.out.println("Connected to " + server.getRemoteSocketAddress());

				/*takes the input and output stream of the server and creates a 
				buffered reader and a data output stream to manage the input
				and output of the server
				*/
				BufferedReader in =
		          new BufferedReader(new InputStreamReader(server.getInputStream()));
	            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());

                //new server has been created
                out.writeUTF("Welcome to the SafeWalkServer");
                try {
	            	request = in.readLine(); //takes the request
	            	String[] parts = request.split(", "); //splits the request into parts
	            	names.add(parts[0]); //the first entry in the request is name
	            	from.add(parts[1]); //the second entry in the request is from
	            	to.add(parts[2]); //the third entry in the request is destination
	            	type.add(Integer.parseInt(parts[3])); //the fourth entry in the request is priority
	            } catch (IndexOutOfBoundsException ie) { //assuming the request is actually a server command
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