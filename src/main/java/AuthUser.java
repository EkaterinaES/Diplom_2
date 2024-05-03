public class AuthUser {
    private String email;
    private String password;

    public AuthUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public AuthUser(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

