package login;

import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.Dimension;



public class Login {

int cnt = 0;
char number = '1';
static String username = "" ;
String[] usernames = new String[9000];
<<<<<<< HEAD:server-client/src/login/Login.java
 JFrame f = new JFrame("User Login");
 JLabel l = new JLabel("Username:");
 JTextField t = new JTextField(20);
 JButton b = new JButton("login");
=======
 JFrame userframe = new JFrame("User Login");
 JLabel userlabel = new JLabel("Username:");
 JTextField usertext = new JTextField(20);
 JButton loginbutton = new JButton("Login");
>>>>>>> 43f6579f61e6faec6332ee680994ffa1cbd0a924:server-client/src/Login/login.java
 

public Login() {
	frame();
}

public void frame(){
	// modify JFrame component layout
	userframe.setSize(650,300);
	userframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	userframe.setVisible(true);
	userlabel.setFont(new Font("Courier New", Font.BOLD, 75));
	loginbutton.setPreferredSize(new Dimension(100,50));
	usertext.setFont(new Font("Courier New", Font.ITALIC , 50));
	// Create the window for the Login
	JPanel userpanel = new JPanel();
	userpanel.add(userlabel);
	userpanel.add(usertext);
	userpanel.add(loginbutton);
	userframe.add(userpanel);
	
	
	
	loginbutton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			
				    String user = usertext.getText();
				
		// Save username		
					username = user;
					usernames[cnt] = user;
					cnt++;

//search if username already exist and show proposition if it's true				
					for(int i = 0; i < cnt-1;i++){
						if(usernames[i].equals(user)){
								JOptionPane.showMessageDialog(null, "Username not avaible"); 
							    
							    usertext.setText(user+"0"+number);
							    number++;
							    break;
						}
							
						
					}
				
			System.out.println(getusers());
			
		}
	});

}

public static String getusers(){
	return username;
}

public static void main(String[] args){
	
	new Login();
}






}
