package server;

import java.net.Socket;

/**
 * Created by m on 3/20/17.
 */
public class User {
    private String name;
	
	/**
	 * @return The username. Can be null!
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
    
    public User(String name, Socket socket){
        this.name = name;
        this.socket = socket;
    }

}
