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
import com.arsyiaziz.finalproject.frontend.interfaces.OnItemClickListener;
import com.arsyiaziz.finalproject.backend.models.ListedMovieModel;
import com.arsyiaziz.finalproject.misc.ImageSize;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final List<ListedMovieModel> movieList;
    private OnItemClickListener<ListedMovieModel> clickListener;

    public MovieAdapter(List<ListedMovieModel> movieList) {
        this.movieList = movieList;
    }

    public void setClickListener(OnItemClickListener<ListedMovieModel> clickListener) {
        this.clickListener = clickListener;
    }

    public void appendList(List<ListedMovieModel> listToAppend) {
        movieList.addAll(listToAppend);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(movieList.get(position).getPosterPath(ImageSize.W200))
                .placeholder(new ColorDrawable(Color.BLACK))
                .error(R.drawable.ic_image_not_available)
                .into(holder.ivPoster);
        holder.tvTitle.setText(movieList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView ivPoster;
        private final TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(movieList.get(getBindingAdapterPosition()));
        }
    }
}
