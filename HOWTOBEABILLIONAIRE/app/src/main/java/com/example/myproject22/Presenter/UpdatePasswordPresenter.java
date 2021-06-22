package com.example.myproject22.Presenter;

import android.view.View;

public class UpdatePasswordPresenter {
    private UpdatePasswordInterface anInterface;

    public UpdatePasswordPresenter(UpdatePasswordInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void setInit() {
        anInterface.SetInit();
    }
    public void getBundleData(){
        anInterface.GetBundleData();
    }

    public void btnSaveClick(){
        anInterface.BtnSaveClick();
    }
    public void btnCancelClick(){
        anInterface.BtnCancelClick();
    }

    public Boolean getNoPassword(String password){
        return anInterface.GetNoPassword(password);
    }
    public Boolean getNoConfirmPassword(String password, String password_confirm) {
        return anInterface.GetNoConfirmPassword(password,password_confirm);
    }

    public Boolean getNoOldPassword(String password){
        return anInterface.GetNoOldPassword(password);
    }

    public void uploadPasswordToServer(String oldpassword, String newpassword){
        anInterface.UploadPasswordToServer(oldpassword,newpassword);
    }

    public void hideKeyboard(View view){
        anInterface.HideKeyboard(view);
    }
}
