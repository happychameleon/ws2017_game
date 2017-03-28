package game.gamegui;


/**
 * Created by flavia on 17.03.17.
 */
public class Camera {
	
	/**
	 * The window of which this class controls the camera.
	 * TODO: maybe the window can just give all the needed information in the methods and this can be deleted.
	 */
	private Window window;
	
	private float scrollSpeed = 10f;
	
	private float x;
	private float y;
	
	public float getXPosition() {
		return x;
	}
	
	public float getYPosition() {
		return y;
	}
	
	public void moveRight() {
		if (window.getMapWidthInPixels() <= window.getWidth()) {
			return; // Map fits (horizontally) inside the whole window. Ignore scrolling.
		}
		x += scrollSpeed;
		if (x > window.getMapWidthInPixels() - window.getWidth()) {
			x = window.getMapWidthInPixels() - window.getWidth();
		}
	}
	
	public void moveDown() {
		if (window.getMapHeightInPixels() <= window.getHeightWithoutTitlebar()) {
			return; // Map fits (vertically) inside the whole window. Ignore scrolling.
		}
		y += scrollSpeed;
		if (y > window.getMapHeightInPixels() - window.getHeightWithoutTitlebar()) {
			y = window.getMapHeightInPixels() - window.getHeightWithoutTitlebar();
		}
	}
	
	public void moveLeft() {
		x -= scrollSpeed;
		if (x < 0) {
			x = 0;
		}
	}
	
	public void moveUp() {
		y -= scrollSpeed;
		if (y < 0) {
			y = 0;
		}
	}
	
	
	
	
	public Camera (Window window) {
		this.window = window;
	}
	
}
