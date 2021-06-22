package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myproject22.Model.UserClass;
import com.example.myproject22.Presenter.UserInterface;
import com.example.myproject22.Presenter.UserPresenter;
import com.example.myproject22.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.myproject22.Model.ConnectionClass.urlString;

public class UserAcitvity extends AppCompatActivity implements UserInterface {

    private TextView tv_name;
    private TextView tv_date;
    private TextView tv_income;
    private TextView tv_money;
    private CircleImageView iv_profile;
    private MaterialButton btnUpdate;
    private MaterialButton btnPassword;
    private MaterialButton btnMap;
    private MaterialButton btnLogout;
    private ProgressBar pb_user;
    private ConstraintLayout cl_total;

    private int id_user = 1;
    private UserClass userClass;
    private UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_acitvity);

        presenter = new UserPresenter(this);
        presenter.setInit();
        presenter.getBundleData();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnLogout();
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnPassword();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnUpdateUser();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.btnMap();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadDataToLayout();
    }

    @Override
    public void SetInit() {
        tv_name = findViewById(R.id.tv_username);
        tv_date = findViewById(R.id.tv_userdate);
        tv_income = findViewById(R.id.tv_income);
        tv_money = findViewById(R.id.tv_money);
        iv_profile = findViewById(R.id.profile_image);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnPassword = findViewById(R.id.btnPassword);
        btnMap = findViewById(R.id.btnMap);
        btnLogout = findViewById(R.id.btnLogOut);
        pb_user = findViewById(R.id.pb_user);
        cl_total = findViewById(R.id.cl_total);
        pb_user.bringToFront();
        cl_total.setVisibility(View.INVISIBLE);
    }

    @Override
    public void GetBundleData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("ID_USER");
    }

    @Override
    public void BtnUpdateUser() {
        Intent intent = new Intent(UserAcitvity.this, UpdateUserActivity.class);
        intent.putExtra("ID_USER", id_user);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public void BtnPassword() {
        Intent intent = new Intent(UserAcitvity.this, UpdatePasswordActivity.class);
        intent.putExtra("ID_USER", id_user);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public void BtnMap() {

    }

    @Override
    public void BtnLogOut() {
        Intent i = new Intent(UserAcitvity.this, LoginActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
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
                            Double income = object.getDouble("INCOME");
                            Double outcome = object.getDouble("OUTCOME");

                            if(!image_string.equals("null")){
                                String url_image = urlString + "ImagesUser/" + image_string;
                                userClass = new UserClass(fullname, date_string, url_image, income, outcome);
                            }
                            else{
                                userClass = new UserClass(fullname,date_string,image_string, income, outcome);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                presenter.loadUser(userClass);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(btnMap, error.getMessage(), Snackbar.LENGTH_SHORT);
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
        RequestQueue requestQueue = Volley.newRequestQueue(UserAcitvity.this);
        requestQueue.add(request);
    }

    @Override
    public void LoadUser(UserClass userClass) {

        tv_name.setText(userClass.getFULLNAME());
        String date_temp = userClass.getDATESTART();
        String[] slipdate = date_temp.split(" ");
        String[] slipday = slipdate[0].split("-");
        String date_string = "Đã tham gia vào \nngày " + slipday[2] + "/" + slipday[1] + "/" + slipday[0];
        tv_date.setText(date_string);

        if(!userClass.getIMAGE().equals("null")){
            Glide.with(UserAcitvity.this).load(userClass.getIMAGE()).into(iv_profile);
        }

        Double total = userClass.getINCOME() - userClass.getOUTCOME();
        long money = total.longValue();
        if(money < 0){
            Log.i("MONEY1",String.valueOf(money));
            String money_string = "Hiện tại đang nợ: " ;
            tv_income.setText(money_string);
            String smoney = money + "VND";
            tv_money.setText(smoney);
        }
        else{
            Log.i("MONEY1",String.valueOf(money));
            String money_string = "Hiện tại đang có: ";
            tv_income.setText(money_string);
            String smoney = money + "VND";
            tv_money.setText(smoney);
        }


        pb_user.setVisibility(View.GONE);
        cl_total.setVisibility(View.VISIBLE);
    }

    @Override
    public void LoadDataToLayout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FetchUserFromServer();
            }
        },1000);
    }
}