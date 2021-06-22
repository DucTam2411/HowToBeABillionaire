package com.example.myproject22.Presenter;

import com.example.myproject22.Model.UserClass;

public interface UserInterface {
    public void SetInit();

    public void GetBundleData();

    public void BtnUpdateUser();
    public void BtnPassword();
    public void BtnMap();
    public void BtnLogOut();

    public void FetchUserFromServer();
    public void LoadUser(UserClass userClass);
    public void LoadDataToLayout();
}
