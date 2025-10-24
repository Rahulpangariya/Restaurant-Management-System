import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame implements ActionListener {
    JTextField userField;
    JPasswordField passField;
    JButton loginButton;

    public LoginPage() {
        setTitle("Restaurant Management - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        userField = new JTextField();
        passField = new JPasswordField();
        loginButton = new JButton("Login");

        loginButton.addActionListener(this);

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(new JLabel()); // Empty cell
        add(loginButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        // Temporary login check
        if (username.equals("admin") && password.equals("1234")) {
            dispose();
            new Dashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials!");
        }
    }
}
