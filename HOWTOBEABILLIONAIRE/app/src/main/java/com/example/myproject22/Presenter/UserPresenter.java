package com.example.myproject22.Presenter;

import com.example.myproject22.Model.UserClass;

public class UserPresenter {
    UserInterface anInterface;

    public UserPresenter(UserInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void setInit() {
        anInterface.SetInit();
    }

    public void getBundleData() {
        anInterface.GetBundleData();
    }

    public void btnUpdateUser() {
        anInterface.BtnUpdateUser();
    }

    public void btnPassword() {
        anInterface.BtnPassword();
    }

    public void btnMap() {
        anInterface.BtnMap();
    }

    public void btnLogout(){
        anInterface.BtnLogOut();
    }

    public void fetchUserFromServer(){
        anInterface.FetchUserFromServer();
    }

    public void loadUser(UserClass userClass){
        anInterface.LoadUser(userClass);
    }

    public void loadDataToLayout() {
        anInterface.LoadDataToLayout();
    }
}
