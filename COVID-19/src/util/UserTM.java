package util;

public class UserTM {
    private String userId;
    private String name;
    private String username;
    private String password;
    private String userRole;
    private String contactNumber;
    private String email;
    private String location;

    public UserTM() {
    }

    public UserTM(String userId, String name, String username, String password, String userRole, String contactNumber, String email, String location) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.contactNumber = contactNumber;
        this.email = email;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}
