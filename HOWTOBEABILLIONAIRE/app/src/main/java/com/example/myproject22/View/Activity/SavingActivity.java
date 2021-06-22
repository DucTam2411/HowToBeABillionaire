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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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


    // for debugg
    double ivSize = 0;
    private SavingPresenter mSavingPresenter;

    //region tiet kiem
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
    private UserClass userClass;
    //endregion


    //region Xử lí profile image
    private File photoFile = null;
    private String mCurrentPhotoPath;
    private Bitmap bmImage;
    //endregion


    //region Const mặc định để xét permission
    private static final int PERMISSION_IMAGE = 1000;
    private static final int PERMISSION_EXTERNAL_STORAGE = 1001;
    //endregion


    //region Xử lí thời gian và tiền
    private ArrayList<Date> arrayDate = new ArrayList<>();
    private int count_date = 0;
    private String money_string = "";
    //endregion




    //region BARCHART
    ArrayList<String> ngayTrongTuan = new ArrayList<String>();
    ArrayList<BarEntry> recordTietKiem = new ArrayList<>();
    private BarChart weekchart;
    //endregion


    //region DEFAULT FUNC

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_saving);

        mSavingPresenter = new SavingPresenter(this);
        mSavingPresenter.initView();
        mSavingPresenter.getBundleData();
        mSavingPresenter.createDataBarChart();


      /*  View overlay = findViewById(R.id.mylayout);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);*/

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSavingPresenter.chooseImage();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSavingPresenter.loadDataFromServer();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }
    //endregion


    //region INIT VIEWS

    @Override
    public void CreateDataBarChart() {

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
    public void InitViews() {
        //saving
        weekchart = findViewById(R.id.weekChar);
        tvDayStreak = findViewById(R.id.tvDayStreak);
        tvTotalSaving = findViewById(R.id.tvTotalSaving);

        //cl_user = findViewById(R.id.cl_user);
        tvUserName = findViewById(R.id.tv_username);
        tvUserDate = findViewById(R.id.tv_userdate);
        ivProfile = findViewById(R.id.profile_image);


        pb_saving = findViewById(R.id.pb_saving);
        pb_savingdate = findViewById(R.id.pb_savingdate);
        pb_savingmoney = findViewById(R.id.pb_savingmoney);
        cl_savingdate = findViewById(R.id.cl_savingdate);
        cl_savingmoney = findViewById(R.id.cl_savingmoney);


        //Ẩn layout khi đang load từ server
        pb_savingmoney.bringToFront();
        pb_savingdate.bringToFront();
        pb_saving.bringToFront();


        /* cl_user.setVisibility(View.INVISIBLE); AAAA*/


        cl_savingmoney.setVisibility(View.INVISIBLE);
        cl_savingdate.setVisibility(View.INVISIBLE);
        weekchart.setVisibility(View.INVISIBLE);


    }

    @Override
    public void GetBundleData() {
    /*    Intent intent = getIntent();
        Bundle bundle = intent.getExtras();*/
        id_user = 9;
        id_saving = 3;
    }

    //endregion


    //region BUTTON HANDLE

    @Override
    public void LoadUser(UserClass userClass) {

        tvUserName.setText(userClass.getFULLNAME());

        String date_temp = userClass.getDATESTART();
        String[] slipdate = date_temp.split(" ");
        String[] slipday = slipdate[0].split("-");
        String date_string = "Đã tham gia vào \nngày " + slipday[2] + "/" + slipday[1] + "/" + slipday[0];
        tvUserDate.setText(date_string);

        if (!userClass.getIMAGE().equals("null")) {
            Glide.with(SavingActivity.this).load(userClass.getIMAGE()).into(ivProfile);
        }

        /*   cl_user.setVisibility(View.VISIBLE);AAAA*/
    }


    public void onModifyGoalClicked(View view) {
        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }


    //endregion


    //region Take image from camera

    @Override
    public void ChooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SavingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_picture, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        ImageButton ivCamera = dialogView.findViewById(R.id.ivCamera);
        ImageButton ivGallery = dialogView.findViewById(R.id.ivGallery);

        AlertDialog dialog = builder.create();
        dialog.show();

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(SavingActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            mSavingPresenter.takeImageFromCamera();
                        } else {
                            Snackbar snackbar = Snackbar.make(weekchart, "All permissions are not granted", Snackbar.LENGTH_SHORT);
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                            snackbar.show();
                            /*Toast.makeText(AddingCategoryActivity.this, "All permissions are not granted", Toast.LENGTH_SHORT).show();*/
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
                dialog.dismiss();
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(SavingActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mSavingPresenter.takeImageFromGallery();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar snackbar = Snackbar.make(weekchart, "Permission is not granted", Snackbar.LENGTH_SHORT);
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                        /*Toast.makeText(AddingCategoryActivity.this, "Permission is not granted", Toast.LENGTH_SHORT).show();*/
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void TakeImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PERMISSION_EXTERNAL_STORAGE);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public void TakeImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Snackbar snackbar = Snackbar.make(weekchart, e.getMessage(), Snackbar.LENGTH_SHORT);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.show();
            /*Toast.makeText(AddingCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();*/
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.myproject22.provider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, PERMISSION_IMAGE);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
        }
    }

    @Override
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE: {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        bmImage = BitmapFactory.decodeStream(inputStream);
                        ivProfile.setImageBitmap(bmImage);
                        String image_string = SavingPresenter.convertByteToString(SavingPresenter.encodeTobase64(bmImage));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSavingPresenter.updateUserImageToServer(image_string);
                            }
                        }, 1000);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            case PERMISSION_IMAGE: {
                if (resultCode == RESULT_OK) {
                    bmImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    ivProfile.setImageBitmap(bmImage);
                    String image_string = SavingPresenter.convertByteToString(SavingPresenter.encodeTobase64(bmImage));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSavingPresenter.updateUserImageToServer(image_string);
                        }
                    }, 1000);
                }
            }
        }
    }

    @Override
    public void DeleteImage() {
        if (photoFile != null) {
            if (photoFile.exists()) {
                photoFile.delete();
            }
        }
    }


    //endregion


    //region DATABASE HANDLE
    @Override
    public void UpdateUserImageToServer(String image) {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "updateUserImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar snackbar = Snackbar.make(weekchart, response, Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
                if (response.equals("Update image succes")) {
                    DeleteImage();
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
                params.put("userimage", image);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SavingActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void FetchSavingDetailFromServer(String date_start, String date_end) {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getSavingDetailByDate.php", new Response.Listener<String>() {
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

                            String date_string = object.getString("DATE");
                            Double money = object.getDouble("MONEY");

                            //Load saving vào trong bar chart
                            Date date = SavingPresenter.DateFromString(date_string);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int dow = cal.get(Calendar.DAY_OF_WEEK);
                            if (dow == 1) {
                                recordTietKiem.get(6).setY(money.floatValue());
                            } else {
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SavingActivity.this);
        requestQueue.add(request);
    }

    public void FetchArrayDateFromServer() {
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

                            if (arrayDate.size() == 0) {
                                arrayDate.add(date);
                                count_date++;
                            } else {
                                Date last_array = arrayDate.get(arrayDate.size() - 1);
                                if (SavingPresenter.CalculateDateUse(last_array, date) > 1) {
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
    public void FetchMoneySavingFromServer() {
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
    public void FetchUserFromServer() {
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

                            if (!image_string.equals("null")) {
                                String url_image = urlString + "ImagesUser/" + image_string;
                                userClass = new UserClass(fullname, date_string, url_image);
                            } else {
                                userClass = new UserClass(fullname, date_string, image_string);
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
    public void LoadDataFromServer() {
        String date_start = SavingPresenter.StringFromDate(SavingPresenter.FindMondayFromDate(new Date()));
        String date_end = SavingPresenter.StringFromDate(SavingPresenter.FindEndOfWeek(SavingPresenter.FindMondayFromDate(new Date())));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSavingPresenter.fetchUserFromServer();
                mSavingPresenter.fetchArrayDateFromServer();
                mSavingPresenter.fetchMoneySavingFromServer();
                mSavingPresenter.fetchSavingDetailFromServer(date_start, date_end);
            }
        }, 1000);
    }

    @Override
    public void LoadDataFromServerToBarChart() {

        //get data from database
        BarDataSet barDataSet = new BarDataSet(recordTietKiem, "Ngày trong tuần");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(0f);
        barDataSet.setValueTypeface(Typeface.MONOSPACE);
        barDataSet.setBarBorderWidth(1);
        barDataSet.setBarBorderColor(Color.WHITE);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        barData.setValueTextColor(Color.WHITE);
        weekchart.setFitBars(true);
        weekchart.setData(barData);
        weekchart.setHighlightFullBarEnabled(true);


        weekchart.getAxisLeft().setTextColor(Color.WHITE);
        weekchart.getAxisRight().setTextSize(0f);


        Legend l = weekchart.getLegend();
        l.setTextColor(Color.WHITE);


        // set XAxis value formater
        XAxis xAxis = weekchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(ngayTrongTuan));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setLabelCount(ngayTrongTuan.size());
        xAxis.setTextColor(Color.WHITE);


        pb_saving.setVisibility(View.GONE);
        weekchart.setVisibility(View.VISIBLE);
        weekchart.animateY(1000);

    }


    //endregion
}

