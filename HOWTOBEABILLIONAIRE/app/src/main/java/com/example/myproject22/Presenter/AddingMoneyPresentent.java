package com.example.myproject22.Presenter;

import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class AddingMoneyPresentent {

    private AddingMoneyInterface anInterface;

    public AddingMoneyPresentent(AddingMoneyInterface anInterface) {
        this.anInterface = anInterface;
    }
    public void getDataBundle(){anInterface.GetDataBundle();}

    public void setInit(){
        anInterface.SetInit();
    }

    public void loadingIncomeCategory(){
        anInterface.LoadCategory();
    }

    public void chooseImage(){
        anInterface.ChooseImage();
    }

    public void CaptureRecord(){
        anInterface.CaptureRecord();
    }

    public void CaptureAudio(){
        anInterface.CaptureAudio();
    }

    public void SetOnTextChange(CharSequence s){
        anInterface.IsValidNumber(s);
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9]+");
    }

    public int findIdByName(String name){
        return anInterface.FindIdByName(name);
    }

    public Boolean isNullAudio(){
        return anInterface.IsNullAudio();
    }

    public Boolean isNullImage(Bitmap bitmap){
        return anInterface.IsNullImage(bitmap);
    }

    public byte[] convert3gbToByte(){
        return anInterface.Convert3gbToByte();
    }

    public String getStringImage(){
        return anInterface.GetStringImage();
    }

    public String getStringAudio(){
        return anInterface.GetStringAudio();
    }

    public static String convertByteToString(byte[] bytes){
        if(bytes == null){
            String s = "NULL";
            return s;
        }
        else{
            String s = Base64.encodeToString(bytes,Base64.DEFAULT);
            return s;
        }
    }

    public void savingMoneyData(String money, String description, int category_id, String image, String audio){
        //Check valid money
        if(!anInterface.GetNoMoneyData(money)){
            return;
        }

        //Check valid Category
        if(category_id == 0){
            anInterface.GetNoCategoryData();
            return;
        }

        anInterface.SavingMoneyData(money,description,category_id,image,audio);
    }

    public static byte[] encodeTobase64(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void hideKeyBoard(View view){
        anInterface.HideKeyboard(view);
    }

    public void startRecord(){
        anInterface.StartRecord();
    }

    public void stopRecord(){
        anInterface.StopRecord();
    }

    public void startAudio(){
        anInterface.StartAudio();
    }

    public void stopAudio(){
        anInterface.StopAudio();
    }

    public void takeImageFromCamera(){
        anInterface.TakeImageFromCamera();
    }

    public void takeImageFromGallery(){
        anInterface.TakeImageFromGallery();
    }

    public void fetchIncomeCategoryFromServer(){
        anInterface.FetchIncomeCategory();
    }

    public void fetchOutcomeCategoryFromServer(){
        anInterface.FetchOutcomeCategory();
    }
}
