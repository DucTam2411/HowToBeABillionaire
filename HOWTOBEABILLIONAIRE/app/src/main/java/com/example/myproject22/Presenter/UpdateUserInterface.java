package com.example.myproject22.Presenter;

import android.graphics.Bitmap;
import android.view.View;

import com.android.volley.toolbox.StringRequest;
import com.example.myproject22.Model.UserClass;

import java.io.File;
import java.io.IOException;

public interface UpdateUserInterface {
    public void SetInit();
    public void GetBundleData();
    public void FetchUserFromServer();
    public void LoadUser(UserClass userClass);
    public void LoadDataToLayout();
    public void BtnSaveClick();
    public void BtnCancelClick();

    //Image User
    public void ChooseImage();
    public void TakeImageFromGallery();
    public void TakeImageFromCamera();
    public File createImageFile() throws IOException;
    public void DeleteImage();
    public Boolean IsNullImage(Bitmap bitmap);
    public String GetStringImage();

    //Edit Text
    public Boolean GetNoFullName(String fullname);
    public Boolean GetNoEmail(String email);
    public void HideKeyboard(View view);

    //Put Data To Server
    public void UploadUserToServer(String fullname, String email, String image);
    public void UploadUserToServerNoImage(String fullname, String email);

}
