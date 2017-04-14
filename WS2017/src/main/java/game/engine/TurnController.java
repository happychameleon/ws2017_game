package game.engine;

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
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			Team newTeam = new Team("Team " + (players.size() + 1));
			PlayerColor color = PlayerColor.values()[i];
			Player newPlayer = new Player(newTeam, user, color, world);
			players.add(newPlayer);
			newTeam.addPlayerToTeam(newPlayer);
		}
		currentPlayer = players.get(0);
	}
	
	public void endTurn () {
		getCurrentPlayer().endCurrentTurn();
		if (players.indexOf(currentPlayer) == players.size() - 1) {
			currentPlayer = players.get(0);
		} else {
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		}
		
		world.printToGameLobby(getCurrentPlayer().getName() + " started their turn!");
		
		getCurrentPlayer().startNewTurn();
	}
	
	/**
	 * TODO: Sends a message to the server telling it about this client's ended turn.
	 */
	public void askServerToEndTurn() {
	
	}
	
	/**
	 * Removes the player representing the given user from the list of players.
	 * @param user The user to remove from the game.
	 */
	public void removePlayer(User user) {
		System.out.println("TurnController#removePlayer");
		// Remove the player
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUser() == user) {
				if (getCurrentPlayer() == players.get(i)) {
					// Give up the turn (both on the server and client side. Doesn't need to be synchronised, since leavg is already synchronised.
					endTurn();
					// Set the currentPlayerIndex back one, because the players got one smaller.
				}
				// Remove all this player's characters if there are any.
				if (players.get(i).hasCharactersLeft()) {
					for (Character character : world.getAllCharacterOfOwner(players.get(i))) {
						System.out.println("TurnController#removePlayer - Character Removed.");
						world.removeCharacter(character);
					}
				}
				// Remove Player
				players.remove(players.get(i));
				break;
			}
		}
		// TODO: Check how many players are left and if someone has won.
		
	}
	
	
}
