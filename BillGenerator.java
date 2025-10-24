import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BillGenerator extends JFrame implements ActionListener {

    private JTextArea billArea;
    private JButton printBtn, saveBtn, backBtn;
    private double taxRate = 0.05; // 5% tax
    private double totalAmount = 0.0;
    private ArrayList<OrderManager.OrderItem> orderList;

    public BillGenerator(ArrayList<OrderManager.OrderItem> orderList, double totalAmount) {
        this.orderList = orderList;
        this.totalAmount = totalAmount;

        setTitle("Bill Generator");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ---------- Text area ----------
        billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Customer Bill"));

        // ---------- Buttons ----------
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        printBtn = new JButton("Print Bill");
        saveBtn = new JButton("Save as Text File");
        backBtn = new JButton("Back");
        btnPanel.add(printBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);

        printBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        backBtn.addActionListener(this);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        generateBill();

        setVisible(true);
    }

    private void generateBill() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        sb.append("=========================================\n");
        sb.append("         RESTAURANT BILL RECEIPT\n");
        sb.append("=========================================\n");
        sb.append("Date/Time: ").append(date).append("\n\n");

        sb.append(String.format("%-20s %-10s %-10s %-10s\n", "Item", "Qty", "Price", "Total"));
        sb.append("---------------------------------------------------------\n");

        for (OrderManager.OrderItem item : orderList) {
            sb.append(String.format("%-20s %-10d %-10.2f %-10.2f\n",
                    item.itemName, item.quantity, item.price, item.subtotal));
        }

        sb.append("---------------------------------------------------------\n");
        double tax = totalAmount * taxRate;
        double grandTotal = totalAmount + tax;
        sb.append(String.format("%40s %-10.2f\n", "Subtotal:", totalAmount));
        sb.append(String.format("%40s %-10.2f\n", "Tax (5%):", tax));
        sb.append(String.format("%40s %-10.2f\n", "Grand Total:", grandTotal));
        sb.append("=========================================================\n");
        sb.append("        THANK YOU! PLEASE VISIT AGAIN 🍽️\n");
        sb.append("=========================================================\n");

        billArea.setText(sb.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == printBtn) {
            try {
                billArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error printing bill: " + ex.getMessage());
            }
        } else if (e.getSource() == saveBtn) {
            try {
                String fileName = "Bill_" + System.currentTimeMillis() + ".txt";
                FileWriter writer = new FileWriter(fileName);
                writer.write(billArea.getText());
                writer.close();
                JOptionPane.showMessageDialog(this, "Bill saved as " + fileName);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            dispose();
        }
    }
}
