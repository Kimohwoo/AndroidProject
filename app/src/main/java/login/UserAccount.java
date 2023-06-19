package login;
public class UserAccount
{
    private String idToken; //Firebase Uid(고유 정보)
    private String emailId; // 이메일 아이디
    private String password; // 비밀번호

    public UserAccount(){}

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
