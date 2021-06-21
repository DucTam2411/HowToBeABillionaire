package com.example.myproject22.Presenter;

import android.view.View;

public interface ForgotPasswordInterface {
    public void SetInIt();

    public Boolean GetNoUserName(String username);
    public Boolean GetNoPassword(String password);
    public Boolean GetNoEmail(String email);
    public Boolean GetNoConfirmPassword(String password, String password_confirm);

    public void BtnForgetClick();
    public void TextViewClick();

    public void UploadNewPassword(String username, String email, String password);

    public void HideKeyboard(View view);
}
