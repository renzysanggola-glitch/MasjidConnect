package com.example.masjidconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DigitalSignageActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvDeviceIp, tvDeviceStatus, tvDigitalClock;
    private TextView tvPreviewMasjidName, tvPreviewTicker;
    private View viewStatusDot;
    private LinearLayout btnRefresh, btnPreviewFull;

    // Tabs
    private LinearLayout tabMedia, tabTicker, tabTiming;
    private ImageView ivTabMediaIcon, ivTabTickerIcon, ivTabTimingIcon;
    private TextView tvTabMediaTitle, tvTabTickerTitle, tvTabTimingTitle;
    private LinearLayout sectionMedia, sectionTicker, sectionTiming;

    // Playlist
    private TextView tvPlaylistCount, btnReorderPlaylist;
    private RecyclerView rvPlaylist;
    private LinearLayout layoutEmptyPlaylist;
    private PlaylistAdapter playlistAdapter;
    private List<MediaItem> mediaList;
    private ItemTouchHelper itemTouchHelper;
    private boolean isReorderMode = false;

    // Storage Usage
    private TextView tvStorageUsageText;
    private ProgressBar progressStorageUsage;

    // Upload
    private LinearLayout btnUploadNewMedia;

    // Ticker & Timing Views
    private LinearLayout containerTickerList;
    private TextView tvTimingInfo;

    private SignageManager signageManager;
    private SharedPreferences profilPref;
    private final Handler clockHandler = new Handler(Looper.getMainLooper());
    private Runnable clockRunnable;

    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_signage);

        signageManager = new SignageManager(this);
        profilPref = getSharedPreferences("ProfilMasjidPref", Context.MODE_PRIVATE);

        initViews();
        setupClock();
        setupTabs();
        setupPlaylist();
        setupUploadLauncher();
        updateStorageUsage();
        loadTickersAndTiming();

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> handleRefreshStatus());
        }

        if (btnPreviewFull != null) {
            btnPreviewFull.setOnClickListener(v -> showFullscreenPreviewDialog());
        }

        if (btnUploadNewMedia != null) {
            btnUploadNewMedia.setOnClickListener(v -> filePickerLauncher.launch("*/*"));
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back_signage);
        tvDeviceIp = findViewById(R.id.tv_device_ip);
        tvDeviceStatus = findViewById(R.id.tv_device_status);
        tvDigitalClock = findViewById(R.id.tv_digital_clock);
        viewStatusDot = findViewById(R.id.view_status_dot);
        btnRefresh = findViewById(R.id.btn_refresh_status);
        btnPreviewFull = findViewById(R.id.btn_preview_full);

        tvPreviewMasjidName = findViewById(R.id.tv_preview_masjid_name);
        tvPreviewTicker = findViewById(R.id.tv_preview_ticker);

        tabMedia = findViewById(R.id.tab_media);
        tabTicker = findViewById(R.id.tab_ticker);
        tabTiming = findViewById(R.id.tab_timing);

        ivTabMediaIcon = findViewById(R.id.iv_tab_media_icon);
        ivTabTickerIcon = findViewById(R.id.iv_tab_ticker_icon);
        ivTabTimingIcon = findViewById(R.id.iv_tab_timing_icon);

        tvTabMediaTitle = findViewById(R.id.tv_tab_media_title);
        tvTabTickerTitle = findViewById(R.id.tv_tab_ticker_title);
        tvTabTimingTitle = findViewById(R.id.tv_tab_timing_title);

        sectionMedia = findViewById(R.id.section_media);
        sectionTicker = findViewById(R.id.section_ticker);
        sectionTiming = findViewById(R.id.section_timing);

        tvPlaylistCount = findViewById(R.id.tv_playlist_count);
        btnReorderPlaylist = findViewById(R.id.btn_reorder_playlist);
        rvPlaylist = findViewById(R.id.rv_playlist);
        layoutEmptyPlaylist = findViewById(R.id.layout_empty_playlist);

        tvStorageUsageText = findViewById(R.id.tv_storage_usage_text);
        progressStorageUsage = findViewById(R.id.progress_storage_usage);
        btnUploadNewMedia = findViewById(R.id.btn_upload_new_media);

        containerTickerList = findViewById(R.id.container_ticker_list);
        tvTimingInfo = findViewById(R.id.tv_timing_info);

        tvDeviceIp.setText(signageManager.getDeviceIp());
        String masjidName = profilPref.getString("nama_masjid", "Al-Ikhlas");
        tvPreviewMasjidName.setText(masjidName);
    }

    private void setupClock() {
        clockRunnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                if (tvDigitalClock != null) {
                    tvDigitalClock.setText(currentTime);
                }
                clockHandler.postDelayed(this, 1000);
            }
        };
        clockHandler.post(clockRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clockHandler.removeCallbacks(clockRunnable);
    }

    private void setupTabs() {
        tabMedia.setOnClickListener(v -> selectTab("media"));
        tabTicker.setOnClickListener(v -> selectTab("ticker"));
        tabTiming.setOnClickListener(v -> selectTab("timing"));
    }

    private void selectTab(String tab) {
        tabMedia.setBackgroundResource("media".equals(tab) ? R.drawable.bg_tab_pill_active : 0);
        tabTicker.setBackgroundResource("ticker".equals(tab) ? R.drawable.bg_tab_pill_active : 0);
        tabTiming.setBackgroundResource("timing".equals(tab) ? R.drawable.bg_tab_pill_active : 0);

        int primaryColor = getColor(R.color.text_primary);
        int secondaryColor = getColor(R.color.text_secondary);

        tvTabMediaTitle.setTextColor("media".equals(tab) ? primaryColor : secondaryColor);
        ivTabMediaIcon.setColorFilter("media".equals(tab) ? primaryColor : secondaryColor);

        tvTabTickerTitle.setTextColor("ticker".equals(tab) ? primaryColor : secondaryColor);
        ivTabTickerIcon.setColorFilter("ticker".equals(tab) ? primaryColor : secondaryColor);

        tvTabTimingTitle.setTextColor("timing".equals(tab) ? primaryColor : secondaryColor);
        ivTabTimingIcon.setColorFilter("timing".equals(tab) ? primaryColor : secondaryColor);

        sectionMedia.setVisibility("media".equals(tab) ? View.VISIBLE : View.GONE);
        sectionTicker.setVisibility("ticker".equals(tab) ? View.VISIBLE : View.GONE);
        sectionTiming.setVisibility("timing".equals(tab) ? View.VISIBLE : View.GONE);
    }

    private void setupPlaylist() {
        mediaList = signageManager.getPlaylist();
        rvPlaylist.setLayoutManager(new LinearLayoutManager(this));

        playlistAdapter = new PlaylistAdapter(this, mediaList, new PlaylistAdapter.OnItemActionListener() {
            @Override
            public void onEdit(int position, MediaItem item) {
                showEditMediaBottomSheet(position, item);
            }

            @Override
            public void onDelete(int position, MediaItem item) {
                showDeleteConfirmationDialog(position, item);
            }

            @Override
            public void onReorderChanged() {
                signageManager.savePlaylist(mediaList);
            }
        });

        rvPlaylist.setAdapter(playlistAdapter);
        updatePlaylistUI();

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                playlistAdapter.onItemMove(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}

            @Override
            public boolean isLongPressDragEnabled() {
                return isReorderMode;
            }
        };

        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvPlaylist);

        btnReorderPlaylist.setOnClickListener(v -> {
            isReorderMode = !isReorderMode;
            if (isReorderMode) {
                btnReorderPlaylist.setText(getString(R.string.btn_done));
                Toast.makeText(this, getString(R.string.msg_reorder_enabled), Toast.LENGTH_SHORT).show();
            } else {
                btnReorderPlaylist.setText(getString(R.string.btn_reorder));
                signageManager.savePlaylist(mediaList);
                Toast.makeText(this, getString(R.string.msg_reorder_disabled), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlaylistUI() {
        int count = mediaList.size();
        tvPlaylistCount.setText(String.format(Locale.getDefault(), "(%d Items)", count));

        if (count == 0) {
            rvPlaylist.setVisibility(View.GONE);
            layoutEmptyPlaylist.setVisibility(View.VISIBLE);
        } else {
            rvPlaylist.setVisibility(View.VISIBLE);
            layoutEmptyPlaylist.setVisibility(View.GONE);
        }
    }

    private void showEditMediaBottomSheet(int position, MediaItem item) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_media, null);
        dialog.setContentView(dialogView);

        EditText etTitle = dialogView.findViewById(R.id.et_edit_title);
        EditText etDuration = dialogView.findViewById(R.id.et_edit_duration);
        View containerDuration = dialogView.findViewById(R.id.container_edit_duration);
        View btnSave = dialogView.findViewById(R.id.btn_save_edit);

        etTitle.setText(item.getTitle());
        etDuration.setText(String.valueOf(item.getDurationSeconds()));

        if (item.isVideo()) {
            if (containerDuration != null) containerDuration.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String newTitle = etTitle.getText().toString().trim();
            String durStr = etDuration.getText().toString().trim();

            if (TextUtils.isEmpty(newTitle)) {
                Toast.makeText(this, "Judul tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            int dur = item.getDurationSeconds();
            if (!item.isVideo() && !TextUtils.isEmpty(durStr)) {
                try {
                    dur = Integer.parseInt(durStr);
                } catch (NumberFormatException ignored) {}
            }

            signageManager.updateMediaItem(position, newTitle, dur);
            mediaList.get(position).setTitle(newTitle);
            mediaList.get(position).setDurationSeconds(dur);
            playlistAdapter.notifyItemChanged(position);

            dialog.dismiss();
            Toast.makeText(this, "Perubahan media disimpan", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void showDeleteConfirmationDialog(int position, MediaItem item) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_delete_media_title))
                .setMessage(getString(R.string.dialog_delete_media_msg))
                .setPositiveButton(getString(R.string.btn_delete), (dialog, which) -> {
                    signageManager.removeMediaItem(position);
                    mediaList.remove(position);
                    playlistAdapter.notifyItemRemoved(position);
                    updatePlaylistUI();
                    updateStorageUsage();
                    Toast.makeText(this, "Media berhasil dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void updateStorageUsage() {
        long usedBytes = signageManager.getTotalStorageUsedBytes();
        double usedGB = (double) usedBytes / (1024.0 * 1024.0 * 1024.0);
        double maxGB = 2.0;

        tvStorageUsageText.setText(String.format(Locale.getDefault(), "%.1f GB / %.1f GB", usedGB, maxGB));

        int percent = (int) ((usedBytes * 100) / SignageManager.MAX_STORAGE_BYTES);
        if (percent > 100) percent = 100;

        progressStorageUsage.setProgress(percent);

        if (percent > 90) {
            progressStorageUsage.setProgressTintList(getColorStateList(R.color.danger_red));
        } else {
            progressStorageUsage.setProgressTintList(getColorStateList(R.color.amber_accent));
        }
    }

    private void setupUploadLauncher() {
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        handleUploadedFile(uri);
                    }
                }
        );
    }

    private void handleUploadedFile(Uri uri) {
        String fileName = getFileNameFromUri(uri);
        long fileSize = getFileSizeFromUri(uri);
        String mimeType = getContentResolver().getType(uri);

        // Validation 1: Size Max 50MB (52,428,800 bytes)
        long maxFileBytes = 50L * 1024L * 1024L;
        if (fileSize > maxFileBytes) {
            Toast.makeText(this, getString(R.string.error_file_too_large), Toast.LENGTH_LONG).show();
            return;
        }

        // Validation 2: Format Check (JPG, PNG, MP4)
        boolean isVideo = (mimeType != null && mimeType.contains("video")) || (fileName != null && fileName.toLowerCase().endsWith(".mp4"));
        boolean isImage = (mimeType != null && mimeType.contains("image")) || (fileName != null && (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpeg")));

        if (!isVideo && !isImage) {
            Toast.makeText(this, getString(R.string.error_invalid_file_type), Toast.LENGTH_LONG).show();
            return;
        }

        // Validation 3: Storage Limit Check
        long currentUsed = signageManager.getTotalStorageUsedBytes();
        if (currentUsed + fileSize > SignageManager.MAX_STORAGE_BYTES) {
            Toast.makeText(this, getString(R.string.error_storage_full), Toast.LENGTH_LONG).show();
            return;
        }

        // Default thumbnail/media creation
        String type = isVideo ? "video" : "image";
        int duration = 15;
        String videoDuration = isVideo ? "02:15" : null;

        MediaItem newItem = new MediaItem(
                String.valueOf(System.currentTimeMillis()),
                fileName != null ? fileName : "Media_Baru_" + System.currentTimeMillis(),
                uri.toString(),
                type,
                duration,
                videoDuration,
                fileSize > 0 ? fileSize : 15L * 1024L * 1024L,
                System.currentTimeMillis()
        );

        signageManager.addMediaItem(newItem);
        mediaList.add(newItem);
        playlistAdapter.notifyItemInserted(mediaList.size() - 1);
        updatePlaylistUI();
        updateStorageUsage();

        Toast.makeText(this, getString(R.string.msg_upload_success), Toast.LENGTH_SHORT).show();
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            } catch (Exception ignored) {}
        }
        if (result == null) {
            result = uri.getPath();
            if (result != null) {
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result;
    }

    private long getFileSizeFromUri(Uri uri) {
        long size = 0;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (index != -1) {
                        size = cursor.getLong(index);
                    }
                }
            } catch (Exception ignored) {}
        }
        return size;
    }

    private void handleRefreshStatus() {
        if (tvDeviceStatus != null && viewStatusDot != null) {
            viewStatusDot.setBackgroundTintList(getColorStateList(R.color.amber_accent));
            tvDeviceStatus.setText(getString(R.string.status_syncing));
            tvDeviceStatus.setTextColor(getColor(R.color.amber_accent));

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                viewStatusDot.setBackgroundTintList(getColorStateList(R.color.emerald_primary));
                tvDeviceStatus.setText(getString(R.string.status_connected));
                tvDeviceStatus.setTextColor(getColor(R.color.emerald_primary));
                Toast.makeText(this, getString(R.string.msg_refresh_success), Toast.LENGTH_SHORT).show();
            }, 1200);
        }
    }

    private void showFullscreenPreviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View previewView = LayoutInflater.from(this).inflate(R.layout.dialog_fullscreen_preview, null);
        builder.setView(previewView);

        AlertDialog dialog = builder.create();

        View btnClose = previewView.findViewById(R.id.btn_close_preview);
        TextView tvClock = previewView.findViewById(R.id.tv_preview_clock_fs);
        TextView tvMasjid = previewView.findViewById(R.id.tv_preview_masjid_fs);
        TextView tvTicker = previewView.findViewById(R.id.tv_preview_ticker_fs);

        String masjidName = profilPref.getString("nama_masjid", "Masjid Al-Ikhlas");
        if (tvMasjid != null) tvMasjid.setText(masjidName);

        List<String> tickers = signageManager.getTickers();
        if (!tickers.isEmpty() && tvTicker != null) {
            tvTicker.setText(tickers.get(0));
        }

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                if (tvClock != null) tvClock.setText(sdf.format(new Date()));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
            });
        }

        dialog.show();
    }

    private void loadTickersAndTiming() {
        containerTickerList.removeAllViews();
        List<String> tickers = signageManager.getTickers();

        for (int i = 0; i < tickers.size(); i++) {
            String ticker = tickers.get(i);
            View tickerRow = LayoutInflater.from(this).inflate(R.layout.item_ticker_row, containerTickerList, false);

            TextView tvNum = tickerRow.findViewById(R.id.tv_ticker_num);
            TextView tvText = tickerRow.findViewById(R.id.tv_ticker_text);

            if (tvNum != null) tvNum.setText((i + 1) + ".");
            if (tvText != null) tvText.setText(ticker);

            containerTickerList.addView(tickerRow);
        }

        if (!tickers.isEmpty()) {
            tvPreviewTicker.setText(tickers.get(0));
        }

        tvTimingInfo.setText(signageManager.getDefaultDuration() + " Detik per Gambar (Video diputar penuh)");
    }
}