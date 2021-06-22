package com.example.myproject22.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myproject22.Model.DetailItem;
import com.example.myproject22.R;
import com.example.myproject22.View.Activity.RecordDetailActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecordItemAdapter extends RecyclerView.Adapter<RecordItemAdapter.ViewHolder> {

    private ArrayList<DetailItem> arrays;
    private Context context;
    public RecordItemAdapter(ArrayList<DetailItem> arrays, Context context) {
        this.arrays = arrays;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView cardView = ((MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_record, parent, false));
        return new RecordItemAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaterialCardView cardView = holder.cardView;

        TextView tvMoney = cardView.findViewById(R.id.tvMoney);
        TextView tvDescription = cardView.findViewById(R.id.tvDescription);
        TextView tvTime = cardView.findViewById(R.id.tvTime);
        TextView tvName = cardView.findViewById(R.id.tvCategory);
        CircleImageView btnImage = cardView.findViewById(R.id.profile_image);
        CircleImageView btnImageCategory = cardView.findViewById(R.id.ivCategoryImage);
        ImageView ivCategory = cardView.findViewById(R.id.circleImageView4);
        ImageView ivSetAudio = cardView.findViewById(R.id.ivSetAudio);


        DetailItem item = arrays.get(position);
        tvMoney.setText(String.valueOf(item.get_MONEY()));
        tvDescription.setText(item.get_DESCRIPTION());
        tvName.setText(item.get_NAME());
        tvTime.setText(item.get_DATE());

        int IsCategory = item.get_TYPE();
        if(IsCategory == 1){
            ivCategory.setImageResource(R.drawable.round_background_income);
            ivSetAudio.setImageResource(R.drawable.round_background_income);
        }
        else if(IsCategory == -1){
            ivCategory.setImageResource(R.drawable.round_background_outcome);
            ivSetAudio.setImageResource(R.drawable.round_background_outcome);
        }

        String image_url = item.get_IMAGE();
        if (image_url.equals("NULL")) {
            btnImage.setImageResource(R.drawable.backgroundflower);
        } else {
            Glide.with(cardView).load(image_url).into(btnImage);
        }

        String image_category_url = item.get_IMAGECATEGORY();
        if (image_category_url.equals("NULL")) {
        } else {
            Glide.with(cardView).load(image_category_url).into(btnImageCategory);
        }

        String audio_url = item.get_AUDIO();
        if(audio_url.equals("NULL")){
            ivSetAudio.setVisibility(View.INVISIBLE);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RecordDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("IS_CATEGORY", item.get_TYPE());
                    bundle.putInt("ID_DETAIL", item.get_ID_DETAIL());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                    ((Activity)v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                }
            });
        }else{
            ivSetAudio.setVisibility(View.VISIBLE);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RecordDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("IS_CATEGORY", item.get_TYPE());
                    bundle.putInt("ID_DETAIL", item.get_ID_DETAIL());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                    ((Activity)v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;

        public ViewHolder(MaterialCardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
