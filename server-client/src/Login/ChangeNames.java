package Login;

import javax.swing.*;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Julischka Saravia on 23.03.2017.
 */
public class ChangeNames {

    ArrayList<String> usernames;

    JFrame framechange = new JFrame("Change username"); //JFchange.setTitle("...");
    JLabel labelchange = new JLabel("New name:");
    JTextField textchange = new JTextField(20);
    JButton buttonchange = new JButton("Change");

    /**
     * Frame properties
     */

    public ChangeNames(){
        frame();
    }
    public void frame(){
        framechange.setVisible(true);
        framechange.setSize(400, 200);
        framechange.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framechange.setLocation(300, 200);
        labelchange.setFont(new Font("Courier New", Font.BOLD, 20));
        textchange.setFont(new Font("Courier New", Font.ITALIC , 30));
        buttonchange.setPreferredSize(new Dimension(100,50));
        JPanel panelchange = new JPanel();
        panelchange.add(labelchange);
        panelchange.add(textchange);
        panelchange.add(buttonchange);
        framechange.add(panelchange);

        /**
         * ActionListener reacts to button. Tests if new name is free. Changes old name or changes new name till
         * new name is free. Loop for unfree new names doesn't work.
         */

        buttonchange.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {

                String wishname = textchange.getText();
                int counter = 1;
                //FIXME: INFINITE LOOP
                while(usernames.contains(wishname)){
                //if(usernames.contains(wishname)){
                    JOptionPane.showMessageDialog(null, "Username already given!");
                    textchange.setText(wishname + counter);
                    counter++;
                    continue;
                } //else {
                    int value = usernames.indexOf("Pelikan");
                    usernames.set(value, wishname);
                    System.out.println("Hallo " + usernames.get(value) + "!");
                    System.out.println(usernames);//test
                //}

            }
        });


    }

    /**
     * Test entries for class
     * @param args not used
     */
   public static void main(String[] args) {
      ChangeNames change = new ChangeNames();
       // ChangeNames a = new ChangeNames(); // added class object
       change.usernames = new ArrayList<>();
       change.usernames.add("Pelikan");
       change.usernames.add("Flavia");
       change.usernames.add("Patrick");
       change.usernames.add("Max");
       change.usernames.add("Julischka");
       System.out.println(change.usernames);




    }

}
