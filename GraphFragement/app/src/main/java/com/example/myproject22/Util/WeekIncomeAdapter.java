package com.example.myproject22.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject22.Model.WeekItem;
import com.example.myproject22.R;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WeekIncomeAdapter extends  RecyclerView.Adapter<WeekIncomeAdapter.ViewHolder> {
    ArrayList<WeekItem> weeks = new ArrayList<>();
    Context context;
    EventListener listener; //Gọi hàm từ fragment

    public WeekIncomeAdapter(ArrayList<WeekItem> weeks, Context context,  EventListener listener) {
        this.weeks = weeks;
        this.context = context;
        this.listener = listener;
    }

    @NotNull
    @Override
    public WeekIncomeAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MaterialCardView cardView = ((MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_week, parent, false));
        return new WeekIncomeAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WeekIncomeAdapter.ViewHolder holder, int position) {
        MaterialCardView cardView = holder.cardView;

        if(position == weeks.size() - 1){
            String datestart = weeks.get(position).getDatestart();
            String dateend = weeks.get(position).getDateend();
            listener.FetchIncomeFromServer(datestart,dateend);
        }

        TextView week = ((TextView) cardView.findViewById(R.id.tvWeek));
        week.setText(weeks.get(position).getName());

        String datestart = weeks.get(position).getDatestart();
        String dateend = weeks.get(position).getDateend();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.FetchIncomeFromServer(datestart,dateend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weeks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        public ViewHolder(MaterialCardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    //Xử lí fragment
    public interface EventListener {
        void FetchIncomeFromServer(String datestart, String dateend);
    }
}
