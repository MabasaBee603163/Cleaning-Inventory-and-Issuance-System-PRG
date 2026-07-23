package za.ac.belgiumcampus.cleaninginventory;

import za.ac.belgiumcampus.cleaninginventory.ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fall back to the default Swing look and feel.
            }

            new LoginFrame().setVisible(true);
        });
    }
}
