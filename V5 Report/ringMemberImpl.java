import java.rmi.*;
import java.net.*;
import java.net.UnknownHostException;

public class ringMemberImpl extends java.rmi.server.UnicastRemoteObject implements ringMember {

	private String next_id;
	private String next_host;
	private String this_id;
	private String this_host;
	private criticalSection critical;
	
	
	
	/**
	 * Constructor for ringMemberImpl
	 * @param t_node	current node id
	 * @param t_add		current node address
	 * @param n_node	next node id
	 * @param n_add		next node address
	 * @throws RemoteException
	 */
	public ringMemberImpl(String t_node, String t_add, String n_node, String n_add) throws RemoteException {

		this_host = t_node;
		this_id = t_add;
		next_host = n_node;
		next_id = n_add;
	}
	
	/* 
	 * Function that receives token from previous node
	 * @param token TokenObject
	 */
	public synchronized void takeToken(TokenObject token) {
	
		// start critical section by instantiating and starting criticalSection thread
		critical = new criticalSection(this_host, this_id, next_host, next_id, token);
		
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
			System.out.println("Usage: [this host][this id][next host][next id]");
			System.out.println("Only " + argv.length + " parameters entered");
			System.exit(1);
		}

		// get current node hostname, id and next node hostname, id and filename from command line args
		String current_node = argv[0];
		String current_add = argv[1];
		String next_node = argv[2];
		String next_add = argv[3];

		// get host name
		try {
			InetAddress inet_address = InetAddress.getLocalHost();
			String member_hostname = inet_address.getHostName();

			System.out.println("Ring member hostname: " + member_hostname);
			System.out.println("Ring member " + member_hostname + " binding to RMI Registry");

				// instantiate ringMemberImpl class with appropriate parameters
				ringMemberImpl server = new ringMemberImpl(current_node, current_add, next_node, next_add);
				// register object with RMI registry
				Naming.rebind("rmi://" + current_add + "/" + current_node, server);
				
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