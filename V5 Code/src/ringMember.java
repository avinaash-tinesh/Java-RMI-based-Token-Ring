
public interface ringMember extends java.rmi.Remote {

	/**
	 * @param token TokenObject object 
	 * @throws java.rmi.RemoteException
	 */
	public void takeToken(TokenObject token) throws java.rmi.RemoteException;

}
