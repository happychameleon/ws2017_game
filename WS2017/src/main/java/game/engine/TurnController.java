package game.engine;

import client.commands.ClientEndtnHandler;
import game.GameState;
import serverclient.User;
import java.util.ArrayList;

/**
 * Created by flavia on 09.03.17.
 */
public class TurnController {
	
	//region Data
	private final World world;
	
	/**
	 * All the players Playing the game. The order of the Players in here represents the turn order
	 * (players.get(0) is first, then players.get(1) etc.).
	 * The players are sorted alphabetically by their names.
	 * @see #getCurrentPlayer()
	 */
	private final ArrayList<Player> players;
	
	/**
	 * @return {@link #players}.
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * All the Teams in this game.
	 */
	private final ArrayList<Team> teams;
	
	/**
	 * @return {@link #teams}.
	 */
	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	/**
	 * The total number of played turns.
	 */
	private int turnCount = 1;
	
	/**
	 * @return {@link #turnCount}
	 */
	public int getTurnCount() {
		return turnCount;
	}
	
	/**
	 * The Player which's turn it is.
	 */
	private Player currentPlayer;
	
	/**
	 * @return {@link #currentPlayer}.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	//endregion
	
	
	public TurnController(ArrayList<User> users, World world) {
		this.world = world;
		
		if (users.size() > 4) {
			System.err.println("TurnController#TurnController - There should never be more than 4 Players!");
		} else if (users.size() < 1) {
			System.err.println("TurnController#TurnController - There are no players?!");
		}
		
		players = new ArrayList<>();
		teams = new ArrayList<>();
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			Team newTeam = new Team("Team" + (players.size() + 1));
			PlayerColor color = PlayerColor.values()[i];
			Player newPlayer = new Player(newTeam, user, color, world);
			players.add(newPlayer);
			newTeam.addPlayerToTeam(newPlayer);
			teams.add(newTeam);
		}
		currentPlayer = players.get(0);
	}
	
	/**
	 * This is called when the server informs the client that the current player's turn has ended.
	 * After endTurn the next Player in {@link #players} has the turn.
	 * When the next user who's turn it is has no characters left, the turn is immediately passed on.
	 * It is also called, when a user who left had the turn.
	 */
	public void endTurn () {
		getCurrentPlayer().endCurrentTurn();
		if (players.indexOf(currentPlayer) == players.size() - 1) {
			currentPlayer = players.get(0);
		} else {
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		}
		
		world.printToGameLobby(getCurrentPlayer().getName() + " started their turn!");
		
		getCurrentPlayer().startNewTurn();
		
		if (world.getCurrentPlayer().hasCharactersLeft() == false && players.size() > 1 && world.getGameController().getGameState() == GameState.RUNNING) {
			endTurn();
		}
	}
	
	/**
	 * Sends a message to the server telling it about this client's ended turn.
	 * Only possible, when this client is actually holding the turn.
	 */
	public void askServerToEndTurn() {
		if (getCurrentPlayer().isThisClient()) {
			ClientEndtnHandler.askServerToEndTurn(world.getGameController().getGameName());
		}
	}
	
	/**
	 * Removes the player representing the given user from the list of players.
	 * @param userToRemove The user to remove from the game.
	 */
	public void removePlayer(User userToRemove) {
		System.out.println("TurnController#removePlayer");
		// Remove the player
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if (player.getUser() == userToRemove) {
				// Remove all this player's characters if there are any.
				if (player.hasCharactersLeft()) {
					for (Character character : world.getAllCharacterOfOwner(player)) {
						System.out.println("TurnController#removePlayer - Character Removed.");
						world.removeCharacter(character);
					}
				} else {
					System.out.printf("TurnController#removePlayer - Player %s had no Characters left!%n", player.getName());
				}
				if (getCurrentPlayer() == player) {
					// Give up the turn (both on the server and client side. Doesn't need to be synchronised, since leavg is already synchronised.
					endTurn();
				}
				// Remove Player
				player.getTeam().removePlayerFromTeam(player);
				players.remove(player);
				break;
			}
		}
	}
}
