package user;

import javax.imageio.ImageIO;
import javax.swing.*;

import expense.ExpenseDashboard;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.awt.image.*;

public class LoginPage extends JFrame {
    private User user;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton forgotPasswordButton;

    public LoginPage() {
        setTitle("Expense Tracker - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1193, 695);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        setIconImage(Toolkit.getDefaultToolkit().getImage("resources/expense.png"));

        try {
            BufferedImage bgImage = ImageIO.read(new File("resources/bg1.jpg"));
            JLabel bgLabel = new JLabel(new ImageIcon(bgImage));
            bgLabel.setBounds(0, 0, bgImage.getWidth(), bgImage.getHeight());
            getLayeredPane().add(bgLabel, Integer.valueOf(Integer.MIN_VALUE));

            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(null);
            setContentPane(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel heading = new JLabel("USER LOGIN");
        heading.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 23));
        heading.setForeground(Color.decode("#F78C6A"));
        heading.setBackground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBounds(783, 170, 200, 30);
        add(heading);

        usernameField = createUsernameField();
        add(usernameField);

        passwordField = createPasswordField();
        add(passwordField);

        JButton loginButton = createLoginButton();
        add(loginButton);
        this.getRootPane().setDefaultButton(loginButton);

        forgotPasswordButton = createForgotPasswordButton();
        add(forgotPasswordButton);

        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        signupLabel.setForeground(Color.decode("#F78C6A"));
        signupLabel.setBackground(Color.WHITE);
        signupLabel.setBounds(730, 470, 140, 20);
        signupLabel.setOpaque(true);
        add(signupLabel);

        JButton newAccountButton = createNewAccountButton();
        add(newAccountButton);

        setVisible(true);
    }

    private JButton createLoginButton() {
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Montserrat", Font.BOLD, 30));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.decode("#F78C6A"));
        loginButton.setBounds(727, 375, 250, 40);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setVerticalAlignment(SwingConstants.CENTER);
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(true);

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Color.decode("#F1A07A"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Color.decode("#F78C6A"));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginUser();
            }
        });

        loginButton.addActionListener(_ -> loginUser());

        return loginButton;
    }

    private JButton createForgotPasswordButton() {
        JButton forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(new Font("Montserrat", Font.BOLD, 12));
        forgotPasswordButton.setForeground(Color.decode("#073A4B"));
        forgotPasswordButton.setBackground(Color.WHITE);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setBounds(840, 335, 160, 30);
        forgotPasswordButton.setBorder(BorderFactory.createEmptyBorder());
        forgotPasswordButton.setContentAreaFilled(false);

        forgotPasswordButton.addActionListener(_ -> {
            dispose();
            new Forgetpass().setVisible(true);
        });

        return forgotPasswordButton;
    }

    private JButton createNewAccountButton() {
        JButton newAccountButton = new JButton("Create new one");
        newAccountButton.setFont(new Font("Montserrat", Font.BOLD, 12));
        newAccountButton.setForeground(Color.decode("#073A4B"));
        newAccountButton.setBackground(Color.WHITE);
        newAccountButton.setFocusPainted(false);
        newAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newAccountButton.setBounds(860, 470, 120, 20);
        newAccountButton.setBorder(BorderFactory.createEmptyBorder());
        newAccountButton.setContentAreaFilled(false);

        newAccountButton.addActionListener(_ -> {
            dispose();
            new SignupFrame().setVisible(true);
        });

        return newAccountButton;
    }

    /**
     * Creates a JTextField for the username with a changing underline when focused
     * and unfocused
     * 
     * @return JTextField with default text and a changing underline
     */
    private JTextField createUsernameField() {
        JTextField usernameField = new JTextField("Username");
        usernameField.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 20));
        usernameField.setForeground(Color.decode("#F78C6A"));
        usernameField.setBounds(725, 220, 250, 35);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));

        JPanel underlinePanel = new JPanel();
        underlinePanel.setBounds(725, 255, 250, 3);
        underlinePanel.setBackground(Color.decode("#F78C6A"));
        add(underlinePanel);

        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                }
                underlinePanel.setBackground(Color.decode("#F78C6A"));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Username");
                }
                underlinePanel.setBackground(Color.decode("#F78C6A"));
            }
        });

        return usernameField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 20));
        passwordField.setForeground(Color.decode("#F78C6A"));
        passwordField.setBounds(725, 290, 250, 35);
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));

        JPanel underlinePanel = new JPanel();
        underlinePanel.setBounds(725, 325, 250, 3);
        underlinePanel.setBackground(Color.decode("#F78C6A"));
        add(underlinePanel);

        final ImageIcon closeEyeIcon = new ImageIcon("resources/closeye.png");
        Image closeImg = closeEyeIcon.getImage();
        Image scaledCloseImg = closeImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        closeEyeIcon.setImage(scaledCloseImg);

        final ImageIcon openEyeIcon = new ImageIcon("resources/openeye.png");
        Image img = openEyeIcon.getImage();
        Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        openEyeIcon.setImage(scaledImg);

        JButton eyeButton = new JButton(closeEyeIcon);
        eyeButton.setBounds(975, 295, 30, 30);
        eyeButton.setBorder(BorderFactory.createEmptyBorder());
        eyeButton.setContentAreaFilled(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        eyeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordField.getEchoChar() == (char) 0) {
                    passwordField.setEchoChar('*');
                    eyeButton.setIcon(closeEyeIcon);
                } else {
                    passwordField.setEchoChar((char) 0);
                    eyeButton.setIcon(openEyeIcon);
                }
            }
        });

        add(eyeButton);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                }
                underlinePanel.setBackground(Color.decode("#F78C6A"));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password");
                }
                underlinePanel.setBackground(Color.decode("#F78C6A"));
            }
        });

        return passwordField;
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Check if the user has entered all the information
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connect to the database and check login information
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Database driver not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Initialize connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            // Check user account in the database
            user = new User(username, password);
            user = user.getUser(username, password, conn);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Login successful", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                user.setLoggedIn(true); // Mark the user as logged in
                setEmailFromDatabase(user, conn); // Get email from the database
                dispose(); // Close LoginPage
                new ExpenseDashboard().setVisible(true); // Open the main window or
                // perform other actions after successful login
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Connection error. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setEmailFromDatabase(User user, Connection conn) throws SQLException {
        String query = "SELECT email FROM account WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user.setEmail(rs.getString("email"));
                }
            }
        }
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User User) {
        this.user = User;
    }
}
