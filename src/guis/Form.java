package guis;

import javax.swing.*;

public class Form extends JFrame{
    // constructor
    public Form(String title){
        // set the title of the title bar
        super(title);

        // set the size of the GUI
        setSize(520, 680);

        // configure GUI to end process when closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set the layout to null to disable layout management so we can use absolute positioning
        // to place the components wherever we want
        setLayout(null);

        // load GUI in the center of the screen
        setLocationRelativeTo(null);

        // prevent GUI form from being resized
        setResizable(false);

        // change the background color of the GUI
        getContentPane().setBackground(constant.CommonConstants.PRIMARY_COLOR);
    }
    
}
