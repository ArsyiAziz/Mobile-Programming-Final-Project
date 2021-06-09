package com.arsyiaziz.finalproject.frontend.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arsyiaziz.finalproject.R;
import com.arsyiaziz.finalproject.backend.models.CreditModel;
import com.arsyiaziz.finalproject.misc.ImageSize;
import com.bumptech.glide.Glide;

import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {
    private final List<CreditModel> creditsList;

    public CreditAdapter(List<CreditModel> creditsList) {
        this.creditsList = creditsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(creditsList.get(position).getProfilePath(ImageSize.W200))
                .placeholder(new ColorDrawable(Color.BLACK))
                .error(R.drawable.ic_image_not_available)
                .into(holder.ivProfile);
        holder.tvName.setText(creditsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return creditsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView ivProfile;
        private final TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
        }

    }
}
