package com.example.myproject22.Presenter;

import com.example.myproject22.Model.UserClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SavingPresenter {
    SavingInterface mSavingInterface;

    public SavingPresenter(SavingInterface mSavingInterface) {
        this.mSavingInterface = mSavingInterface;
    }

    public void initView(){
        mSavingInterface.InitViews();
    }

    public void getBundleData(){
        mSavingInterface.GetBundleData();
    }

    public void createDataBarChart(){
        mSavingInterface.CreateDataBarChart();
    }

    public void fetchSavingDetailFromServer(String date_start, String date_end){
        mSavingInterface.FetchSavingDetailFromServer(date_start,date_end);
    }

    public void loadDataFromServerToBarChart(){
        mSavingInterface.LoadDataFromServerToBarChart();
    }

    public void loadDataFromServer(){
        mSavingInterface.LoadDataFromServer();
    }

    public void fetchArrayDateFromServer(){
        mSavingInterface.FetchArrayDateFromServer();
    }

    public void fetchMoneySavingFromServer(){
        mSavingInterface.FetchMoneySavingFromServer();
    }

    public void fetchUserFromServer(){
        mSavingInterface.FetchUserFromServer();
    }

    public void loadUser(UserClass userClass){
        mSavingInterface.LoadUser(userClass);
    }

    public void takeImageFromCamera(){
        mSavingInterface.TakeImageFromCamera();
    }

    public void takeImageFromGallery(){
        mSavingInterface.TakeImageFromGallery();
    }

    public void chooseImage(){
        mSavingInterface.ChooseImage();
    }


    public void LoadGetTietKiemData() {
        mSavingInterface.LoadChiTietTietKiem();
    }
    public void LoadTietKiem(){
        mSavingInterface.LoadTietKiem();
    }
    public void LoadMucTieu(){
        mSavingInterface.LoadMucTieu();
    }

    public static Date FindMondayFromDate(Date date_from){
        Calendar cal = Calendar.getInstance();
        Date date_monday = new Date();

        cal.setTime(date_from);
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        switch (dow){
            case 1: {
                cal.add(Calendar.DATE, -6);
                date_monday = cal.getTime();
                return date_monday;
            }
            case 2: {
                date_monday = cal.getTime();
                return date_monday;
            }
            case 3: {
                cal.add(Calendar.DATE, -1);
                date_monday = cal.getTime();
                return date_monday;
            }
            case 4: {
                cal.add(Calendar.DATE, -2);
                date_monday = cal.getTime();
                return date_monday;
            }
            case 5: {
                cal.add(Calendar.DATE, -3);
                date_monday = cal.getTime();
                return date_monday;
            }
            case 6: {
                cal.add(Calendar.DATE, -4);
                date_monday = cal.getTime();
                return date_monday;
            }
            default: {
                cal.add(Calendar.DATE, -5);
                date_monday = cal.getTime();
                return date_monday;
            }
        }
    }

    public static Date FindEndOfWeek(Date date_from){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_from);
        cal.add(Calendar.DATE, 7);

        Date date_end = cal.getTime();
        return date_end;
    }

    public static String StringFromDate(Date date){
        SimpleDateFormat format_string = new SimpleDateFormat("yyyy-MM-dd");
        String string_date = format_string.format(date);
        return string_date;
    }

    public static long CalculateDateUse(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;
        return (long) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Date DateFromString(String date_string){
        SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = curFormater.parse(date_string);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String GetStringMoney(double money){
        String money_string = "";

        if(money > 1000000000.0){
            double money_bil = money / 1000000000.0;
            double money_round = (int)(Math.round(money_bil * 100))/100.0;
            money_string = money_round + "tỷ";
        }
        else if (money > 1000000.0){
            double money_mil = money / 1000000.0;
            double money_round = (int)(Math.round(money_mil * 100))/100.0;
            money_string = money_round + "tr";
        }else{
            double money_thousand = money / 1000.0;
            int money_round = (int)Math.round(money_thousand);
            money_string = money_round + "k";
        }

        return money_string;
    }
}
