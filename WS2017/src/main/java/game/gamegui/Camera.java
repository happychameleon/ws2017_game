package game.gamegui;


/**
 * Responsible for moving around in the game.
 * Moves the position of the image on which the game is drawn around.
 * Only works while the mainGamePanel is smaller than the game itself (width and height separately)
 *
 * Created by flavia on 17.03.17.
 */
public class Camera {
	
	/**
	 * The mainGamePanel of which this class controls the camera.
	 */
	private MainGamePanel mainGamePanel;
	
	/**
	 * The amount of pixels one call to the scroll methods moves.
	 */
	private float scrollSpeed = 15f;
	
	/**
	 * The top end of the game map relative to the mainGamePanel.
	 */
	private float x;
	
	/**
	 * @return {@link #x}
	 */
	public float getXPosition() {
		return x;
	}
	
	/**
	 * The left end of the game map relative to the mainGamePanel.
	 */
	private float y;
	
	/**
	 * @return {@link #y}
	 */
	public float getYPosition() {
		return y;
	}
	
	
	
	public Camera (MainGamePanel mainGamePanel) {
		this.mainGamePanel = mainGamePanel;
	}
	
	
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels to the right, if the mainGamePanel's width is smaller than the map width.
	 * If the map would move too far (the mainGamePanel background would be shown) it is set exactly to the end of the mainGamePanel.
	 */
	public void moveRight() {
		if (mainGamePanel.getMapWidthInPixels() <= mainGamePanel.getWidth()) {
			return; // Map fits (horizontally) inside the whole mainGamePanel. Ignore scrolling.
		}
		x += scrollSpeed;
		if (x > mainGamePanel.getMapWidthInPixels() - mainGamePanel.getWidth()) {
			x = mainGamePanel.getMapWidthInPixels() - mainGamePanel.getWidth();
		}
	}
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels down, if the mainGamePanel's height is smaller than the map height.
	 * If the map would move too far (the mainGamePanel background would be shown) it is set exactly to the end of the mainGamePanel.
	 */
	public void moveDown() {
		if (mainGamePanel.getMapHeightInPixels() <= mainGamePanel.getHeight()) {
			return; // Map fits (vertically) inside the whole mainGamePanel. Ignore scrolling.
		}
		y += scrollSpeed;
		if (y > mainGamePanel.getMapHeightInPixels() - mainGamePanel.getHeight()) {
			y = mainGamePanel.getMapHeightInPixels() - mainGamePanel.getHeight();
		}
	}
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels to the left, if the mainGamePanel's width is smaller than the map width.
	 * If the map would move too far (the mainGamePanel background would be shown) it is set exactly to the end of the mainGamePanel.
	 */
	public void moveLeft() {
		x -= scrollSpeed;
		if (x < 0) {
			x = 0;
		}
	}
	
	/**
	 * Moves the whole map by {@link #scrollSpeed} pixels up, if the mainGamePanel's height is smaller than the map height.
	 * If the map would move too far (the mainGamePanel background would be shown) it is set exactly to the end of the mainGamePanel.
	 */
	public void moveUp() {
		y -= scrollSpeed;
		if (y < 0) {
			y = 0;
		}
	}
	
	
	
	
	
}
