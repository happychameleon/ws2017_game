package game.gamegui;


/**
 * Responsible for moving around in the game.
 * Moves the position of the image on which the game is drawn around.
 * Only works while the window is smaller than the game itself (width and height separately)
 *
 * Created by flavia on 17.03.17.
 */
public class Camera {
	
	/**
	 * The window of which this class controls the camera.
	 * (TODO) maybe the window can just give all the needed information in the methods and this can be deleted.
	 */
	private Window window;
	
	/**
	 * The amount of pixels one call to the scroll methods moves.
	 */
	private float scrollSpeed = 10f;
	
	/**
	 * The top end of the game map relative to the window.
	 */
	private float x;
	
	/**
	 * @return {@link #x}
	 */
	public float getXPosition() {
		return x;
	}
	
	/**
	 * The left end of the game map relative to the window.
	 */
	private float y;
	
	/**
	 * @return {@link #y}
	 */
	public float getYPosition() {
		return y;
	}
	
	
	
	public Camera (Window window) {
		this.window = window;
	}
	
	
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels to the right, if the window's width is smaller than the map width.
	 * If the map would move too far (the window background would be shown) it is set exactly to the end of the window.
	 */
	public void moveRight() {
		if (window.getMapWidthInPixels() <= window.getWidth()) {
			return; // Map fits (horizontally) inside the whole window. Ignore scrolling.
		}
		x += scrollSpeed;
		if (x > window.getMapWidthInPixels() - window.getWidth()) {
			x = window.getMapWidthInPixels() - window.getWidth();
		}
	}
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels down, if the window's height is smaller than the map height.
	 * If the map would move too far (the window background would be shown) it is set exactly to the end of the window.
	 */
	public void moveDown() {
		if (window.getMapHeightInPixels() <= window.getHeightWithoutTitlebar()) {
			return; // Map fits (vertically) inside the whole window. Ignore scrolling.
		}
		y += scrollSpeed;
		if (y > window.getMapHeightInPixels() - window.getHeightWithoutTitlebar()) {
			y = window.getMapHeightInPixels() - window.getHeightWithoutTitlebar();
		}
	}
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels to the left, if the window's width is smaller than the map width.
	 * If the map would move too far (the window background would be shown) it is set exactly to the end of the window.
	 */
	public void moveLeft() {
		x -= scrollSpeed;
		if (x < 0) {
			x = 0;
		}
	}
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels up, if the window's height is smaller than the map height.
	 * If the map would move too far (the window background would be shown) it is set exactly to the end of the window.
	 */
	public void moveUp() {
		y -= scrollSpeed;
		if (y < 0) {
			y = 0;
		}
	}
	
	
	
	
	
}
