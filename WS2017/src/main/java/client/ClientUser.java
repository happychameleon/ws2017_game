package client;

import serverclient.User;

/**
 * Represents one client.
 *
 * Created by flavia on 24.03.17.
 */
public class ClientUser extends User {
	
	/**
	 * Creates a ClientUser with the given name
	 * @param name The new name
	 */
	public ClientUser (String name) {
		super(name);
	}
	
}
