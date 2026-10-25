package com.example.masjidconnect;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    public interface OnItemActionListener {
        void onEdit(int position, MediaItem item);
        void onDelete(int position, MediaItem item);
        void onReorderChanged();
    }

    private final Context context;
    private final List<MediaItem> items;
    private final OnItemActionListener listener;

    public PlaylistAdapter(Context context, List<MediaItem> items, OnItemActionListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist_media, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaItem item = items.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvRelativeTime.setText(SignageManager.formatRelativeTime(item.getTimestampMs()));

        if (item.isVideo()) {
            holder.containerImageDur.setVisibility(View.GONE);
            holder.tvBadgeFullVideo.setVisibility(View.VISIBLE);
            holder.containerVideoOverlay.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(item.getVideoDuration())) {
                holder.tvVideoDuration.setText(item.getVideoDuration());
            } else {
                holder.tvVideoDuration.setText("FULL");
            }
        } else {
            holder.containerImageDur.setVisibility(View.VISIBLE);
            holder.tvBadgeFullVideo.setVisibility(View.GONE);
            holder.containerVideoOverlay.setVisibility(View.GONE);
            holder.tvDurationSec.setText(item.getDurationSeconds() + "s");
        }

        // Load thumbnail with Glide or fallback icon
        if (!TextUtils.isEmpty(item.getFileUri())) {
            Glide.with(context)
                    .load(item.getFileUri())
                    .placeholder(R.drawable.ic_media_image)
                    .error(R.drawable.ic_media_image)
                    .centerCrop()
                    .into(holder.ivThumbnail);
        } else {
            // Placeholder colors/icons based on type
            if (item.isVideo()) {
                holder.ivThumbnail.setImageResource(R.drawable.ic_play_video);
                holder.ivThumbnail.setBackgroundColor(context.getColor(R.color.navy_dark));
            } else {
                holder.ivThumbnail.setImageResource(R.drawable.ic_media_image);
                holder.ivThumbnail.setBackgroundColor(context.getColor(R.color.tab_bg_gray));
            }
        }

        holder.btnEdit.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onEdit(pos, items.get(pos));
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onDelete(pos, items.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        if (listener != null) {
            listener.onReorderChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        FrameLayout containerVideoOverlay;
        TextView tvVideoDuration;
        TextView tvTitle;
        LinearLayout containerImageDur;
        TextView tvDurationSec;
        TextView tvBadgeFullVideo;
        TextView tvRelativeTime;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            containerVideoOverlay = itemView.findViewById(R.id.container_video_overlay);
            tvVideoDuration = itemView.findViewById(R.id.tv_video_duration);
            tvTitle = itemView.findViewById(R.id.tv_media_title);
            containerImageDur = itemView.findViewById(R.id.container_image_dur);
            tvDurationSec = itemView.findViewById(R.id.tv_duration_sec);
            tvBadgeFullVideo = itemView.findViewById(R.id.tv_badge_full_video);
            tvRelativeTime = itemView.findViewById(R.id.tv_relative_time);
            btnEdit = itemView.findViewById(R.id.btn_edit_item);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}