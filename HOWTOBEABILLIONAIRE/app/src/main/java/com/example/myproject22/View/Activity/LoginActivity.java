package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

public class LoginActivity extends AppCompatActivity implements LoginInterface {

    //Component
    private TextInputEditText et_username;
    private TextInputEditText et_password;
    private Button btnLogin;
    private TextView tvSignUp;
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
    }

    @Override
    public void SetInIt() {
        et_username = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvRegister);
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
        if(presenter.getNoUserName(username) == false){
            return;
        }

        String password = et_password.getText().toString().trim();
        if(presenter.getNoPassword(password) == false){
            return;
        }

        presenter.loginFromServer(username,password);
    }

    @Override
    public void LoginFromServer(String username, String password){
        StringRequest request = new StringRequest(Request.Method.POST,
                ConnectionClass.urlString + "logIn.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TESTLOGIN", response);
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
                    }
                    else{
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
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
}