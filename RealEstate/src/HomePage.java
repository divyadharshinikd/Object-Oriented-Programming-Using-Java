import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class HomePage {
    private ArrayList<JToggleButton> toggleButtons;

    public HomePage() {
        JFrame frame = new JFrame("Real Estate Management - Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(30, 35, 45), 0, getHeight(), new Color(0, 173, 181)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel);

        JLabel welcomeLabel = new JLabel("Real Estate Management");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(0, 173, 181));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(welcomeLabel);

        JLabel promptLabel = new JLabel("How can we help you?");
        promptLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(promptLabel);

        JLabel instructionLabel = new JLabel("Choose a few options if you need to:");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(instructionLabel);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        mainPanel.add(optionsPanel);

        String[] options = {"Buy", "Sell", "Rent"};
        toggleButtons = new ArrayList<>();

        for (String option : options) {
            JToggleButton toggleButton = new JToggleButton(option);
            toggleButton.setMaximumSize(new Dimension(200, 40));
            toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            toggleButton.setBackground(new Color(57, 62, 70));
            toggleButton.setForeground(Color.WHITE);
            toggleButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
            toggleButton.setFocusPainted(false);
            toggleButtons.add(toggleButton);
            optionsPanel.add(Box.createVerticalStrut(10));
            optionsPanel.add(toggleButton);
        }

        JButton nextButton = new JButton("Next");
        nextButton.setPreferredSize(new Dimension(100, 30));
        nextButton.setBackground(new Color(0, 173, 181));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        nextButton.setFocusPainted(false);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(nextButton);

        nextButton.addActionListener(e -> {
            List<String> selectedOptions = new ArrayList<>();
            for (JToggleButton toggleButton : toggleButtons) {
                if (toggleButton.isSelected()) {
                    selectedOptions.add(toggleButton.getText());
                }
            }
            if (selectedOptions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select at least one option.");
            } else {
                frame.dispose();
                new PropertyPage(selectedOptions);
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HomePage();
    }
}
