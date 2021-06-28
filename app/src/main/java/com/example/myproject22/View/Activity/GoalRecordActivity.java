package com.example.myproject22.View.Activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myproject22.Model.ConnectionClass;
import com.example.myproject22.Model.GoalRecord;
import com.example.myproject22.Presenter.Interface.GoalRecordInterface;
import com.example.myproject22.Presenter.Presenter.GoalRecordPresenter;
import com.example.myproject22.R;
import com.example.myproject22.Util.GoalItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoalRecordActivity extends AppCompatActivity implements GoalRecordInterface {

    //region UI COMPONENTS
    RecyclerView goal_recycler;
    TextView tvNoGoal;
    //endregion


    //region  GLOBAL VARIABLES
    JSONArray jsonArray;
    GoalRecordPresenter mGoalRecordPresenter;
    //endregion


    //region DEFAULT FUNCTION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_record);

        mGoalRecordPresenter = new GoalRecordPresenter(this);
        mGoalRecordPresenter.LoadData();

    }

    @Override
    public void onBackPressed() {
        OnBackPress();
    }

    //endregion


    //region INIT VIEWS
    @Override
    public void SetUpBeforeInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public void InitViews() {
        goal_recycler = findViewById(R.id.goal_recycler);
        tvNoGoal = findViewById(R.id.tvNoGoal);
    }

    @Override
    public boolean hasGoalRecord() {
        return (jsonArray.length() >= 1);
    }

    @Override
    public void OnBackPress() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    //endregion


    //region DATABASE HANDLE

    @Override
    public void FetchGoalRecordFromServer() {
        StringRequest request = new StringRequest(Request.Method.POST,
                ConnectionClass.urlString + "getFinishGoal.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    jsonArray = jsonObject.getJSONArray("data");
                    if(!hasGoalRecord()){
                        ShowNoGoal();
                    }
                    if (success.equals("1")) {
                        ArrayList<GoalRecord> goals = new ArrayList<>(); // prepare for adapter
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GoalRecord record = new GoalRecord(
                                    object.get("NAME_GOAL").toString(),
                                    Float.parseFloat(object.get("MONEY_GOAL").toString()),
                                    object.get("DATE_START").toString(),
                                    object.get("IMAGE_GOAL").toString()
                            );
                            Log.d("SSSSS", record.getDate_start());
                            goals.add(record);
                        }

                        GoalItemAdapter adapter = new GoalItemAdapter(goals);
                        goal_recycler.setAdapter(adapter);
                        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        goal_recycler.setLayoutManager(linearLayoutManager);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("iduser", String.valueOf(1));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void ShowNoGoal() {
        tvNoGoal.setVisibility(View.VISIBLE);
    }
    //endregion

}





