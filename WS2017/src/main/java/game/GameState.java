package game;

/**
 * The current state of a game, specifically a {@link GameController}.
 *
 * Created by flavia on 11.04.17.
 */
public enum GameState {
	
	/**
	 * When the game can still be joined (if not full yet) and the users can choose their team
	 */
	STARTING,
	/**
	 * When the game started and the users are playing.
	 * It can only be watched now by users not joined in the {@link #STARTING} phase.
	 */
	RUNNING,
	/**
	 * The game has ended and has a highscore now. The highscore can be viewed.
	 */
	FINISHED
	
}
