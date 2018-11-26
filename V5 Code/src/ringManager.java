import java.rmi.*;
import java.util.Scanner;
import java.net.*;
import java.net.UnknownHostException;
import java.io.*;

public class ringManager {
	
	private String ring_node_address;
	private String ring_node;
	
	
	/**
	 * Constructor for ringManager
	 * @param ring_node_address		start node host address
	 * @param ring_node_id			start node id
	 */
	public ringManager(String ring_node_address, String ring_node_id) {
		System.setSecurityManager(new SecurityManager());
		
		this.ring_node_address = ring_node_address;
		this.ring_node = ring_node_id;
	}
	

	/**
	 * Function to initialize connection between nodes in the network
	 * @param file_name 	filename.extension for shared file 
	 * @param token     	TokenObject object
	 */
	public void initConnection(String file_name, TokenObject token)  {
		try {
			// create fileWriter and clear file
			FileWriter file_writer = new FileWriter(file_name, false);
			// close fileWriter
			file_writer.close();
		} catch (Exception e) {
			// Error message for file writing process
			System.out.println("Error in printing file: " + e );
		}
		
		//get host name 
		try {
			InetAddress inet_address = InetAddress.getLocalHost();
			String ring_manager_hostname = inet_address.getHostName();
			System.out.println("Ring manager host is: " + ring_manager_hostname);
			System.out.println("Ring element host is: " + ring_node);
		} catch (UnknownHostException e) {
			System.out.println("Cannot resolve host: ");
			e.printStackTrace();
		}
		
		System.out.println("Clearing record.txt file...");
				
		// get remote reference to ring element/node and inject token by calling
		// takeToken()
		try {
			ringMember ring_member = (ringMember) Naming.lookup("rmi://" +  ring_node_address + "/" + ring_node);
			System.out.println("Connecting to Node");
			ring_member.takeToken(token);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("An RMI related error has been thrown: ");
			e.printStackTrace();
		}
			
	}

	/**
	 * Main method for ringManager
	 * @param argv
	 */
	public static void main(String argv[]) {
		if((argv.length < 8 || argv.length > 8)) {
			System.out.println("Usage: [this node host][this node id][filename.extension][time to live][node to sleep][node to sleep host][node to skip][node to skip host]");
			System.out.println("Only " + argv.length + " parameters entered");
			System.exit(1);
		}
		
		String init_node_host = argv[0];
		String init_node_id = argv[1];
		String shared_filename = argv[2];
		int ttl = Integer.parseInt(argv[3]);
		String node_to_sleep = argv[4];
		String node_to_sleep_host = argv[5];
		String node_to_skip = argv[6];
		String node_to_skip_host = argv[7];
		
		//init TokenObject  
		TokenObject token = new TokenObject(node_to_sleep, node_to_sleep_host, ttl, shared_filename, init_node_id, init_node_host, node_to_skip, node_to_skip_host);
		// instantiate ringManager with parameters
		ringManager client = new ringManager(init_node_host, init_node_id);
		client.initConnection(shared_filename, token);

	}
}