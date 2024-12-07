package user;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.awt.image.*;

public class Forgetpass extends JFrame {
    private User user;
    private JTextField usernameEntry;
    private JPasswordField passwordEntry, password2Entry;

    public Forgetpass() {
        setTitle("Expense Tracker - Forgot Password"); // Đặt tiêu đề cho cửa sổ
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
            BufferedImage bgImage = ImageIO.read(new File("resources/bg3.jpg"));
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
        JLabel heading = new JLabel("Reset Password");
        heading.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 23)); // Đặt phông chữ
        heading.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        heading.setBackground(Color.WHITE); // Màu nền trắng
        heading.setOpaque(true); // Cần để màu nền có hiệu lực
        heading.setBounds(770, 160, 200, 30); // Vị trí và kích thước của heading
        add(heading);

        // Tạo JLabel cho Username
        JLabel usernamelabel = new JLabel("Username");
        usernamelabel.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18)); // Đặt phông chữ
        usernamelabel.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        usernamelabel.setBackground(Color.WHITE); // Màu nền trắng
        usernamelabel.setOpaque(true); // Cần để màu nền có hiệu lực
        usernamelabel.setBounds(710, 220, 200, 25); // Vị trí và kích thước của heading
        add(usernamelabel);

        // Tạo JTextField tương ứng với Entry trong Tkinter
        usernameEntry = new JTextField(30); // Tạo JTextField với độ rộng 30 ký tự
        usernameEntry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18)); // Đặt font chữ
        usernameEntry.setForeground(Color.WHITE); // Màu chữ
        usernameEntry.setBackground(Color.decode("#F78C6A")); // Màu nền tương ứng với 'dark goldenrod'
        usernameEntry.setOpaque(true); // Cần để màu nền có hiệu lực
        usernameEntry.setBorder(null); // Loại bỏ viền xung quanh nút
        usernameEntry.setBounds(710, 245, 300, 24); // Vị trí và kích thước của heading
        add(usernameEntry);

        // Tạo JPanel chứa đường viền dưới và đặt màu sắc
        JPanel underlinePanel1 = new JPanel();
        underlinePanel1.setBounds(710, 272, 300, 3);
        underlinePanel1.setBackground(Color.decode("#F78C6A")); // Màu vàng đậm cho đường viền dưới
        add(underlinePanel1);

        // Tạo JLabel cho password
        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18)); // Đặt phông chữ
        passwordlabel.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        passwordlabel.setBackground(Color.WHITE); // Màu nền trắng
        passwordlabel.setOpaque(true); // Cần để màu nền có hiệu lực
        passwordlabel.setBounds(710, 285, 200, 25); // Vị trí và kích thước của heading
        add(passwordlabel);

        // Tạo JTextField tương ứng với Entry trong Tkinter
        passwordEntry = new JPasswordField(30); // Tạo JTextField với độ rộng 30 ký tự
        passwordEntry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18)); // Đặt font chữ
        passwordEntry.setForeground(Color.WHITE); // Màu chữ
        passwordEntry.setBackground(Color.decode("#F78C6A")); // Màu nền tương ứng với 'dark goldenrod'
        passwordEntry.setOpaque(true); // Cần để màu nền có hiệu lực
        passwordEntry.setBorder(BorderFactory.createEmptyBorder()); // Loại bỏ viền xung quanh
        passwordEntry.setBounds(710, 310, 300, 24); // Đặt vị trí và kích thước cho JTextField
        add(passwordEntry);

        // Tạo JPanel chứa đường viền dưới và đặt màu sắc
        JPanel underlinePanel2 = new JPanel();
        underlinePanel2.setBounds(710, 337, 300, 3);
        underlinePanel2.setBackground(Color.decode("#F78C6A")); // Màu vàng đậm cho đường viền dưới
        add(underlinePanel2);

        // Tạo JLabel cho password2
        JLabel password2label = new JLabel("Check Password");
        password2label.setFont(new Font("Microsoft Yahei UI Dark", Font.BOLD, 18)); // Đặt phông chữ
        password2label.setForeground(Color.decode("#F78C6A")); // Màu chữ 'dark goldenrod' tương đương trong RGB
        password2label.setBackground(Color.WHITE); // Màu nền trắng
        password2label.setOpaque(true); // Cần để màu nền có hiệu lực
        password2label.setBounds(710, 350, 200, 25); // Vị trí và kích thước của heading
        add(password2label);

        // Tạo JTextField tương ứng với Entry trong Tkinter
        password2Entry = new JPasswordField(30); // Tạo JTextField với độ rộng 30 ký tự
        password2Entry.setFont(new Font("Microsoft Yahei UI Light", Font.BOLD, 18)); // Đặt font chữ
        password2Entry.setForeground(Color.WHITE); // Màu chữ
        password2Entry.setBackground(Color.decode("#F78C6A")); // Màu nền tương ứng với 'dark goldenrod'
        password2Entry.setOpaque(true); // Cần để màu nền có hiệu lực
        password2Entry.setBorder(BorderFactory.createEmptyBorder()); // Loại bỏ viền xung quanh
        password2Entry.setBounds(710, 375, 300, 24); // Đặt vị trí và kích thước cho JTextField
        add(password2Entry);

        // Tạo JPanel chứa đường viền dưới và đặt màu sắc
        JPanel underlinePanel3 = new JPanel();
        underlinePanel3.setBounds(710, 402, 300, 3);
        underlinePanel3.setBackground(Color.decode("#F78C6A")); // Màu vàng đậm cho đường viền dưới
        add(underlinePanel3);

        // Nút Đăng Nhập
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Montserrat", Font.BOLD, 30)); // Cài đặt phông chữ cho nút
        submitButton.setForeground(Color.decode("#FFFFFF")); // Màu chữ trắng (hoặc #F1F1F1 cho nhẹ nhàng hơn)
        submitButton.setBackground(Color.decode("#F78C6A")); // Màu nền cam của nút
        submitButton.setBounds(735, 430, 250, 40); // Vị trí và kích thước của nút
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thay đổi con trỏ khi di chuột vào nút

        // Căn chỉnh chữ trong nút cho phù hợp
        submitButton.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều ngang
        submitButton.setVerticalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều dọc

        // Xóa viền và làm nút trông hiện đại hơn
        submitButton.setBorder(null); // Loại bỏ viền xung quanh nút
        submitButton.setFocusPainted(false); // Loại bỏ viền khi nút được chọn
        submitButton.setContentAreaFilled(true); // Làm nền nút bao phủ toàn bộ diện tích của nó

        // Thêm hiệu ứng hover: Thay đổi màu nền của nút khi chuột di vào/ra
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(Color.decode("#F1A07A")); // Màu nền sáng hơn khi chuột di vào
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(Color.decode("#F78C6A")); // Quay lại màu nền ban đầu khi chuột ra ngoài
            }
        });

        // Thêm nút vào giao diện
        this.getContentPane().add(submitButton);

        // Thêm nút vào giao diện
        this.getContentPane().add(submitButton);

        // Đặt nút làm default button
        this.getRootPane().setDefaultButton(submitButton);

        // Action for forgot Button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forgotpassword();
            }
        });

        // Nút Back trong suốt
        JButton backButton = new JButton("");
        backButton.setFont(new Font("Montserrat", Font.BOLD, 20)); // Cài đặt phông chữ cho nút
        backButton.setForeground(Color.decode("#F78C6A")); // Màu chữ cam
        backButton.setBounds(700, 490, 330, 40); // Vị trí và kích thước của nút

        // Làm nút trong suốt
        backButton.setContentAreaFilled(false); // Xóa nền nút
        backButton.setBorderPainted(false); // Xóa viền nút
        backButton.setFocusPainted(false); // Xóa hiệu ứng viền khi nút được chọn
        backButton.setOpaque(false); // Làm nút trong suốt hoàn toàn

        // Thêm hiệu ứng hover: Đổi màu chữ và thay đổi con trỏ khi di chuột vào/ra
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.decode("#F1A07A")); // Màu chữ sáng hơn khi chuột di vào
                backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thay đổi con trỏ chuột thành
                                                                                      // hình bàn tay
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.decode("#F78C6A")); // Màu chữ quay lại ban đầu
                backButton.setCursor(Cursor.getDefaultCursor()); // Quay lại con trỏ chuột mặc định
            }
        });

        // Xử lý sự kiện khi nhấn nút Back
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Đóng cửa sổ hiện tại
                new LoginPage(); // Mở lại trang đăng nhập (thay bằng lớp LoginPage của bạn)
            }
        });

        // Thêm nút Back vào giao diện
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            // Add your code to handle the connection here
            user = new User(username, password);
            user.forgetpass(conn);
            JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the forgetpass frame
            new LoginPage(); // Open the login frame (replace with your login frame)
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Connection error. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
