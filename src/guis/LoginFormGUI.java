package guis;

import javax.swing.*;
import db.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Cursor;
import java.awt.Font;
import constant.*;

public class LoginFormGUI extends Form{
    public LoginFormGUI(){
        super("Login");
        addGUIComponents();
    }

    private void addGUIComponents() {
        // create login label
        JLabel loginLabel = new JLabel("Login");

        // configure component's x, y and width, height relative to the GUI
        loginLabel.setBounds(0, 25, 520, 100);

        // change the font color
        loginLabel.setForeground(CommonConstants.TEXT_COLOR);

        // change the font size
        loginLabel.setFont(new Font("Dialog", Font.BOLD, 40));

        // center text
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add component to the GUI
        add(loginLabel);

        // create username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(30, 150, 400, 25);
        usernameLabel.setForeground(CommonConstants.TEXT_COLOR);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(usernameLabel);

        // create username text field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(30, 185, 450, 55);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 20));
        usernameField.setBackground(CommonConstants.SECONDARY_COLOR);
        usernameField.setForeground(CommonConstants.TEXT_COLOR);
        add(usernameField);

        // create password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30, 335, 400, 25);
        passwordLabel.setForeground(CommonConstants.TEXT_COLOR);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // create username text field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(30, 365, 450, 55);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordField.setBackground(CommonConstants.SECONDARY_COLOR);
        passwordField.setForeground(CommonConstants.TEXT_COLOR);
        add(passwordField);

        // create login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.setBounds(125, 520, 250, 50);
        loginButton.setBackground(CommonConstants.TEXT_COLOR);

        // change the cursor to hand when hovering over the button
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
            // get username
            String username = usernameField.getText();

            // get password
            String password = new String(passwordField.getPassword());

            // check database if the username and password combo is valid
            if(MyJDBC.validateLogin(username, password)){
                // login succesful
                JOptionPane.showMessageDialog(LoginFormGUI.this,
                "Login Succesful!");
            }
            else{
                // login failed
                JOptionPane.showMessageDialog(LoginFormGUI.this,
                "Login failed!");
            }
           } 
        });
        add(loginButton);

        // create register label (used to load the register GUI)
        JLabel registerLabel = new JLabel("Don't have an account? Register here");
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(CommonConstants.TEXT_COLOR);

        // add functionality so that when clicked it will launch the login GUI
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // dispose of this GUI
                LoginFormGUI.this.dispose();

                //launch the register GUI
                new RegisterFormGUI().setVisible(true);
            }
        });

        registerLabel.setBounds(125, 600, 250, 30);
        add(registerLabel);
    }
}
