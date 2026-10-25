package com.example.masjidconnect;

import java.io.Serializable;

public class MediaItem implements Serializable {

    private String id;
    private String title;
    private String fileUri;
    private String type; // "image" atau "video"
    private int durationSeconds; // misalnya 15 untuk 15s
    private String videoDuration; // misalnya "01:45" untuk video
    private long sizeBytes; // ukuran file dalam byte
    private long timestampMs; // waktu upload dalam milidetik

    public MediaItem() {}

    public MediaItem(String id, String title, String fileUri, String type, int durationSeconds, String videoDuration, long sizeBytes, long timestampMs) {
        this.id = id;
        this.title = title;
        this.fileUri = fileUri;
        this.type = type;
        this.durationSeconds = durationSeconds;
        this.videoDuration = videoDuration;
        this.sizeBytes = sizeBytes;
        this.timestampMs = timestampMs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public boolean isVideo() {
        return "video".equalsIgnoreCase(type);
    }
}