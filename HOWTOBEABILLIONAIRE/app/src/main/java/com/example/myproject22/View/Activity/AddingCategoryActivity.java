package com.example.myproject22.View.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myproject22.Presenter.AddingCategoryInterface;
import com.example.myproject22.Presenter.AddingCategoryPresenter;
import com.example.myproject22.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myproject22.Model.ConnectionClass.urlString;
import static com.example.myproject22.Presenter.AddingCategoryPresenter.encodeTobase64;
import static com.example.myproject22.Presenter.AddingMoneyPresentent.convertByteToString;

public class AddingCategoryActivity extends AppCompatActivity implements AddingCategoryInterface {

    EditText etCategory;
    RadioGroup rgCategory;
    RadioButton rbtnIncome;
    RadioButton rbtnOutcome;
    ImageButton btnImage;
    TextView tv;
    ImageButton btnSaving;
    ProgressBar progressBar;

    private File photoFile = null;
    String mCurrentPhotoPath;

    Bitmap bmImage;
    int id_user;
    int isCategory = 1;

    //Const mặc định để xét permission
    private static final int PERMISSION_IMAGE = 1000;
    private static final int PERMISSION_EXTERNAL_STORAGE = 1001;

    //Presenter
    AddingCategoryPresenter presentent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adding_category);

        presentent = new AddingCategoryPresenter(this);
        presentent.setInit();
        presentent.getBundleData();

        etCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    presentent.hideKeyboard(v);
                }
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentent.chooseImage();
            }
        });

        rgCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                presentent.checkRadioButtonCategory(group, checkedId);
            }
        });

        btnSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                presentent.savingNewCategory(bmImage);
            }
        });
    }

    @Override
    public void SetInit() {
        etCategory = findViewById(R.id.etCategory);
        rbtnIncome = findViewById(R.id.rbtnIncome);
        rbtnOutcome = findViewById(R.id.rbtnOutcome);
        btnImage = findViewById(R.id.ibtnCategory);
        btnSaving = findViewById(R.id.ibtnSavingCategory);
        tv = findViewById(R.id.tvCategory);
        rgCategory = findViewById(R.id.rgCategory);
        progressBar = findViewById(R.id.pbCategory);
    }

    @Override
    public void ChooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddingCategoryActivity.this);
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
                Dexter.withContext(AddingCategoryActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            presentent.takeImageFromCamera();
                        }
                        else{
                            Toast.makeText(AddingCategoryActivity.this, "All permissions are not granted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
                dialog.cancel();
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(AddingCategoryActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        presentent.takeImageFromGallery();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(AddingCategoryActivity.this, "Permission is not granted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
                dialog.cancel();
            }
        });
    }

    @Override
    public void GetBundleData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        id_user = bundle.getInt("ID_USER");
    }

    @Override
    public void SavingNewCategory() {
        String name = etCategory.getText().toString().trim();
        String image = GetStringImage();
        if (isCategory == 1) {
            UploadIncomeCategoryToServer(name, id_user, image);
        } else {
            UploadOutcomeCategoryToServer(name, id_user, image);
        }
    }

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
            Toast.makeText(AddingCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    tv.setVisibility(View.GONE);
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
                    tv.setVisibility(View.GONE);
                    bmImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    btnImage.setImageBitmap(bmImage);
                }
            }
        }
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
    public void GetNoImage() {
        Toast.makeText(AddingCategoryActivity.this, "Vui lòng chọn hình ảnh cho danh mục.", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public String GetStringImage() {
        String image = "";
        if (presentent.isNullImage(bmImage)) {
            image = "NULL";
        } else {
            byte[] bytes = encodeTobase64(bmImage);
            image = convertByteToString(bytes);
            Log.i("IMAGETEST", image);
        }
        return image;
    }

    @Override
    public void CheckRadioButtonCategory(RadioGroup radioGroup, int idChecked) {
        int checkRadio = radioGroup.getCheckedRadioButtonId();

        if (checkRadio == R.id.rbtnIncome) {
            isCategory = 1;
        } else {
            isCategory = -1;
        }
    }

    @Override
    public Boolean IsNullName() {
        String name = etCategory.getText().toString().trim();

        if (name.equals("")) {
            Toast.makeText(AddingCategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return true;
        } else return false;
    }

    public void UploadIncomeCategoryToServer(String name, int id_user, String image) {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertIncomeCategory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response.equals("Add new income category success")) {
                    Toast.makeText(AddingCategoryActivity.this, response, Toast.LENGTH_SHORT).show();
                    DeleteImage();
                    finish();
                } else {
                    Toast.makeText(AddingCategoryActivity.this, "Tên danh mục đã tồn tại. Vui lòng chọn tên danh mục khác.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                params.put("name", name);
                params.put("image", image);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingCategoryActivity.this);
        requestQueue.add(request);
    }

    public void UploadOutcomeCategoryToServer(String name, int id_user, String image) {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "insertOutcomeCategory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response.equals("Add new outcome category success")) {
                    Toast.makeText(AddingCategoryActivity.this, response, Toast.LENGTH_SHORT).show();
                    DeleteImage();
                    finish();
                } else {
                    Toast.makeText(AddingCategoryActivity.this, "Tên danh mục đã tồn tại. Vui lòng chọn tên danh mục khác.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddingCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                params.put("name", name);
                params.put("image", image);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddingCategoryActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
    public void onBackPressed() {
        super.onBackPressed();
        DeleteImage();
    }
}