package game.engine;

/**
 * Created by flavia on 11.05.17.
 */
public enum Direction {
	
	NORTH   (0, 1),
	EAST    (1, 0),
	SOUTH   (0, -1),
	WEST    (-1, 0);
	
	
	private final int x;
	private final int y;
	
	Direction(int x, int y) {
		
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Gets the correct Tile.
	 * @param originTile The given Tile.
	 * @return The Tile in the direction of the given Tile. Can be null if originTile is at the edge of the World.
	 */
	public Tile getTileInDirectionOf(Tile originTile) {
		World world = originTile.getWorld();
		return world.getTileAt(originTile.getXPosition() + x, originTile.getYPosition() + y);
	}
	
}
