package com.m.elshenawy.musicapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m.elshenawy.musicapp.data.model.Track;
import com.m.elshenawy.musicapp.databinding.ItemTrackBinding;

import java.util.List;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.TrackViewHolder> {
    private List<Track> tracks;
    private ItemClickListener itemClickListener;

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackViewHolder(ItemTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = tracks.get(position);
        holder.render(track, position);
        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(tracks, track, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(List<Track> tracks, Track track, int position);
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        private ItemTrackBinding binding;

        public TrackViewHolder(@NonNull ItemTrackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void render(Track track, Integer position) {
            final String trackTitle = (position + 1) + "." + track.name;
            binding.textViewTrackTitle.setText(trackTitle);
            binding.textViewTrackAlbum.setText(track.album.albumName);

            if (track.album.trackImages.size() > 0) {
                binding.imageViewTrack.setScaleType(ImageView.ScaleType.FIT_XY);
                for (int i = 0; i < track.album.trackImages.size(); i++) {
                    if (track.album.trackImages.get(i) != null) {
                        renderImage(track.album.trackImages.get(0).url);
                    }
                }
            } else {
                renderImage("http://www.iphonemode.com/wp-content/uploads/2016/07/Spotify-new-logo.jpg");
            }
        }

        private void renderImage(String url) {
            Glide.with(binding.imageViewTrack.getContext())
                    .load(url)
                    .into(binding.imageViewTrack);
        }
    }

}
