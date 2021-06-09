package com.arsyiaziz.finalproject.backend.api;

import androidx.annotation.NonNull;

import com.arsyiaziz.finalproject.backend.callbacks.OnMovieCallback;
import com.arsyiaziz.finalproject.backend.callbacks.OnMovieListCallback;
import com.arsyiaziz.finalproject.backend.callbacks.OnSearchCallback;
import com.arsyiaziz.finalproject.backend.responses.ListedMovieResponse;
import com.arsyiaziz.finalproject.backend.models.MovieModel;
import com.arsyiaziz.finalproject.misc.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static MovieRepository repository;
    private final Service service;

    private MovieRepository(Service service) {
        this.service = service;
    }

    public static MovieRepository getRepository() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            repository = new MovieRepository(retrofit.create(Service.class));
        }
        return repository;
    }

    public void getMovie(String queryType, int page, final OnMovieListCallback callback) {
        service.getResults(queryType, Constants.API_KEY, page)
                .enqueue(new Callback<ListedMovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ListedMovieResponse> call, @NonNull Response<ListedMovieResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getResults() != null) {
                                    callback.onSuccess(response.body().getResults(), response.body().getPage(), response.message());
                                } else {
                                    callback.onFailure("response.body().getResults() is null");
                                }
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListedMovieResponse> call, @NonNull Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }
    public void getMovieDetails(int movieId, final OnMovieCallback callback) {
        service.getDetails(movieId, Constants.API_KEY, "credits,videos")
                .enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                callback.onSuccess(response.body(), response.message());
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message() + ", Error Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieModel> call, @NonNull Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }

    public void search(String query, int page, final OnSearchCallback callback) {
        service.search(query, Constants.API_KEY, page)
                .enqueue(new Callback<ListedMovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ListedMovieResponse> call, @NonNull Response<ListedMovieResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getResults() != null) {
                                    callback.onSuccess(response.body().getResults(), response.body().getPage(), response.message());
                                } else {
                                    callback.onFailure("No results");
                                }
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message() + ", Error Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListedMovieResponse> call, @NonNull Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }
}
