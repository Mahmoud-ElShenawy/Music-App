package com.m.elshenawy.musicapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.databinding.ItemArtistBinding;

import java.util.Collections;
import java.util.List;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {
    private List<Artist> artists;
    private ItemClickListener itemClickListener;

    public ArtistsAdapter() {
        artists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistViewHolder(ItemArtistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.render(artist);
        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(artist, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(Artist artist, int position);
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        private ItemArtistBinding binding;

        public ArtistViewHolder(@NonNull ItemArtistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void render(Artist artist) {
            binding.textViewArtistName.setText(artist.name);
            if (artist.artistImages.size() > 0) {
                for (int i = 0; i < artist.artistImages.size(); i++) {
                    if (artist.artistImages.get(i) != null) {
                        artist.artistImages.size();
                        renderImage(artist.artistImages.get(0).url);
                    }
                }
            } else {
                final String imageHolder = "http://www.iphonemode.com/wp-content/uploads/2016/07/Spotify-new-logo.jpg";
                renderImage(imageHolder);
            }
        }

        private void renderImage(String url) {
            Glide.with(binding.imageViewArtistsImage.getContext())
                    .load(url)
                    .into(binding.imageViewArtistsImage);
        }
    }


}
