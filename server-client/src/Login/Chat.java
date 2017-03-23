package Login;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Chat {
	
	static String username = "";
	int cnt = 1;
	JFrame f = new JFrame("Chat");
	JTextField t = new JTextField(20);
	JButton b = new JButton("Send");
	JButton b1 = new JButton("Change Username");
	JTextArea a = new JTextArea(25,50);
	public Chat() {

		frame();
	}

		public void frame() {

	    t.setFont(new Font("Courier New", Font.ITALIC, 50));
		f.setSize(650, 650);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		JPanel p = new JPanel();
		p.add(a);
		p.add(t);
		p.add(b);
		p.add(b1);
		f.add(p);
		//t.setPreferredSize(new Dimension(15,50));
		b.setPreferredSize(new Dimension(100, 50));
        b1.setPreferredSize(new Dimension(200,50));
		
        b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String user = t.getText();

			
				username = user;
				cnt++;

//				if (cnt != 0) {
//					for (int i = 0; i < cnt - 1; i++) {
//						if (usernames[i].equals(user)) {
//							JOptionPane.showMessageDialog(null, "username excestiert bereits");
//							break;
//								
//							
//						} 
//						
//					}
//				}
				System.out.println(getusers());
				f.setVisible(false);

			}
			
		});
		

	}
		public static String getusers(){
			return username;
		}


	public static void main(String[] args) {

		new Chat();
		
	}

}

