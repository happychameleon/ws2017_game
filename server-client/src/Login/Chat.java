package Login;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Chat {
	
	static String username = "";
	JFrame chatframe = new JFrame("Chat");
	JTextField chat_in = new JTextField(15);
	JTextField userchange_in = new JTextField(15);
	JButton send = new JButton("Send");
	JButton userchange = new JButton("Change Username");
	JTextArea text_out = new JTextArea(30,50);
	public Chat() {

		frame();
	}

		public void frame() {
// modify JFrame component layout
	    chatframe.setSize(700, 700);
	    chatframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatframe.setVisible(true);	
	    chat_in.setFont(new Font("Courier New", Font.ITALIC, 50));
	    userchange_in.setFont(new Font("Courier New", Font.ITALIC, 50));
	    send.setPreferredSize(new Dimension(100, 50));
        userchange.setPreferredSize(new Dimension(200,50));
// Create window for the chat		
        JPanel p = new JPanel();
		p.add(text_out);
		p.add(chat_in);
		p.add(send);
		p.add(userchange_in);
		p.add(userchange);
		chatframe.add(p);
		
		
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				

			}
			
		});
		userchange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				

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

