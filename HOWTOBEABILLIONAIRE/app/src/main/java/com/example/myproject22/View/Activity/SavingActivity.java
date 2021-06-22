package com.example.myproject22.View.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myproject22.Model.SavingDatabaseHelper;
import com.example.myproject22.Model.UserClass;
import com.example.myproject22.R;
import com.example.myproject22.Presenter.SavingInterface;
import com.example.myproject22.Presenter.SavingPresenter;
import com.example.myproject22.View.Fragment.IncomeCategoryGraphFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.myproject22.Model.ConnectionClass.urlString;
import static com.example.myproject22.Util.FormatImage.ByteToBitmap;

public class SavingActivity extends AppCompatActivity implements SavingInterface {

    private BarChart weekchart;

    // for debugg
    double ivSize = 0;

    // tiet kiem
    private int id_saving = 3;
    private int id_user = 9;
    private TextView tvDayStreak;
    private TextView tvTotalSaving;
    private TextView tvUserName;
    private TextView tvUserDate;
    private CircleImageView ivProfile;
    private ProgressBar pb_saving;
    private ProgressBar pb_savingdate;
    private ProgressBar pb_savingmoney;
    private ConstraintLayout cl_savingdate;
    private ConstraintLayout cl_savingmoney;
    private ConstraintLayout cl_user;
    private MaterialCardView cardView;
    private UserClass userClass;

    //Xử lí thời gian và tiền
    private ArrayList<Date> arrayDate = new ArrayList<>();
    private int count_date = 0;
    private String money_string = "";

    // muc tieu
    private TextView tvGoalName;
    private TextView tvGoalDescription;
    private TextView tvMoneyGoal;
    private ImageView ivGoal;
    private TextRoundCornerProgressBar ProgressSaving;


    private static final int DAYSOFWEEK = 7;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SavingPresenter mSavingPresenter;

    SQLiteDatabase db;
    Cursor cursor;
    ArrayList<String> ngayTrongTuan = new ArrayList<String>();
    ArrayList<BarEntry> recordTietKiem = new ArrayList<>();

    SavingDatabaseHelper ASavingDatabaseHelper = new SavingDatabaseHelper(this, null, null, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_saving);

        mSavingPresenter = new SavingPresenter(this);
        mSavingPresenter.initView();
        mSavingPresenter.getBundleData();
        mSavingPresenter.createDataBarChart();

        /*AddRecords();
        mSavingPresenter.LoadGetTietKiemData();
        mSavingPresenter.LoadTietKiem();*/
        mSavingPresenter.LoadMucTieu();

      /*  View overlay = findViewById(R.id.mylayout);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);*/

        /*ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSavingPresenter.chooseImage();
            }
        });*/

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSavingPresenter.btnUserClick();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ASavingDatabaseHelper.closeAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
       mSavingPresenter.loadDataFromServer();
    }

    @Override
    public void CreateDataBarChart(){

        //Load bar chart cột x
        ngayTrongTuan.add("Thứ 2");
        ngayTrongTuan.add("Thứ 3");
        ngayTrongTuan.add("Thứ 4");
        ngayTrongTuan.add("Thứ 5");
        ngayTrongTuan.add("Thứ 6");
        ngayTrongTuan.add("Thứ 7");
        ngayTrongTuan.add("Chủ Nhật");

        //Khởi tạo bar chart cho cột y tương ứng với từng cột x
        recordTietKiem.add(new BarEntry(0, 0));
        recordTietKiem.add(new BarEntry(1, 0));
        recordTietKiem.add(new BarEntry(2, 0));
        recordTietKiem.add(new BarEntry(3, 0));
        recordTietKiem.add(new BarEntry(4, 0));
        recordTietKiem.add(new BarEntry(5, 0));
        recordTietKiem.add(new BarEntry(6, 0));
    }

    @Override
    public void LoadDataFromServerToBarChart() {
        //get data from database
        BarDataSet barDataSet = new BarDataSet(recordTietKiem, "Ngày trong tuần");
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(0f);
        barDataSet.setValueTypeface(Typeface.MONOSPACE);
        barDataSet.setBarBorderWidth(1);
        BarData barData = new BarData(barDataSet);


        barData.setBarWidth(0.5f);
        weekchart.setFitBars(true);
        weekchart.setData(barData);
        weekchart.getDescription().setText("");
        weekchart.setHighlightFullBarEnabled(true);


        // set XAxis value formater
        XAxis xAxis = weekchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(ngayTrongTuan));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setLabelCount(ngayTrongTuan.size());

        pb_saving.setVisibility(View.GONE);
        weekchart.setVisibility(View.VISIBLE);
    }


    @Override
    public void LoadChiTietTietKiem() {
        new LoadChiTietTietKiem().execute();
    }

    @Override
    public void LoadTietKiem() {
        new LoadTietKiem().execute();
    }

    @Override
    public void LoadMucTieu() {
        new LoadMucTieu().execute();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mSavingPresenter.LoadMucTieu();
    }


    public void AddRecords() {
/*

        ASavingDatabaseHelper.insertChitietTienChi(20, "me cho", 1, null, "2020-06-03");
        ASavingDatabaseHelper.insertChitietTienChi(20, "me cho", 1, null, "2020-06-04");
        ASavingDatabaseHelper.insertChitietTienChi(20, "me cho", 1, null, "2020-06-05");
        ASavingDatabaseHelper.insertChitietTienChi(20, "me cho", 1, null, "2020-06-06");

        ASavingDatabaseHelper.insertChiTietTienThu(10, "ssa", 1, "2020-06-07");
        ASavingDatabaseHelper.insertChitietTienChi(10, "ssa", 1, null,"2020-06-07");


        ASavingDatabaseHelper.insertChitietTienChi(10, "ssa", 1, null,"2020-06-08");
        ASavingDatabaseHelper.insertChiTietTienThu(10, "ssa", 1, "2020-06-09");
*/


    }

    @Override
    public void InitViews() {
        //saving
        weekchart = findViewById(R.id.weekChar);
        tvDayStreak = findViewById(R.id.tvDayStreak);
        tvTotalSaving = findViewById(R.id.tvTotalSaving);
        tvUserName = findViewById(R.id.tv_username);
        tvUserDate = findViewById(R.id.tv_userdate);
        ivProfile = findViewById(R.id.profile_image);
        pb_saving = findViewById(R.id.pb_saving);
        pb_savingdate = findViewById(R.id.pb_savingdate);
        pb_savingmoney = findViewById(R.id.pb_savingmoney);
        cl_savingdate = findViewById(R.id.cl_savingdate);
        cl_savingmoney = findViewById(R.id.cl_savingmoney);
        cl_user = findViewById(R.id.cl_user);
        cardView = findViewById(R.id.materialCardView);

        //Ẩn layout khi đang load từ server
        pb_savingmoney.bringToFront();
        pb_savingdate.bringToFront();
        pb_saving.bringToFront();
        cl_user.setVisibility(View.INVISIBLE);
        cl_savingmoney.setVisibility(View.INVISIBLE);
        cl_savingdate.setVisibility(View.INVISIBLE);
        weekchart.setVisibility(View.INVISIBLE);

        // goal
        tvGoalName = findViewById(R.id.tvGoalName);
        tvGoalDescription = findViewById(R.id.tvGoalDescription);
        tvMoneyGoal = findViewById(R.id.tvMoneyGoal);
        ProgressSaving = findViewById(R.id.Progress_saving);
        ivGoal = findViewById(R.id.ivGoal);
    }

    @Override
    public void GetBundleData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("ID_USER");
        id_saving = bundle.getInt("ID_SAVING");
    }

    @Override
    public void FetchSavingDetailFromServer(String date_start, String date_end){
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getSavingDetailByDate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("TESTER1", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String date_string = object.getString("DATE");
                            Double money = object.getDouble("MONEY");
                            Log.i("TESTRECORD", String.valueOf(money));
                            //Load saving vào trong bar chart
                            Date date = SavingPresenter.DateFromString(date_string);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int dow = cal.get(Calendar.DAY_OF_WEEK);
                            if(dow == 1){
                                recordTietKiem.get(6).setY(money.floatValue());
                            }
                            else{
                                recordTietKiem.get(dow - 2).setY(money.floatValue());
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSavingPresenter.loadDataFromServerToBarChart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(weekchart, error.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_saving", String.valueOf(id_saving));
                params.put("datestart", date_start);
                params.put("dateend", date_end);
                Log.i("TESTRECORD", date_start + "\n" + date_end);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SavingActivity.this);
        requestQueue.add(request);
    }

    public void FetchArrayDateFromServer(){
        arrayDate = new ArrayList<>();
        count_date = 0;
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getSavingDetailDate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String date_string = object.getString("DATE");
                            Date date = SavingPresenter.DateFromString(date_string);

                            if(arrayDate.size() == 0){
                                arrayDate.add(date);
                                count_date++;
                            } else{
                                Date last_array = arrayDate.get(arrayDate.size() - 1);
                                if(SavingPresenter.CalculateDateUse(last_array, date) > 1){
                                    count_date = 0;
                                }
                                arrayDate.add(date);
                                count_date++;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pb_savingdate.setVisibility(View.GONE);
                tvDayStreak.setText(String.valueOf(count_date));
                cl_savingdate.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(weekchart, error.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_saving", String.valueOf(id_saving));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SavingActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void FetchMoneySavingFromServer(){
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getMoneySaving.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            Double money_saving = object.getDouble("TOTAL_SAVING");
                            money_string = SavingPresenter.GetStringMoney(money_saving);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pb_savingmoney.setVisibility(View.GONE);
                tvTotalSaving.setText(money_string);
                cl_savingmoney.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(weekchart, error.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_saving", String.valueOf(id_saving));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SavingActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void FetchUserFromServer(){
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("TESTER", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String fullname = object.getString("FULLNAME");
                            String date_string = object.getString("DATESTART");
                            String image_string = object.getString("USERIMAGE");

                            if(!image_string.equals("null")){
                                String url_image = urlString + "ImagesUser/" + image_string;
                                userClass = new UserClass(fullname, date_string, url_image);
                            }
                            else{
                                userClass = new UserClass(fullname,date_string,image_string);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSavingPresenter.loadUser(userClass);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(weekchart, error.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SavingActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void LoadUser(UserClass userClass) {
        tvUserName.setText(userClass.getFULLNAME());
        String date_temp = userClass.getDATESTART();
        String[] slipdate = date_temp.split(" ");
        String[] slipday = slipdate[0].split("-");
        String date_string = "Đã tham gia vào \nngày " + slipday[2] + "/" + slipday[1] + "/" + slipday[0];
        tvUserDate.setText(date_string);

        if(!userClass.getIMAGE().equals("null")){
            Glide.with(SavingActivity.this).load(userClass.getIMAGE()).into(ivProfile);
        }

        cl_user.setVisibility(View.VISIBLE);
    }

    @Override
    public void LoadDataFromServer() {
        String date_start = SavingPresenter.StringFromDate(SavingPresenter.FindMondayFromDate(new Date()));
        String date_end = SavingPresenter.StringFromDate(SavingPresenter.FindEndOfWeek(SavingPresenter.FindMondayFromDate(new Date())));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSavingPresenter.fetchUserFromServer();
                mSavingPresenter.fetchArrayDateFromServer();
                mSavingPresenter.fetchMoneySavingFromServer();
                mSavingPresenter.fetchSavingDetailFromServer(date_start,date_end);
            }
        }, 1000);
    }

    @Override
    public void BtnUserClick() {
        Intent intent = new Intent(SavingActivity.this, UserAcitvity.class);
        intent.putExtra("ID_USER", id_user);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    // async stask
    class LoadChiTietTietKiem extends AsyncTask<Void, Process, Void> {

        @Override
        protected void onPreExecute() {
            db = ASavingDatabaseHelper.getWritableDatabase();
            weekchart.animateY(2000);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                cursor = db.query("CHITIETTIETKIEM", new String[]{"_id_tietkiem", "SOTIENTIETKIEMTRONGNGAY", "NGAY_TIETKIEM"},
                        null, null, null, null, "_id_ChiTietTietKiem DESC");

                if (cursor.moveToFirst()) {
                    int cnt = 0;
                    do {
                        cnt++;
                        double tietKiem = cursor.getDouble(1);
                        String strDate = cursor.getString(2);
                        Date date = dateFormat.parse(strDate);
                        int ngay = date.getDay();
                        recordTietKiem.add(new BarEntry((float) ngay, (float) tietKiem));
                    }
                    while (cnt <= DAYSOFWEEK && cursor.moveToNext());
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // assign value to char1
            weekchart = findViewById(R.id.weekChar);

            //get data from database
            BarDataSet barDataSet = new BarDataSet(recordTietKiem, "Ngày trong tuần");
            barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(0f);
            barDataSet.setValueTypeface(Typeface.MONOSPACE);
            barDataSet.setBarBorderWidth(1);
            BarData barData = new BarData(barDataSet);


            barData.setBarWidth(0.5f);
            weekchart.setFitBars(true);
            weekchart.setData(barData);
            weekchart.getDescription().setText("");
            weekchart.setHighlightFullBarEnabled(true);


            // set XAxis value formater
            XAxis xAxis = weekchart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(ngayTrongTuan));

            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(true);
            xAxis.setGranularity(1f);
            xAxis.setDrawLabels(true);
            xAxis.setLabelCount(ngayTrongTuan.size());

        }
    }

    class LoadTietKiem extends AsyncTask<Void, Process, Boolean> {

        double totalSaving;
        int dayStreak;


        @Override
        protected void onPreExecute() {

            // sieu nhan dien quang


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            cursor = ASavingDatabaseHelper.getTietKiem();
            if (cursor.moveToFirst()) {
                totalSaving = cursor.getDouble(1);
                dayStreak = cursor.getInt(4);
                return true;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            tvTotalSaving.setText(String.valueOf(totalSaving));
            tvDayStreak.setText(String.valueOf(dayStreak));
        }
    }

    class LoadMucTieu extends AsyncTask<Void, Process, Boolean> {


        String goalName;
        String goalDescription;
        double goalMoney;
        double SavingMoney;
        byte[] goal_image;
        String strGoal;
        double progress;

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            cursor = ASavingDatabaseHelper.getMucTieu();

            try {
                if (cursor.moveToFirst()) {
                    goalName = "[ " + cursor.getString(1) + " ]";
                    goalDescription = "*" + cursor.getString(2) + "*";
                    goalMoney = cursor.getDouble(3);
                    SavingMoney = cursor.getDouble(4);
                    strGoal = SavingMoney + "/" + goalMoney;
                    progress = SavingMoney * 100 / goalMoney;
                    if (progress >= 100) {
                        progress = 100;
                    }

                    goal_image = cursor.getBlob(5);


                    return true;
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean havingRecord) {

            if (havingRecord) {

                tvGoalName.setText(goalName);
                tvGoalDescription.setText(goalDescription);
                tvMoneyGoal.setText(strGoal);

                ProgressSaving.setProgressText(String.valueOf(Math.round((float) progress)));
                ProgressSaving.setProgress(Math.round((float) progress));


                Toast.makeText(SavingActivity.this, String.valueOf(progress), Toast.LENGTH_SHORT).show();
                Bitmap bitmap = ByteToBitmap(goal_image);
                Drawable d = new BitmapDrawable(bitmap);
                ivGoal.setImageDrawable(d);
                if (progress <= 90) {
                    ProgressSaving.setSecondaryProgress((float) progress + 10);
                }

            }
        }
    }


    public void onModifyGoalClicked(View view) {
        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

}

