package client;

/**
 * Represents one client.
 *
 * Created by flavia on 24.03.17.
 */
public class ClientUser {
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public ClientUser (String name) {
		this.name = name;
	}
	
	public ClientUser () {
	
	}
	
}
