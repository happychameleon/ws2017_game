import Engine.World;
import GraphicAndInput.Window;

import javax.swing.*;

/**
 * Created by flavia on 06.03.17.
 */
public class Main {
	
	static World world;
	
	static JFrame window;
	
	
	public static void main (String[] args) {
		
		world = new World(20, 10, 2);
		
		window = new Window(world, "WasserschlachtSimulator 2017 Prototype");
		
	}
	
}
