import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PropertyPage {
    private Connection connection;

    public PropertyPage(List<String> selectedOptions) {
        initializeDatabaseConnection();

        JFrame frame = new JFrame("Real Estate Management - Properties");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 35, 45));
        frame.add(mainPanel);

        // Navigation bar with selected options as buttons
        JPanel navBar = new JPanel();
        navBar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navBar.setBackground(new Color(0, 173, 181));

        for (String option : selectedOptions) {
            JButton optionButton = new JButton(option);
            optionButton.setFont(new Font("SansSerif", Font.BOLD, 12));
            optionButton.setForeground(Color.WHITE);
            optionButton.setBackground(new Color(57, 62, 70));
            optionButton.setFocusPainted(false);
            optionButton.setPreferredSize(new Dimension(100, 30));
            optionButton.setBorder(BorderFactory.createLineBorder(new Color(57, 62, 70), 2));

            optionButton.addActionListener(e -> {
                if ("Sell".equals(option)) {
                    showSellForm(frame);
                } else if ("Buy".equals(option)) {
                    showBuyPage(frame);
                } else if ("Rent".equals(option)) {
                    showRentPage(frame);
                }
            });
            navBar.add(optionButton);
        }
        mainPanel.add(navBar, BorderLayout.NORTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(frame, "Thank you for using the app!");
                closeDatabaseConnection();
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }

    // Database connection setup
    private void initializeDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/real_estate";
            String user = "root";
            String password = "divyak193"; // Replace with your password
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot connect to the database.");
        }
    }

    private void closeDatabaseConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show the sell form
    private void showSellForm(JFrame parentFrame) {
        JDialog sellDialog = new JDialog(parentFrame, "Sell Property", true);
        JPanel sellPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        sellPanel.setBackground(new Color(30, 35, 45));

        JLabel nameLabel = new JLabel("Property Name:");
        nameLabel.setForeground(Color.WHITE);  // Set white color for text
        JTextField nameField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setForeground(Color.WHITE);  // Set white color for text
        JTextArea descriptionArea = new JTextArea(3, 20);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setForeground(Color.WHITE);  // Set white color for text
        JTextField locationField = new JTextField();

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Color.WHITE);  // Set white color for text
        JTextField priceField = new JTextField();

        JButton sellButton = new JButton("Sell Property");
        sellButton.setBackground(new Color(0, 173, 181));
        sellButton.setForeground(Color.WHITE);
        sellButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descriptionArea.getText();
            String location = locationField.getText();
            String price = priceField.getText();

            try {
                addPropertyToDatabase(name, description, location, Double.parseDouble(price), "sell");
                JOptionPane.showMessageDialog(sellDialog, "Property added for sale!");
                nameField.setText("");
                descriptionArea.setText("");
                locationField.setText("");
                priceField.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        sellPanel.add(nameLabel);
        sellPanel.add(nameField);
        sellPanel.add(descriptionLabel);
        sellPanel.add(new JScrollPane(descriptionArea));
        sellPanel.add(locationLabel);
        sellPanel.add(locationField);
        sellPanel.add(priceLabel);
        sellPanel.add(priceField);
        sellPanel.add(new JLabel());
        sellPanel.add(sellButton);

        sellDialog.add(sellPanel);
        sellDialog.setSize(400, 300);
        sellDialog.setLocationRelativeTo(parentFrame);
        sellDialog.setVisible(true);
    }

    // Add property to the database
    private void addPropertyToDatabase(String name, String description, String location, double price, String type) throws SQLException {
        String table = type.equals("sell") ? "sell_properties" : "rent_properties";
        String query = "INSERT INTO " + table + " (name, description, location, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
        }
    }

    // Show the buy page
    private void showBuyPage(JFrame parentFrame) {
        JDialog buyDialog = new JDialog(parentFrame, "Available Properties for Sale", true);
        JPanel buyPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buyPanel.setBackground(new Color(30, 35, 45));

        try {
            String query = "SELECT * FROM sell_properties WHERE status = 'available'";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int propertyId = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String location = rs.getString("location");
                    double price = rs.getDouble("price");

                    JButton viewButton = new JButton("View Details");
                    viewButton.setBackground(new Color(0, 173, 181));
                    viewButton.setForeground(Color.WHITE);
                    viewButton.addActionListener(e -> showPropertyDetails(parentFrame, propertyId, name, description, location, price));

                    JPanel propertyPanel = new JPanel(new GridLayout(0, 1));
                    propertyPanel.setBackground(new Color(57, 62, 70));
                    propertyPanel.add(new JLabel("Name: " + name) {{
                        setForeground(Color.WHITE);  // Set white color for text
                    }});
                    propertyPanel.add(new JLabel("Location: " + location) {{
                        setForeground(Color.WHITE);  // Set white color for text
                    }});
                    propertyPanel.add(new JLabel("Price: ₹" + price) {{
                        setForeground(Color.WHITE);  // Set white color for text
                    }});
                    propertyPanel.add(viewButton);

                    buyPanel.add(propertyPanel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(buyPanel);
        buyDialog.add(scrollPane);
        buyDialog.setSize(500, 500);
        buyDialog.setLocationRelativeTo(parentFrame);
        buyDialog.setVisible(true);
    }

    // Show property details
    private void showPropertyDetails(JFrame parentFrame, int propertyId, String name, String description, String location, double price) {
        JDialog detailsDialog = new JDialog(parentFrame, "Property Details", true);
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        detailsPanel.setBackground(new Color(30, 35, 45));

        detailsPanel.add(new JLabel("Name: " + name) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});
        detailsPanel.add(new JLabel("Description: " + description) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});
        detailsPanel.add(new JLabel("Location: " + location) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});
        detailsPanel.add(new JLabel("Price: ₹" + price) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});

        JButton buyButton = new JButton("Buy Property");
        buyButton.setBackground(new Color(0, 173, 181));
        buyButton.setForeground(Color.WHITE);
        buyButton.addActionListener(e -> {
            try {
                String updateQuery = "UPDATE sell_properties SET status = 'sold' WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                    stmt.setInt(1, propertyId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(parentFrame, "Property bought!");
                    detailsDialog.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        detailsPanel.add(buyButton);

        detailsDialog.add(detailsPanel);
        detailsDialog.setSize(400, 300);
        detailsDialog.setLocationRelativeTo(parentFrame);
        detailsDialog.setVisible(true);
    }

    // Show the rent page
    private void showRentPage(JFrame parentFrame) {
        JDialog rentDialog = new JDialog(parentFrame, "Available Properties for Rent", true);
        JPanel rentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        rentPanel.setBackground(new Color(30, 35, 45));

        try {
            String query = "SELECT * FROM sell_properties WHERE status = 'available'";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int propertyId = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String location = rs.getString("location");
                    double price = rs.getDouble("price");

                    // Calculate rent price as 2% of selling price
                    double rentPrice = price * 0.02;

                    JButton viewButton = new JButton("View Details");
                    viewButton.setBackground(new Color(0, 173, 181));
                    viewButton.setForeground(Color.WHITE);
                    viewButton.addActionListener(e -> showRentPropertyDetails(parentFrame, propertyId, name, description, location, rentPrice));

                    JPanel propertyPanel = new JPanel(new GridLayout(0, 1));
                    propertyPanel.setBackground(new Color(57, 62, 70));
                    propertyPanel.add(new JLabel("Name: " + name) {{
                        setForeground(Color.WHITE);  // Set white color for text
                    }});
                    propertyPanel.add(new JLabel("Location: " + location) {{
                        setForeground(Color.WHITE);  // Set white color for text
                    }});
                    propertyPanel.add(new JLabel("Rent Price: ₹" + rentPrice) {{
                        setForeground(Color.WHITE);  // Set white color for text
                    }});
                    propertyPanel.add(viewButton);

                    rentPanel.add(propertyPanel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(rentPanel);
        rentDialog.add(scrollPane);
        rentDialog.setSize(500, 500);
        rentDialog.setLocationRelativeTo(parentFrame);
        rentDialog.setVisible(true);
    }

    // Show rent property details
    private void showRentPropertyDetails(JFrame parentFrame, int propertyId, String name, String description, String location, double rentPrice) {
        JDialog detailsDialog = new JDialog(parentFrame, "Property Details", true);
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        detailsPanel.setBackground(new Color(30, 35, 45));

        detailsPanel.add(new JLabel("Name: " + name) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});
        detailsPanel.add(new JLabel("Description: " + description) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});
        detailsPanel.add(new JLabel("Location: " + location) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});
        detailsPanel.add(new JLabel("Rent Price: ₹" + rentPrice) {{
            setForeground(Color.WHITE);  // Set white color for text
        }});

        JButton rentButton = new JButton("Rent Property");
        rentButton.setBackground(new Color(0, 173, 181));
        rentButton.setForeground(Color.WHITE);
        rentButton.addActionListener(e -> {
            try {
                String updateQuery = "UPDATE sell_properties SET status = 'rented' WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                    stmt.setInt(1, propertyId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(parentFrame, "Property rented!");
                    detailsDialog.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        detailsPanel.add(rentButton);

        detailsDialog.add(detailsPanel);
        detailsDialog.setSize(400, 300);
        detailsDialog.setLocationRelativeTo(parentFrame);
        detailsDialog.setVisible(true);
    }

    public static void main(String[] args) {
        List<String> selectedOptions = new ArrayList<>();
        selectedOptions.add("Sell");
        selectedOptions.add("Buy");
        selectedOptions.add("Rent");

        new PropertyPage(selectedOptions);
    }
}
