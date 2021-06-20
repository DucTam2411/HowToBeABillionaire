package com.example.myproject22.Util;

import android.content.Context;
import android.os.SystemClock;
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

public class WeekOutcomeAdapter extends  RecyclerView.Adapter<WeekOutcomeAdapter.ViewHolder> {
    ArrayList<WeekItem> weeks = new ArrayList<>();
    Context context;
    WeekOutcomeAdapter.EventListener listener; //Gọi hàm từ fragment
    private static long mLastClickTime = 0;

    public WeekOutcomeAdapter(ArrayList<WeekItem> weeks, Context context, WeekOutcomeAdapter.EventListener listener) {
        this.weeks = weeks;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public WeekOutcomeAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        MaterialCardView cardView = ((MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_week, parent, false));
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WeekOutcomeAdapter.ViewHolder holder, int position) {
        MaterialCardView cardView = holder.cardView;

        TextView week = ((TextView) cardView.findViewById(R.id.tvWeek));
        week.setText(weeks.get(position).getName());

        String datestart = weeks.get(position).getDatestart();
        String dateend = weeks.get(position).getDateend();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                { return;}
                mLastClickTime = SystemClock.elapsedRealtime();
                listener.FetchOutcomeFromServer(datestart,dateend);
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

    public interface EventListener {
        void FetchOutcomeFromServer(String datestart, String dateend);
    }
}
