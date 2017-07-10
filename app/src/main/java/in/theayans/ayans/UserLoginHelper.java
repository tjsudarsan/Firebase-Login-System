package in.theayans.ayans;

/**
 * Created by Sudarsan on 12-02-2017.
 */

public class UserLoginHelper {

    public String disid;
    public String password;
    public String username;
    public String phno;
    public String email;
    public String upline;

    public UserLoginHelper(){
    }

    public UserLoginHelper(String email, String password, String phno,String upline,String username){
        this.email = email;
        this.password = password;
        this.phno = phno;
        this.upline = upline;
        this.username = username;
    }

}
