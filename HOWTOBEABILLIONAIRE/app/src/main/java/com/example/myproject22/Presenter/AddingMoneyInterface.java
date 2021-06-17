package com.example.myproject22.Presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

public interface AddingMoneyInterface {
    //Set component
    public void SetInit();
    public void GetDataBundle();

    //Load income and spending category
    public void LoadCategory();

    //Check Permission
    public Boolean CheckPermissionRecord();
    public Boolean CheckPermissionImage();
    public Boolean CheckPermissionRead();

    //Load Image from Camera of Gallery
    public void ChooseImage();

    //Take image from camera and gallery
    public void TakeImageFromGallery();
    public void TakeImageFromCamera();

    //Capture record and play audio
    public void CaptureRecord();
    public void CaptureAudio();

    //Record and Audio
    public void StartRecord();
    public void StopRecord();
    public void StartAudio();
    public void StopAudio();

    //Check valid number
    public void IsValidNumber(CharSequence s);

    //Find id category by its name
    public int FindIdByName(String name);

    //Check image null
    public Boolean IsNullImage(Bitmap bitmap);
    public String GetStringImage();

    //Check audio null
    public Boolean IsNullAudio();
    public byte[] Convert3gbToByte();
    public String GetStringAudio();

    //Saving
    public void SavingMoneyData(String money, String description, int category_id, String image, String audio);

    //If money not numeric or category, toast user
    public void GetNoMoneyData();
    public void GetNoCategoryData();

    //Hide Keyboard
    public void HideKeyboard(View view);

    //Fetch category from server
    public void FetchIncomeCategory();
    public void FetchOutcomeCategory();

    public void ResetSound();
}
