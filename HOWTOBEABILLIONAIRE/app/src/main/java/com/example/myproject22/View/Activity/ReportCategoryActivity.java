package com.example.myproject22.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myproject22.Model.DayItem;
import com.example.myproject22.Presenter.ReportCategoryInterface;
import com.example.myproject22.Presenter.ReportCategoryPresenter;
import com.example.myproject22.R;
import com.example.myproject22.Util.DayItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.myproject22.Model.ConnectionClass.urlString;

public class ReportCategoryActivity extends AppCompatActivity implements ReportCategoryInterface {

    //Component
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    //Get Id_income, Id_outcome
    private int id_income;
    private int id_outcome;

    //Check id_income, id_outcome (check to make sure income data and outcome data already get in layout)
    private Boolean isIncome = false;
    private Boolean isOutcome = false;

    //Create array list to control income list and outcome from server
    private ArrayList<DayItem> incomeDate = new ArrayList<>();
    private ArrayList<DayItem> outcomeDate = new ArrayList<>();
    private ArrayList<DayItem> categoryDate = new ArrayList<>();

    //Presenter
    private ReportCategoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report);

        presenter = new ReportCategoryPresenter(this);
        presenter.getBundleData();
        presenter.setInit();
        presenter.loadData();


    }

    @Override
    public void SetInit() {
        recyclerView = findViewById(R.id.day_recycler);
        recyclerView.setVisibility(View.INVISIBLE);
        toolbar = findViewById(R.id.toolbar2);
        progressBar = findViewById(R.id.pbReportCategory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("     Nhật ký thu chi");
        getSupportActionBar().setIcon(R.drawable.yoyoyo);
    }

    @Override
    public void GetBundleData() {
        Intent intent = getIntent();
       Bundle bundle = intent.getExtras();
        if (bundle == null) {
            id_income = 1;
            id_outcome = 1;
        } else {
            id_income = bundle.getInt("ID_INCOME");
            id_outcome = bundle.getInt("ID_OUTCOME");
        }
    }

    @Override
    public void LoadRecycleView() {
        int i = 0, j = 0;
        int m = incomeDate.size();
        int n = outcomeDate.size();
        while (i < m && j < n) {
            if (incomeDate.get(i).getDate().getTime() < outcomeDate.get(j).getDate().getTime()) {
                categoryDate.add(incomeDate.get(i));
                i++;
            } else if (incomeDate.get(i).getDate().getTime() > outcomeDate.get(j).getDate().getTime()) {
                categoryDate.add(outcomeDate.get(j));
                j++;
            } else {
                DayItem item1 = incomeDate.get(i);
                DayItem item2 = outcomeDate.get(j);
                DayItem item3 = new DayItem(item1.getDate(), item1.getDateString(), item1.getNumberOfRecord() + item2.getNumberOfRecord());
                categoryDate.add(item3);
                i++;
                j++;
            }
        }

        while (i < m) {
            categoryDate.add(incomeDate.get(i));
            i++;
        }

        while (j < n) {
            categoryDate.add(outcomeDate.get(j));
            j++;
        }
        DayItemAdapter dayItemAdapter = new DayItemAdapter(categoryDate, this, id_income, id_outcome);
        recyclerView = findViewById(R.id.day_recycler);
        recyclerView.setAdapter(dayItemAdapter);


        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void FetchIncomeInServer() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getNumberRecordIncomeByDate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        isIncome = true;
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String sdate = object.getString("DATE");
                                String scount = object.getString("COUNT");

                           /* String[] splitdate = sdate.split("-");
                            Date date = new Date(Integer.parseInt(splitdate[2]),Integer.parseInt(splitdate[1]),Integer.parseInt(splitdate[0]));*/
                                SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
                                Date dateObj = curFormater.parse(sdate);
                                int count = Integer.parseInt(scount);
                                DayItem item = new DayItem(dateObj, sdate, count);
                                incomeDate.add(item);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (outcomeDate.size() > 0 || isOutcome == true) {
                    presenter.loadRecycleView();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReportCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_income", String.valueOf(id_income));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ReportCategoryActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void FetchOutcomeInServer() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getNumberRecordOutcomeByDate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        isOutcome = true;
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String sdate = object.getString("DATE");
                                String scount = object.getString("COUNT");
                            /*String[] splitdate = sdate.split("-");
                            Date date = new Date(Integer.parseInt(splitdate[2]),Integer.parseInt(splitdate[1]),Integer.parseInt(splitdate[0]));*/
                                SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
                                Date dateObj = curFormater.parse(sdate);
                                int count = Integer.parseInt(scount);
                                DayItem item = new DayItem(dateObj, sdate, count);
                                outcomeDate.add(item);
                            }
                        }
                    }

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                if (incomeDate.size() > 0 || isIncome == true) {
                    presenter.loadRecycleView();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReportCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_outcome", String.valueOf(id_outcome));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ReportCategoryActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void LoadData() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.fetchIncomeInServer();
                presenter.fetchOutcomeInServer();
            }
        }, 1000);
    }
}