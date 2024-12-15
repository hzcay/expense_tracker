package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainScreen extends JFrame {
    public MainScreen() {
        setTitle("Expense Tracking");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("resources/expense.png"); // Đường dẫn đến icon
            setIconImage(icon); // Đặt icon
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Main content
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBackground(new Color(240, 240, 255)); // Soft white background
        add(content, BorderLayout.CENTER);

        // Create and add ExpenseDashboard panel
        ExpenseDashboard dashboard = new ExpenseDashboard();
        content.add(dashboard, BorderLayout.CENTER);

        // Add action listener for the Dashboard button
        JButton[] buttons = new JButton[sidebar.getComponentCount()];
        int buttonIndex = 0;
        for (Component comp : sidebar.getComponents()) {
            if (comp instanceof JButton) {
                buttons[buttonIndex++] = (JButton) comp;
            }
        }

        buttons[0].addActionListener(_ -> {
            content.removeAll();
            content.add(dashboard, BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        });
        buttons[1].addActionListener(_ -> {
            content.removeAll();
            content.add(new Extrack(), BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        });

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        // Modern gradient background with a richer blue
        Color sidebarColor = new Color(28, 35, 51); // Darker, more professional blue
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Enhanced Logo section with modern design
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(sidebarColor);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.X_AXIS));

        JLabel logoIcon = new JLabel("      ");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        logoIcon.setForeground(new Color(82, 186, 255)); // Brighter blue for contrast
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoText = new JLabel("Expenses");
        logoText.setFont(new Font("Product Sans", Font.BOLD, 24));
        logoText.setForeground(new Color(240, 240, 255)); // Soft white
        logoText.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(logoText);
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(40));

        // Modern menu items with enhanced colors
        String[][] menuItems = {
                { "📊 Dashboard", "#82BAFF" },
                { "💸 Expenses", "#82BAFF" },
                { "📈 Analytics", "#82BAFF" },
                { "📅 Calendar", "#82BAFF" },
                { "🏷️ Categories", "#82BAFF" },
                { "⚙️ Settings", "#82BAFF" }
        };

        for (String[] item : menuItems) {
            JButton button = createModernMenuButton(item[0]);
            sidebar.add(Box.createVerticalStrut(12));
            sidebar.add(button);
        }

        return sidebar;
    }

    private JButton createModernMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(37, 44, 61)); // Darker shade when pressed
                } else if (getModel().isRollover()) {
                    g2d.setPaint(new GradientPaint(0, 0, new Color(45, 55, 72),
                            getWidth(), getHeight(), new Color(52, 63, 83))); // Gradient on hover
                } else {
                    g2d.setColor(new Color(37, 47, 63)); // Base color
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(new Color(200, 215, 235)); // Softer text color
        button.setFont(new Font("Product Sans", Font.PLAIN, 16));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Enhanced hover effects with smoother color transition
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(82, 186, 255)); // Bright blue on hover
                button.putClientProperty("hover", true);
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(200, 215, 235)); // Back to original color
                button.putClientProperty("hover", false);
                button.repaint();
            }
        });

        button.setMaximumSize(new Dimension(240, 45));
        return button;
    }

    public static void main(String[] args) {
        new MainScreen();
    }
}
