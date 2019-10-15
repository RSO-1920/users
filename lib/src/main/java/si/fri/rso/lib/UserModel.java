package si.fri.rso.lib;

public class UserModel {

    private Integer userId;
    private String userFirstName;
    private String userLastName;
    private String userMail;
    private String userPassword;

    public UserModel(Integer User_id, String User_first_name, String User_last_name, String User_mail, String User_password ){
        this.userId = User_id;
        this.userFirstName = User_first_name;
        this.userLastName = User_last_name;
        this.userMail = User_mail;
        this.userPassword = User_password;
    }

    public Integer getUser_id() {
        return userId;
    }

    public String getUser_first_name() {
        return userFirstName;
    }

    public String getUser_last_name() {
        return userLastName;
    }

    public String getUser_mail() {
        return userMail;
    }

    public String getUser_password() {
        return userPassword;
    }


    public void setUser_first_name(String userFirstName) {
        this.userFirstName  = userFirstName;
    }

    public void setUser_last_name(String userLastName) {
        this.userLastName = userLastName;
    }

    public void setUser_mail(String userMail) {
        this.userMail = userMail;
    }

    public void setUser_password(String userPassword) {
        this.userPassword = userPassword;
    }

}
