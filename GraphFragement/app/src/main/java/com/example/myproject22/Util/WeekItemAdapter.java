package com.example.myproject22.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject22.Model.WeekItem;
import com.example.myproject22.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class WeekItemAdapter extends  RecyclerView.Adapter<WeekItemAdapter.ViewHolder> {

    ArrayList<WeekItem>weeks = new ArrayList<>();
    Context context;

    public WeekItemAdapter(ArrayList<WeekItem> weeks, Context context) {
        this.weeks = weeks;
        this.context = context;
    }

    @NonNull
    @Override
    public WeekItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView cardView = ((MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_week, parent, false));
        return new WeekItemAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekItemAdapter.ViewHolder holder, int position) {
        MaterialCardView cardView = holder.cardView;
        TextView week = ((TextView) cardView.findViewById(R.id.tvWeek));
        week.setText(weeks.get(position).getName());

        String datestart = weeks.get(position).getDatestart();
        String dateend = weeks.get(position).getDateend();
        Log.i("TEST", datestart + "\n" + dateend);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),datestart + "\n" + dateend ,Toast.LENGTH_SHORT).show();
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
}
