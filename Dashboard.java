import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Dashboard extends JFrame implements ActionListener {
    JButton menuBtn, orderBtn, billBtn, exitBtn;

    public Dashboard() {
        setTitle("Restaurant Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        menuBtn = new JButton("Manage Menu");
        orderBtn = new JButton("Take Order");
        billBtn = new JButton("Generate Bill");
        exitBtn = new JButton("Exit");

        add(menuBtn);
        add(orderBtn);
        add(billBtn);
        add(exitBtn);

        menuBtn.addActionListener(this);
        orderBtn.addActionListener(this);
        billBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuBtn) {
            new MenuManager();
        } else if (e.getSource() == orderBtn) {
            new OrderManager();
        } else if (e.getSource() == billBtn) {
            new BillGenerator(new ArrayList<>(), 0.0);

        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }
}
