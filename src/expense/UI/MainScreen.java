package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import user.Login_Page.LoginPage;
import user.user_class.User;

public class MainScreen extends JFrame {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int SIDEBAR_WIDTH = 280;
    private static final int BUTTON_SPACING = 12;
    private static final int BORDER_SPACING = 30;
    private static final int TEXT_BORDER = 10;
    private static final int LOGOUT_BUTTON_SPACING = 20;

    private static final Color SIDEBAR_COLOR = new Color(28, 35, 51);
    private static final Color TEXT_COLOR_PRIMARY = new Color(240, 240, 255);
    private static final Color TEXT_COLOR_SECONDARY = new Color(200, 215, 235);
    private static final Color BUTTON_COLOR_ACTIVE = new Color(82, 186, 255);
    private static final Color CONTENT_BACKGROUND_COLOR = new Color(240, 240, 255);

    private static final String ICON_PATH = "resources/expense.png";

    private static final String LOGO_TEXT = "Expenses Tracker";
    private static final String LOGOUT_TEXT = "🚪 Logout";

    private static final String[] MENU_ITEMS_TEXT = {
            "Dashboard",
            "Expenses",
            "Analytics",
            "Calendar"
    };

    private static final String CONFIRM_LOGOUT_MESSAGE = "Are you sure you want to logout?";
    private static final String LOGOUT_DIALOG_TITLE = "Logout";
    private static final Object[] LOGOUT_OPTIONS = { "Yes", "No" };
    private static final int LOGOUT_DEFAULT_OPTION = 1;

    private JPanel contentPanel;
    private JButton activeButton;
    private User currentUser;

    public MainScreen(User user) {
        this.currentUser = user;
        setupFrame();
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        setupContentPanel();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Expense Tracking");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIcon();
    }

    private void setIcon() {
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(ICON_PATH);
            setIconImage(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(CONTENT_BACKGROUND_COLOR);
        add(contentPanel, BorderLayout.CENTER);
        showPanel(new ExpenseDashboard(currentUser));
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(BORDER_SPACING, BORDER_SPACING, BORDER_SPACING, BORDER_SPACING));

        JPanel logoPanel = createLogoPanel();
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(40));

        JButton[] menuButtons = createMenuButtons(sidebar);

        activeButton = menuButtons[0];
        activeButton.setBackground(BUTTON_COLOR_ACTIVE);
        activeButton.setForeground(Color.WHITE);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = createLogoutButton();
        sidebar.add(Box.createVerticalStrut(BUTTON_SPACING));
        sidebar.add(logoutButton);
        sidebar.add(Box.createVerticalStrut(LOGOUT_BUTTON_SPACING));

        return sidebar;
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR_COLOR);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.X_AXIS));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoText = new JLabel(LOGO_TEXT);
        logoText.setFont(new Font("Product Sans", Font.BOLD, 24));
        logoText.setForeground(TEXT_COLOR_PRIMARY);
        logoText.setBorder(BorderFactory.createEmptyBorder(0, TEXT_BORDER, 0, TEXT_BORDER));
        logoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(logoText);
        return logoPanel;
    }

    private JButton[] createMenuButtons(JPanel sidebar) {
        JButton[] buttons = new JButton[MENU_ITEMS_TEXT.length];
        for (int i = 0; i < MENU_ITEMS_TEXT.length; i++) {
            buttons[i] = createModernMenuButton(MENU_ITEMS_TEXT[i]);
            sidebar.add(Box.createVerticalStrut(BUTTON_SPACING));
            sidebar.add(buttons[i]);

            final int index = i;
            buttons[i].addActionListener(_ -> handleMenuButtonClick(index));
        }
        return buttons;
    }

    private void handleMenuButtonClick(int index) {
        contentPanel.removeAll();
        JPanel panel = null;

        switch (index) {
            case 0:
                panel = new ExpenseDashboard(currentUser);
                break;
            case 1:
                panel = new Extrack(currentUser);
                break;
            case 2:
                panel = new Analyticspanel(currentUser);
                break;
            case 3:
                panel = new CalendarPanel(currentUser);
                break;
            default:
                panel = new JPanel();
                break;
        }

        showPanel(panel);
    }

    private void showPanel(JPanel panel) {
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JButton createLogoutButton() {
        JButton logoutButton = createModernMenuButton(LOGOUT_TEXT);
        logoutButton.removeActionListener(logoutButton.getActionListeners()[0]);

        logoutButton.addActionListener(_ -> confirmLogout());
        return logoutButton;
    }

    private void confirmLogout() {

        JOptionPane pane = new JOptionPane(
                CONFIRM_LOGOUT_MESSAGE,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                LOGOUT_OPTIONS,
                LOGOUT_OPTIONS[LOGOUT_DEFAULT_OPTION]);

        pane.setBackground(CONTENT_BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageFont", new Font("Product Sans", Font.PLAIN, 16));
        UIManager.put("Button.font", new Font("Product Sans", Font.BOLD, 14));

        JDialog dialog = pane.createDialog(this, LOGOUT_DIALOG_TITLE);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setVisible(true);

        Object selectedValue = pane.getValue();
        if (selectedValue != null && selectedValue.equals(LOGOUT_OPTIONS[0])) {
            logout();
        }
    }

    private void logout() {
        setVisible(false);
        dispose();
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }

    private JButton createModernMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (this == activeButton) {
                    g2d.setColor(BUTTON_COLOR_ACTIVE);
                } else if (getModel().isPressed()) {
                    g2d.setColor(SIDEBAR_COLOR);
                } else if (getModel().isRollover()) {
                    g2d.setColor(SIDEBAR_COLOR);
                } else {
                    g2d.setColor(SIDEBAR_COLOR);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(TEXT_COLOR_SECONDARY);
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
                    button.setForeground(BUTTON_COLOR_ACTIVE);
                }
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != activeButton) {
                    button.setForeground(TEXT_COLOR_SECONDARY);
                }
                button.repaint();
            }
        });

        button.addActionListener(_ -> {
            if (activeButton != null) {
                activeButton.setForeground(TEXT_COLOR_SECONDARY);
            }
            activeButton = button;
            button.setForeground(Color.WHITE);
            button.repaint();
        });
        button.setMaximumSize(new Dimension(240, 45));
        return button;
    }
}