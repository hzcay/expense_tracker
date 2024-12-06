package cont;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
    private static Connection conn;
    private static final String URL = "jdbc:mysql://localhost:3306/expense_management";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    // Private constructor để ngăn tạo nhiều đối tượng Connect
    public connect() {
    }

    // Phương thức để lấy kết nối (sử dụng singleton pattern)
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                // Đăng ký driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Khởi tạo kết nối
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to database");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver not found: " + e.getMessage());
                throw new SQLException("Failed to load JDBC driver", e);
            } catch (SQLException e) {
                System.out.println("Error connecting to database: " + e.getMessage());
                throw e;
            }
        }
        return conn;
    }

    // Phương thức để đóng kết nối khi không còn sử dụng
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
