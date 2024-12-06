package user;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.awt.image.*;

public class LoginPage extends JFrame {
    private User user;
    private JTextField UsernameField;
    private JPasswordField passwordField;
    private JButton forgotPasswordButton;

    public LoginPage() {
        setTitle("Expense Tracker - Login"); // Đặt tiêu đề cho cửa sổ
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
            BufferedImage bgImage = ImageIO.read(new File("resources/bg1.jpg"));
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
        JLabel heading = new JLabel("USER LOGIN");
        heading.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 23)); // Đặt phông chữ
        heading.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        heading.setBackground(Color.WHITE); // Màu nền trắng
        heading.setOpaque(true); // Cần để màu nền có hiệu lực
        heading.setBounds(783, 170, 200, 30); // Vị trí và kích thước của heading
        add(heading);

        // Tạo các thành phần giao diện và thêm vào JFrame
        UsernameField = createUsernameField();
        add(UsernameField);

        passwordField = createPasswordField();
        add(passwordField);

        // Nút Đăng Nhập
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Montserrat", Font.BOLD, 30));
        loginButton.setForeground(Color.decode("#FFFFFF"));
        loginButton.setBackground(Color.decode("#F78C6A"));
        loginButton.setBounds(727, 375, 250, 40);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setVerticalAlignment(SwingConstants.CENTER);
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(true);

        // Hover effect and ActionListener for mouse click and Enter key
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

        // Thêm nút vào giao diện
        this.getContentPane().add(loginButton);

        // Đặt nút làm default button
        this.getRootPane().setDefaultButton(loginButton);

        // Thêm ActionListener để xử lý sự kiện đăng nhập
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        // Create and set up the "Forgot Password?" button
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(new Font("Montserrat", Font.BOLD, 12));
        forgotPasswordButton.setForeground(Color.decode("#073A4B")); // dark goldenrod
        forgotPasswordButton.setBackground(Color.WHITE);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setBounds(840, 335, 160, 30); // Position and size
        forgotPasswordButton.setBorder(BorderFactory.createEmptyBorder()); // Remove the border
        forgotPasswordButton.setContentAreaFilled(false); // Optional: Makes the background transparent

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Đóng SignupFrame
                new forgetpass().setVisible(true); // Mở LoginFrame
            }
        });
        add(forgotPasswordButton);

        // Tạo label "Don't have an account?"
        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        signupLabel.setForeground(Color.decode("#F78C6A")); // Màu dark goldenrod
        signupLabel.setBackground(Color.WHITE); // Màu nền trắng
        signupLabel.setBounds(730, 470, 140, 20); // Vị trí và kích thước của label
        signupLabel.setOpaque(true); // Đặt màu nền cho label

        // Tạo nút "Create new one"
        JButton newaccountButton = new JButton("Create new one");
        newaccountButton.setFont(new Font("Montserrat", Font.BOLD, 12));
        newaccountButton.setForeground(Color.decode("#073A4B")); // dark goldenrod
        newaccountButton.setBackground(Color.WHITE);
        newaccountButton.setFocusPainted(false);
        newaccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newaccountButton.setBounds(860, 470, 120, 20); // Position and size
        newaccountButton.setBorder(BorderFactory.createEmptyBorder()); // Remove the border
        newaccountButton.setContentAreaFilled(false); // Optional: Makes the background transparent

        // Thêm ActionListener cho nút "Create new one"
        newaccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Đóng SignupFrame
                new SignupFrame().setVisible(true); // Mở LoginFrame
            }
        });

        // Thêm label và nút vào JFrame
        add(signupLabel);
        add(newaccountButton);

        setVisible(true); // Hiển thị cửa sổ
    }

    /**
     * Tạo JTextField cho tên người dùng với đường viền dưới thay đổi khi focus vào
     * và mất focus
     * 
     * @return JTextField với văn bản mặc định và đường viền dưới có thể thay đổi
     */
    private JTextField createUsernameField() {
        JTextField UsernameField = new JTextField("Username");
        UsernameField.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 20));
        UsernameField.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod'
        UsernameField.setBounds(725, 220, 250, 35); // Đặt vị trí và kích thước cho JTextField
        UsernameField.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0)); // Đặt đường viền dưới

        // Tạo JPanel chứa đường viền dưới và đặt màu sắc
        JPanel underlinePanel = new JPanel();
        underlinePanel.setBounds(725, 255, 250, 3);
        underlinePanel.setBackground(Color.decode("#F78C6A")); // Màu vàng đậm cho đường viền dưới
        add(underlinePanel);

        // Thêm sự kiện focus vào và mất focus
        UsernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (UsernameField.getText().equals("Username")) {
                    UsernameField.setText(""); // Xóa văn bản mặc định khi focus vào
                }
                underlinePanel.setBackground(Color.decode("#F78C6A")); // Màu cam khi focus vào
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (UsernameField.getText().isEmpty()) {
                    UsernameField.setText("Username"); // Đặt lại văn bản mặc định nếu không có gì được nhập
                }
                underlinePanel.setBackground(Color.decode("#F78C6A")); // Màu vàng khi mất focus
            }
        });

        return UsernameField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 20));
        passwordField.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod'
        passwordField.setBounds(725, 290, 250, 35); // Đặt vị trí và kích thước cho JTextField
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0)); // Đặt đường viền dưới

        // Tạo JPanel chứa đường viền dưới và đặt màu sắc
        JPanel underlinePanel = new JPanel();
        underlinePanel.setBounds(725, 325, 250, 3);
        underlinePanel.setBackground(Color.decode("#F78C6A")); // Màu vàng đậm cho đường viền dưới
        add(underlinePanel);

        // Khởi tạo icon mắt đóng
        final ImageIcon closeEyeIcon = new ImageIcon("resources/closeye.png");
        Image closeImg = closeEyeIcon.getImage();
        Image scaledCloseImg = closeImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Đổi kích thước của mắt
        closeEyeIcon.setImage(scaledCloseImg);

        // Khởi tạo icon mắt mở
        final ImageIcon openEyeIcon = new ImageIcon("resources/openeye.png");
        Image img = openEyeIcon.getImage();
        Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Đổi kích thước của mắt
        openEyeIcon.setImage(scaledImg);

        // Tạo JButton cho mắt và set icon mặc định là mắt đóng
        JButton eyeButton = new JButton(closeEyeIcon);
        eyeButton.setBounds(975, 295, 30, 30); // Đặt vị trí mắt
        eyeButton.setBorder(BorderFactory.createEmptyBorder());
        eyeButton.setContentAreaFilled(false); // Không tô màu nền
        eyeButton.setFocusPainted(false); // Không có viền khi nhấn nút
        eyeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thêm con trỏ khi di chuột vào

        // Thêm action cho eyeButton
        eyeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordField.getEchoChar() == (char) 0) {
                    // Nếu mật khẩu đang hiển thị, ẩn mật khẩu
                    passwordField.setEchoChar('*');
                    eyeButton.setIcon(closeEyeIcon); // Đổi lại icon mắt đóng
                } else {
                    // Nếu mật khẩu đang ẩn, hiển thị mật khẩu
                    passwordField.setEchoChar((char) 0); // Hiển thị mật khẩu
                    eyeButton.setIcon(openEyeIcon); // Đổi lại icon mắt mở
                }
            }
        });

        // Thêm nút mắt vào giao diện
        add(eyeButton);

        // Thêm sự kiện focus vào và mất focus
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText(""); // Xóa văn bản mặc định khi focus vào
                }
                underlinePanel.setBackground(Color.decode("#F78C6A")); // Màu vàng khi focus vào
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password"); // Đặt lại văn bản mặc định nếu không có gì được nhập
                }
                underlinePanel.setBackground(Color.decode("#F78C6A")); // Màu vàng khi mất focus
            }
        });

        return passwordField;
    }

    private void loginUser() {
        String username = UsernameField.getText();
        String password = new String(passwordField.getPassword());

        // Kiểm tra xem người dùng có nhập đủ thông tin không
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kết nối cơ sở dữ liệu và kiểm tra thông tin đăng nhập
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Database driver not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Khởi tạo kết nối
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            // Kiểm tra tài khoản người dùng trong cơ sở dữ liệu
            user = getUser(username, password, conn);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Login successful", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                user.setIsLoggedIn(true); // Đánh dấu người dùng đã đăng nhập
                setEmailFromDatabase(user, conn); // Lấy email từ cơ sở dữ liệu
                dispose(); // Đóng SignupFrame
                // new ModernExpenseDashboard().setVisible(true); // Mở LoginFrame
                // Mở cửa sổ chính hoặc thực hiện các hành động khác sau khi đăng nhập thành
                // công
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Connection error. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức kiểm tra thông tin người dùng trong cơ sở dữ liệu
    private User getUser(String username, String password, Connection conn) throws SQLException {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Tạo đối tượng User từ kết quả truy vấn
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                } else {
                    return null;
                }
            }
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
