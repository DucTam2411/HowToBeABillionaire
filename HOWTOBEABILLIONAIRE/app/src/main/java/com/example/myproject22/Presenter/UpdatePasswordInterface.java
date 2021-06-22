package com.example.myproject22.Presenter;

import android.view.View;

public interface UpdatePasswordInterface {
    public void SetInit();
    public void GetBundleData();

    public void BtnSaveClick();
    public void BtnCancelClick();

    public Boolean GetNoPassword(String password);
    public Boolean GetNoOldPassword(String password);
    public Boolean GetNoConfirmPassword(String password, String password_confirm);

    public void UploadPasswordToServer(String oldpassword, String newpassword);

    public void HideKeyboard(View view);
}
