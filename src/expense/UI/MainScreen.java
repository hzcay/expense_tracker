package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import user.LoginPage;
import user.User;

import java.awt.*;

public class MainScreen extends JFrame {

    private JButton activeButton;

    public MainScreen(User user) {
        setTitle("Expense Tracking");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("resources/expense.png"); // Đường dẫn đến icon
            setIconImage(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBackground(new Color(240, 240, 255));
        add(content, BorderLayout.CENTER);

        // Create panels
        ExpenseDashboard dashboard = new ExpenseDashboard(user);

        // Default view
        content.add(dashboard, BorderLayout.CENTER);

        // Get all buttons from sidebar
        JButton[] buttons = new JButton[sidebar.getComponentCount()];
        int buttonIndex = 0;
        for (Component comp : sidebar.getComponents()) {
            if (comp instanceof JButton) {
                buttons[buttonIndex++] = (JButton) comp;
            }
        }
        JPanel[] currentPanel = { dashboard };

        buttons[0].addActionListener(_ -> {
            content.removeAll();
            currentPanel[0] = new ExpenseDashboard(user);
            content.add(currentPanel[0], BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        });

        buttons[1].addActionListener(_ -> {
            content.removeAll();
            currentPanel[0] = new Extrack(user);
            content.add(currentPanel[0], BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        });

        buttons[2].addActionListener(_ -> {
            content.removeAll();
            currentPanel[0] = new Analyticspanel(user);
            content.add(currentPanel[0], BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        });

        buttons[3].addActionListener(_ -> {
            content.removeAll();
            currentPanel[0] = new CalendarPanel(user);
            content.add(currentPanel[0], BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        });

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        Color sidebarColor = new Color(28, 35, 51);
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Logo section remains the same
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(sidebarColor);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.X_AXIS));

        JLabel logoIcon = new JLabel(" ");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        logoIcon.setForeground(new Color(82, 186, 255));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoText = new JLabel("Expenses Tracker");
        logoText.setFont(new Font("Product Sans", Font.BOLD, 24));
        logoText.setForeground(new Color(240, 240, 255));
        logoText.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(logoText);
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(40));

        String[][] menuItems = {
                { "Dashboard", "#82BAFF" },
                { "Expenses", "#82BAFF" },
                { "Analytics", "#82BAFF" },
                { "Calendar", "#82BAFF" },
        };

        JButton[] buttons = new JButton[menuItems.length];
        for (int i = 0; i < menuItems.length; i++) {
            buttons[i] = createModernMenuButton(menuItems[i][0]);
            sidebar.add(Box.createVerticalStrut(12));
            sidebar.add(buttons[i]);
        }

        // Set initial active button
        activeButton = buttons[0];
        activeButton.setBackground(new Color(82, 186, 255));
        activeButton.setForeground(Color.WHITE);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = createModernMenuButton("🚪 Logout");
        logoutButton.removeActionListener(logoutButton.getActionListeners()[0]); // Remove the color change listener
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(logoutButton);
        sidebar.add(Box.createVerticalStrut(20));
        // Add logout functionality with custom styled dialog
        logoutButton.addActionListener(_ -> {
            // Create custom dialog styling
            Object[] options = { "Yes", "No" };
            JOptionPane pane = new JOptionPane(
                    "Are you sure you want to logout?",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION,
                    null,
                    options,
                    options[1] // Default to "No"
            );

            // Style the dialog
            pane.setBackground(new Color(240, 240, 255));
            UIManager.put("OptionPane.messageFont", new Font("Product Sans", Font.PLAIN, 16));
            UIManager.put("Button.font", new Font("Product Sans", Font.BOLD, 14));

            // Create and show dialog
            JDialog dialog = pane.createDialog(this, "Logout");
            dialog.setAlwaysOnTop(true);
            dialog.setModal(true);
            dialog.setVisible(true);

            // Handle result
            Object selectedValue = pane.getValue();
            if (selectedValue != null && selectedValue.equals(options[0])) { // "Yes" selected
                setVisible(false);
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new LoginPage().setVisible(true);
                });
            }
        });

        return sidebar;
    }

    private JButton createModernMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (this == activeButton) {
                    g2d.setColor(new Color(82, 186, 255)); // Active button color
                } else if (getModel().isPressed()) {
                    g2d.setColor(new Color(28, 35, 51)); // Changed to match sidebar
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(28, 35, 51)); // Changed to match sidebar
                } else {
                    g2d.setColor(new Color(28, 35, 51)); // Changed to match sidebar
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(new Color(200, 215, 235));
        button.setFont(new Font("Product Sans", Font.PLAIN, 16));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != activeButton) {
                    button.setForeground(new Color(82, 186, 255));
                }
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != activeButton) {
                    button.setForeground(new Color(200, 215, 235));
                }
                button.repaint();
            }
        });

        button.addActionListener(_ -> {
            if (activeButton != null) {
                activeButton.setForeground(new Color(200, 215, 235));
            }
            activeButton = button;
            button.setForeground(Color.WHITE);
            button.repaint();
        });

        button.setMaximumSize(new Dimension(240, 45));
        return button;
    }

}
