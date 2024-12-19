package user.Forget_Page;

import expense.Connection.connectdb;
import user.Login_Page.LoginPage;
import user.user_class.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import java.awt.Cursor;

public class ForgetpassController {
    private ForgetpassView view;
    private ForgetpassPage forgetpassPage;
    private User user;
    private Connection conn = new connectdb().getconnectdb();
    UserRepository userRepository = new UserRepository(conn);

    public ForgetpassController(ForgetpassView view, ForgetpassPage forgetpassPage) {
        this.view = view;
        this.forgetpassPage = forgetpassPage;

        setupButtonActions();
        setupHoverEffects();
    }

    private void setupButtonActions() {
        view.getSubmitButton().addActionListener(_ -> resetPassword());
        view.getBackButton().addActionListener(_ -> openLoginPage());
    }

    private void setupHoverEffects() {
        view.getSubmitButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                view.getSubmitButton().setBackground(ForgetPassConstants.SUBMIT_BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                view.getSubmitButton().setBackground(ForgetPassConstants.SUBMIT_BUTTON_BACKGROUND_COLOR);
            }
        });
        view.getBackButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                view.getBackButton().setForeground(ForgetPassConstants.BACK_BUTTON_HOVER_COLOR);
                view.getBackButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                view.getBackButton().setForeground(ForgetPassConstants.BACK_BUTTON_COLOR);
                view.getBackButton().setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    private void resetPassword() {
        String username = view.getUsernameEntry().getText();
        String password = new String(view.getPasswordEntry().getPassword());
        String password2 = new String(view.getPassword2Entry().getPassword());

        if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!password.equals(password2)) {
            showError("Passwords do not match.");
            return;
        }
        try {
            user = new User(username, password);
            userRepository.forgetpass(user);
            JOptionPane.showMessageDialog(forgetpassPage, "Password updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            openLoginPage();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Connection error. Try again.");
        }
    }

    private void openLoginPage() {
        forgetpassPage.dispose();
        new LoginPage().setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(forgetpassPage, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}