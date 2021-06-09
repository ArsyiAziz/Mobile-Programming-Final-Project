package com.arsyiaziz.finalproject.backend.callbacks;

import com.arsyiaziz.finalproject.backend.models.MovieModel;

public interface OnMovieCallback {
    void onSuccess(MovieModel movie, String msg);

    void onFailure(String msg);
}
