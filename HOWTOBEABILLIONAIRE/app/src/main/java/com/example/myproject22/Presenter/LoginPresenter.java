package com.example.myproject22.Presenter;

public class LoginPresenter {
    private LoginInterface anInterface;


    public LoginPresenter(LoginInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void setInit(){
        anInterface.SetInIt();
    }

    public Boolean getNoUserName(String username){
        return anInterface.GetNoUserName(username);
    }

    public Boolean getNoPassword(String password){
        return anInterface.GetNoPassword(password);
    }

    public void btnSignIn(){
        anInterface.BtnSignIn();
    }

    public void textViewClick(){
        anInterface.TextViewClick();
    }
    public void loginFromServer(String username, String password){
        anInterface.LoginFromServer(username,password);
    }
}
