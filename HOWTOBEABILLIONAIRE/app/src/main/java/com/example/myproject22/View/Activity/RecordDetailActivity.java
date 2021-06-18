package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myproject22.Model.DetailItem;
import com.example.myproject22.Presenter.RecordDetailInterface;
import com.example.myproject22.Presenter.RecordDetailPresenter;
import com.example.myproject22.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.alterac.blurkit.BlurLayout;

import static com.example.myproject22.Model.ConnectionClass.urlAudio;
import static com.example.myproject22.Model.ConnectionClass.urlImage;
import static com.example.myproject22.Model.ConnectionClass.urlImageCategory;
import static com.example.myproject22.Model.ConnectionClass.urlString;

public class RecordDetailActivity extends AppCompatActivity implements RecordDetailInterface {

    //ID_DETAIL
    private int id_detail;
    private int isCategory;

    //Component
    private TextView tvMoney;
    private TextView tvDescription;
    private TextView tvNameCategory;
    private TextView tvTime;
    private TextView tvStart;
    private TextView tvEnd;
    private ImageView ivImage;
    private SeekBar seekBar;
    private ImageButton btnDescrease;
    private ImageButton btnIncrease;
    private ImageButton btnPause;
    private BlurLayout blurLayout_1;
    private BlurLayout blurLayout_2;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    private DetailItem item;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Boolean flag = false;
    private Boolean isLoading = true;

    //Presenter
    private RecordDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record_detail);

        presenter = new RecordDetailPresenter(this);
        presenter.setInit();

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getPauseClick();
            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return presenter.getSeekbarTouch(v);
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(percent);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                presenter.setCompleteMedia();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setNext5Second();
            }
        });

        btnDescrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setBack5Second();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.GetBundleData();

        presenter.loadDataFromServer();
    }

    @Override
    public void SetInit() {
        tvMoney = findViewById(R.id.textView6);
        tvDescription = findViewById(R.id.tvDescriptionRecord);
        tvNameCategory = findViewById(R.id.tvNameCategory);
        ivImage = findViewById(R.id.imageView6);
        tvTime = findViewById(R.id.textView7);
        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);
        seekBar = findViewById(R.id.seekBar2);
        btnDescrease = findViewById(R.id.imageButton5);
        btnIncrease = findViewById(R.id.imageButton3);
        btnPause = findViewById(R.id.imageButton4);
        linearLayout = findViewById(R.id.linearLayout2);
        blurLayout_1 = findViewById(R.id.blurLayoutRecord2);
        blurLayout_2 = findViewById(R.id.blurLayoutRecord);
        progressBar = findViewById(R.id.pbRecord);
        progressBar.bringToFront();
        mediaPlayer = new MediaPlayer();
        seekBar.setMax(100);

        progressBar.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        blurLayout_2.setVisibility(View.INVISIBLE);
        blurLayout_1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void GetBundleData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_detail = bundle.getInt("ID_DETAIL");
        isCategory = bundle.getInt("IS_CATEGORY");
    }

    @Override
    public void LoadDataToLayout() {
        String sMoney = String.valueOf(item.get_MONEY());
        String isPlus = isCategory == 1 ? "+ " : "- ";
        tvMoney.setText(isPlus + " " + sMoney);
        tvDescription.setText(item.get_DESCRIPTION());
        tvNameCategory.setText(item.get_NAME());

        String[] splitdate = item.get_DATE().split(" ");
        tvTime.setText(splitdate[1] + " ngày " + splitdate[0]);

        if (!item.get_IMAGE().equals("NULL")) {
            Glide.with(RecordDetailActivity.this).load(item.get_IMAGE()).into(ivImage);
        }

        progressBar.setVisibility(View.GONE);
        blurLayout_1.setVisibility(View.VISIBLE);
        blurLayout_2.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void LoadDataToLayoutNoAudio() {
        String sMoney = String.valueOf(item.get_MONEY());
        String isPlus = isCategory == 1 ? "+ " : "- ";
        tvMoney.setText(isPlus + " " + sMoney);
        tvDescription.setText(item.get_DESCRIPTION());
        tvNameCategory.setText(item.get_NAME());

        String[] splitdate = item.get_DATE().split(" ");
        tvTime.setText(splitdate[1] + " ngày " + splitdate[0]);

        if (!item.get_IMAGE().equals("NULL")) {
            Glide.with(RecordDetailActivity.this).load(item.get_IMAGE()).into(ivImage);
        }

        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
    }


    @Override
    public void FetchIncomeDataFromServer() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getIncomeDetailByID.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INCOME", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String smoney = object.getString("MONEY");
                                String description = object.getString("DESCRIPTION");
                                String sdate = object.getString("DATE");
                                String name = object.getString("NAME");
                                String image = object.getString("IMAGE");
                                String imagecategory = object.getString("IMAGECATEGORY");
                                String audio = object.getString("AUDIO");

                                SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                Date dateObj = curFormater.parse(sdate);


                                Double money = Double.parseDouble(smoney);

                                String url_image = image.equals("null") ? "NULL" : urlImage + image;
                                String url_image_category = imagecategory.equals("null") ? "NULL" : urlImageCategory + imagecategory;
                                String url_audio = audio.equals("null") ? "NULL" : urlAudio + audio;

                                item = new DetailItem(money, description, sdate, name, url_image, url_image_category, url_audio, dateObj);

                            }
                        }
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                if(item.get_AUDIO().equals("NULL")){
                    presenter.loadDataToLayoutNoAudio();
                }
                else{
                    presenter.loadDataToLayout();
                    presenter.prepareMedia(item.get_AUDIO());
                    isLoading = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecordDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_incomedetail", String.valueOf(id_detail));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RecordDetailActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void FetchOutcomeDataFromServer() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getOutcomeDetailByID.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String smoney = object.getString("MONEY");
                                String description = object.getString("DESCRIPTION");
                                String sdate = object.getString("DATE");
                                String name = object.getString("NAME");
                                String imagecategory = object.getString("IMAGECATEGORY");
                                String image = object.getString("IMAGE");
                                String audio = object.getString("AUDIO");

                                SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                Date dateObj = curFormater.parse(sdate);


                                Double money = Double.parseDouble(smoney);

                                String url_image = image.equals("null") ? "NULL" : urlImage + image;
                                String url_image_category = imagecategory.equals("null") ? "NULL" : urlImageCategory + imagecategory;
                                String url_audio = audio.equals("null") ? "NULL" : urlAudio + audio;

                                item = new DetailItem(money, description, sdate, name, url_image, url_image_category, url_audio, dateObj);

                            }
                        }
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }


                if(item.get_AUDIO().equals("NULL")){
                    presenter.loadDataToLayoutNoAudio();
                }
                else{
                    presenter.loadDataToLayout();
                    presenter.prepareMedia(item.get_AUDIO());
                    isLoading = false;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecordDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_outcomedetail", String.valueOf(id_detail));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RecordDetailActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void LoadFromServer() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isCategory == 1) {
                    presenter.fetchIncomeDatFromServer();
                } else {
                    presenter.fetchOutcomeDataFromServer();
                }
            }
        }, 3000);
    }

    @Override
    public void PrepareMedia(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            tvEnd.setText(presenter.getTimeMedia(mediaPlayer.getDuration()));
            isLoading = false;
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public String GetTimeMedia(long millionsecond) {
        String timeString = "";
        String secondString;

        int hour = (int) (millionsecond / (1000 * 60 * 60));
        int minute = (int) (millionsecond % (1000 * 60 * 60)) / (1000 * 60);
        int second = (int) ((millionsecond % (1000 * 60 * 60) % (1000 * 60)) / 1000);

        if (hour > 0) {
            timeString = hour + ":";
        }

        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = "" + second;
        }

        timeString = timeString + minute + ":" + secondString;

        return timeString;
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            if (flag == true) {
                presenter.updateSeekbar();
                long currentduration = mediaPlayer.getCurrentPosition();
                tvStart.setText(presenter.getTimeMedia(currentduration));
            }
        }
    };

    @Override
    public void UpdateSeekbar() {
        if (flag == true) {
            if (mediaPlayer.isPlaying()) {
                seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
                handler.postDelayed(updater, 1000);
            }
        }
    }

    @Override
    public void GetPauseClick() {
        if (mediaPlayer.isPlaying()) {
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            btnPause.setImageResource(R.drawable.icon_play_red);
        } else {
            flag = true;
            mediaPlayer.start();
            btnPause.setImageResource(R.drawable.icon_pause_red);
            presenter.updateSeekbar();
        }
    }

    @Override
    public boolean GetSeekbarTouch(View view) {
        seekBar = (SeekBar) view;
        int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
        mediaPlayer.seekTo(playPosition);
        tvStart.setText(presenter.getTimeMedia(mediaPlayer.getCurrentPosition()));
        return false;
    }

    @Override
    public void SetCompleteMedia() {
        flag = false;
        seekBar.setProgress(0);
        btnPause.setImageResource(R.drawable.icon_play_red);
        tvStart.setText("00:00");
        tvEnd.setText("00:00");
        mediaPlayer.reset();
        presenter.prepareMedia(item.get_AUDIO());
    }

    @Override
    public void SetNext5Second() {
        if (flag == true) {
            int ADD_SECOND = 5000;

            int curPosition = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (curPosition + ADD_SECOND < duration) {
                mediaPlayer.seekTo(curPosition + ADD_SECOND);
                presenter.updateSeekbar();
            }
        }
    }

    @Override
    public void SetBack5Second() {
        if (flag == true) {
            int BACK_SECOND = 5000;

            int curPosition = mediaPlayer.getCurrentPosition();

            if (curPosition - BACK_SECOND > 0) {
                mediaPlayer.seekTo(curPosition - BACK_SECOND);
                presenter.updateSeekbar();
            }
        }
    }

    @Override
    public void SetRealseMedia() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        flag = false;
    }

    @Override
    public void onBackPressed() {
        if(isLoading == false) {
            super.onBackPressed();
            presenter.setReleaseMedia();
        }
        else{
            super.onBackPressed();
        }
    }
}