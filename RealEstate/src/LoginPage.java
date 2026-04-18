import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Real Estate Management - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(30, 35, 45), 0, getHeight(), new Color(0, 173, 181)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));
        frame.add(gradientPanel);

        JLabel title = new JLabel("Real Estate Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(0, 173, 181));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        gradientPanel.add(title);

        JPanel loginBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(57, 62, 70));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            }
        };
        loginBox.setOpaque(false);
        loginBox.setPreferredSize(new Dimension(350, 220));
        gradientPanel.add(Box.createVerticalStrut(20));
        gradientPanel.add(loginBox);

        loginBox.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        JTextField usernameField = new JTextField(12);
        usernameField.setBackground(new Color(57, 62, 70));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField(12);
        passwordField.setBackground(new Color(57, 62, 70));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0;
        loginBox.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginBox.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginBox.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginBox.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 173, 181));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(120, 30));
        loginButton.addActionListener(e -> {
            frame.dispose();
            new HomePage();
        });
        gbc.gridx = 1; gbc.gridy = 2;
        loginBox.add(loginButton, gbc);

        frame.setVisible(true);
    }
}
