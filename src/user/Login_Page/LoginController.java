package user.Login_Page;

import expense.Connection.connectdb;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import expense.UI.MainScreen;
import user.Signup_Page.SignupPage;
import user.user_class.User;
import user.user_class.UserRepository;
import user.Forget_Page.ForgetpassPage;

public class LoginController {
    private LoginView view;
    private LoginPage loginPage;
    private User user;
    private Connection conn = new connectdb().getconnectdb();
    UserRepository userRepository = new UserRepository(conn);

    public LoginController(LoginView view, LoginPage loginPage) {
        this.view = view;
        this.loginPage = loginPage;

        setupButtonActions();
        setupHoverEffects();
    }

    private void setupButtonActions() {
        view.getLoginButton().addActionListener(_ -> loginUser());
        view.getForgotPasswordButton().addActionListener(_ -> openForgotPasswordPage());
        view.getNewAccountButton().addActionListener(_ -> openSignupPage());
    }

    private void setupHoverEffects() {
        view.getLoginButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                view.getLoginButton().setBackground(LoginConstants.LOGIN_BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                view.getLoginButton().setBackground(LoginConstants.LOGIN_BUTTON_BACKGROUND_COLOR);
            }
        });
    }

    private void loginUser() {
        String username = view.getUsernameField().getText();
        String password = new String(view.getPasswordField().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("All fields are required.");
            return;
        }
        try {
            user = new User(username, password);
            user = userRepository.getUser(username, password);

            if (user == null) {
                showError("Invalid username or password");
            } else {
                JOptionPane.showMessageDialog(loginPage, "Login successful", "Welcome",
                        JOptionPane.INFORMATION_MESSAGE);
                user.setLoggedIn(true);
                loginPage.dispose();
                MainScreen mainScreen = new MainScreen(user);
                mainScreen.setVisible(true);
            }
        } catch (SQLException ex) {
            showError("Connection error. Try again.");
        }
    }

    private void openForgotPasswordPage() {
        loginPage.dispose();
        new ForgetpassPage().setVisible(true);
    }

    private void openSignupPage() {
        loginPage.dispose();
        new SignupPage().setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(loginPage, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}