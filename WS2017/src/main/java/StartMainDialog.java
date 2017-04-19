import client.Client;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Opens when the application was started without any command line arguments.
 * Here the User can choose to open either the server or the client.
 *
 * TODO? (optionally) Doesn't work atm.
 *
 * Created by flavia on 16.04.17.
 */
public class StartMainDialog extends JDialog implements ActionListener {
	
	/**
	 * The Button to start the client.
	 */
	private final JButton clientStartButton = new JButton("Start Client");
	
	/**
	 * Here the user can enter the port on which to start the server or client.
	 */
	private final JTextField portTextField = new JTextField("1030");
	
	/**
	 * Here the user can enter the ip for the Client on which the server is running.
	 */
	private final JTextField ipTextField = new JTextField("127.0.0.1");
	
	/**
	 * The Button to start the server.
	 */
	private final JButton serverStartButton = new JButton("Start Server");
	
	
	public StartMainDialog() {
		
		setLayout(new GridLayout(3, 2));
		
		add(new JLabel("Port:"));
		add(portTextField);
		add(new JLabel("Server IP:"));
		add(ipTextField);
		
		add(serverStartButton);
		add(clientStartButton);
		
		serverStartButton.addActionListener(this);
		clientStartButton.addActionListener(this);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		pack();
		setVisible(true);
		
	}
	
	
	/**
	 * Invoked when an action occurs.
	 *
	 * @param e The Action Event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == serverStartButton) {
			dispose();
			Server.serverMain(new String[]{"server", portTextField.getText()});
		} else if (e.getSource() == clientStartButton) {
			dispose();
			Client.clientMain(new String[]{"client", ipTextField.getText(), portTextField.getText()});
		}
	}
	
}
