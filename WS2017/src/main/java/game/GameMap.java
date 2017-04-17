package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
	 * @param name The name of the map to get.
	 * @param readInMaps Whether the method should try to read in the maps again, in case the name doesn't exist.
	 * @return The map with the name or <code>null</code> if the given name doesn't exist.
	 */
	public static GameMap getMapForName(String name, boolean readInMaps) {
		for (GameMap gameMap : gameMaps) {
			if (gameMap.getName().equals(name))
				return gameMap;
		}
		// name not found, maybe the maps were changed while the application was running. read in the maps and try again.
		if (readInMaps) {
			readInAllMaps();
			return getMapForName(name, false);
		} else {
			return null;
		}
	}
	
	/**
	 * Reads in all the gameMaps and stores them in {@link #gameMaps} at the start of the game.
	 * With help from:
	 * http://cs.dvc.edu/HowTo_ReadJars.html
	 * http://stackoverflow.com/questions/11012819/how-can-i-get-a-resource-folder-from-inside-my-jar-file
	 */
	public static void readInAllMaps() {
		gameMaps = new ArrayList<>();
		
		// All the mapname.txt files stored in the maps folder.
		ArrayList<File> mapFiles = new ArrayList<>();
		
		// Mapnames stored as key, their InputStreams stored as values.
		HashMap<String, InputStream> mapNameInputStreamMap = new HashMap<>();
		
		final String path = "maps/";
		final File jarFile = new File(GameMap.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		JarFile jar = null;
		
		try {
			if (jarFile.isFile()) {  // Run with JAR file
				System.out.println("GameMap#readInAllMaps - Run with JAR");
				jar = new JarFile(jarFile);
				final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
				while (entries.hasMoreElements()) {
					final JarEntry jarEntry = entries.nextElement();
					final String jarEntryName = jarEntry.getName();
					//filter according to the path
					if (jarEntryName.startsWith(path))
						if (jarEntryName.endsWith(".txt")) {
							String mapName = jarEntryName.substring(jarEntryName.lastIndexOf("/") + 1, jarEntryName.lastIndexOf("."));
							InputStream inputStream = jar.getInputStream(jarEntry);
							mapNameInputStreamMap.put(mapName, inputStream);
						}
				}
			} else { // Run with IDE
				File mapsFolder = new File("src/main/resources/maps");
				System.out.println("GameMap#readInAllMaps - Run with IDE");
				for (File mapFile : mapsFolder.listFiles())
					if (mapFile.getName().endsWith(".txt")) {
						String mapName = mapFile.getName().substring(mapFile.getName().lastIndexOf("/") + 1, mapFile.getName().lastIndexOf("."));
						InputStream inputStream = new FileInputStream(mapFile);
						mapNameInputStreamMap.put(mapName, inputStream);
					}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		for (String mapName : mapNameInputStreamMap.keySet()) {
			
			// Now read in the map file's data.
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mapNameInputStreamMap.get(mapName)));
				// Stores all the lines of the file in the Array List.
				ArrayList<String> lines = new ArrayList<>();
				String nextLine = bufferedReader.readLine();
				while (nextLine != null) {
					if (nextLine.startsWith("//") == false && nextLine.isEmpty() == false) // Ignore commented out lines.
						lines.add(nextLine);
					nextLine = bufferedReader.readLine();
				}
				// Store the lines as char[][] and throws an error when they aren't all the same size.
				int lineSize = lines.get(0).length();
				char[][] mapChars = new char[lines.size()][];
				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i);
					if (line.length() != lineSize)
						System.err.println("Map wrongly formated (lines different length): " + mapName);
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
		if (jar != null)
			try {
				jar.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private static void createDefaultMaps() {
	
	}
	//endregion
	
	
	
}
