package models;

public class User {
    private int idUser;
    private String login;
    private String password;
    private String email;

    public User(String email, int idUser, String login, String password) {
        this.email = email;
        this.idUser = idUser;
        this.login = login;
        this.password = password;
    }
    public User(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
