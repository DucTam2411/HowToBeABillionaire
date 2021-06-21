package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.myproject22.Model.DayItem;
import com.example.myproject22.Presenter.LoginInterface;
import com.example.myproject22.Presenter.LoginPresenter;
import com.example.myproject22.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements LoginInterface {

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
    private TextInputLayout til_username;
    private TextInputLayout tif_password;
    private TextInputEditText et_username;
    private TextInputEditText et_password;
    private Button btnLogin;
    private TextView tvSignUp;
    private TextView tvForget;
    private ProgressBar pb_signin;
    private CoordinatorLayout mSnackbarLayout;

    //Presenter
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this);
        presenter.setInit();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.textViewClick();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnSignIn();
            }
        });

        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.textViewForgetClick();
            }
        });
        
        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    presenter.hideKeyboard(v);
                }
            }
        });

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    til_username.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                    tif_password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    presenter.hideKeyboard(v);
                }
            }
        });
    }

    @Override
    public void SetInIt() {
        til_username = findViewById(R.id.til_user);
        tif_password = findViewById(R.id.til_password);
        et_username = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvRegister);
        tvForget = findViewById(R.id.tvForget);
        pb_signin = findViewById(R.id.pb_signin);
        mSnackbarLayout = findViewById(R.id.cl_snackbar);
    }

    @Override
    public Boolean GetNoUserName(String username) {
        if (username.isEmpty()) {
            til_username.setError("Username không được để trống");
            /*et_username.setError("Username không được để trống");*/
            pb_signin.setVisibility(View.INVISIBLE);
            return false;
        } else {
            til_username.setError(null);
            /*et_username.setError(null);*/
            return true;
        }
    }

    @Override
    public Boolean GetNoPassword(String password) {
        if (password.isEmpty()) {
            tif_password.setError("Mật khẩu không được để trống");
            /*et_password.setError("Mật khẩu không được để trống");*/
            pb_signin.setVisibility(View.INVISIBLE);
            return false;
        } else {
            tif_password.setError(null);
            /*et_password.setError(null);*/
            return true;
        }
    }

    @Override
    public void BtnSignIn() {
        pb_signin.setVisibility(View.VISIBLE);

        String username = et_username.getText().toString().trim();
        if (presenter.getNoUserName(username) == false) {
            return;
        }

        String password = et_password.getText().toString().trim();
        if (presenter.getNoPassword(password) == false) {
            return;
        }

        presenter.loginFromServer(username, password);
    }

    @Override
    public void LoginFromServer(String username, String password) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ConnectionClass.urlString + "logIn.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_signin.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("Login Success")) {
                        Toast.makeText(LoginActivity.this, success, Toast.LENGTH_SHORT).show();
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                int id_user = object.getInt("ID_USER");
                                int id_income = object.getInt("ID_INCOME");
                                int id_outcome = object.getInt("ID_OUTCOME");
                                int id_saving = object.getInt("ID_SAVING");

                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("ID_USER", id_user);
                                bundle.putInt("ID_INCOME", id_income);
                                bundle.putInt("ID_OUTCOME", id_outcome);
                                bundle.putInt("ID_SAVING", id_saving);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                            }
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(mSnackbarLayout,success,Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(btnLogin);
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                        /*Toast.makeText(LoginActivity.this, success, Toast.LENGTH_SHORT).show();*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(mSnackbarLayout,error.getMessage(),Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
                /*Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();*/
                pb_signin.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void TextViewClick() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public void TextViewForgetClick() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}