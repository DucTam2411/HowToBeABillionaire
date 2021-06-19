package com.example.myproject22.Presenter;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

public interface SignUpInterface {
    //Set init
    public void SetInit();

    //Check null reference
    public Boolean GetNoUserName(String username);
    public Boolean GetNoPassword(String password);
    public Boolean GetNoSalary(String salary);
    public Boolean GetNoFullName(String fullname);
    public Boolean GetNoEmail(String email);

    //Image
    public void ChooseImage();
    public void TakeImageFromGallery();
    public void TakeImageFromCamera();
    public File CreateImageFile() throws IOException;
    public Boolean IsNullImage(Bitmap bitmap);
    public String GetStringImage();
    public void DenyPermission();

    //Handle Click Event
    public void BtnSignUp();
    public void TextViewClick();

    //Get data from server
    public void UploadUserToServer(String username, String password, String email,
                                   String fullname, String salary, String image);
    public void UploadUserNoImageToServer(String username, String password, String email,
                                          String fullname, String salary);
}
