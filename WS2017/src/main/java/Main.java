import client.Client;
import game.GameMap;
import game.engine.Weapon;
import server.Server;

/**
 * Reads in the command line arguments and Starts the whole application.
 * When specified in the command line arguments it starts either the server or the client.
 * If no arguments are present it starts the server on the default port.
 *
 * Created by flavia on 27.03.17.
 */
public class Main {
	
	public static void main(String[] args) {
		
		doStartMethods();
		
		if (args.length == 0) {
			// If no command arguments were entered we start the server with default port 1030.
			Server.serverMain(new String[] {"server"});
			// new StartMainDialog(); Doesn't work atm.
			
		} else {
			
			// To remove the ':' character to be able to enter the command line argument as supposed.
			for (int i = 0; i < args.length; i++) {
				if (args[i].contains(":"))
					args[i] = args[i].replace(":", "");
			}
			
			if (args[0].equals("server")) {
				Server.serverMain(args);
			} else if (args[0].equals("client")) {
				Client.clientMain(args);
			} else {
				System.err.println("Please start the application with one of the following arguments:\n" +
						"'server <port>'\n" +
						"'client <server_ip> <port> <username>'\n" +
						"'client <server_ip> <port>'\n" +
						"'client <port>'\t\t\t\t\t\t\tshortcut for 'client 127.0.0.1 <port>'\n" +
						"'client'\t\t\t\t\t\t\t\tshortcut for 'client 127.0.0.1 1030'\n" +
						"'server'\t\t\t\t\t\t\t\tshortcut for 'server 1030'\n" +
						"<empty>\t\t\t\t\t\t\t\t\tshortcut for 'server 1030'");
			}
		}
	}
	
	/**
	 * Calls all methods which need to run at the start of the application.
	 */
	private static void doStartMethods() {
		
		GameMap.readInAllMaps();
		Weapon.createWeaponPrototypes();
		
	}
	
}
