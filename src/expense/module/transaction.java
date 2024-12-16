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
        this.category = category;
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
            if (category.equals("📦 Others")) {
                this.category = (getType().equals("Income")) ? "IC_0" : "EX_0";
            } else {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management", "root",
                        "123456");
                String sql = "SELECT * FROM category WHERE name = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, category);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    this.category = rs.getString("category_id");
                } else {
                    String prefix = (getType().equals("Income")) ? "IC_" : "EX_";
                    this.category = prefix + "N/A";
                }
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

}
