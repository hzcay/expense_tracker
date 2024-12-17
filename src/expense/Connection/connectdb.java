package expense.Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class connectdb {
    Connection conn;

    public Connection getconnectdb() {
        conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root", "123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
