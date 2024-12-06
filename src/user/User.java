package user;
public class User {
    private String email;
    private String username;
    private String password;
    private Boolean isLoggedIn;

    // Constructor mặc định
    public User() {
        this.email = "";
        this.username = "";
        this.password = "";
        this.isLoggedIn = false;
    }

    // Constructor với username và password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

    // Constructor với email, username và password
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

    // Getter và Setter cho email, username, password, và isLoggedIn
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

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    // Hiển thị thông tin người dùng
    public void display() {
        System.out.println("Email: " + this.email);
        System.out.println("Username: " + this.username);
        System.out.println("Password: " + this.password);
        System.out.println("Is logged in: " + this.isLoggedIn);
    }
}
