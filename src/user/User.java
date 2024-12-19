package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private String email;
    private String username;
    private String password;
    private boolean isLoggedIn;

    public User() {
        this.email = "";
        this.username = "";
        this.password = "";
        this.isLoggedIn = false;
    }

    public User(String username, String password) {
        this("", username, password);
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }

    public void setuser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void displayUserInfo() {
        System.out.println("User Information:");
        System.out.println("Email: " + (email.isEmpty() ? "N/A" : email));
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Is Logged In: " + isLoggedIn);
    }

    public User getUser(String username, String password, Connection conn) throws SQLException {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create User object from query result
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

    public boolean setUser(Connection conn) throws SQLException {
        try {

            String checkUserQuery = "SELECT * FROM account WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;
            }

            String insertQuery = "INSERT INTO account (email, username, password) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, email);
            insertStmt.setString(2, username);
            insertStmt.setString(3, password);
            insertStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean forgetpass(Connection conn) throws SQLException {
        try {
            String checkUserQuery = "SELECT * FROM account WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String updateQuery = "UPDATE account SET password = ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, password);
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            throw ex;
        }
    }
}