package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myproject22.Model.ConnectionClass;
import com.example.myproject22.Presenter.UpdatePasswordInterface;
import com.example.myproject22.Presenter.UpdatePasswordPresenter;
import com.example.myproject22.Presenter.UpdateUserInterface;
import com.example.myproject22.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UpdatePasswordActivity extends AppCompatActivity implements UpdatePasswordInterface {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    //Set component
    private ProgressBar pb_password;
    private TextInputLayout til_oldpassword;
    private TextInputLayout til_newpassword;
    private TextInputLayout til_newconfirm;
    private TextInputEditText et_oldpassword;
    private TextInputEditText et_newpassword;
    private TextInputEditText et_newconfirm;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    //Presenter
    private UpdatePasswordPresenter presenter;
    private int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_password);

        presenter = new UpdatePasswordPresenter(this);
        presenter.setInit();
        presenter.getBundleData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnSaveClick();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnCancelClick();
            }
        });

        et_oldpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 4)
                    til_oldpassword.setError("Mật khẩu tối thiểu 4 ký tự");
                else
                    til_oldpassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_oldpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    presenter.hideKeyboard(v);
                }
                else{
                    til_oldpassword.setError(null);
                }
            }
        });

        et_newpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 4)
                    til_newpassword.setError("Mật khẩu tối thiểu 4 ký tự");
                else
                    til_newpassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_newpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    presenter.hideKeyboard(v);
                }
                else{
                    til_newpassword.setError(null);
                }
            }
        });

        et_newconfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 4)
                    til_newconfirm.setError("Mật khẩu tối thiểu 4 ký tự");
                else
                    til_newconfirm.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_newconfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    presenter.hideKeyboard(v);
                }
                else{
                    til_newconfirm.setError(null);
                }
            }
        });
    }

    @Override
    public void SetInit() {
        til_newconfirm = findViewById(R.id.til_password_confirm);
        til_newpassword = findViewById(R.id.til_new_password);
        til_oldpassword = findViewById(R.id.til_password);
        et_newconfirm = findViewById(R.id.et_password_confirm);
        et_newpassword = findViewById(R.id.et_new_pasword);
        et_oldpassword = findViewById(R.id.et_password);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        pb_password = findViewById(R.id.pb_password);
    }

    @Override
    public void GetBundleData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user =bundle.getInt("ID_USER");
    }

    @Override
    public void BtnSaveClick() {
        pb_password.bringToFront();
        pb_password.setVisibility(View.VISIBLE);

        String oldpassword = et_oldpassword.getText().toString().trim();
        if(!presenter.getNoOldPassword(oldpassword)){
            return;
        }

        String newpassword = et_newpassword.getText().toString().trim();
        if(!presenter.getNoPassword(newpassword)){
            return;
        }

        String confirmpassword = et_newconfirm.getText().toString().trim();
        if(!presenter.getNoConfirmPassword(newpassword, confirmpassword)){
            return;
        }

        presenter.uploadPasswordToServer(oldpassword,newpassword);
    }

    @Override
    public void BtnCancelClick() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public Boolean GetNoPassword(String password) {
        if (password.isEmpty()) {
            til_newpassword.setError("Mật khẩu không được để trống");
            pb_password.setVisibility(View.INVISIBLE);
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            til_newpassword.setError("Mật khẩu quá yếu");
            pb_password.setVisibility(View.INVISIBLE);
            return false;
        } else {
            til_newpassword.setError(null);
            return  true;
        }
    }

    @Override
    public Boolean GetNoOldPassword(String password) {
        if (password.isEmpty()) {
            til_oldpassword.setError("Mật khẩu không được để trống");
            pb_password.setVisibility(View.INVISIBLE);
            return false;
        } else {
            til_oldpassword.setError(null);
            return  true;
        }
    }

    @Override
    public Boolean GetNoConfirmPassword(String password, String password_confirm) {
        if (password_confirm.isEmpty()) {
            til_newconfirm.setError("Mật khẩu không được để trống");
            pb_password.setVisibility(View.INVISIBLE);
            return false;
        } else if (!password_confirm.equals(password)) {
            til_newconfirm.setError("Mật khẩu xác nhận không trùng khớp");
            pb_password.setVisibility(View.INVISIBLE);
            return false;
        } else {
            til_newpassword.setError(null);
            return true;
        }
    }

    @Override
    public void UploadPasswordToServer(String oldpassword, String newpassword) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ConnectionClass.urlString + "updatePasswordUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb_password.setVisibility(View.GONE);
                if (response.equals("Update password success")) {
                    Toast.makeText(UpdatePasswordActivity.this, response, Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                }
                else{
                    Snackbar snackbar = Snackbar.make(btnCancel,response,Snackbar.LENGTH_SHORT);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(btnCancel,error.getMessage(),Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
                /*Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();*/
                pb_password.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                params.put("password", oldpassword);
                params.put("new_password", newpassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UpdatePasswordActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}