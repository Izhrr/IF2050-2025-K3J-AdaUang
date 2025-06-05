package views;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        setTitle("AdaUang - Kontrak Pembiayaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new SidebarView(), BorderLayout.WEST);
        add(new ContractView(), BorderLayout.CENTER);
    }
}