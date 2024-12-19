package user.Signup_Page;

import expense.Connection.connectdb;
import user.Login_Page.LoginPage;
import user.user_class.User;
import user.user_class.UserRepository;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;

public class SignupController {
    private SignupView view;
    private SignupPage signupPage;
    private User user;
    private Connection conn = new connectdb().getconnectdb();
    UserRepository userRepository = new UserRepository(conn);

    public SignupController(SignupView view, SignupPage signupPage) {
        this.view = view;
        this.signupPage = signupPage;

        setupButtonActions();
        setupHoverEffects();
    }

    private void setupButtonActions() {
        view.getSignupButton().addActionListener(_ -> signupUser());
        view.getLoginButton().addActionListener(_ -> openLoginPage());
    }

    private void setupHoverEffects() {
        view.getSignupButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                view.getSignupButton().setBackground(SignupConstants.SIGNUP_BUTTON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                view.getSignupButton().setBackground(SignupConstants.SIGNUP_BUTTON_BACKGROUND);
            }
        });
    }

    private void signupUser() {
        String email = view.getEmailEntry().getText();
        String username = view.getUsernameEntry().getText();
        String password = new String(view.getPasswordEntry().getPassword());
        String password2 = new String(view.getPassword2Entry().getPassword());

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            showError("All fields are required.");
            return;
        }
        if (!password.equals(password2)) {
            showError("Passwords do not match.");
            return;
        }
        if (!email.contains("@")) {
            showError("Invalid email address.");
            return;
        }
        if (!view.getTermsAndConditions().isSelected()) {
            showError("Please agree to the Terms & Conditions.");
            return;
        }
        try {
            user = new User(email, username, password);
            if (userRepository.setUser(user)) {
                JOptionPane.showMessageDialog(signupPage, "User created successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                signupPage.dispose();
                new LoginPage().setVisible(true);
            } else {
                showError("User already exists.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("An error occurred. Please try again.");
        }
    }

    private void openLoginPage() {
        signupPage.dispose();
        new LoginPage().setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(signupPage, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
