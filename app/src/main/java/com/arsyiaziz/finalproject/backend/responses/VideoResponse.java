package com.arsyiaziz.finalproject.backend.responses;

import com.arsyiaziz.finalproject.backend.models.VideoModel;

import java.util.List;

public class VideoResponse {
    private List<VideoModel> results;

    public List<VideoModel> getVideos() {
        return results;
    }
}
