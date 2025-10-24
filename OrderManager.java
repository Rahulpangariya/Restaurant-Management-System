import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OrderManager extends JFrame implements ActionListener {
    private JComboBox<String> itemDropdown;
    private JTextField quantityField;
    private JButton addBtn, viewOrderBtn, clearBtn, backBtn, generateBillBtn;
    private JTextArea orderArea;
    private JLabel totalLabel;
    private double totalAmount = 0.0;

    private final ArrayList<OrderItem> orderList = new ArrayList<>();

    public OrderManager() {
        setTitle("Order Manager");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ----------- Top Panel (Inputs) -----------
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Add Item to Order"));

        ArrayList<MenuManager.MenuItem> menu = MenuManager.getMenuList();
        if (menu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No menu items available! Please add items first.");
            dispose();
            return;
        }

        String[] items = menu.stream().map(m -> m.itemName + " - ₹" + m.price).toArray(String[]::new);
        itemDropdown = new JComboBox<>(items);
        quantityField = new JTextField();

        topPanel.add(new JLabel("Select Item:"));
        topPanel.add(itemDropdown);
        topPanel.add(new JLabel("Quantity:"));
        topPanel.add(quantityField);

        // ----------- Button Panel -----------
        JPanel btnPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        addBtn = new JButton("Add");
        viewOrderBtn = new JButton("View Order");
        clearBtn = new JButton("Clear");
        generateBillBtn = new JButton("Generate Bill");
        backBtn = new JButton("Back");

        btnPanel.add(addBtn);
        btnPanel.add(viewOrderBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(generateBillBtn);
        btnPanel.add(backBtn);

        addBtn.addActionListener(this);
        viewOrderBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        generateBillBtn.addActionListener(this);
        backBtn.addActionListener(this);

        // ----------- Order Area -----------
        orderArea = new JTextArea();
        orderArea.setEditable(false);
        orderArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(orderArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Current Order"));

        // ----------- Total Panel -----------
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        // ----------- Layout Setup -----------
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            addItemToOrder();
        } else if (e.getSource() == viewOrderBtn) {
            updateOrderDisplay();
        } else if (e.getSource() == clearBtn) {
            clearOrder();
        } else if (e.getSource() == generateBillBtn) {
            generateBill();
        } else if (e.getSource() == backBtn) {
            dispose();
        }
    }

    private void addItemToOrder() {
        try {
            int selectedIndex = itemDropdown.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "No items available in menu!");
                return;
            }

            String quantityText = quantityField.getText().trim();
            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter quantity!");
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0)
                throw new NumberFormatException();

            MenuManager.MenuItem menuItem = MenuManager.getMenuList().get(selectedIndex);
            double price = Double.parseDouble(menuItem.price);
            double subtotal = price * quantity;

            OrderItem orderItem = new OrderItem(menuItem.itemName, quantity, price, subtotal);
            orderList.add(orderItem);

            totalAmount += subtotal;
            totalLabel.setText(String.format("Total: ₹%.2f", totalAmount));

            JOptionPane.showMessageDialog(this, "Item added successfully!");
            quantityField.setText("");
            updateOrderDisplay();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity!");
        }
    }

    private void updateOrderDisplay() {
        if (orderList.isEmpty()) {
            orderArea.setText("No items in the order yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s %-10s %-10s %-10s\n", "Item", "Qty", "Price", "Total"));
        sb.append("------------------------------------------------------------\n");
        for (OrderItem item : orderList) {
            sb.append(String.format("%-20s %-10d %-10.2f %-10.2f\n",
                    item.itemName, item.quantity, item.price, item.subtotal));
        }
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("%40s ₹%.2f", "Total Amount:", totalAmount));
        orderArea.setText(sb.toString());
    }

    private void clearOrder() {
        orderList.clear();
        totalAmount = 0.0;
        orderArea.setText("");
        totalLabel.setText("Total: ₹0.00");
        JOptionPane.showMessageDialog(this, "Order cleared!");
    }

    private void generateBill() {
        if (orderList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in order to generate bill!");
            return;
        }
        new BillGenerator(orderList, totalAmount);
    }

    // ---------- Inner class for order items ----------
    public static class OrderItem {
        String itemName;
        int quantity;
        double price;
        double subtotal;

        OrderItem(String itemName, int quantity, double price, double subtotal) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
            this.subtotal = subtotal;
        }
    }
}
