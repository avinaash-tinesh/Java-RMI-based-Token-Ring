import java.rmi.*;
import java.net.*;
import java.net.UnknownHostException;

public class ringMemberImpl extends java.rmi.server.UnicastRemoteObject implements ringMember {

	private String next_node_host;
	private String next_node;
	private String this_node_host;
	private String this_node;
	private criticalSection critical;
	
	
	
	/**
	 * Constructor for ringMemberImpl
	 * @param t_node		current node id
	 * @param t_host		current node host address
	 * @param n_node		next node id
	 * @param n_host		next node host address
	 * @throws RemoteException
	 */
	public ringMemberImpl(String t_node, String t_host, String n_node, String n_host) throws RemoteException {

		this_node = t_node;
		this_node_host = t_host;
		next_node = n_node;
		next_node_host = n_host;
	}
	
	/* 
	 * Function that receives token from previous node
	 * @param token TokenObject
	 */
	public synchronized void takeToken(TokenObject token) {
	
		// start critical section by instantiating and starting criticalSection thread
		critical = new criticalSection(this_node, this_node_host, next_node, next_node_host, token);
		
		System.out.println("Entered method takeToken(): ringMemberImpl");
		critical.start();
		System.out.println("Exiting method takeToken(): ringMemberImpl");
			
	}

	/**
	 * Main method for ringMemberImpl
	 * @param argv 	Command line arguments
	 */
	public static void main(String argv[]) {
		System.setSecurityManager(new SecurityManager());
		if ((argv.length < 4) || (argv.length > 4)) {
			System.out.println("Usage: [this node][this node host][next node][next node host]");
			System.out.println("Only " + argv.length + " parameters entered");
			System.exit(1);
		}

		// get current node hostname, id and next node hostname, id and filename from command line args
		String current_node = argv[0];
		String current_host = argv[1];
		String next_node = argv[2];
		String next_host = argv[3];

		// get host name
		try {
			InetAddress inet_address = InetAddress.getLocalHost();
			String member_hostname = inet_address.getHostName();

			System.out.println("Ring member hostname: " + member_hostname);
			System.out.println("Ring member " + member_hostname + " binding to RMI Registry");

				// instantiate ringMemberImpl class with appropriate parameters
				ringMemberImpl server = new ringMemberImpl(current_node, current_host, next_node, next_host);
				// register object with RMI registry
				Naming.rebind("rmi://" + current_host + "/" + current_node, server);
				
				System.out.println("Ring element: " + member_hostname + "/" + current_node + " is bound with RMIregistry");
				
		} catch (UnknownHostException e) {
			System.out.println("Cannot resolve host: ");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.out.println("RMI related exception thrown: ");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Error in input URL: ");
			e.printStackTrace();
		}

	}

}