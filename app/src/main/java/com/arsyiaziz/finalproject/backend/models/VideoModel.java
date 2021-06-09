package com.arsyiaziz.finalproject.backend.models;

import com.google.gson.annotations.SerializedName;

public class VideoModel {
    @SerializedName("key")
    private String videoKey;

    public String getVideoKey() {
        return videoKey;
    }
}
