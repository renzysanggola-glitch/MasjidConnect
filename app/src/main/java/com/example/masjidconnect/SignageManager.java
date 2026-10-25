package com.example.masjidconnect;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SignageManager {

    private static final String PREF_NAME = "DigitalSignagePref";
    private static final String KEY_PLAYLIST = "playlist_media";
    private static final String KEY_TICKERS = "ticker_list";
    private static final String KEY_DEFAULT_DURATION = "default_duration";
    private static final String KEY_DEVICE_IP = "device_ip";

    public static final long MAX_STORAGE_BYTES = 2L * 1024L * 1024L * 1024L; // 2.0 GB

    private final SharedPreferences pref;
    private final Gson gson;

    public SignageManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        initDefaultDataIfNeeded();
    }

    private void initDefaultDataIfNeeded() {
        if (!pref.contains(KEY_PLAYLIST)) {
            List<MediaItem> defaultList = new ArrayList<>();
            long now = System.currentTimeMillis();

            // Item 1: Poster Kajian
            defaultList.add(new MediaItem(
                    "1",
                    "Poster Kajian Jumat Malam.png",
                    "",
                    "image",
                    15,
                    null,
                    450L * 1024L * 1024L, // 450 MB
                    now - (2L * 3600L * 1000L) // 2 hours ago
            ));

            // Item 2: Donasi Renovasi Atap
            defaultList.add(new MediaItem(
                    "2",
                    "Donasi Renovasi Atap.jpg",
                    "",
                    "image",
                    10,
                    null,
                    250L * 1024L * 1024L, // 250 MB
                    now - (24L * 3600L * 1000L) // 1 day ago
            ));

            // Item 3: Video Qurban
            defaultList.add(new MediaItem(
                    "3",
                    "Video Qurban.mp4",
                    "",
                    "video",
                    105, // 1m45s
                    "01:45",
                    500L * 1024L * 1024L, // 500 MB (Total = 1.2 GB)
                    now - (3L * 24L * 3600L * 1000L) // 3 days ago
            ));

            savePlaylist(defaultList);
        }

        if (!pref.contains(KEY_TICKERS)) {
            List<String> defaultTickers = new ArrayList<>();
            defaultTickers.add("\"Sesungguhnya sholat itu mencegah dari perbuatan keji dan munkar.\" (QS. AL-ANKABUT: 45)");
            defaultTickers.add("Mohon merapatkan dan meluruskan shaf sebelum shalat dimulai.");
            defaultTickers.add("Kajian Rutin Ba'da Maghrib setiap hari Jumat di Ruang Utama Masjid Al-Ikhlas.");
            saveTickers(defaultTickers);
        }

        if (!pref.contains(KEY_DEFAULT_DURATION)) {
            pref.edit().putInt(KEY_DEFAULT_DURATION, 15).apply();
        }

        if (!pref.contains(KEY_DEVICE_IP)) {
            pref.edit().putString(KEY_DEVICE_IP, "192.168.1.50").apply();
        }
    }

    public List<MediaItem> getPlaylist() {
        String json = pref.getString(KEY_PLAYLIST, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<MediaItem>>() {}.getType();
        List<MediaItem> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    public void savePlaylist(List<MediaItem> list) {
        String json = gson.toJson(list);
        pref.edit().putString(KEY_PLAYLIST, json).apply();
    }

    public void addMediaItem(MediaItem item) {
        List<MediaItem> list = getPlaylist();
        list.add(item);
        savePlaylist(list);
    }

    public void updateMediaItem(int position, String newTitle, int newDuration) {
        List<MediaItem> list = getPlaylist();
        if (position >= 0 && position < list.size()) {
            MediaItem item = list.get(position);
            item.setTitle(newTitle);
            item.setDurationSeconds(newDuration);
            savePlaylist(list);
        }
    }

    public void removeMediaItem(int position) {
        List<MediaItem> list = getPlaylist();
        if (position >= 0 && position < list.size()) {
            list.remove(position);
            savePlaylist(list);
        }
    }

    public long getTotalStorageUsedBytes() {
        List<MediaItem> list = getPlaylist();
        long total = 0;
        for (MediaItem item : list) {
            total += item.getSizeBytes();
        }
        return total;
    }

    public List<String> getTickers() {
        String json = pref.getString(KEY_TICKERS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    public void saveTickers(List<String> tickers) {
        String json = gson.toJson(tickers);
        pref.edit().putString(KEY_TICKERS, json).apply();
    }

    public String getDeviceIp() {
        return pref.getString(KEY_DEVICE_IP, "192.168.1.50");
    }

    public int getDefaultDuration() {
        return pref.getInt(KEY_DEFAULT_DURATION, 15);
    }

    public void setDefaultDuration(int duration) {
        pref.edit().putInt(KEY_DEFAULT_DURATION, duration).apply();
    }

    public static String formatRelativeTime(long timestampMs) {
        long diffMs = System.currentTimeMillis() - timestampMs;
        long minutes = diffMs / (1000 * 60);
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days == 1 ? "1 day ago" : days + " days ago";
        } else if (hours > 0) {
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        } else if (minutes > 0) {
            return minutes == 1 ? "1 minute ago" : minutes + " minutes ago";
        } else {
            return "Just now";
        }
    }
}