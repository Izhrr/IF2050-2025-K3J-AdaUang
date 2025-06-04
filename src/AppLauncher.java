import javax.swing.SwingUtilities;
import guis.*;
import db.*;
public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and show the login form GUI
                new LoginFormGUI().setVisible(true);

                // check user test
                // System.out.println(MyJDBC.checkUser("123"));

                // check register test
                // System.out.println(MyJDBC.register("username1234", "passwrod"));

                // check validate login test
                // System.out.println(MyJDBC.validateLogin("username1234", "passwrod"));
            }
        });
    }
}
