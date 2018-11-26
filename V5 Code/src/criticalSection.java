import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.ConnectException;
import java.rmi.UnknownHostException;
import java.util.*;

/**
 * @author ast
 * 
 * Critical section within executing Thread
 *
 */
public class criticalSection extends Thread {

	private String this_node_address;
	private String this_node;
	private String next_node_address;
	private String next_node;

	private TokenObject token;

	/**
	 * Constructor for critical section
	 * @param t_node		current node id
	 * @param t_node_add	current node address
	 * @param n_node		next node id
	 * @param n_node_add	next node address
	 * @param t				TokenObject object
	 */
	public criticalSection(String t_node, String t_node_add, String n_node, String n_node_add, TokenObject t) {

		this_node = t_node;
		this_node_address = t_node_add;
		next_node = n_node;
		next_node_address = n_node_add;
		token = t;
	}

	/*
	 * Initializes Thread for running of critical section
	 */
	public void run() {
		// entering critical section
		try {
			
			System.out.println("Writing to file: record.txt...");

			// init timestamp
			Date time = new Date();
			String timestamp = time.toString();

			token.reduceTimeLive();
			
			// increment cycles in TokenObject
			if (token.getInitNode().equals(this_node) && token.getInitNodeHost().equals(this_node_address))
				token.incrementCycles();
			
			// if getSkipNode() returns true, pass the token without
			// incrementing,
			// else continue as normal
			if(token.getCycles() % 2 == 0 && (token.getSkipNode().equals(this_node) && token.getSkipNodeHost().equals(this_node_address))) {
				System.out.println("Token on cycle: " + token.getCycles() + "\n");
			} else {
				// increment counter in TokenObject
				token.incrementCounter();
				
				// write timestamp (date) to file
				FileWriter file_writer = new FileWriter(token.getFileName(), true);

				// init PrintWriter to write to the file
				PrintWriter print_writer = new PrintWriter(file_writer, true);
				print_writer.println("Record from ring node on host: " + this_node_address + ", node: " + this_node
						+ ", is: " + timestamp + ", token count: " + token.getCount());

				print_writer.close();
				file_writer.close();

				System.out.println("Looking up RMIRegistry with rmi://" + next_node_address + "/" + next_node);

				System.out.println("Received token, count value is: " + token.getCount());

				// sleep to symbolise critical section duration
				System.out.println("Taking a nap...\n");

				if (token.getNodeToModify().equals(this_node) && token.getNodeToModifyHost().equals(this_node_address)) {
					System.out.println("Extra sleep of: " + (token.getTimeToSleep() / 1000) + " seconds");
					sleep(token.getTimeToSleep());
				}
				sleep(2000);

				System.out.println("Token released, exiting critical region\n");

				// Print dashed lines for ease of reading
				System.out.print("---------------------------------------------------\n");
			}

			if (token.getTimeToLive() == 0) {
				System.out.println("Max passes reached, all remaining nodes waiting...");
				return;
			}
			
			// get remote reference to next ring element, and pass token on
			// ...
			ringMember next_ring_element = (ringMember) Naming.lookup("rmi://" + next_node_address + "/" + next_node);
			next_ring_element.takeToken(token);

		} catch (MalformedURLException e) {
			System.out.println("Error in input URL: ");
			e.printStackTrace();
		} catch (RemoteException e) {
			 System.out.println("RMI related exception thrown: ");
			 e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Sleep error: ");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in file writing: ");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Server not bound error: ");
			e.printStackTrace();
		}

	}
}
