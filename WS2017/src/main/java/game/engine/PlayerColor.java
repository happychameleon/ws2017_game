package game.engine;

import java.awt.Color;

/**
 * The Colors available to the players.
 * With this we know the name of the color.
 * If we would only use java.awt.Color directly we can't get the name if we ever have to print it.
 * The available colors can also easily be changed here.
 *
 * Created by flavia on 28.03.17.
 */
public enum PlayerColor {
	
	RED     (Color.red),
	YELLOW  (Color.yellow),
	PINK    (Color.pink),
	ORANGE  (Color.orange);
	
	
	/**
	 * The {@link Color} associated with this PlayerColor.
	 */
	private final Color color;
	
	/**
	 * @return {@link #color}.
	 */
	public Color getAWTColor() {
		return color;
	}
	
	PlayerColor(Color color) {
		this.color = color;
	}
	
}
