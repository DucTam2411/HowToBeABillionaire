package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputEditText;

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
    private TextInputEditText et_username;
    private TextInputEditText et_password;
    private Button btnLogin;
    private TextView tvSignUp;
    private TextView tvForget;
    private ProgressBar pb_signin;

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
        et_username = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvRegister);
        tvForget = findViewById(R.id.tvForget);
        pb_signin = findViewById(R.id.pb_signin);
    }

    @Override
    public Boolean GetNoUserName(String username) {
        if (username.isEmpty()) {
            et_username.setError("Username không được để trống");
            pb_signin.setVisibility(View.INVISIBLE);
            return false;
        } else {
            et_username.setError(null);
            return true;
        }
    }

    @Override
    public Boolean GetNoPassword(String password) {
        if (password.isEmpty()) {
            et_password.setError("Mật khẩu không được để trống");
            pb_signin.setVisibility(View.INVISIBLE);
            return false;
        } else {
            et_password.setError(null);
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

                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("ID_USER", id_user);
                                bundle.putInt("ID_INCOME", id_income);
                                bundle.putInt("ID_OUTCOME", id_outcome);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, success, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        finish();
    }

    @Override
    public void TextViewForgetClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_forget_password, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        //Set component
        TextInputEditText et_user_forgot = dialogView.findViewById(R.id.et_user_forgot);
        TextInputEditText et_email_forgot = dialogView.findViewById(R.id.et_email_forgot);
        TextInputEditText et_password_forgot = dialogView.findViewById(R.id.et_password_forgot);
        TextInputEditText et_password_confirm_forgot = dialogView.findViewById(R.id.et_password_forgot_confirm);
        Button btn_forgot = dialogView.findViewById(R.id.btnForgotPassword);
        TextView tv_signup_forgot = dialogView.findViewById(R.id.tvRegister_forgot);
        ProgressBar pb_forgot = dialogView.findViewById(R.id.pb_password_forgot);

        AlertDialog dialog = builder.create();
        dialog.show();

        et_user_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    presenter.hideKeyboard(v);
                }
            }
        });

        et_email_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    presenter.hideKeyboard(v);
                }
            }
        });

        et_password_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    presenter.hideKeyboard(v);
                }
            }
        });

        et_password_confirm_forgot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    presenter.hideKeyboard(v);
                }
            }
        });

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_forgot.setVisibility(View.VISIBLE);

                String username = et_user_forgot.getText().toString().trim();
                if (username.isEmpty()) {
                    et_user_forgot.setError("Username không được để trống");
                    pb_forgot.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    et_user_forgot.setError(null);
                }

                String email = et_email_forgot.getText().toString().trim();
                if (email.isEmpty()) {
                    et_email_forgot.setError("Email không được để trống");
                    pb_forgot.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    et_email_forgot.setError(null);
                }

                String password = et_password_forgot.getText().toString().trim();
                if (password.isEmpty()) {
                    et_password.setError("Mật khẩu không được để trống");
                    pb_forgot.setVisibility(View.INVISIBLE);
                    return;
                } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    et_password_forgot.setError("Mật khẩu quá yếu");
                    pb_forgot.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    et_password_forgot.setError(null);
                }

                String password_confirm = et_password_confirm_forgot.getText().toString().trim();
                if (password_confirm.isEmpty()) {
                    et_password_confirm_forgot.setError("Mật khẩu không được để trống");
                    pb_forgot.setVisibility(View.INVISIBLE);
                    return;
                } else if (!password_confirm.equals(password)) {
                    et_password_confirm_forgot.setError("Mật khẩu xác nhận không trùng khớp");
                    pb_forgot.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    et_password_confirm_forgot.setError(null);
                }

                StringRequest request = new StringRequest(Request.Method.POST,
                        ConnectionClass.urlString + "updatePasswordUser.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("TESTFORGOT",response);
                        pb_forgot.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                        if (response.equals("Update password success")) {
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        pb_signin.setVisibility(View.GONE);
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

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(request);
            }
        });

        tv_signup_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextViewClick();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}