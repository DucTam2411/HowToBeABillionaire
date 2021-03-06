package com.example.myproject22.Model;

public class CategoryClass {

    //region Component
    private int ID_CATEGORY;
    private String NAME;
    private String IMAGE;
    //endregion

    //region Constructor
    public CategoryClass(int ID_CATEGORY, String NAME, String IMAGE) {
        this.ID_CATEGORY = ID_CATEGORY;
        this.NAME = NAME;
        this.IMAGE = IMAGE;
    }
    //endregion

    //region Get and Set
    public int Get_ID(){return this.ID_CATEGORY;}

    public String Get_NAME(){return this.NAME;}
    public void Set_NAME(String name){this.NAME = name;}

    public String Get_IMAGE(){return this.IMAGE;}
    public void Set_IMAGE(String image){this.IMAGE = image;}
    //endregion

}
