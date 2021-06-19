package com.example.myproject22.Presenter;

public interface LoginInterface {
    //Set init
    public void SetInIt();

    //Check null reference
    public Boolean GetNoUserName(String username);
    public Boolean GetNoPassword(String password);

    //Handle Click Event
    public void BtnSignIn();
    public void TextViewClick();

    //Login From Server
    public void LoginFromServer(String username, String password);
}
