package com.example.myproject22.View.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myproject22.Model.IncomeClass;
import com.example.myproject22.Model.OutcomeClass;
import com.example.myproject22.R;
import com.example.myproject22.Util.WeekItemAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OutcomeCategoryGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutcomeCategoryGraphFragment extends Fragment {
    String urlString = "https://howtobeabillionaire.000webhostapp.com/";

    private ArrayList<OutcomeClass> outcome = new ArrayList<>();

    private int id_user;
    private int id_outcome = 1;
    private String dateStart = "2021-06-09";
    private String dateEnd = "2021-6-17";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OutcomeCategoryGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OutcomeCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OutcomeCategoryGraphFragment newInstance(String param1, String param2) {
        OutcomeCategoryGraphFragment fragment = new OutcomeCategoryGraphFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outcome_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FetchOutcome();

        PieChart pieChart;
        pieChart = view.findViewById(R.id.pie_chart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.2f, "Nhà hàng "));
        entries.add(new PieEntry(0.2f, "Mua sắm"));
        entries.add(new PieEntry(0.2f, "Tiền điện"));
        entries.add(new PieEntry(0.2f, "Tiền nước"));

        PieDataSet dataSet = new PieDataSet(entries, "Danh mục");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS); // lib is best until now
        PieData data = new PieData(dataSet);
        data.setDrawValues(false); // no text
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.MONOSPACE);
        data.setValueFormatter(new PercentFormatter(pieChart));


        //pieChart.setHoleRadius(0);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true); // set precent

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Tiền chi");
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


        HorizontalBarChart weekchart = view.findViewById(R.id.weekChart1);

        ArrayList<BarEntry> dataList = new ArrayList<>();
        ArrayList<String> danhMucList = new ArrayList<>();


        dataList.add(new BarEntry(0, 5211221f));
        dataList.add(new BarEntry(1, 1212122f));
        dataList.add(new BarEntry(2, 1221212f));
        dataList.add(new BarEntry(3, 1221212f));


        danhMucList.add("Nhà hàng");
        danhMucList.add("Di động");
        danhMucList.add("Mua sắm");
        danhMucList.add("Bim bim");

        BarDataSet barDataSet = new BarDataSet(dataList, "Tiền theo tháng");
        barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(20f);
        barDataSet.setValueTypeface(Typeface.MONOSPACE);
        BarData barData = new BarData(barDataSet);


        barData.setBarWidth(0.5f);
        weekchart.setFitBars(true);
        weekchart.setData(barData);
        weekchart.getDescription().setText("");
        weekchart.setHighlightFullBarEnabled(true);

        YAxis yAxis = weekchart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(12);


        // set XAxis value formater
        XAxis xAxis = weekchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(danhMucList));

        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setLabelCount(danhMucList.size());

        Legend l2 = weekchart.getLegend();
        l2.setTextColor(Color.WHITE);
        l2.setTextSize(15);

        ArrayList<String>weeks = new ArrayList<>();

        for(int i=0 ;i< 12; i++) {
            weeks.add("Tuần 1.1.2020");
        }


        RecyclerView weekRecycler = view.findViewById(R.id.week_recycler);
        WeekItemAdapter adapter = new WeekItemAdapter(weeks, getContext());

        weekRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        weekRecycler.setLayoutManager(layoutManager1);
    }
    public void FetchOutcome() {
        StringRequest request = new StringRequest(Request.Method.POST,
                urlString + "getOutcomeByDate.php", new Response.Listener<String>() {
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
                            // String date = object.getString("date");

                           /* SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                            try{
                            Date day = fmt.parse(date);


                            }
                            catch (ParseException pe){
                                pe.printStackTrace();
                            }*/
                            OutcomeClass outcomeClass = new OutcomeClass(name, money);
                            outcome.add(outcomeClass);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //dataPiechart();
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
                params.put("id_outcome", String.valueOf(id_outcome));
                params.put("datestart", dateStart);
                params.put("dateend", dateEnd);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
    private PieChart pieChart;

    /*public void dataPiechart(){
        int m = outcome.size();
        if(m > 0)
        {
            ArrayList<PieEntry> Entries = new ArrayList<>();
            for(int i =0; i < m; i++)
            {
                OutcomeClass item = outcome.get(i);
                PieEntry outcomePE = new PieEntry(item.getMoney().floatValue(), item.getCategory());
                Entries.add(outcomePE);
            }
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
        }
    }*/

}