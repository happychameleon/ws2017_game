package server;

import java.net.Socket;

/**
 * This Class represents a client connected to the Server.
 *
 * Created by m on 3/20/17.
 */
public class User {
    private String name;

    /**
     * constructor for User class
     * @param name
     * @param socket
     */
    public User(String name, Socket socket){
        this.name = name;
        this.socket = socket;
    }

	/**
	 * @return The username. Can be null (if not logged in)!
	 */
	public String getName() {
        return name;
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
	private Socket socket;
    
    public Socket getSocket() {
        return socket;
    }
}
