package expense.module;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import user.user_class.User;

public class Tabletransaction {
    private final Connection conn;
    private final User user;
    private Object[][] data;

    private static final String GET_TRANSACTIONS_SQL = "SELECT * FROM expensetracker WHERE Username = ?";
    private static final String GET_CATEGORY_SQL = "SELECT * FROM category WHERE category_id = ?";
    private static final String GET_MAX_ID_SQL = "SELECT MAX(id) FROM expensetracker";
    private static final String ADD_TRANSACTION_SQL = "INSERT INTO expensetracker (Date, Description, Category_id, Amount, username) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_TRANSACTION_SQL = "DELETE FROM expensetracker WHERE id = ?";
    private static final String GET_TRANSACTION_SQL = "SELECT * FROM expensetracker WHERE id = ? AND username = ?";
    private static final String UPDATE_TRANSACTION_SQL = "UPDATE expensetracker SET Date = ?, Description = ?, Category_id = ?, Amount = ? WHERE ID = ? AND username = ?";
    private static final String GET_TRANSACTIONS_BY_DATE_SQL = "SELECT * FROM expensetracker WHERE Username = ? AND Date = ?";
    private static final String GET_TRANSACTIONS_BY_CATEGORY_SQL = "SELECT * FROM expensetracker WHERE Username = ? AND Category_id = ?";

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String EXPENSE_PREFIX = "EX";
    private static final String INCOME_PREFIX = "IN";
    private static final String OTHER_CATEGORY = "N/A";

    public Tabletransaction(Connection conn, User user) {
        this.conn = conn;
        this.user = user;
    }

    public void setData(Object[][] newData) {

        this.data = newData;

    }

    public Object[][] getData() {

        return this.data;

    }

    public Object[][] getTransactions() {
        ArrayList<Object[]> transactions = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(GET_TRANSACTIONS_SQL)) {
            stmt.setString(1, user.getUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = createTransactionRow(rs);
                    transactions.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions.toArray(new Object[0][]);
    }

    private Object[] createTransactionRow(ResultSet rs) throws SQLException {
        String categoryId = rs.getString("category_id");
        String categoryName = getCategoryName(categoryId);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return new Object[] {
                categoryName,
                rs.getString("description"),
                dateFormat.format(rs.getDate("Date")),
                rs.getDouble("Amount"),
                rs.getInt("id")
        };
    }

    private String determineTransactionType(String categoryId) {
        if (categoryId.startsWith(EXPENSE_PREFIX))
            return "Expense";
        if (categoryId.startsWith(INCOME_PREFIX))
            return "Income";
        return "Category";
    }

    private String getCategoryName(String categoryId) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_CATEGORY_SQL)) {
            stmt.setString(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return OTHER_CATEGORY;
    }

    public int getNextAvailableId() {
        int maxId = 0;
        try (PreparedStatement stmt = conn.prepareStatement(GET_MAX_ID_SQL)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    maxId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

    public void addTransaction(transaction transaction) {
        try (PreparedStatement stmt = conn.prepareStatement(ADD_TRANSACTION_SQL)) {
            double amount = transaction.getAmount();
            if (determineTransactionType(transaction.getCategory()).equals("Expense")) {
                amount = -1 * Math.abs(amount);
            } else {
                amount = Math.abs(amount);
            }
            stmt.setString(1, transaction.getDate());
            stmt.setString(2, transaction.getDescription());
            stmt.setString(3, transaction.getCategory());
            stmt.setDouble(4, amount);
            stmt.setString(5, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(int id) {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_TRANSACTION_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public transaction getTransaction(int id) {
        transaction transaction = null;
        try (PreparedStatement stmt = conn.prepareStatement(GET_TRANSACTION_SQL)) {
            stmt.setInt(1, id);
            stmt.setString(2, user.getUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    transaction = new transaction(rs.getInt("id"), rs.getString("Date"), rs.getString("Description"),
                            rs.getString("Category_id"), rs.getDouble("Amount"),
                            determineTransactionType(rs.getString("Category_id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public void updateTransaction(transaction transaction) {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_TRANSACTION_SQL)) {
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

    public Object[][] getTransactionwithDATE(String date) {
        ArrayList<Object[]> transactions = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(GET_TRANSACTIONS_BY_DATE_SQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, date);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = createTransactionRow(rs);
                    transactions.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][];
        }
        return transactions.toArray(new Object[0][]);
    }

    public Object[][] getTransactionwithCategory(String category) {
        ArrayList<Object[]> transactions = new ArrayList<>();
        transaction transaction = new transaction(category);

        if (category != "IC_0" && category != "EX_0") {
            transaction.setCategory(category, conn);
        }
        try (PreparedStatement stmt = conn.prepareStatement(GET_TRANSACTIONS_BY_CATEGORY_SQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, transaction.getCategory());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = createTransactionRow(rs);
                    transactions.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][];
        }
        return transactions.toArray(new Object[0][]);
    }
}