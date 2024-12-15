package expense.module;

import java.sql.*;
import java.util.ArrayList;
import user.User;

public class Tabletransaction {
    private final Connection conn;
    private final User user;

    public Tabletransaction(Connection conn, User user) {
        this.conn = conn;
        this.user = user;
    }

    public Object[][] getTransactions() {
        ArrayList<Object[]> transactions = new ArrayList<>();
        String sql = "SELECT * FROM expensetracker WHERE Username = ?";

        // Di chuyển return xuống cuối phương thức
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[5];
                    row[0] = rs.getString("category_id");
                    row[1] = rs.getString("description");
                    row[2] = rs.getString("Date");
                    row[3] = rs.getDouble("Amount");

                    String category = rs.getString("category_id");
                    row[0] = setCategory(category);
                    if (determineTransactionType(category).equals("Expense")) {
                        row[3] = -1 * (Double) row[3];
                    }

                    transactions.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Chuyển ArrayList thành Object[][]
        return transactions.toArray(new Object[transactions.size()][]);
    }

    private String determineTransactionType(String category) {
        if (category.startsWith("EX"))
            return "Expense";
        if (category.startsWith("IN"))
            return "Income";
        return "Category";
    }

    public String setCategory(String category) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            String sql = "SELECT * FROM category WHERE category_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "N/A";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
