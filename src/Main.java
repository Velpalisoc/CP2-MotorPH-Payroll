import app.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Start GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
