package com.arsyiaziz.finalproject.backend.api;

import com.arsyiaziz.finalproject.backend.responses.ListedMovieResponse;
import com.arsyiaziz.finalproject.backend.models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    @GET("movie/{query_type}")
    Call<ListedMovieResponse> getResults(
            @Path("query_type") String queryType,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<ListedMovieResponse> search(
            @Query("query") String query,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<MovieModel> getDetails(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse
        );

}
