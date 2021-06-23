package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.myproject22.R;
import com.example.myproject22.Util.CategoryGraphPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ReportCategoryGraphActivity extends AppCompatActivity  {

    //region Parameter
    private int id_user =0 ;
    private int id_income = 0;
    private int id_outcome = 0;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_graph);

        GetBundleData();

        //region Xử lí ViewPager
        CategoryGraphPagerAdapter categoryGraphPagerAdapter = new CategoryGraphPagerAdapter(getSupportFragmentManager(), id_user, id_income, id_outcome);
        ViewPager viewPager = findViewById(R.id.category_graph_pager);
        viewPager.setAdapter(categoryGraphPagerAdapter);
        //endregion

        //region Xử lí TabLayout
        TabLayout tabLayout = findViewById(R.id.category_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_edit);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_music);
        //endregion

    }

    //region Get bundle
    public void GetBundleData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("ID_USER");
        id_income = bundle.getInt("ID_INCOME");
        id_outcome = bundle.getInt("ID_OUTCOME");
    }
    //endregion
}