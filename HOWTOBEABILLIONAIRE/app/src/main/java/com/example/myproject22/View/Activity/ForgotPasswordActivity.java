package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myproject22.Model.ConnectionClass;
import com.example.myproject22.Presenter.ForgotPasswordInterface;
import com.example.myproject22.Presenter.ForgotPasswordPresenter;
import com.example.myproject22.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordInterface {
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

    //Component
    private TextInputLayout til_user;
    private TextInputLayout til_email;
    private TextInputLayout til_password;
    private TextInputLayout til_password_confirm;
    private TextInputEditText et_user_forgot;
    private TextInputEditText et_email_forgot;
    private TextInputEditText et_password_forgot;
    private TextInputEditText et_password_confirm_forgot;
    private Button btn_forgot;
    private TextView tv_signup_forgot;
    private ProgressBar pb_forgot;
    private CoordinatorLayout mSnackbarLayout;

    //Presenter
    private ForgotPasswordPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        presenter = new ForgotPasswordPresenter(this);
        presenter.setInit();

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnForgetClick();
            }
        });

        tv_signup_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.textViewClick();
            }
        });

        et_user_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    presenter.hideKeyboard(v);
                else
                    til_user.setError(null);
            }
        });

        et_email_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    presenter.hideKeyboard(v);
                else
                    til_email.setError(null);
            }
        });

        et_password_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    presenter.hideKeyboard(v);
                else
                    til_password.setError(null);
            }
        });

        et_password_forgot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 4){
                    til_password.setError("Mật khẩu phải tối thiểu 4 ký tự");
                }
                else{
                    til_password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password_confirm_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    presenter.hideKeyboard(v);
                else
                    til_password_confirm.setError(null);
            }
        });

        et_password_confirm_forgot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 4){
                    til_password_confirm.setError("Mật khẩu phải tối thiểu 4 ký tự");
                }
                else{
                    til_password_confirm.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void SetInIt() {
        til_user = findViewById(R.id.til_user);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        til_password_confirm = findViewById(R.id.til_password_confirm);
        et_user_forgot = findViewById(R.id.et_user_forgot);
        et_email_forgot = findViewById(R.id.et_email_forgot);
        et_password_forgot = findViewById(R.id.et_password_forgot);
        et_password_confirm_forgot = findViewById(R.id.et_password_forgot_confirm);
        btn_forgot = findViewById(R.id.btnForgotPassword);
        tv_signup_forgot = findViewById(R.id.tvRegister_forgot);
        pb_forgot = findViewById(R.id.pb_password_forgot);
        mSnackbarLayout = findViewById(R.id.cl_snackbar);
    }

    @Override
    public Boolean GetNoUserName(String username) {
        if (username.isEmpty()) {
            til_user.setError("Username không được để trống");
            /*et_user_forgot.setError("Username không được để trống");*/
            pb_forgot.setVisibility(View.INVISIBLE);
            return false;
        } else {
            til_user.setError(null);
            /*et_user_forgot.setError(null);*/
            return true;
        }
    }

    @Override
    public Boolean GetNoPassword(String password) {
        if (password.isEmpty()) {
            til_password.setError("Mật khẩu không được để trống");
            /*et_password_forgot.setError("Mật khẩu không được để trống");*/
            pb_forgot.setVisibility(View.INVISIBLE);
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            til_password.setError("Mật khẩu quá yếu");
            /*et_password_forgot.setError("Mật khẩu quá yếu");*/
            pb_forgot.setVisibility(View.INVISIBLE);
            return false;
        } else {
            til_password.setError(null);
            /*et_password_forgot.setError(null);*/
            return  true;
        }
    }

    @Override
    public Boolean GetNoEmail(String email) {
        if (email.isEmpty()) {
            til_email.setError("Email không được để trống");
            /*et_email_forgot.setError("Email không được để trống");*/
            pb_forgot.setVisibility(View.INVISIBLE);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            til_email.setError("Vui lòng nhập email chính xác");
            /*et_email_forgot.setError("Vui lòng nhập email chính xác");*/
            pb_forgot.setVisibility(View.INVISIBLE);
            return  false;
        } else {
            til_email.setError(null);
            /*et_email_forgot.setError(null);*/
            return true;
        }
    }

    @Override
    public Boolean GetNoConfirmPassword(String password, String password_confirm) {
        if (password_confirm.isEmpty()) {
            til_password_confirm.setError("Mật khẩu không được để trống");
            /*et_password_confirm_forgot.setError("Mật khẩu không được để trống");*/
            pb_forgot.setVisibility(View.INVISIBLE);
            return false;
        } else if (!password_confirm.equals(password)) {
            til_password_confirm.setError("Mật khẩu xác nhận không trùng khớp");
            /*et_password_confirm_forgot.setError("Mật khẩu xác nhận không trùng khớp");*/
            pb_forgot.setVisibility(View.INVISIBLE);
            return false;
        } else {
            til_password_confirm.setError(null);
            /*et_password_confirm_forgot.setError(null);*/
            return true;
        }
    }

    @Override
    public void BtnForgetClick() {
        pb_forgot.setVisibility(View.VISIBLE);

        String username = et_user_forgot.getText().toString().trim();
        if(!presenter.getNoUserName(username)){
            return;
        }

        String email = et_email_forgot.getText().toString().trim();
        if(!presenter.getNoEmail(email)){
            return;
        }

        String password = et_password_forgot.getText().toString().trim();
        if(!presenter.getNoPassword(password)){
            return;
        }

        String password_confirm = et_password_confirm_forgot.getText().toString().trim();
        if(!presenter.getNoConfirmPassword(password, password_confirm)){
            return;
        }

        presenter.uploadNewPassword(username,email,password);
    }

    @Override
    public void TextViewClick() {
        Intent intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public void UploadNewPassword(String username, String email, String password) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ConnectionClass.urlString + "updatePasswordUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb_forgot.setVisibility(View.GONE);
                if (response.equals("Update password success")) {
                    Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                }
                else{
                    Snackbar snackbar = Snackbar.make(mSnackbarLayout,response,Snackbar.LENGTH_SHORT);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(mSnackbarLayout,error.getMessage(),Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
                /*Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();*/
                pb_forgot.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("new_password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasswordActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}