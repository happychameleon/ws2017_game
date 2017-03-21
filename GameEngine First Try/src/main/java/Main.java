

import Engine.World;
import GraphicAndInput.Window;

/**
 * Created by flavia on 06.03.17.
 */
public class Main {
	
	static World world;
	
	static Window window;
	
	
	public static void main (String[] args) {
		
		world = new World(30, 25, 2);
		
		window = new Window(world, "WasserschlachtSimulator 2017 Prototype");
		
	}
	
	
}
