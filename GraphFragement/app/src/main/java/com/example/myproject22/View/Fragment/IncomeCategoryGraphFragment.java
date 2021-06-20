package com.example.myproject22.View.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.example.myproject22.Model.IncomeClass;
import com.example.myproject22.Model.WeekItem;
import com.example.myproject22.R;
import com.example.myproject22.Util.WeekIncomeAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeCategoryGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeCategoryGraphFragment extends Fragment implements WeekIncomeAdapter.EventListener {
    String urlString = "https://howtobeabillionaire.000webhostapp.com/";

    private ArrayList<IncomeClass> income = new ArrayList<>();

    private int id_user = 1;
    private int id_income = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IncomeCategoryGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeCategoryGraphFragment newInstance(String param1, String param2) {
        IncomeCategoryGraphFragment fragment = new IncomeCategoryGraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ProgressBar pb1;
    private ProgressBar pb2;
    private ArrayList<WeekItem> weeks = new ArrayList<>();
    private RecyclerView weekRecycler;
    private WeekIncomeAdapter adapter;
    private PieChart pieChart;
    private HorizontalBarChart weekchart;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pb1 = view.findViewById(R.id.pb1);
        pb2 = view.findViewById(R.id.pb2);
        pb1.bringToFront();
        pb2.bringToFront();

        pieChart = view.findViewById(R.id.pie_chart);
        pieChart.setVisibility(View.INVISIBLE);

        weekchart = view.findViewById(R.id.weekChart1);
        weekchart.setVisibility(View.INVISIBLE);
        weekRecycler = view.findViewById(R.id.week_recycler);
    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FetchDateFromServer();
            }
        }, 1000);
    }

    public void FetchDateFromServer() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getDateByUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String sdate = object.getString("DATESTART");

                            GetArrayWeek(sdate);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new WeekIncomeAdapter(weeks, getContext(), IncomeCategoryGraphFragment.this);

                weekRecycler.setAdapter(adapter);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
                layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
                weekRecycler.setLayoutManager(layoutManager1);


                String datestart = weeks.get(weeks.size() -1).getDatestart();
                String dateend = weeks.get(weeks.size() -1).getDateend();
                FetchIncomeFromServer(datestart,dateend);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public static long CalculateDateUse(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;
        return (long) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public void GetArrayWeek(String sDate) {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        Date dateto = new Date();
        try {
            Date datefrom = curFormater.parse(sDate);
            int i = 1;
            do{
                Date datetemp = datefrom;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formattest = new SimpleDateFormat("yyyy-MM-dd");
                String date_string = simpleDateFormat.format(datefrom);
                String date_from = formattest.format(datefrom);
                String s ="Tuần " + i + " " + date_string;
                Calendar c = Calendar.getInstance();
                c.setTime(datefrom);
                c.add(Calendar.DATE, 7);
                datefrom = c.getTime();

                Calendar c1 = Calendar.getInstance();
                c1.setTime(datetemp);
                c1.add(Calendar.DATE, 8);
                dateto = c1.getTime();
                String date_to = formattest.format(dateto);
                WeekItem weekItem = new WeekItem(s, date_string, date_to);
                weeks.add(weekItem);
                Log.i("GetData", s + "\n" + date_string + "\n" + date_to);
                i++;
            }while(CalculateDateUse(datefrom, now) > 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void dataPiechart() {
        pieChart.invalidate();
        int m = income.size();
        if (m > 0) {
            ArrayList<PieEntry> Entries = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                IncomeClass item = income.get(i);
                PieEntry incomePE = new PieEntry(item.getMoney().floatValue(), item.getCategory());
                Entries.add(incomePE);
            }

            //thiet ke pie chart
            PieDataSet dataSet = new PieDataSet(Entries, "Danh mục");
            dataSet.setColors(ColorTemplate.LIBERTY_COLORS); // lib is best until now
            PieData data = new PieData(dataSet);
            data.setDrawValues(false); // no text
            data.setValueTextSize(16f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(Typeface.MONOSPACE);
            data.setValueFormatter(new PercentFormatter(pieChart));

            pieChart.setDrawHoleEnabled(true);

            pieChart.setData(data);
            pieChart.setUsePercentValues(true); // set precent
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setCenterText("Tiền thu");
            pieChart.setCenterTextSize(14f);
            pieChart.setCenterTextTypeface(Typeface.MONOSPACE);
            pieChart.getDescription().setEnabled(false);

            Legend l = pieChart.getLegend();
            l.setTextColor(Color.WHITE);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setWordWrapEnabled(true);
            l.setDrawInside(true);
            l.setEnabled(true);

            pieChart.animateY(1200, Easing.EaseInBack);
            pieChart.animate();

            pb1.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
        }
    }

    public void dataBarchart() {
        weekchart.invalidate();
        int m = income.size();
        if (m > 0) {
            ArrayList<BarEntry> dataList = new ArrayList<>();
            ArrayList<String> danhMucList = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                IncomeClass item = income.get(i);
                float a = (float) i;
                float b = item.getMoney().floatValue();
                String c = item.getCategory();
                BarEntry incomeBE = new BarEntry(a, b);
                dataList.add(incomeBE);
                danhMucList.add(c);
            }


            BarDataSet barDataSet = new BarDataSet(dataList, "Tiền theo tháng");
            barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
            barDataSet.setValueTextColor(Color.WHITE);
            barDataSet.setValueTextSize(20f);
            barDataSet.setValueTypeface(Typeface.MONOSPACE);
            BarData barData = new BarData(barDataSet);

            barData.setBarWidth(0.2f);
            weekchart.setFitBars(true);
            weekchart.setData(barData);
            weekchart.getDescription().setText("");
            weekchart.setHighlightFullBarEnabled(true);

            YAxis yAxis = weekchart.getAxisLeft();
            yAxis.setTextColor(Color.WHITE);
            yAxis.setTextSize(10);

            // set XAxis value formater
            XAxis xAxis = weekchart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(danhMucList));

            xAxis.setTextColor(Color.WHITE);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(12f);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(true);
            xAxis.setGranularity(1f);
            xAxis.setDrawLabels(true);
            xAxis.setLabelCount(danhMucList.size());

            Legend l2 = weekchart.getLegend();
            l2.setTextColor(Color.WHITE);
            l2.setTextSize(15);

            pb2.setVisibility(View.GONE);
            weekchart.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_income_category, container, false);
        return v;
    }

    @Override
    public void FetchIncomeFromServer(String datestart, String dateend) {
        income = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getIncomeByDate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("NAME");
                            Double money = object.getDouble("TOTAL");

                            IncomeClass incomeClass = new IncomeClass(name, money);
                            income.add(incomeClass);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(income.size() > 0) {
                    dataPiechart();
                    dataBarchart();
                }
                else
                {
                    InvisibleChart();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IncomeCategoryGraphFragment.this.getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_income", String.valueOf(id_income));
                params.put("datestart", datestart);
                params.put("dateend", dateend);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(IncomeCategoryGraphFragment.this.getActivity());
        requestQueue.add(request);
    }

    public void InvisibleChart(){
        pieChart.setVisibility(View.INVISIBLE);
        weekchart.setVisibility(View.INVISIBLE);
    }
}