package user;

import javax.imageio.ImageIO;
import javax.swing.*;

import expense.Connection.connectdb;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.awt.image.*;

public class SignupFrame extends JFrame {
    private User user;
    private Connection conn = new connectdb().getconnectdb();
    private JTextField usernameEntry;
    private JPasswordField passwordEntry;
    private JPasswordField password2Entry;
    private JTextField emailEntry;
    private JCheckBox termsAndConditions;

    public SignupFrame() {
        setTitle("Expense Tracker - Sign up"); // Đặt tiêu đề cho cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1193, 695);
        setLocationRelativeTo(null);
        setLayout(null); // Cấm layout mặc định
        setResizable(false);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("resources/expense.png"); // Đường dẫn đến icon
            setIconImage(icon); // Đặt icon
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Đọc ảnh nền
            BufferedImage bgImage = ImageIO.read(new File("resources/bg2.jpg"));
            ImageIcon bgIcon = new ImageIcon(bgImage);
            JLabel bgLabel = new JLabel(bgIcon);

            // Đặt vị trí và kích thước cho JLabel để nó chiếm toàn bộ cửa sổ
            bgLabel.setBounds(0, 0, bgImage.getWidth(), bgImage.getHeight());

            // Thêm ảnh nền vào LayeredPane
            getLayeredPane().add(bgLabel, Integer.valueOf(Integer.MIN_VALUE));

            // Tạo JPanel để chứa các thành phần khác
            JPanel panel = new JPanel();
            panel.setOpaque(false); // Đảm bảo panel trong suốt
            panel.setLayout(null); // Cấm layout mặc định cho panel
            setContentPane(panel); // Đặt JPanel làm content pane
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tạo JLabel cho tiêu đề
        JLabel heading = new JLabel("CREATE AN ACCOUNT");
        heading.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 23)); // Đặt phông chữ
        heading.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        heading.setBackground(Color.WHITE); // Màu nền trắng
        heading.setOpaque(true); // Cần để màu nền có hiệu lực
        heading.setBounds(725, 105, 300, 30); // Vị trí và kích thước của heading
        add(heading);

        // Tạo JLabel cho email
        JLabel Emaillabel = new JLabel("Email");
        Emaillabel.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18)); // Đặt phông chữ
        Emaillabel.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        Emaillabel.setBackground(Color.WHITE); // Màu nền trắng
        Emaillabel.setOpaque(true); // Cần để màu nền có hiệu lực
        Emaillabel.setBounds(705, 155, 200, 25); // Vị trí và kích thước của heading
        add(Emaillabel);

        // Tạo JTextField tương ứng với Entry trong Tkinter
        emailEntry = new JTextField(30); // Tạo JTextField với độ rộng 30 ký tự
        emailEntry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18)); // Đặt font chữ
        emailEntry.setForeground(Color.WHITE); // Màu chữ
        emailEntry.setBackground(Color.decode("#F78C6A")); // Màu nền tương ứng với 'dark goldenrod'
        emailEntry.setOpaque(true); // Cần để màu nền có hiệu lực
        emailEntry.setBorder(null); // Loại bỏ viền xung quanh nút
        emailEntry.setBounds(705, 180, 300, 24); // Vị trí và kích thước của heading
        add(emailEntry);

        // Tạo JLabel cho Username
        JLabel usernamelabel = new JLabel("Username");
        usernamelabel.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18)); // Đặt phông chữ
        usernamelabel.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        usernamelabel.setBackground(Color.WHITE); // Màu nền trắng
        usernamelabel.setOpaque(true); // Cần để màu nền có hiệu lực
        usernamelabel.setBounds(705, 220, 200, 25); // Vị trí và kích thước của heading
        add(usernamelabel);

        // Tạo JTextField tương ứng với Entry trong Tkinter
        usernameEntry = new JTextField(30); // Tạo JTextField với độ rộng 30 ký tự
        usernameEntry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18)); // Đặt font chữ
        usernameEntry.setForeground(Color.WHITE); // Màu chữ
        usernameEntry.setBackground(Color.decode("#F78C6A")); // Màu nền tương ứng với 'dark goldenrod'
        usernameEntry.setOpaque(true); // Cần để màu nền có hiệu lực
        usernameEntry.setBorder(null); // Loại bỏ viền xung quanh nút
        usernameEntry.setBounds(705, 245, 300, 24); // Vị trí và kích thước của heading
        add(usernameEntry);

        // Tạo JLabel cho password
        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18)); // Đặt phông chữ
        passwordlabel.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        passwordlabel.setBackground(Color.WHITE); // Màu nền trắng
        passwordlabel.setOpaque(true); // Cần để màu nền có hiệu lực
        passwordlabel.setBounds(705, 285, 200, 25); // Vị trí và kích thước của heading
        add(passwordlabel);

        // Tạo JTextField tương ứng với Entry trong Tkinter
        passwordEntry = new JPasswordField(30); // Tạo JTextField với độ rộng 30 ký tự
        passwordEntry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18)); // Đặt font chữ
        passwordEntry.setForeground(Color.WHITE); // Màu chữ
        passwordEntry.setBackground(Color.decode("#F78C6A")); // Màu nền tương ứng với 'dark goldenrod'
        passwordEntry.setOpaque(true); // Cần để màu nền có hiệu lực
        passwordEntry.setBorder(BorderFactory.createEmptyBorder()); // Loại bỏ viền xung quanh
        passwordEntry.setBounds(705, 310, 300, 24); // Đặt vị trí và kích thước cho JTextField
        add(passwordEntry);

        // Tạo JLabel cho password2
        JLabel password2label = new JLabel("Check Password");
        password2label.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18)); // Đặt phông chữ
        password2label.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        password2label.setBackground(Color.WHITE); // Màu nền trắng
        password2label.setOpaque(true); // Cần để màu nền có hiệu lực
        password2label.setBounds(705, 350, 200, 25); // Vị trí và kích thước của heading
        add(password2label);

        // Tạo JTextField tương ứng với Entry trong Tkinter
        password2Entry = new JPasswordField(30); // Tạo JTextField với độ rộng 30 ký tự
        password2Entry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18)); // Đặt font chữ
        password2Entry.setForeground(Color.WHITE); // Màu chữ
        password2Entry.setBackground(Color.decode("#F78C6A")); // Màu nền tương ứng với 'dark goldenrod'
        password2Entry.setOpaque(true); // Cần để màu nền có hiệu lực
        password2Entry.setBorder(BorderFactory.createEmptyBorder()); // Loại bỏ viền xung quanh
        password2Entry.setBounds(705, 375, 300, 24); // Đặt vị trí và kích thước cho JTextField
        add(password2Entry);

        // Tạo JCheckBox
        termsAndConditions = new JCheckBox("I agree to the Terms & Conditions");
        termsAndConditions.setFont(new Font("Montserrat", Font.BOLD, 14)); // Đặt font chữ
        termsAndConditions.setForeground(Color.decode("#F78C6A")); // Màu chữ tương ứng với 'dark goldenrod'
        termsAndConditions.setBackground(Color.WHITE); // Màu nền trắng
        termsAndConditions.setFocusPainted(false); // Loại bỏ đường viền khi focus
        termsAndConditions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thêm con trỏ "hand" khi hover
        termsAndConditions.setBounds(720, 420, 300, 25); // Đặt vị trí và kích thước
        add(termsAndConditions);

        // Nút Đăng Nhập
        JButton SigninButton = new JButton("Sign Up"); // Tạo nút với nội dung là "Sign Up"
        SigninButton.setFont(new Font("Montserrat", Font.BOLD, 30)); // Cài đặt phông chữ cho nút
        SigninButton.setForeground(Color.decode("#FFFFFF")); // Màu chữ trắng (hoặc #F1F1F1 cho nhẹ nhàng hơn)
        SigninButton.setBackground(Color.decode("#F78C6A")); // Màu nền cam của nút
        SigninButton.setBounds(727, 470, 250, 40); // Vị trí và kích thước của nút
        SigninButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thay đổi con trỏ khi di chuột vào nút

        // Căn chỉnh chữ trong nút cho phù hợp
        SigninButton.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều ngang
        SigninButton.setVerticalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều dọc

        // Xóa viền và làm nút trông hiện đại hơn
        SigninButton.setBorder(null); // Loại bỏ viền xung quanh nút
        SigninButton.setFocusPainted(false); // Loại bỏ viền khi nút được chọn
        SigninButton.setContentAreaFilled(true); // Làm nền nút bao phủ toàn bộ diện tích của nó

        // Thêm hiệu ứng hover: Thay đổi màu nền của nút khi chuột di vào/ra
        SigninButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SigninButton.setBackground(Color.decode("#F1A07A")); // Màu nền sáng hơn khi chuột di vào
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SigninButton.setBackground(Color.decode("#F78C6A")); // Quay lại màu nền ban đầu khi chuột ra ngoài
            }

        });

        // Thêm nút vào giao diện
        this.getContentPane().add(SigninButton);

        // Đặt nút làm default button
        this.getRootPane().setDefaultButton(SigninButton);

        // Thêm ActionListener để xử lý sự kiện đăng nhập
        SigninButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupUser();
            }
        });

        // Tạo label "Already have an account?"
        JLabel LoginLabel = new JLabel("Already have an account?"); // Tạo label với nội dung "Don't have an account?"
        LoginLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        LoginLabel.setForeground(Color.decode("#F78C6A")); // Màu dark goldenrod
        LoginLabel.setBackground(Color.WHITE); // Màu nền trắng
        LoginLabel.setBounds(740, 520, 160, 20); // Vị trí và kích thước của label
        LoginLabel.setOpaque(true); // Đặt màu nền cho label

        // Tạo nút "Create new one"
        JButton LoginButton = new JButton("Login"); // Tạo nút với nội dung "Create new one"
        LoginButton.setFont(new Font("Montserrat", Font.BOLD, 12));
        LoginButton.setForeground(Color.decode("#073A4B")); // dark goldenrod
        LoginButton.setBackground(Color.WHITE);
        LoginButton.setFocusPainted(false);
        LoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        LoginButton.setBounds(860, 520, 120, 20); // Position and size
        LoginButton.setBorder(BorderFactory.createEmptyBorder()); // Remove the border
        LoginButton.setContentAreaFilled(false); // Optional: Makes the background transparent

        // Thêm ActionListener cho nút "Create new one"
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Đóng SignupFrame
                new LoginPage().setVisible(true); // Mở LoginFrame
            }
        });

        // Thêm label và nút vào JFrame
        add(LoginLabel);
        add(LoginButton);
    }

    private void signupUser() {
        String email = emailEntry.getText();
        String username = usernameEntry.getText();
        String password = new String(passwordEntry.getPassword());
        String password2 = new String(password2Entry.getPassword());

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(password2)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!termsAndConditions.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please agree to the Terms & Conditions.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            user = new User(email, username, password);
            user.setUser(conn);
            JOptionPane.showMessageDialog(this, "User created successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginPage().setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}