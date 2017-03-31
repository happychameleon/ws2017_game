package server;

import serverclient.User;

import java.net.Socket;

/**
 * This Class represents a client connected to the Server.
 *
 * Created by m on 3/20/17.
 */
public class ServerUser extends User {
	
    /**
     * Constructor for the ServerUser class
     * @param name the name of the user.
     * @param socket the Socket to the client.
     */
    public ServerUser(String name, Socket socket){
		super(name);
        this.socket = socket;
        
    }

	
	
	private Socket socket;
    
    public Socket getSocket() {
        return socket;
    }
    
}
