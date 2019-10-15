package si.fri.rso.lib;

public class UserModel {

    //Ime priimek id mail geslo

    private Integer User_id;
    private String User_first_name;
    private String User_last_name;
    private String User_mail;
    private String User_password;

    public UserModel(Integer User_id, String User_first_name, String User_last_name, String User_mail, String User_password ){
        this.User_id = User_id;
        this.User_first_name = User_first_name;
        this.User_last_name = User_last_name;
        this.User_mail = User_mail;
        this.User_password = User_password;
    }

    public Integer getUser_id() {
        return User_id;
    }

    public String getUser_first_name() {
        return User_first_name;
    }

    public String getUser_last_name() {
        return User_last_name;
    }

    public String getUser_mail() {
        return User_mail;
    }

    public String getUser_password() {
        return User_password;
    }


}
