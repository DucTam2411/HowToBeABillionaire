package com.example.myproject22.Presenter;

public interface ReportCategoryDetailInterface {
    //Set init
    public void SetInit();

    //Get Bundle Data
    public void GetBundleData();

    //Get data from server
    public void FetchIncomeDetailInServer();
    public void FetchOutcomeDetailInServer();

    //Load Array include income and outcome
    public void LoadTotalArray();

    //Load Layout
    public void LoadData();
}
