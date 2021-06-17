package com.example.myproject22.Presenter;

public interface ReportCategoryInterface {
    //Set init
    public void SetInit();

    //Get Bundle data
    public void GetBundleData();

    //Load RecycleView
    public void LoadRecycleView();

    //Fetch data income, outcome from server
    public void FetchIncomeInServer();
    public void FetchOutcomeInServer();

    //Load layout
    public void LoadData();
}
