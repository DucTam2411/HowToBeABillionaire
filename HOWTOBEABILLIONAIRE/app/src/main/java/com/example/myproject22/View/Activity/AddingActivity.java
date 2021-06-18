package com.example.myproject22.View.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myproject22.Model.CategoryClass;
import com.example.myproject22.Presenter.AddingMoneyInterface;
import com.example.myproject22.Presenter.AddingMoneyPresentent;
import com.example.myproject22.R;
import com.example.myproject22.Util.CategoryAdapter;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.alterac.blurkit.BlurLayout;

import static com.example.myproject22.Model.ConnectionClass.urlString;
import static com.example.myproject22.Presenter.AddingMoneyPresentent.convertByteToString;
import static com.example.myproject22.Presenter.AddingMoneyPresentent.encodeTobase64;
import static com.example.myproject22.Presenter.AddingMoneyPresentent.isNumeric;

public class AddingActivity extends AppCompatActivity implements AddingMoneyInterface {
    //Khởi tạo các component để thực hiện event

    //Component về camera
    private ImageButton btnImage;
    private TextView tvImage;
    private Bitmap bmImage;
    private File photoFile = null;
    String mCurrentPhotoPath;

    //Component về record và audio
    private ImageButton btnPlay;
    private ImageButton btnRecord;
    private WaveVisualizer mVisualizer;
    private BlurLayout blurLayout;
    private MediaPlayer mediaPlayer;
    private Boolean isRecord = false;
    private Boolean isPlaying = false;
    private MediaRecorder mediaRecorder;
    private String recordFile = "NO";
    private long starttime = 0;
    private long stoptime = 0;

    //Component về list category
    private RecyclerView categoryRecycler;
    private RecyclerView categoryRecycler1;
    private BottomSheetBehavior bottomSheetBehavior;
    private ConstraintLayout playerSheet;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    TextView tvChooseImage;
    MaterialButton btnAddCategory;

    //Component về tiền
    private EditText etMoney;
    private SeekBar seekBar;
    private EditText etDescription;

    private ImageButton btnSaving;
    private ProgressBar progressBar3;

    //Các array list để lấy danh sách
    private ArrayList<CategoryClass> arrayList = new ArrayList<>();
    private ArrayList<CategoryClass> arrayList1 = new ArrayList<>();
    private CategoryClass categoryClass;


    //Const mặc định để xét permission
    private static final int PERMISSION_IMAGE = 1000;
    private static final int PERMISSION_EXTERNAL_STORAGE = 1001;
    private static final int PERMISSION_AUDIO = 1002;

    //Presenter
    AddingMoneyPresentent addingMoneyPresentent;

    //Parameter for saving
    int isCategory = 0;
    Boolean isMax = false;

    private int id_user;
    private int id_income;
    private int id_outcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adding);

        addingMoneyPresentent = new AddingMoneyPresentent(this);
        addingMoneyPresentent.getDataBundle();
        addingMoneyPresentent.setInit();


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingMoneyPresentent.chooseImage();
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(AddingActivity.this,"Start", Toast.LENGTH_SHORT).show();*/
                addingMoneyPresentent.CaptureRecord();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingMoneyPresentent.CaptureAudio();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isMax == false) {
                    int value = progress;
                    if (value == 0) {
                        etMoney.setText("");
                    } else {
                        String svalue = String.valueOf(value);
                        etMoney.setText(svalue);
                        etMoney.setSelection(svalue.length());
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isMax = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Hide keyboard
        etMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    addingMoneyPresentent.hideKeyBoard(v);
                }
            }
        });

        //Hide keyboard
        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    addingMoneyPresentent.hideKeyBoard(v);
                }
            }
        });

        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addingMoneyPresentent.SetOnTextChange(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etMoney.isEnabled() == false) {
                    etMoney.setEnabled(true);
                }
            }
        });

        btnSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar3.setVisibility(View.VISIBLE);
                String money = etMoney.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                String category = tvChooseImage.getText().toString().trim();
                int category_id = addingMoneyPresentent.findIdByName(category);

                String image = addingMoneyPresentent.getStringImage();
                String audio = addingMoneyPresentent.getStringAudio();

                addingMoneyPresentent.savingMoneyData(money, description, category_id, image, audio);

                /*Toast.makeText(AddingActivity.this, String.valueOf(id_user) + "\n" + String.valueOf(id_income) + "\n" + String.valueOf(id_outcome), Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    @Override
    public void GetDataBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        id_user = bundle.getInt("ID_USER");
        id_income = bundle.getInt("ID_INCOME");
        id_outcome = bundle.getInt("ID_OUTCOME");
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoryRecycler.setVisibility(View.INVISIBLE);
        categoryRecycler1.setVisibility(View.INVISIBLE);
        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        LoadCategory();
    }

    @Override
    public void LoadCategory() {
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addingMoneyPresentent.fetchIncomeCategoryFromServer();
                addingMoneyPresentent.fetchOutcomeCategoryFromServer();
            }
        }, 3000);
    }

    @Override
    public void FetchIncomeCategory() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getIncomeCategory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("ID_CATEGORY");
                            String name = object.getString("NAME");
                            String image_category = object.getString("IMAGE");

                            String url_image = urlString + "ImagesCategory/" + image_category;

                            categoryClass = new CategoryClass(Integer.parseInt(id), name, url_image);
                            arrayList.add(categoryClass);
                        }

                        CategoryAdapter adapter = new CategoryAdapter(AddingActivity.this, arrayList, bottomSheetBehavior, tvChooseImage, btnAddCategory, id_user);
                        categoryRecycler.setAdapter(adapter);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(AddingActivity.this);
                        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        categoryRecycler.setLayoutManager(layoutManager);
                        categoryRecycler.setVisibility(View.VISIBLE);
                        progressBar1.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar1.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void FetchOutcomeCategory() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getOutcomeCategory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("ID_CATEGORY");
                            String name = object.getString("NAME");
                            String image_category = object.getString("IMAGE");

                            String url_image = urlString + "ImagesCategory/" + image_category;

                            categoryClass = new CategoryClass(Integer.parseInt(id), name, url_image);
                            arrayList1.add(categoryClass);
                        }

                        LinearLayoutManager layoutManager1 = new LinearLayoutManager(AddingActivity.this);
                        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);

                        CategoryAdapter adapter1 = new CategoryAdapter(AddingActivity.this, arrayList1, bottomSheetBehavior, tvChooseImage, btnAddCategory, id_user);
                        categoryRecycler1.setAdapter(adapter1);
                        categoryRecycler1.setLayoutManager(layoutManager1);
                        categoryRecycler1.setVisibility(View.VISIBLE);
                        progressBar2.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar2.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void ResetSound() {
        if (isRecord == true) {
            addingMoneyPresentent.stopRecord();
        }
        if (isPlaying == true) {
            addingMoneyPresentent.stopAudio();
        }
        DeleteImage();
        DeleteRecord();
    }

    @Override
    public void DeleteRecord() {
        if (!recordFile.equals("NO")) {
            String recordPath = AddingActivity.this.getExternalFilesDir("/").getAbsolutePath();
            String folder = recordPath + "/" + recordFile;
            File file = new File(folder);
            if (file.exists()) {
                file.delete();
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

    @Override
    public Boolean CheckPermissionRecord() {
        if (ContextCompat.checkSelfPermission(AddingActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(AddingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(AddingActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_AUDIO);
            return false;
        }
    }

    @Override
    public Boolean CheckPermissionImage() {
        if (ContextCompat.checkSelfPermission(AddingActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(AddingActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_IMAGE);
            return false;
        }
    }

    @Override
    public Boolean CheckPermissionRead() {
        if (ContextCompat.checkSelfPermission(AddingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(AddingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_STORAGE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_IMAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    TakeImageFromCamera();
                } else {
                    Toast.makeText(AddingActivity.this, "Camera permission not Granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case PERMISSION_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TakeImageFromGallery();
                } else {
                    Toast.makeText(AddingActivity.this, "Read external permission not Granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }

    @Override
    public void GetNoMoneyData() {
        Toast.makeText(getApplicationContext(), "Nhập thông tin về tiền!", Toast.LENGTH_SHORT).show();
        progressBar3.setVisibility(View.GONE);
    }

    @Override
    public void GetNoCategoryData() {
        Toast.makeText(getApplicationContext(), "Chọn loại thu chi.", Toast.LENGTH_SHORT).show();
        progressBar3.setVisibility(View.GONE);
    }

    @Override
    public void SetInit() {
        btnSaving = findViewById(R.id.imageButton2);
        btnImage = findViewById(R.id.imageButton);
        tvImage = findViewById(R.id.textView11);
        btnPlay = findViewById(R.id.btnPlay);
        btnRecord = findViewById(R.id.imageView2);
        blurLayout = findViewById(R.id.blurLayout);
        mVisualizer = findViewById(R.id.blast);
        playerSheet = findViewById(R.id.player_sheet);
        tvChooseImage = findViewById(R.id.tvChooseCategory);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        categoryRecycler = findViewById(R.id.category_recycler);
        categoryRecycler1 = findViewById(R.id.category_recycler2);
        progressBar1 = findViewById(R.id.progress1);
        progressBar2 = findViewById(R.id.progress2);
        etMoney = findViewById(R.id.editTextNumber2);
        etDescription = findViewById(R.id.editTextTextMultiLine);
        progressBar3 = findViewById(R.id.progress3);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(5000000);

        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            ImageButton btnList = findViewById(R.id.btnList);

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    btnList.setImageDrawable(getDrawable(R.drawable.icon_arrow_up));

                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    btnList.setImageDrawable(getDrawable(R.drawable.icon_arrow_down));
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //We cant do anything here for this app
            }
        });
    }

    @Override
    public void ChooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddingActivity.this);
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
                if (CheckPermissionImage()) {
                    addingMoneyPresentent.takeImageFromCamera();
                    dialog.cancel();
                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermissionRead()) {
                    addingMoneyPresentent.takeImageFromGallery();
                    dialog.cancel();
                }
            }
        });
    }

    @Override
    public void CaptureRecord() {
        if (isRecord) {
            //Stop recording
            addingMoneyPresentent.stopRecord();
        } else {
            if (CheckPermissionRecord()) {
                DeleteRecord();
                addingMoneyPresentent.startRecord();
            } else {
                Toast.makeText(this, "Not permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void CaptureAudio() {
        if (!recordFile.equals("NO")) {
            if (isPlaying) {
                addingMoneyPresentent.stopAudio();
            } else {
                addingMoneyPresentent.startAudio();
            }
        } else {
            Toast.makeText(this, "No Audio is saving now", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void IsValidNumber(CharSequence s) {
        if (s.length() > 15) {
            Toast.makeText(AddingActivity.this, "Số tiền quá lớn.", Toast.LENGTH_SHORT).show();
            isMax = false;
            etMoney.setEnabled(false);
            etMoney.setText("");
            seekBar.setProgress(0);
        } else if (s.length() > 0) {
            etMoney.setSelection(s.length());
            if (isNumeric(s.toString())) {
                long progress = Long.parseLong(s.toString());
                if (progress > 5000000) {
                    isMax = true;
                    seekBar.setProgress(5000000);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        int progress_int = Math.toIntExact(progress);
                        isMax = false;
                        int value = progress_int;
                        seekBar.setProgress(value);
                    }

                }
            } else {
                etMoney.setText("");
            }
        }
    }

    @Override
    public int FindIdByName(String name) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (name.equals(arrayList.get(i).Get_NAME())) {
                isCategory = 1;
                return arrayList.get(i).Get_ID();
            }
        }
        for (int i = 0; i < arrayList1.size(); i++) {
            if (name.equals(arrayList1.get(i).Get_NAME())) {
                isCategory = -1;
                return arrayList1.get(i).Get_ID();
            }
        }
        return 0;
    }

    @Override
    public Boolean IsNullImage(Bitmap bitmap) {
        if (bitmap == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String GetStringImage() {
        String image = "";
        if (addingMoneyPresentent.isNullImage(bmImage)) {
            image = "NULL";
        } else {
            byte[] bytes = encodeTobase64(bmImage);
            image = convertByteToString(bytes);
            Log.i("IMAGETEST", image);
        }
        return image;
    }

    @Override
    public Boolean IsNullAudio() {
        if (recordFile.equals("NO")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String GetStringAudio() {
        String audio = "";
        if (addingMoneyPresentent.isNullAudio()) {
            audio = "NULL";
        } else {
            byte[] audio_byte = addingMoneyPresentent.convert3gbToByte();
            audio = convertByteToString(audio_byte);
            Log.i("AUDIOTEST", audio);
        }

        return audio;
    }

    //Stop record
    @Override
    public void StopRecord() {
        mediaRecorder.stop();
        stoptime = System.nanoTime();
        mediaRecorder.release();
        mediaRecorder = null;
        mVisualizer.setVisibility(View.INVISIBLE);
        btnRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_surround_sound_24, null));
        Toast.makeText(this, "Stop record in  " + String.valueOf((stoptime - starttime) / 1000000000) + "s", Toast.LENGTH_SHORT).show();
        isRecord = false;
    }

    //Start record
    @Override
    public void StartRecord() {

        String recordPath = AddingActivity.this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.forLanguageTag("vi_VN"));
        Date now = new Date();
        recordFile = "Record_" + format.format(now) + ".3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
        starttime = System.nanoTime();

        mVisualizer.setVisibility(View.VISIBLE);
        btnRecord.setImageDrawable(getResources().getDrawable(R.drawable.icon_pause, null));
        Toast.makeText(this, "Start record", Toast.LENGTH_SHORT).show();
        isRecord = true;
    }

    //Start audio
    @Override
    public void StartAudio() {
        mediaPlayer = new MediaPlayer();
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            String outputFile = AddingActivity.this.getExternalFilesDir("/").getAbsolutePath() + "/" + recordFile;

            Toast.makeText(this, "Start current record in " + String.valueOf((stoptime - starttime) / 1000000000) + "s", Toast.LENGTH_SHORT).show();
            try {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int audioSessionId = mediaPlayer.getAudioSessionId();
            mVisualizer.setAudioSessionId(audioSessionId);

            mediaPlayer.start();

            mVisualizer.setVisibility(View.VISIBLE);
            btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.icon_pause, null));

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    StopAudio();
                }
            });
            isPlaying = true;
        }
    }

    //Stop audio
    @Override
    public void StopAudio() {
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
        mVisualizer.setVisibility(View.INVISIBLE);
        btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.icon_play, null));
        Toast.makeText(this, "Stop current record", Toast.LENGTH_SHORT).show();
    }

    //Take image from gallery
    @Override
    public void TakeImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PERMISSION_EXTERNAL_STORAGE);
    }

    //Take image from camera
    @Override
    public void TakeImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(AddingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.i("Mayank", photoFile.getAbsolutePath());

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.myproject22.provider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, PERMISSION_IMAGE);
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
                    btnImage.setScaleX(1.0f);
                    btnImage.setScaleY(1.0f);
                    tvImage.setVisibility(View.GONE);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        bmImage = BitmapFactory.decodeStream(inputStream);
                        btnImage.setImageBitmap(bmImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            case PERMISSION_IMAGE: {
                if (resultCode == RESULT_OK) {
                    btnImage.setScaleX(1.0f);
                    btnImage.setScaleY(1.0f);
                    tvImage.setVisibility(View.GONE);
                    bmImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    btnImage.setImageBitmap(bmImage);

                }
            }
        }
    }

    //Convert 3gp to byte[] (file name from recordFile)
    @Override
    public byte[] Convert3gbToByte() {
        if(!recordFile.equals("NO"))
        {
            Log.i("TEST", recordFile);
            StopRecord();
        }
        byte[] audio = null;
        String outputFile = AddingActivity.this.getExternalFilesDir("/").getAbsolutePath() + "/" + recordFile;
        try {
            FileInputStream inputStream = new FileInputStream(outputFile);
            BufferedInputStream bif = new BufferedInputStream(inputStream);
            audio = new byte[bif.available()];
            bif.read(audio);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return audio;
    }

    @Override
    public void SavingMoneyData(String money, String description, int category_id, String image, String audio) {
        if (image.equals("NULL") && audio.equals("NULL")) {
            if (isCategory == 1) {
                UploadIncomeNoBothToServer(money, description, category_id);
            } else if (isCategory == -1) {
                UploadOutcomeNoBothToServer(money, description, category_id);
            }
        } else {
            if (!image.equals("NULL") && !audio.equals("NULL")) {
                if (isCategory == 1) {
                    UploadIncomeToServer(money, description, category_id, image, audio);
                } else if (isCategory == -1) {
                    UploadOutcomeToServer(money, description, category_id, image, audio);
                }
            } else {
                if (image.equals("NULL")) {
                    if (isCategory == 1) {
                        UploadIncomeNoImageToServer(money, description, category_id, audio);
                    } else if (isCategory == -1) {
                        UploadOutcomeNoImageToServer(money, description, category_id, audio);
                    }
                } else {
                    if (isCategory == 1) {
                        UploadIncomeNoAudioToServer(money, description, category_id, image);
                    } else if (isCategory == -1) {
                        UploadOutcomeNoAudioToServer(money, description, category_id, image);
                    }
                }
            }
        }
    }

    //Upload income detail to server (all attribute or not image or not audio or not both)
    public void UploadIncomeToServer(String money, String description, int category_id, String image, String audio) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertIncomeDetail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new income detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_income));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                params.put("image", image);
                params.put("audio", audio);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    public void UploadIncomeNoAudioToServer(String money, String description, int category_id, String image) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertIncomeDetailNoAudio.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new income detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    ResetSound();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_income));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                params.put("image", image);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    public void UploadIncomeNoImageToServer(String money, String description, int category_id, String audio) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertIncomeDetailNoImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new income detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    ResetSound();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_income));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                params.put("audio", audio);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    public void UploadIncomeNoBothToServer(String money, String description, int category_id) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertIncomeDetailNoBoth.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new income detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    ResetSound();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_income));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    //Upload outcome detail to server (all attribute or not image or not audio or not both)
    public void UploadOutcomeToServer(String money, String description, int category_id, String image, String audio) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertOutcomeDetail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new outcome detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    ResetSound();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_outcome));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                params.put("image", image);
                params.put("audio", audio);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    public void UploadOutcomeNoAudioToServer(String money, String description, int category_id, String image) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertOutcomeDetailNoAudio.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new outcome detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    ResetSound();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_outcome));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                params.put("image", image);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    public void UploadOutcomeNoImageToServer(String money, String description, int category_id, String audio) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertOutcomeDetailNoImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new outcome detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    ResetSound();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_outcome));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                params.put("audio", audio);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    public void UploadOutcomeNoBothToServer(String money, String description, int category_id) {
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertOutcomeDetailNoBoth.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddingActivity.this, response, Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
                if (response.equals("Add new outcome detailed success")) {
                    Intent intent = new Intent(AddingActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    ResetSound();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idmoney", String.valueOf(id_outcome));
                params.put("money", money);
                params.put("idcategory", String.valueOf(category_id));
                params.put("description", description);
                params.put("date", currentDateandTime);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ResetSound();
    }

}

