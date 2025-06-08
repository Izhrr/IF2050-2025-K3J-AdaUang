package views;

import controllers.AuthController;
import controllers.ContractController;
import models.Instalment;
import models.User;
import models.Contract;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class InstalmentView extends JPanel {
    private final InstalmentController instalmentController;
    private final AuthController authController;
    private JTable instalmentTable;
    private DefaultTableModel tableModel;
    private ImageIcon optionIcon;
    
    // Komponen untuk fungsionalitas pop-up
    private JLayeredPane layeredPane;
    private JPanel viewDetailPanel;
    private JPanel addInstalmentPanel;
    private JPanel overlayPanel;

}