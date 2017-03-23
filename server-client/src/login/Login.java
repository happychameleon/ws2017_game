package login;

import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.Dimension;



public class Login {

int cnt = 0;
static String username = "" ;
String[] usernames = new String[9000];
 JFrame f = new JFrame("User Login");
 JLabel l = new JLabel("Username:");
 JTextField t = new JTextField(20);
 JButton b = new JButton("login");
 

public Login() {
	frame();
}

public void frame(){
	
	f.setSize(650,300);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setVisible(true);
	JPanel p = new JPanel();
	p.add(l);
	p.add(t);
	p.add(b);
	f.add(p);
	l.setFont(new Font("Courier New", Font.BOLD, 75));
	//t.setPreferredSize(new Dimension(15,50));
	b.setPreferredSize(new Dimension(100,50));
	t.setFont(new Font("Courier New", Font.ITALIC , 50));
	
	
	b.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			
				    String user = t.getText();
				
				
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
	
	new Login();
}






}
