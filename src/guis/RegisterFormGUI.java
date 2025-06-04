package guis;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import constant.CommonConstants;
import db.MyJDBC;

public class RegisterFormGUI extends Form {
    public RegisterFormGUI() {
        super("Register");
        addGUIComponents();
    }

    private void addGUIComponents() {
        // create login label
        JLabel registerLabel = new JLabel("Register");

        // configure component's x, y and width, height relative to the GUI
        registerLabel.setBounds(0, 25, 520, 100);

        // change the font color
        registerLabel.setForeground(CommonConstants.TEXT_COLOR);

        // change the font size
        registerLabel.setFont(new Font("Dialog", Font.BOLD, 40));

        // center text
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add component to the GUI
        add(registerLabel);

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
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 255, 400, 25);
        passwordLabel.setForeground(CommonConstants.TEXT_COLOR);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // create password text field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(30, 285, 450, 55);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordField.setBackground(CommonConstants.SECONDARY_COLOR);
        passwordField.setForeground(CommonConstants.TEXT_COLOR);
        add(passwordField);

        // create re-enter password label
        JLabel rePasswordLabel = new JLabel("Re-enter Password:");
        rePasswordLabel.setBounds(30, 365, 400, 25);
        rePasswordLabel.setForeground(CommonConstants.TEXT_COLOR);
        rePasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(rePasswordLabel);

        // create re-enter password text field
        JPasswordField rePasswordField = new JPasswordField();
        rePasswordField.setBounds(30, 395, 450, 55);
        rePasswordField.setFont(new Font("Dialog", Font.PLAIN, 20));
        rePasswordField.setBackground(CommonConstants.SECONDARY_COLOR);
        rePasswordField.setForeground(CommonConstants.TEXT_COLOR);
        add(rePasswordField);

        // create login button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
        registerButton.setBounds(125, 520, 250, 50);
        registerButton.setBackground(CommonConstants.TEXT_COLOR);

        // change the cursor to hand when hovering over the button
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // get username
                String username = usernameField.getText();

                // get password
                String password = new String(passwordField.getPassword());

                // get re-entered password
                String rePassword = new String(rePasswordField.getPassword());

                // validate user input
                if(validateUserInput(username, password, rePassword)){
                    // register user to the database
                    if(MyJDBC.register(username, password)){
                        // dispose of this GUI
                        RegisterFormGUI.this.dispose();
    
                        // take user back to the login gui
                        LoginFormGUI loginForm = new LoginFormGUI();
                        loginForm.setVisible(true);
    
                        // create a result dialog
                        JOptionPane.showMessageDialog(
                            loginForm,
                            "Registered Account Successfully"
                        ); 
                    }
                    else{
                        // register failed
                        JOptionPane.showMessageDialog(
                            RegisterFormGUI.this,
                            "Username already taken"
                        );
                    }
                }
                else{
                    // invalid user input
                    JOptionPane.showMessageDialog(RegisterFormGUI.this,
                    "Error: Username must be at least 6 character");
                }

            }
        });
        add(registerButton);

        // create register label (used to load the register GUI)
        JLabel loginLabel = new JLabel("Have an account? Login here");
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setForeground(CommonConstants.TEXT_COLOR);

        // add functionality so that when clicked it will launch the login GUI
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // dispose of this GUI
                RegisterFormGUI.this.dispose();

                //launch the register GUI
                new LoginFormGUI().setVisible(true);
            }
        });

        loginLabel.setBounds(125, 600, 250, 30);
        add(loginLabel);
    }

    private boolean validateUserInput(String username, String password, String rePassword){
        // all fields must have a value
        if(username.isEmpty() || password.isEmpty() || rePassword.isEmpty()){
            return false;
        }

        // username has to be at least 6 characters long
        if(username.length() < 6){
            return false;
        }

        // password and repassword must match
        if(!password.equals(rePassword)){
            return false;
        }

        // passes validation
        return true;
    }
    
}
