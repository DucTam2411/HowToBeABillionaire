package com.example.myproject22.Presenter;

import android.graphics.Bitmap;
import android.view.View;

import com.example.myproject22.Model.UserClass;

public class UpdateUserPresenter {
    private UpdateUserInterface anInterface;

    public UpdateUserPresenter(UpdateUserInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void setInit() {
        anInterface.SetInit();
    }

    public void getBundleData(){
        anInterface.GetBundleData();
    }

    public void fetchUserFromServer(){
        anInterface.FetchUserFromServer();
    }

    public void loadUser(UserClass userClass){
        anInterface.LoadUser(userClass);
    }

    public void loadDataToLayout(){
        anInterface.LoadDataToLayout();
    }

    public void btnSaveClick(){
        anInterface.BtnSaveClick();
    }

    public void btnCancelClick(){
        anInterface.BtnCancelClick();
    }

    //Image User
    public void chooseImage() {
        anInterface.ChooseImage();
    }
    public void takeImageFromGallery() {
        anInterface.TakeImageFromGallery();
    }
    public void takeImageFromCamera() {
        anInterface.TakeImageFromCamera();
    }

    public Boolean isNullImage(Bitmap bitmap){
        return anInterface.IsNullImage(bitmap);
    }

    public String getStringImage(){
        return anInterface.GetStringImage();
    }

    public Boolean getNoFullName(String fullname){
        return anInterface.GetNoFullName(fullname);
    }

    public Boolean getNoEmail(String email){
        return anInterface.GetNoEmail(email);
    }

    public void uploadUserToServer(String fullname, String email, String image) {
        anInterface.UploadUserToServer(fullname,email,image);
    }
    public void uploadUserToServerNoImage(String fullname, String email) {
        anInterface.UploadUserToServerNoImage(fullname,email);
    }

    public void hideKeyboard(View view){
        anInterface.HideKeyboard(view);
    }
}
