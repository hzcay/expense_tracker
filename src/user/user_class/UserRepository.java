package user.user_class;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static final String SELECT_USER_QUERY = "SELECT * FROM account WHERE username = ? AND password = ?";
    private static final String CHECK_USER_QUERY = "SELECT * FROM account WHERE username = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO account (email, username, password) VALUES (?, ?, ?)";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE account SET password = ? WHERE username = ?";
    private Connection conn;

    public UserRepository(Connection conn) {
        this.conn = conn;
    }

    public User getUser(String username, String password) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = prepareStatement(SELECT_USER_QUERY, username, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            return null;
        } catch (SQLException ex) {
            logError("Error getting user", ex);
            throw ex;
        } finally {
            closeResources(rs, stmt);
        }
    }

    public boolean setUser(User user) throws SQLException {
        PreparedStatement checkStmt = null;
        ResultSet rs = null;
        PreparedStatement insertStmt = null;
        try {
            checkStmt = prepareStatement(CHECK_USER_QUERY, user.getUsername());
            rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false;
            }
            insertStmt = prepareStatement(INSERT_USER_QUERY, user.getEmail(), user.getUsername(), user.getPassword());
            insertStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logError("Error creating user", ex);
            throw ex;
        } finally {
            closeResources(rs, checkStmt);
            closeResources(null, insertStmt);
        }
    }

    public boolean forgetpass(User user) throws SQLException {
        PreparedStatement checkStmt = null;
        ResultSet rs = null;
        PreparedStatement updateStmt = null;
        try {
            checkStmt = prepareStatement(CHECK_USER_QUERY, user.getUsername());
            rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return false;
            }
            updateStmt = prepareStatement(UPDATE_PASSWORD_QUERY, user.getPassword(), user.getUsername());
            updateStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logError("Error updating password", ex);
            throw ex;
        } finally {
            closeResources(rs, checkStmt);
            closeResources(null, updateStmt);
        }
    }

    private PreparedStatement prepareStatement(String query, String... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setString(i + 1, params[i]);
        }
        return stmt;
    }

    private void closeResources(ResultSet rs, PreparedStatement stmt) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            logError("Error closing resources", e);
        }
    }

    private void logError(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
    }
}