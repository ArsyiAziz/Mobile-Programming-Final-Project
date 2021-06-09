package com.arsyiaziz.finalproject.backend.models;

import com.arsyiaziz.finalproject.misc.Constants;
import com.arsyiaziz.finalproject.misc.ImageSize;
import com.google.gson.annotations.SerializedName;

public class ListedMovieModel {
    private final int id;
    private final String title;
    @SerializedName("poster_path")
    private final String posterPath;

    public ListedMovieModel(int id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getPosterPath(ImageSize imageSize) {
        return Constants.BASE_IMG_URL + imageSize.getValue() + posterPath;
    }


}
