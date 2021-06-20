package com.example.myproject22.Presenter;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class SignUpPresenter {
    private SignUpInterface anInterface;

    public SignUpPresenter(SignUpInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void setInit(){
        anInterface.SetInit();
    }

    public Boolean getNoUserName(String username){
        return anInterface.GetNoUserName(username);
    }

    public Boolean getNoPassword(String password){
        return anInterface.GetNoPassword(password);
    }
    public Boolean getNoSalary(String salary){
        return anInterface.GetNoSalary(salary);
    }

    public Boolean getNoFullName(String fullname){
        return anInterface.GetNoFullName(fullname);
    }

    public Boolean getNoEmail(String email){
        return anInterface.GetNoEmail(email);
    }

    public void chooseImage() {
        anInterface.ChooseImage();
    }

    public void takeImageFromGallery(){
        anInterface.TakeImageFromGallery();
    }

    public void takeImageFromCamera(){
        anInterface.TakeImageFromCamera();
    }

    public File createImageFile() throws IOException{
        return anInterface.CreateImageFile();
    }

    public Boolean isNullImage(Bitmap bitmap) {
        return anInterface.IsNullImage(bitmap);
    }

    public String getStringImage() {
        return anInterface.GetStringImage();
    }

    public void denyPermission(){
        anInterface.DenyPermission();
    }

    public void btnSignUp(){
        anInterface.BtnSignUp();
    }

    public void textViewClick() {
        anInterface.TextViewClick();
    }

    public void hideKeyboard(View view){
        anInterface.HideKeyboard(view);
    }

    public void uploadUserToServer(String username, String password, String email,
                                   String fullname, String salary, String image){
        anInterface.UploadUserToServer(username, password, email, fullname, salary, image);
    }
    public void uploadUserNoImageToServer(String username, String password, String email,
                                          String fullname, String salary) {
        anInterface.UploadUserNoImageToServer(username, password, email, fullname, salary);
    }
}
