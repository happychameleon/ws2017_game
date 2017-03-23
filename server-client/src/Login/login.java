package Login;

import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.Dimension;



public class login {

int cnt = 0;
static String username = "" ;
String[] usernames = new String[9000];
 JFrame userframe = new JFrame("User Login");
 JLabel userlabel = new JLabel("Username:");
 JTextField usertext = new JTextField(20);
 JButton loginbutton = new JButton("Login");
 

public login() {
	
	frame();
}

public void frame(){
	
	userframe.setSize(650,300);
	userframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	userframe.setVisible(true);
	JPanel p = new JPanel();
	p.add(userlabel);
	p.add(usertext);
	p.add(loginbutton);
	userframe.add(p);
	userlabel.setFont(new Font("Courier New", Font.BOLD, 75));
	//t.setPreferredSize(new Dimension(15,50));
	loginbutton.setPreferredSize(new Dimension(100,50));
	usertext.setFont(new Font("Courier New", Font.ITALIC , 50));
	
	
	loginbutton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			
				    String user = usertext.getText();
				
				
					username = user;
					usernames[cnt] = user;
					cnt++;
					
					for(int i = 0; i < cnt-1;i++){
						if(usernames[i].equals(user)){
								JOptionPane.showMessageDialog(null, "excestiert"); 
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
	
	new login();
}






}
