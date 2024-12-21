package expense.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class transaction {
    private int id;
    private String date;
    private String description;
    private String category;
    private double amount;
    private String type;

    private static final String OTHER_CATEGORY_INCOME = "IC_0";
    private static final String OTHER_CATEGORY_EXPENSE = "EX_0";
    private static final String INCOME_PREFIX = "IC_";
    private static final String EXPENSE_PREFIX = "EX_";
    private static final String OTHER_CATEGORY_NAME = "📦 Others";
    private static final String CATEGORY_SQL = "SELECT * FROM category WHERE name = ?";

    public transaction(int id, String date, String description, String category, double amount, String type) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.type = type;
    }

    public transaction(String category) {
        this.category = category;
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
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String categoryName, Connection conn) {
        try {
            if (categoryName.equals(OTHER_CATEGORY_NAME)) {
                this.category = (getType().equals("Income")) ? OTHER_CATEGORY_INCOME : OTHER_CATEGORY_EXPENSE;
            } else {
                this.category = fetchCategoryId(categoryName, conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String fetchCategoryId(String categoryName, Connection conn) throws SQLException {
        String sql = CATEGORY_SQL;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("category_id");
                }
            }
        }
        return (getType().equals("Income")) ? INCOME_PREFIX + "N/A" : EXPENSE_PREFIX + "N/A";
    }

}