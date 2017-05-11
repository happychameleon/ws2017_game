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
			// Server.serverMain(new String[] {"server"});
			new StartMainDialog(); //Doesn't work atm.
			
		} else {
			
			
			try {
				if (args[0].equals("server")) {
					Server.serverMain(args);
				} else if (args[0].equals("client")) {
					
					if (args.length > 1 && args[1].contains(":")) {
						String[] argsCopy = new String[args.length + 1];
						argsCopy[0] = args[0];
						argsCopy[1] = args[1].substring(0, args[1].indexOf(":"));
						argsCopy[2] = args[1].substring(args[1].indexOf(":") + 1);
						for (int i = 2; i < args.length; i++) {
							argsCopy[i + 1] = args[i];
						}
						args = argsCopy;
					}
					
					Client.clientMain(args);
				} else {
					printCommandLineParameters();
				}
			} catch (Exception e) {
				printCommandLineParameters();
			}
		}
	}
	
	private static void printCommandLineParameters() {
		System.out.println("Please start the application with one of the following arguments:\n" +
				"'server <port>'\n" +
				"'client <server_ip> <port> <username>'\n" +
				"'client <server_ip> <port>'\n" +
				"'client <server_ip>:<port>'\n" +
				"'client <port>'\t - shortcut for 'client 127.0.0.1 <port>'\n" +
				"'client'\t - shortcut for 'client 127.0.0.1 1030'\n" +
				"'server'\t - shortcut for 'server 1030'\n" +
				"<empty>\t - shortcut for 'server 1030'");
	}
	
	
	/**
	 * Calls all methods which need to run at the start of the application.
	 */
	private static void doStartMethods() {
		
		GameMap.readInAllMaps();
		Weapon.createWeaponPrototypes();
		
	}
	
}
