package comp5216.sydney.edu.au.a5216login.entity;

import java.io.Serializable;

public class PasswordREQ implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String password;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PasswordREQ(Integer userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordREQ{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                '}';
    }
}