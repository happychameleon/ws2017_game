import Engine.World;
import Graphics.Window;

import javax.swing.*;

/**
 * Created by flavia on 06.03.17.
 */
public class Main {
	
	static World world;
	
	static JFrame window;
	
	
	public static void main (String[] args) {
		
		world = new World(20, 10);
		
		window = new Window(world);
		
	}
	
}
