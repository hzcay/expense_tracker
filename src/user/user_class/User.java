package user.user_class;

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
}