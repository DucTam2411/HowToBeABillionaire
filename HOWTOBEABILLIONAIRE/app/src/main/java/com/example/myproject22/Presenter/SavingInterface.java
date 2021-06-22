package com.example.myproject22.Presenter;

import com.example.myproject22.Model.UserClass;

import java.io.File;
import java.io.IOException;

public interface SavingInterface {
    //Set init
    public void InitViews();
    public void GetBundleData();

    //Create Data bar chart
    public void CreateDataBarChart();
    public void LoadDataFromServerToBarChart();

    //Fetch data from server
    public void FetchSavingDetailFromServer(String date_start, String date_end);
    public void FetchArrayDateFromServer();
    public void FetchMoneySavingFromServer();
    public void FetchUserFromServer();
    public void LoadUser(UserClass userClass);
    public void LoadDataFromServer();

    //Image User
    public void ChooseImage();
    public void TakeImageFromGallery();
    public void TakeImageFromCamera();
    public File createImageFile() throws IOException;
    public void DeleteImage();
    public void UpdateUserImageToServer(String image);


}

