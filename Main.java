import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginPage();
            } catch (Exception e) {
                e.printStackTrace(); // prints the real cause in terminal
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });
    }
}
