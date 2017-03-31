package serverclient;

/**
 * The main class for users.
 *
 * Created by flavia on 31.03.17.
 */
public class User {
	
	private String name;
	
	/**
	 * @return The username. Can be null (if not logged in)!
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public User(String name) {
		this.name = name;
	}
}
