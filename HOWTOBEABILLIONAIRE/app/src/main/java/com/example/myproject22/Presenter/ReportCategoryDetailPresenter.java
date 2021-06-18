package com.example.myproject22.Presenter;

public class ReportCategoryDetailPresenter {
    ReportCategoryDetailInterface anInterface;

    public ReportCategoryDetailPresenter(ReportCategoryDetailInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void setInit(){
        anInterface.SetInit();
    }

    public void getBundleData(){
        anInterface.GetBundleData();
    }

    public void loadTotalArray(){
        anInterface.LoadTotalArray();
    }

    public void fetchIncomeFromServer(){
        anInterface.FetchIncomeDetailInServer();
    }

    public void fetchOutcomeFromServer(){
        anInterface.FetchOutcomeDetailInServer();
    }

    public void loadData(){
        anInterface.LoadData();
    }
}
