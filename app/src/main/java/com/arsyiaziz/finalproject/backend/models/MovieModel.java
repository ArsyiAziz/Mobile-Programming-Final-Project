package com.arsyiaziz.finalproject.backend.models;

import com.arsyiaziz.finalproject.backend.responses.CreditResponse;
import com.arsyiaziz.finalproject.backend.responses.VideoResponse;
import com.arsyiaziz.finalproject.misc.Constants;
import com.arsyiaziz.finalproject.misc.ImageSize;
import com.google.gson.annotations.SerializedName;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MovieModel {
    private String title;
    private int runtime;
    private CreditResponse credits;
    private String overview;
    private int id;
    private List<GenreModel> genres;
    private VideoResponse videos;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String posterPath;

    public String getTitle() {
        return title;
    }

    public String getRuntime() {
        //Convert runtime to readable text
        int hours = runtime/60;
        int minutes = runtime%60;
        String hourText;
        String minuteText;
        if (hours > 1) {
            hourText = "hours";
        } else {
            hourText = "hour";
        }
        if (minutes > 1) {
            minuteText = "minutes";
        } else {
            minuteText = "minute";
        }
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d %s %d %s", hours, hourText, minutes, minuteText);
        } else if (minutes > 0) {
            return String.format(Locale.getDefault(), "%d %s", minutes, minuteText);
        } else {
            return "";
        }
    }

    public String getReleaseYear() {
        //Grab the year portion of release date
        return releaseDate.split("-")[0];
    }

    public String getOverview() {
        return overview;
    }


    public String getBackdropPath(ImageSize imageSize) {
        return Constants.BASE_IMG_URL + imageSize.getValue() + backdropPath;
    }

    public float getVoteAverage() {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Float.parseFloat(df.format(voteAverage/2));
    }

    public String getPosterPath(ImageSize imageSize) {
        return Constants.BASE_IMG_URL + imageSize.getValue() + posterPath;
    }

    public int getId() {
        return id;
    }

    public String getGenres() {
        String output = "";
        for (int i = 0; i < genres.size(); i++) {
            output += genres.get(i).getName();
            if (i != genres.size() - 1) {
                output += ", ";
            }
        }
        return output;
    }

    public List<CreditModel> getCredits() {
        return credits.getCast();
    }

    public List<VideoModel> getVideos() {
        return videos.getVideos();
    }
}
