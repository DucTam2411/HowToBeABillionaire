package com.example.myproject22.Presenter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.RadioGroup;

import java.io.File;
import java.io.IOException;

public interface AddingCategoryInterface {
    public void SetInit();

    //Load Image from Camera of Gallery
    public void ChooseImage();
    public void TakeImageFromGallery();
    public void TakeImageFromCamera();
    public File createImageFile() throws IOException;
    public void DeleteImage();


    //Check image null
    public Boolean IsNullImage(Bitmap bitmap);
    public void GetNoImage();
    public String GetStringImage();

    //Radio button Category
    public  void CheckRadioButtonCategory(RadioGroup radioGroup, int idChecked);

    //Is null name category
    public Boolean IsNullName();

    public void GetBundleData();

    //Saving new category
    public void SavingNewCategory();

    //Hide Keyboard
    public void HideKeyboard(View view);
}
