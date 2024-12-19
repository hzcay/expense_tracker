package user;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.awt.image.*;

import expense.Connection.connectdb;

public class Forgetpass extends JFrame {
    private User user;
    private Connection conn = new connectdb().getconnectdb();
    private JTextField usernameEntry;
    private JPasswordField passwordEntry, password2Entry;

    public Forgetpass() {
        setTitle("Expense Tracker - Forgot Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1193, 695);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("resources/expense.png");
            setIconImage(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedImage bgImage = ImageIO.read(new File("resources/bg3.jpg"));
            ImageIcon bgIcon = new ImageIcon(bgImage);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setBounds(0, 0, bgImage.getWidth(), bgImage.getHeight());
            getLayeredPane().add(bgLabel, Integer.valueOf(Integer.MIN_VALUE));

            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(null);
            setContentPane(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel heading = new JLabel("Reset Password");
        heading.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 23));
        heading.setForeground(Color.decode("#F78C6A"));
        heading.setBackground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBounds(770, 120, 200, 30);
        add(heading);

        JLabel usernamelabel = new JLabel("Username");
        usernamelabel.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18));
        usernamelabel.setForeground(Color.decode("#F78C6A"));
        usernamelabel.setBackground(Color.WHITE);
        usernamelabel.setOpaque(true);
        usernamelabel.setBounds(710, 180, 200, 25);
        add(usernamelabel);

        usernameEntry = new JTextField(30);
        usernameEntry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18));
        usernameEntry.setForeground(Color.WHITE);
        usernameEntry.setBackground(Color.decode("#F78C6A"));
        usernameEntry.setOpaque(true);
        usernameEntry.setBorder(null);
        usernameEntry.setBounds(710, 205, 300, 24);
        add(usernameEntry);

        JPanel underlinePanel1 = new JPanel();
        underlinePanel1.setBounds(710, 232, 300, 3);
        underlinePanel1.setBackground(Color.decode("#F78C6A"));
        add(underlinePanel1);

        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18));
        passwordlabel.setForeground(Color.decode("#F78C6A"));
        passwordlabel.setBackground(Color.WHITE);
        passwordlabel.setOpaque(true);
        passwordlabel.setBounds(710, 245, 200, 25);
        add(passwordlabel);

        passwordEntry = new JPasswordField(30);
        passwordEntry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18));
        passwordEntry.setForeground(Color.WHITE);
        passwordEntry.setBackground(Color.decode("#F78C6A"));
        passwordEntry.setOpaque(true);
        passwordEntry.setBorder(BorderFactory.createEmptyBorder());
        passwordEntry.setBounds(710, 270, 300, 24);
        add(passwordEntry);

        JPanel underlinePanel2 = new JPanel();
        underlinePanel2.setBounds(710, 297, 300, 3);
        underlinePanel2.setBackground(Color.decode("#F78C6A"));
        add(underlinePanel2);

        JLabel password2label = new JLabel("Check Password");
        password2label.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18));
        password2label.setForeground(Color.decode("#F78C6A"));
        password2label.setBackground(Color.WHITE);
        password2label.setOpaque(true);
        password2label.setBounds(710, 310, 200, 25);
        add(password2label);

        password2Entry = new JPasswordField(30);
        password2Entry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18));
        password2Entry.setForeground(Color.WHITE);
        password2Entry.setBackground(Color.decode("#F78C6A"));
        password2Entry.setOpaque(true);
        password2Entry.setBorder(BorderFactory.createEmptyBorder());
        password2Entry.setBounds(710, 335, 300, 24);
        add(password2Entry);

        JPanel underlinePanel3 = new JPanel();
        underlinePanel3.setBounds(710, 362, 300, 3);
        underlinePanel3.setBackground(Color.decode("#F78C6A"));
        add(underlinePanel3);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Montserrat", Font.BOLD, 30));
        submitButton.setForeground(Color.decode("#FFFFFF"));
        submitButton.setBackground(Color.decode("#F78C6A"));
        submitButton.setBounds(735, 390, 250, 40);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.setHorizontalAlignment(SwingConstants.CENTER);
        submitButton.setVerticalAlignment(SwingConstants.CENTER);
        submitButton.setBorder(null);
        submitButton.setFocusPainted(false);
        submitButton.setContentAreaFilled(true);

        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(Color.decode("#F1A07A"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(Color.decode("#F78C6A"));
            }
        });

        this.getContentPane().add(submitButton);
        this.getRootPane().setDefaultButton(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forgotpassword();
            }
        });

        JButton backButton = new JButton("");
        backButton.setFont(new Font("Montserrat", Font.BOLD, 20));
        backButton.setForeground(Color.decode("#F78C6A"));
        backButton.setBounds(700, 430, 330, 40);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.decode("#F1A07A"));
                backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.decode("#F78C6A"));
                backButton.setCursor(Cursor.getDefaultCursor());
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });

        this.getContentPane().add(backButton);
        setVisible(true);
    }

    public void forgotpassword() {
        String username = usernameEntry.getText();
        String password = new String(passwordEntry.getPassword());
        String password2 = new String(password2Entry.getPassword());

        if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(password2)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            user = new User(username, password);
            user.forgetpass(conn);
            JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginPage();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Connection error. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
