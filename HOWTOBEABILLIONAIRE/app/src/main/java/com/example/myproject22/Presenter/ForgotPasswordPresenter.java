package com.example.myproject22.Presenter;

import android.view.View;

public class ForgotPasswordPresenter {
    ForgotPasswordInterface anInterface;

    public ForgotPasswordPresenter(ForgotPasswordInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void setInit(){
        anInterface.SetInIt();
    }

    public Boolean getNoUserName(String username) {
        return anInterface.GetNoUserName(username);
    }

    public Boolean getNoPassword(String password) {
        return anInterface.GetNoPassword(password);
    }

    public Boolean getNoEmail(String email){
        return anInterface.GetNoEmail(email);
    }

    public Boolean getNoConfirmPassword(String password, String password_confirm){
        return anInterface.GetNoConfirmPassword(password, password_confirm);
    }

    public void btnForgetClick(){
        anInterface.BtnForgetClick();
    }

    public void textViewClick(){
        anInterface.TextViewClick();
    }

    public void uploadNewPassword(String username, String email, String password){
        anInterface.UploadNewPassword(username,email,password);
    }

    public void hideKeyboard(View view){
        anInterface.HideKeyboard(view);
    }
}
