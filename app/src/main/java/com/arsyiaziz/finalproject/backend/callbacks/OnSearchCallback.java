package com.arsyiaziz.finalproject.backend.callbacks;

import com.arsyiaziz.finalproject.backend.models.ListedMovieModel;

import java.util.List;

public interface OnSearchCallback {
    void onSuccess(List<ListedMovieModel> movieList, int page, String msg);
    void onFailure(String msg);
}
