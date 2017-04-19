/**
 * This package is the visual representation of the game.
 * It is ignored by the server and only exists on the clients.
 * All the input from mouse and keyboard during the game is received in this package.
 * The game engine is then updated on the server which sends the update to all the clients' engine package, including this client.
 * The communication to the server is handled via the game's {@link game.ClientGameController}.
 *
 * Created by flavia on 27.03.17.
 */
package game.gamegui;