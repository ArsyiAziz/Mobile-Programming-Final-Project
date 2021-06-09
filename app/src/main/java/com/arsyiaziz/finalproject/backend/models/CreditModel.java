package com.arsyiaziz.finalproject.backend.models;

import com.arsyiaziz.finalproject.misc.Constants;
import com.arsyiaziz.finalproject.misc.ImageSize;
import com.google.gson.annotations.SerializedName;

public class CreditModel {
    private String name;
    @SerializedName("profile_path")
    private String profilePath;

    public String getName() {
        return name;
    }


    public String getProfilePath(ImageSize imageSize) {
        return Constants.BASE_IMG_URL + imageSize.getValue() + profilePath;
    }
}
