package views;

import javax.swing.*;

public abstract class BaseView extends JFrame {
    public BaseView(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Basic method to show message dialogs
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}