package com.arsyiaziz.finalproject.backend.responses;

import com.arsyiaziz.finalproject.backend.models.ListedMovieModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListedMovieResponse {
    private List<ListedMovieModel> results;
    @SerializedName("page")
    private int page;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public List<ListedMovieModel> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
