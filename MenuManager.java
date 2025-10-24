import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MenuManager extends JFrame implements ActionListener {
    private JTextField itemField, priceField, categoryField, imagePathField;
    private JButton addBtn, deleteBtn, viewBtn, backBtn, browseBtn;
    private JPanel menuPanel;
    private JLabel imagePreview;
    private static final ArrayList<MenuItem> menuList = new ArrayList<>();

    public MenuManager() {
        setTitle("Menu Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Item"));

        itemField = new JTextField();
        priceField = new JTextField();
        categoryField = new JTextField();
        imagePathField = new JTextField();
        imagePathField.setEditable(false);

        browseBtn = new JButton("Browse...");
        browseBtn.addActionListener(e -> chooseImage());

        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Image:"));
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.add(imagePathField, BorderLayout.CENTER);
        imgPanel.add(browseBtn, BorderLayout.EAST);
        inputPanel.add(imgPanel);

        // Image preview
        imagePreview = new JLabel("No Image Selected", SwingConstants.CENTER);
        imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePreview.setPreferredSize(new Dimension(200, 200));

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        addBtn = new JButton("Add");
        deleteBtn = new JButton("Delete");
        viewBtn = new JButton("View Menu");
        backBtn = new JButton("Back");
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(backBtn);

        addBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        backBtn.addActionListener(this);

        // Menu display area (scrollable panel)
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 3 items per row
        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBorder(BorderFactory.createTitledBorder("Menu Items"));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(imagePreview, BorderLayout.WEST);
        centerPanel.add(scroll, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser("images/");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            imagePathField.setText(path);

            ImageIcon icon = new ImageIcon(path);
            Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imagePreview.setIcon(new ImageIcon(scaledImage));
            imagePreview.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String item = itemField.getText().trim();
            String price = priceField.getText().trim();
            String category = categoryField.getText().trim();
            String imagePath = imagePathField.getText().trim();

            if (item.isEmpty() || price.isEmpty() || category.isEmpty() || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields and select an image!");
                return;
            }

            menuList.add(new MenuItem(item, category, price, imagePath));
            JOptionPane.showMessageDialog(this, "Item added successfully!");
            clearFields();

        } else if (e.getSource() == viewBtn) {
            displayMenuItems();

        } else if (e.getSource() == deleteBtn) {
            String itemName = JOptionPane.showInputDialog(this, "Enter item name to delete:");
            if (itemName != null) {
                boolean removed = menuList.removeIf(m -> m.itemName.equalsIgnoreCase(itemName));
                JOptionPane.showMessageDialog(this, removed ? "Item deleted!" : "Item not found!");
                displayMenuItems();
            }

        } else if (e.getSource() == backBtn) {
            dispose();
        }
    }

    private void clearFields() {
        itemField.setText("");
        priceField.setText("");
        categoryField.setText("");
        imagePathField.setText("");
        imagePreview.setIcon(null);
        imagePreview.setText("No Image Selected");
    }

    private void displayMenuItems() {
        menuPanel.removeAll(); // clear old items
        if (menuList.isEmpty()) {
            menuPanel.add(new JLabel("No items to display."));
        } else {
            for (MenuItem m : menuList) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

                ImageIcon icon = new ImageIcon(m.imagePath);
                Image scaledImg = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));

                JLabel info = new JLabel(
                        "<html><center>" + m.itemName + "<br>(" + m.category + ")<br>₹" + m.price + "</center></html>",
                        SwingConstants.CENTER);
                info.setFont(new Font("Arial", Font.PLAIN, 12));

                card.add(imgLabel, BorderLayout.CENTER);
                card.add(info, BorderLayout.SOUTH);
                menuPanel.add(card);
            }
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    // ✅ FIX: This method allows other classes (like OrderManager) to access the
    // menu
    public static ArrayList<MenuItem> getMenuList() {
        return menuList;
    }

    // Inner class for menu items
    public static class MenuItem {
        String itemName, category, price, imagePath;

        MenuItem(String itemName, String category, String price, String imagePath) {
            this.itemName = itemName;
            this.category = category;
            this.price = price;
            this.imagePath = imagePath;
        }
    }
}
