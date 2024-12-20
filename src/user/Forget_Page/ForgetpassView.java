package user.Forget_Page;

import javax.swing.*;
import java.awt.*;

public class ForgetpassView extends JPanel {
        private JTextField usernameEntry;
        private JPasswordField passwordEntry;
        private JPasswordField password2Entry;
        private JButton submitButton;
        private JButton backButton;

        public ForgetpassView() {
                setOpaque(false);
                setLayout(null);

                JLabel heading = createLabel("Reset Password", ForgetPassConstants.HEADING_FONT,
                                ForgetPassConstants.HEADING_COLOR,
                                ForgetPassConstants.HEADING_X, ForgetPassConstants.HEADING_Y,
                                ForgetPassConstants.HEADING_WIDTH,
                                ForgetPassConstants.HEADING_HEIGHT);
                add(heading);

                JLabel usernameLabel = createLabel("Username", ForgetPassConstants.LABEL_FONT,
                                ForgetPassConstants.LABEL_COLOR,
                                ForgetPassConstants.USERNAME_LABEL_X, ForgetPassConstants.USERNAME_LABEL_Y,
                                ForgetPassConstants.LABEL_WIDTH, ForgetPassConstants.LABEL_HEIGHT);
                add(usernameLabel);
                usernameEntry = createTextField(ForgetPassConstants.ENTRY_FONT, ForgetPassConstants.ENTRY_TEXT_COLOR,
                                ForgetPassConstants.ENTRY_BACKGROUND_COLOR,
                                ForgetPassConstants.USERNAME_ENTRY_X, ForgetPassConstants.USERNAME_ENTRY_Y,
                                ForgetPassConstants.ENTRY_WIDTH, ForgetPassConstants.ENTRY_HEIGHT);
                add(usernameEntry);
                JPanel underlinePanel1 = createUnderlinePanel(ForgetPassConstants.USERNAME_UNDERLINE_X,
                                ForgetPassConstants.USERNAME_UNDERLINE_Y, ForgetPassConstants.ENTRY_WIDTH,
                                ForgetPassConstants.UNDERLINE_HEIGHT);
                add(underlinePanel1);

                JLabel passwordLabel = createLabel("Password", ForgetPassConstants.LABEL_FONT,
                                ForgetPassConstants.LABEL_COLOR,
                                ForgetPassConstants.PASSWORD_LABEL_X, ForgetPassConstants.PASSWORD_LABEL_Y,
                                ForgetPassConstants.LABEL_WIDTH, ForgetPassConstants.LABEL_HEIGHT);
                add(passwordLabel);
                passwordEntry = createPasswordField(ForgetPassConstants.ENTRY_FONT,
                                ForgetPassConstants.ENTRY_TEXT_COLOR,
                                ForgetPassConstants.ENTRY_BACKGROUND_COLOR,
                                ForgetPassConstants.PASSWORD_ENTRY_X, ForgetPassConstants.PASSWORD_ENTRY_Y,
                                ForgetPassConstants.ENTRY_WIDTH, ForgetPassConstants.ENTRY_HEIGHT);
                add(passwordEntry);
                JPanel underlinePanel2 = createUnderlinePanel(ForgetPassConstants.PASSWORD_UNDERLINE_X,
                                ForgetPassConstants.PASSWORD_UNDERLINE_Y, ForgetPassConstants.ENTRY_WIDTH,
                                ForgetPassConstants.UNDERLINE_HEIGHT);
                add(underlinePanel2);

                JLabel password2Label = createLabel("Check Password", ForgetPassConstants.LABEL_FONT,
                                ForgetPassConstants.LABEL_COLOR,
                                ForgetPassConstants.PASSWORD2_LABEL_X, ForgetPassConstants.PASSWORD2_LABEL_Y,
                                ForgetPassConstants.LABEL_WIDTH, ForgetPassConstants.LABEL_HEIGHT);
                add(password2Label);
                password2Entry = createPasswordField(ForgetPassConstants.ENTRY_FONT,
                                ForgetPassConstants.ENTRY_TEXT_COLOR,
                                ForgetPassConstants.ENTRY_BACKGROUND_COLOR,
                                ForgetPassConstants.PASSWORD2_ENTRY_X, ForgetPassConstants.PASSWORD2_ENTRY_Y,
                                ForgetPassConstants.ENTRY_WIDTH, ForgetPassConstants.ENTRY_HEIGHT);
                add(password2Entry);
                JPanel underlinePanel3 = createUnderlinePanel(ForgetPassConstants.PASSWORD2_UNDERLINE_X,
                                ForgetPassConstants.PASSWORD2_UNDERLINE_Y, ForgetPassConstants.ENTRY_WIDTH,
                                ForgetPassConstants.UNDERLINE_HEIGHT);
                add(underlinePanel3);

                submitButton = createButton("Submit", ForgetPassConstants.SUBMIT_BUTTON_FONT,
                                ForgetPassConstants.SUBMIT_BUTTON_TEXT_COLOR,
                                ForgetPassConstants.SUBMIT_BUTTON_BACKGROUND_COLOR,
                                ForgetPassConstants.SUBMIT_BUTTON_X, ForgetPassConstants.SUBMIT_BUTTON_Y,
                                ForgetPassConstants.SUBMIT_BUTTON_WIDTH, ForgetPassConstants.SUBMIT_BUTTON_HEIGHT);
                add(submitButton);

                backButton = createButton("", ForgetPassConstants.BACK_BUTTON_FONT,
                                ForgetPassConstants.BACK_BUTTON_COLOR,
                                Color.WHITE,
                                ForgetPassConstants.BACK_BUTTON_X, ForgetPassConstants.BACK_BUTTON_Y,
                                ForgetPassConstants.BACK_BUTTON_WIDTH, ForgetPassConstants.BACK_BUTTON_HEIGHT);
                backButton.setContentAreaFilled(false);
                backButton.setBorderPainted(false);
                backButton.setFocusPainted(false);
                backButton.setOpaque(false);
                add(backButton);
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

        public JButton getSubmitButton() {
                return submitButton;
        }

        public JButton getBackButton() {
                return backButton;
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

        private JPanel createUnderlinePanel(int x, int y, int width, int height) {
                JPanel underlinePanel = new JPanel();
                underlinePanel.setBounds(x, y, width, height);
                underlinePanel.setBackground(ForgetPassConstants.UNDERLINE_COLOR);
                return underlinePanel;
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
}
