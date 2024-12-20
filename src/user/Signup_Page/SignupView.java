package user.Signup_Page;

import javax.swing.*;
import java.awt.*;

public class SignupView extends JPanel {
    private JTextField usernameEntry;
    private JPasswordField passwordEntry;
    private JPasswordField password2Entry;
    private JTextField emailEntry;
    private JCheckBox termsAndConditions;
    private JButton signupButton;
    private JButton loginButton;

    public SignupView() {
        setOpaque(false);
        setLayout(null);

        JLabel heading = createLabel("CREATE AN ACCOUNT", SignupConstants.HEADING_FONT, SignupConstants.HEADING_COLOR,
                SignupConstants.HEADING_X, SignupConstants.HEADING_Y, SignupConstants.HEADING_WIDTH,
                SignupConstants.HEADING_HEIGHT);
        add(heading, Integer.valueOf(1));

        JLabel emailLabel = createLabel("Email", SignupConstants.LABEL_FONT, SignupConstants.LABEL_COLOR,
                SignupConstants.EMAIL_LABEL_X, SignupConstants.EMAIL_LABEL_Y, SignupConstants.LABEL_WIDTH,
                SignupConstants.LABEL_HEIGHT);
        add(emailLabel);
        emailEntry = createTextField(SignupConstants.ENTRY_FONT, SignupConstants.ENTRY_TEXT_COLOR,
                SignupConstants.ENTRY_BACKGROUND_COLOR,
                SignupConstants.EMAIL_ENTRY_X, SignupConstants.EMAIL_ENTRY_Y, SignupConstants.ENTRY_WIDTH,
                SignupConstants.ENTRY_HEIGHT);
        add(emailEntry);

        JLabel usernameLabel = createLabel("Username", SignupConstants.LABEL_FONT, SignupConstants.LABEL_COLOR,
                SignupConstants.USERNAME_LABEL_X, SignupConstants.USERNAME_LABEL_Y, SignupConstants.LABEL_WIDTH,
                SignupConstants.LABEL_HEIGHT);
        add(usernameLabel);
        usernameEntry = createTextField(SignupConstants.ENTRY_FONT, SignupConstants.ENTRY_TEXT_COLOR,
                SignupConstants.ENTRY_BACKGROUND_COLOR,
                SignupConstants.USERNAME_ENTRY_X, SignupConstants.USERNAME_ENTRY_Y, SignupConstants.ENTRY_WIDTH,
                SignupConstants.ENTRY_HEIGHT);
        add(usernameEntry);

        JLabel passwordLabel = createLabel("Password", SignupConstants.LABEL_FONT, SignupConstants.LABEL_COLOR,
                SignupConstants.PASSWORD_LABEL_X, SignupConstants.PASSWORD_LABEL_Y, SignupConstants.LABEL_WIDTH,
                SignupConstants.LABEL_HEIGHT);
        add(passwordLabel);
        passwordEntry = createPasswordField(SignupConstants.ENTRY_FONT, SignupConstants.ENTRY_TEXT_COLOR,
                SignupConstants.ENTRY_BACKGROUND_COLOR,
                SignupConstants.PASSWORD_ENTRY_X, SignupConstants.PASSWORD_ENTRY_Y, SignupConstants.ENTRY_WIDTH,
                SignupConstants.ENTRY_HEIGHT);
        add(passwordEntry);

        JLabel password2Label = createLabel("Check Password", SignupConstants.LABEL_FONT, SignupConstants.LABEL_COLOR,
                SignupConstants.PASSWORD2_LABEL_X, SignupConstants.PASSWORD2_LABEL_Y, SignupConstants.LABEL_WIDTH,
                SignupConstants.LABEL_HEIGHT);
        add(password2Label);
        password2Entry = createPasswordField(SignupConstants.ENTRY_FONT, SignupConstants.ENTRY_TEXT_COLOR,
                SignupConstants.ENTRY_BACKGROUND_COLOR,
                SignupConstants.PASSWORD2_ENTRY_X, SignupConstants.PASSWORD2_ENTRY_Y, SignupConstants.ENTRY_WIDTH,
                SignupConstants.ENTRY_HEIGHT);
        add(password2Entry);

        termsAndConditions = createCheckBox("I agree to the Terms & Conditions", SignupConstants.TERMS_FONT,
                SignupConstants.TERMS_COLOR,
                SignupConstants.TERMS_X, SignupConstants.TERMS_Y, SignupConstants.TERMS_WIDTH,
                SignupConstants.TERMS_HEIGHT);
        add(termsAndConditions);

        signupButton = createButton("Sign Up", SignupConstants.SIGNUP_BUTTON_FONT,
                SignupConstants.SIGNUP_BUTTON_TEXT_COLOR, SignupConstants.SIGNUP_BUTTON_BACKGROUND,
                SignupConstants.SIGNUP_BUTTON_X, SignupConstants.SIGNUP_BUTTON_Y, SignupConstants.SIGNUP_BUTTON_WIDTH,
                SignupConstants.SIGNUP_BUTTON_HEIGHT);
        add(signupButton);

        JLabel loginLabel = createLabel("Already have an account?", SignupConstants.LOGIN_LABEL_FONT,
                SignupConstants.LOGIN_BUTTON_COLOR,
                SignupConstants.LOGIN_LABEL_X, SignupConstants.LOGIN_LABEL_Y, SignupConstants.LOGIN_LABEL_WIDTH,
                SignupConstants.LOGIN_LABEL_HEIGHT);
        add(loginLabel);

        loginButton = createButton("Login", SignupConstants.LOGIN_BUTTON_FONT, SignupConstants.LOGIN_BUTTON_COLOR,
                SignupConstants.LOGIN_BUTTON_BACKGROUND,
                SignupConstants.LOGIN_BUTTON_X, SignupConstants.LOGIN_BUTTON_Y, SignupConstants.LOGIN_BUTTON_WIDTH,
                SignupConstants.LOGIN_BUTTON_HEIGHT);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setContentAreaFilled(false);
        add(loginButton);
    }

    public JTextField getUsernameEntry() {
        return usernameEntry;
    }

    public JPasswordField getPasswordEntry() {
        return passwordEntry;
    }

    public JPasswordField getPassword2Entry() {
        return password2Entry;
    }

    public JTextField getEmailEntry() {
        return emailEntry;
    }

    public JCheckBox getTermsAndConditions() {
        return termsAndConditions;
    }

    public JButton getSignupButton() {
        return signupButton;
    }

    public JButton getLoginButton() {
        return loginButton;
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

    private JTextField createTextField(Font font, Color textColor, Color backgroundColor, int x, int y, int width,
            int height) {
        JTextField textField = new JTextField(30);
        textField.setFont(font);
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);
        textField.setOpaque(true);
        textField.setBorder(null);
        textField.setBounds(x, y, width, height);
        return textField;
    }

    private JPasswordField createPasswordField(Font font, Color textColor, Color backgroundColor, int x, int y,
            int width, int height) {
        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setFont(font);
        passwordField.setForeground(textColor);
        passwordField.setBackground(backgroundColor);
        passwordField.setOpaque(true);
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setBounds(x, y, width, height);
        return passwordField;
    }

    private JCheckBox createCheckBox(String text, Font font, Color color, int x, int y, int width, int height) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(font);
        checkBox.setForeground(color);
        checkBox.setBackground(Color.WHITE);
        checkBox.setFocusPainted(false);
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.setBounds(x, y, width, height);
        return checkBox;
    }

    private JButton createButton(String text, Font font, Color textColor, Color backgroundColor, int x, int y,
            int width, int height) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(textColor);
        button.setBackground(backgroundColor);
        button.setBounds(x, y, width, height);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sign Up");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            SignupView signupPage = new SignupView();
            frame.add(signupPage);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
