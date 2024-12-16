package expense.module;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JTable;

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
                    row[2] = new java.text.SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("Date"));
                    row[3] = rs.getDouble("Amount");
                    row[4] = rs.getInt("ID");

                    String category = rs.getString("category_id");
                    row[0] = setCategory(category);

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

    public int getNextAvailableId() {

        int maxId = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            String sql = "SELECT MAX(id) FROM expensetracker";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                maxId = rs.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return maxId + 1;
    }

    public void addTransaction(transaction transaction) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            String sql = "INSERT INTO expensetracker (Date, Description, Category_id, Amount, username) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            double amount = transaction.getAmount();
            if (determineTransactionType(transaction.getCategory()).equals("Expense")) {
                amount = -1 * Math.abs(amount); // Store negative values for expenses
            } else {
                amount = Math.abs(amount); // Store positive values for income
            }
            stmt.setString(1, transaction.getDate());
            stmt.setString(2, transaction.getDescription());
            stmt.setString(3, transaction.getCategory());
            stmt.setDouble(4, amount);
            stmt.setString(5, user.getUsername());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            String sql = "DELETE FROM expensetracker WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTransactionByColumn(String columnName, String value) {
        try {
            String sql = "DELETE FROM expensetracker WHERE " + columnName + " = ? AND username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, value);
            stmt.setString(2, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public transaction getTransaction(int id) {
        transaction transaction = null;
        try {
            String sql = "SELECT * FROM expensetracker WHERE id = ? AND username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                transaction = new transaction(rs.getInt("ID"), rs.getString("Date"), rs.getString("Description"),
                        rs.getString("Category_id"), rs.getDouble("Amount"),
                        determineTransactionType(rs.getString("Category_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public void updateTransaction(transaction transaction) {
        try {
            String sql = "UPDATE expensetracker SET Date = ?, Description = ?, Category_id = ?, Amount = ? WHERE ID = ? AND username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, transaction.getDate());
            stmt.setString(2, transaction.getDescription());
            stmt.setString(3, transaction.getCategory());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setInt(5, transaction.getId());
            stmt.setString(6, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
