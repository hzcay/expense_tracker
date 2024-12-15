package expense.module;

import java.sql.*;

public class transaction {
    private int id;
    private String date;
    private String description;
    private String category;
    private double amount;
    private String type;

    public transaction(int id, String date, String description, String category, double amount, String type) {
        this.id = id;
        this.date = date;
        this.description = description;
        setCategory(category);
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            String sql = "SELECT * FROM category WHERE category_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.category = rs.getString("name");
            } else {
                this.category = "N/A";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addTransaction() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                    "123456");
            String sql = "INSERT INTO expensetracker (Date, Description, Category_id, Amount, username) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, description);
            stmt.setString(3, category);
            stmt.setDouble(4, amount);
            stmt.setString(5, "admin");
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
