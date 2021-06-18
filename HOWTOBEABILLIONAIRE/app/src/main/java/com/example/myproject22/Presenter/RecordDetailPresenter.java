package com.example.myproject22.Presenter;

import android.view.View;

public class RecordDetailPresenter {
    private RecordDetailInterface anInterface;


    public RecordDetailPresenter(RecordDetailInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void GetBundleData(){
        anInterface.GetBundleData();
    }

    public  void setInit(){
        anInterface.SetInit();
    }

    public void loadDataToLayout(){
        anInterface.LoadDataToLayout();
    }

    public void fetchIncomeDatFromServer(){
        anInterface.FetchIncomeDataFromServer();
    }

    public  void fetchOutcomeDataFromServer(){
        anInterface.FetchOutcomeDataFromServer();
    }

    public void loadDataFromServer(){
        anInterface.LoadFromServer();
    }

    public void prepareMedia(String url){
        anInterface.PrepareMedia(url);
    }

    public String getTimeMedia(long millionsecond){
        return anInterface.GetTimeMedia(millionsecond);
    }

    public void updateSeekbar(){
        anInterface.UpdateSeekbar();
    }

    public void getPauseClick(){
        anInterface.GetPauseClick();;
    }

    public boolean getSeekbarTouch(View view){
        return anInterface.GetSeekbarTouch(view);
    }

    public void setCompleteMedia(){
        anInterface.SetCompleteMedia();
    }

    public void setNext5Second(){
        anInterface.SetNext5Second();
    }

    public void setBack5Second(){
        anInterface.SetBack5Second();
    }

    public void setReleaseMedia(){
        anInterface.SetRealseMedia();
    }
}
