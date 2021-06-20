package com.example.myproject22.Presenter;

import android.view.View;

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

    public void textViewForgetClick(){
        anInterface.TextViewForgetClick();
    }

    public void hideKeyboard(View view){
        anInterface.HideKeyboard(view);
    }

    public void loginFromServer(String username, String password){
        anInterface.LoginFromServer(username,password);
    }
}
