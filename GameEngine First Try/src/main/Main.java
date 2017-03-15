package main;

import main.Engine.World;
import main.GraphicAndInput.Window;

import javax.swing.*;

/**
 * Created by flavia on 06.03.17.
 */
public class Main {
	
	static World world;
	
	static Window window;
	
	
	public static void main (String[] args) {
		
		world = new World(20, 10, 2);
		
		window = new Window(world, "WasserschlachtSimulator 2017 Prototype");
		
	}
	
	
}
