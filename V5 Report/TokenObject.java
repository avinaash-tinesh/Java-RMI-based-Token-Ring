import java.io.Serializable;

/**
 * @author ast
 *  
 *  Token Object to serve as token passed through ring network,
 *  also serves as an object that stores and passes on variables such as, 
 *  filename, time to live, node for extra sleep and its host, initial node and its host
 *  and node to skip and its host to each node
 *  eliminating the need to store each of these variables locally in the ringMemberImpl class
 *  
 */
public class TokenObject implements Serializable {

	private int count; // variable holding number of times it has entered a critical section
	private String node_to_modify; // variable holding node id of node to give extra sleep time
	private String node_to_modify_host; // variable holding host of node to give extra sleep
	private int time_to_live; // variable holding max number of passes set for token
	private String file_name; // variable holding file name from user input
	private int sleep_time = 3000; // variable holding preset extra sleep time
	private int num_cycles; // variable holding number of cycles taken by token through the network
	private String init_node; // variable holding the initial node id
	private String init_node_host; // variable holding the initial node host
	private String skip_node; // variable holding the skip node id
	private String skip_node_host; // variable holding the skip node host


	/**
	 * Constructor for TokenObject
	 * @param node				node id for sleep modification
	 * @param time_to_live		max number of passes for token in network 
	 * @param file_name			filename.extension of shared file
	 * @param token_exit		boolean dictating if the node is to be shutdown
	 */
	public TokenObject(String node, String node_host, int time_to_live, String file_name, String init_node, String init_node_host, String skip_node, String skip_node_host) {
		this.node_to_modify = node;
		this.node_to_modify_host = node_host;
		this.time_to_live = time_to_live;
		this.file_name = file_name;
		this.count = 0;
		this.num_cycles = 0;
		this.init_node = init_node;
		this.init_node_host = init_node_host;
		this.skip_node = skip_node;
		this.skip_node_host = skip_node_host;
	}

	/**
	 * Function that increments the counter by one each time it enters a critical section
	 */
	public void incrementCounter() {
		this.count++;
	}

	/**
	 * Function that returns count variable
	 * @return count 
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * Function that returns the token's time to live variable
	 * @return time_to_live 
	 */
	public int getTimeToLive() {
		return this.time_to_live;
	}
	
	
	/**
	 * Reduces time to live variable by one each time it enters a critical section
	 */
	public void reduceTimeLive() {
		time_to_live--;
	}

	/**
	 * Function that returns node id for extra sleep
	 * @return node_to_modify 
	 */
	public String getNodeToModify() {
		return this.node_to_modify;
	}
	
	/**
	 * Function that returns host of node for extra sleep 
	 * @return node_to_modify_host
	 */
	public String getNodeToModifyHost() {
		return this.node_to_modify_host;
	}

	/**
	 * Function that returns sleep_time
	 * @return sleep_time
	 */
	public int getTimeToSleep() {
		return this.sleep_time;
	}
	
	/**
	 * Function that returns the node to skip's id
	 * @return skip_node
	 */
	public String getSkipNode() {
		return this.skip_node;
	}
	
	/**
	 * Function that returns the node to skip's host
	 * @return skip_node_host
	 */
	public String getSkipNodeHost() {
		return this.skip_node_host;
	}
	
	/**
	 * Function that returns the initial node's id
	 * @return init_node
	 */
	public String getInitNode() {
		return this.init_node;
	}
	
	/**
	 * Function that returns the initial node's host 
	 * @return init_node_host
	 */
	public String getInitNodeHost() {
		return this.init_node_host;
	}
	
	/**
	 *  Function that increments the number of cycles token has taken through the network
	 */
	public void incrementCycles() {
		num_cycles++;
	}
	
	/**
	 * Function that returns the number of cycles the token has taken through the network
	 * @return num_cycles
	 */
	public int getCycles() {
		return num_cycles;
	}
	
	/**
	 * Function that returns filename for nodes to write to
	 * @return file_name
	 */
	public String getFileName() {
		return this.file_name;
	}

}
