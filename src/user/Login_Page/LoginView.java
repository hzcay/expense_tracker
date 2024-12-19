package user.Login_Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginView extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JButton newAccountButton;

    public LoginView() {
        setOpaque(false);
        setLayout(null);

        JLabel heading = createLabel("USER LOGIN", LoginConstants.HEADING_FONT, LoginConstants.HEADING_COLOR,
                LoginConstants.HEADING_X, LoginConstants.HEADING_Y, LoginConstants.HEADING_WIDTH,
                LoginConstants.HEADING_HEIGHT);
        add(heading);

        usernameField = createUsernameField();
        add(usernameField);

        passwordField = createPasswordField();
        add(passwordField);

        loginButton = createLoginButton();
        add(loginButton);

        forgotPasswordButton = createForgotPasswordButton();
        add(forgotPasswordButton);

        JLabel signupLabel = createLabel("Don't have an account?", LoginConstants.SIGNUP_LABEL_FONT,
                LoginConstants.SIGNUP_LABEL_COLOR,
                LoginConstants.SIGNUP_LABEL_X, LoginConstants.SIGNUP_LABEL_Y, LoginConstants.SIGNUP_LABEL_WIDTH,
                LoginConstants.SIGNUP_LABEL_HEIGHT);
        add(signupLabel);

        newAccountButton = createNewAccountButton();
        add(newAccountButton);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getForgotPasswordButton() {
        return forgotPasswordButton;
    }

    public JButton getNewAccountButton() {
        return newAccountButton;
    }

    private JLabel createLabel(String text, Font font, Color color, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setBounds(x, y, width, height);
        return label;
    }

    private JTextField createUsernameField() {
        JTextField usernameField = new JTextField("Username");
        usernameField.setFont(LoginConstants.USERNAME_FIELD_FONT);
        usernameField.setForeground(LoginConstants.USERNAME_FIELD_COLOR);
        usernameField.setBounds(LoginConstants.USERNAME_FIELD_X, LoginConstants.USERNAME_FIELD_Y,
                LoginConstants.USERNAME_FIELD_WIDTH, LoginConstants.USERNAME_FIELD_HEIGHT);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));

        JPanel underlinePanel = new JPanel();
        underlinePanel.setBounds(LoginConstants.USERNAME_UNDERLINE_X, LoginConstants.USERNAME_UNDERLINE_Y,
                LoginConstants.USERNAME_UNDERLINE_WIDTH, LoginConstants.UNDERLINE_HEIGHT);
        underlinePanel.setBackground(LoginConstants.UNDERLINE_COLOR);
        add(underlinePanel);

        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                }
                underlinePanel.setBackground(LoginConstants.UNDERLINE_COLOR);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Username");
                }
                underlinePanel.setBackground(LoginConstants.UNDERLINE_COLOR);
            }
        });
        return usernameField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.setFont(LoginConstants.PASSWORD_FIELD_FONT);
        passwordField.setForeground(LoginConstants.PASSWORD_FIELD_COLOR);
        passwordField.setBounds(LoginConstants.PASSWORD_FIELD_X, LoginConstants.PASSWORD_FIELD_Y,
                LoginConstants.PASSWORD_FIELD_WIDTH, LoginConstants.PASSWORD_FIELD_HEIGHT);
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));

        JPanel underlinePanel = new JPanel();
        underlinePanel.setBounds(LoginConstants.PASSWORD_UNDERLINE_X, LoginConstants.PASSWORD_UNDERLINE_Y,
                LoginConstants.PASSWORD_UNDERLINE_WIDTH, LoginConstants.UNDERLINE_HEIGHT);
        underlinePanel.setBackground(LoginConstants.UNDERLINE_COLOR);
        add(underlinePanel);

        ImageIcon closeEyeIcon = new ImageIcon(LoginConstants.CLOSE_EYE_ICON_PATH);
        Image closeImg = closeEyeIcon.getImage();
        Image scaledCloseImg = closeImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        closeEyeIcon.setImage(scaledCloseImg);

        ImageIcon openEyeIcon = new ImageIcon(LoginConstants.OPEN_EYE_ICON_PATH);
        Image img = openEyeIcon.getImage();
        Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        openEyeIcon.setImage(scaledImg);

        JButton eyeButton = new JButton(closeEyeIcon);
        eyeButton.setBounds(LoginConstants.EYE_BUTTON_X, LoginConstants.EYE_BUTTON_Y, 30, 30);
        eyeButton.setBorder(BorderFactory.createEmptyBorder());
        eyeButton.setContentAreaFilled(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        eyeButton.addActionListener(_ -> {
            if (passwordField.getEchoChar() == (char) 0) {
                passwordField.setEchoChar('*');
                eyeButton.setIcon(closeEyeIcon);
            } else {
                passwordField.setEchoChar((char) 0);
                eyeButton.setIcon(openEyeIcon);
            }
        });
        add(eyeButton);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                }
                underlinePanel.setBackground(LoginConstants.UNDERLINE_COLOR);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password");
                }
                underlinePanel.setBackground(LoginConstants.UNDERLINE_COLOR);
            }
        });
        return passwordField;
    }

    private JButton createLoginButton() {
        JButton loginButton = new JButton("Login");
        loginButton.setFont(LoginConstants.LOGIN_BUTTON_FONT);
        loginButton.setForeground(LoginConstants.LOGIN_BUTTON_TEXT_COLOR);
        loginButton.setBackground(LoginConstants.LOGIN_BUTTON_BACKGROUND_COLOR);
        loginButton.setBounds(LoginConstants.LOGIN_BUTTON_X, LoginConstants.LOGIN_BUTTON_Y,
                LoginConstants.LOGIN_BUTTON_WIDTH, LoginConstants.LOGIN_BUTTON_HEIGHT);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setVerticalAlignment(SwingConstants.CENTER);
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(true);
        return loginButton;
    }

    private JButton createForgotPasswordButton() {
        JButton forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(LoginConstants.FORGOT_PASSWORD_BUTTON_FONT);
        forgotPasswordButton.setForeground(LoginConstants.FORGOT_PASSWORD_BUTTON_COLOR);
        forgotPasswordButton.setBackground(LoginConstants.FORGOT_PASSWORD_BUTTON_BACKGROUND);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setBounds(LoginConstants.FORGOT_PASSWORD_BUTTON_X, LoginConstants.FORGOT_PASSWORD_BUTTON_Y,
                LoginConstants.FORGOT_PASSWORD_BUTTON_WIDTH, LoginConstants.FORGOT_PASSWORD_BUTTON_HEIGHT);
        forgotPasswordButton.setBorder(BorderFactory.createEmptyBorder());
        forgotPasswordButton.setContentAreaFilled(false);
        return forgotPasswordButton;
    }

    private JButton createNewAccountButton() {
        JButton newAccountButton = new JButton("Create new one");
        newAccountButton.setFont(LoginConstants.NEW_ACCOUNT_BUTTON_FONT);
        newAccountButton.setForeground(LoginConstants.NEW_ACCOUNT_BUTTON_COLOR);
        newAccountButton.setBackground(LoginConstants.NEW_ACCOUNT_BUTTON_BACKGROUND);
        newAccountButton.setFocusPainted(false);
        newAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newAccountButton.setBounds(LoginConstants.NEW_ACCOUNT_BUTTON_X, LoginConstants.NEW_ACCOUNT_BUTTON_Y,
                LoginConstants.NEW_ACCOUNT_BUTTON_WIDTH, LoginConstants.NEW_ACCOUNT_BUTTON_HEIGHT);
        newAccountButton.setBorder(BorderFactory.createEmptyBorder());
        newAccountButton.setContentAreaFilled(false);
        return newAccountButton;
    }
}