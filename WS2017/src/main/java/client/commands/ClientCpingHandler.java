package client.commands;

import client.Client;

/**
 * Handles the cping by sending cpong back to the server and restarts a
 * timeout thread.
 *
 * Created by m on 3/23/17.
 */
public class ClientCpingHandler extends ClientCommandHandler {

	TimeoutThread newTimeoutThread = new TimeoutThread(commandParser);
	
	/**
	 * Handles the cping by sending cpong back.
	 */
	@Override
	public void handleCommand() {
		sendCpong();
	}

	/**
	 * Function is called if a ping has been received from the server.
	 */
	public void sendCpong(){
		newTimeoutThread.interrupt();
		Client.sendMessageToServer("cpong");
		newTimeoutThread = new TimeoutThread(commandParser);
		newTimeoutThread.start();
	}
	
	@Override
	public void handleAnswer(boolean isOK) {
		// NOT CURRENTLY USED
	}
}
