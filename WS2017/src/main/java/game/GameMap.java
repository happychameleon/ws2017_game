package game;

import java.io.*;
import java.util.ArrayList;

/**
 * Holds the Data for preconstructed Maps read in from files.
 *
 * TODO? For future Milestones if there is enough time add special objects and powerups to maps.
 *
 * Created by flavia on 12.04.17.
 */
public class GameMap {
	
	//region Data
	/**
	 * The unique name of the map.
	 * Uniqueness is provided by the operating system, since the name is taken from the file name and all the files must be in the same folder.
	 */
	private String name;
	
	/**
	 * @return {@link #name}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The Tiles represented by chars.
	 * The coordinates are stored as [y][x].
	 */
	private char[][] tiles;
	
	/**
	 * @return A shallow copy of {@link #tiles}.
	 */
	public char[][] getTilesAsChars() {
		return tiles.clone();
	}
	
	/**
	 * @return The width of the map in tiles.
	 */
	public int getWidth() {
		return tiles[0].length;
	}
	
	/**
	 * @return The height of the map in tiles.
	 */
	public int getHeight() {
		return tiles.length;
	}
	//endregion
	
	
	/**
	 * Constructs the map read in from the file.
	 * @param name {@link #name}.
	 * @param tiles {@link #tiles}.
	 */
	public GameMap(String name, char[][] tiles) {
		this.name = name;
		this.tiles = tiles;
		
	}
	
	//region Methods
	
	/**
	 * @return {@link #name}.
	 */
	@Override
	public String toString() {
		return name;
	}
	
	//endregion
	
	
	//region static Data and Methods
	/**
	 * All the maps stored with their names and the charArray representing the TileTypes.
	 */
	private static ArrayList<GameMap> gameMaps;
	
	/**
	 * @return A shallow copy of {@link #gameMaps}.
	 */
	public static ArrayList<GameMap> getAllMaps() {
		return (ArrayList<GameMap>) gameMaps.clone();
	}
	
	/**
	 * Gets the map with the same name as this map.
	 * @param name the name of the map to get.
	 * @return the map with the name or <code>null</code> if the given name doesn't exist.
	 */
	public static GameMap getMapForName(String name) {
		for (GameMap gameMap : gameMaps) {
			if (gameMap.getName().equals(name))
				return gameMap;
		}
		return null;
	}
	
	/**
	 * The folder containing all the map Files.
	 */
	private static final File mapsFolder = new File("src/main/resources/maps");
	
	/**
	 * Reads in all the gameMaps and stores them in {@link #gameMaps} at the start of the game.
	 */
	public static void readInAllMaps() {
		gameMaps = new ArrayList<>();
		
		File[] allMapFiles = mapsFolder.listFiles();
		for (File mapFile : allMapFiles) {
			if (mapFile.isFile() == false || mapFile.getName().endsWith(".txt") == false)
				continue;
			// First get the name of the map (== the file name)
			String mapName = mapFile.getName();
			if (mapName.contains(".")) {
				mapName = mapName.substring(0, mapName.indexOf("."));
			}
			
			// Now read in the map file's data.
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(mapFile));
				// Stores all the lines of the file in the Array List.
				ArrayList<String> lines = new ArrayList<>();
				String nextLine = bufferedReader.readLine();
				while (nextLine != null) {
					lines.add(nextLine);
					nextLine = bufferedReader.readLine();
				}
				// Store the lines as char[][] and throws an error when they aren't all the same size.
				int lineSize = lines.get(0).length();
				char[][] mapChars = new char[lines.size()][];
				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i);
					if (line.length() != lineSize)
						System.err.println("Map wrongly formated (lines different length): " + mapFile.getName());
					mapChars[i] = line.toCharArray();
				}
				
				// Adds the read in map to the stored gameMaps.
				gameMaps.add(new GameMap(mapName, mapChars));
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (gameMaps.isEmpty()) {
			System.err.println("GameMap#readInAllMaps - There were no maps to read in! Please create some!");
		} else {
			System.out.println("Read in " + gameMaps.size() + " map(s).");
		}
	}
	//endregion
	
	
	
}