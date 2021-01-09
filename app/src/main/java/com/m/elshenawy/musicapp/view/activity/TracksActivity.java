package com.m.elshenawy.musicapp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.m.elshenawy.musicapp.R;
import com.m.elshenawy.musicapp.data.api.client.SpotifyClient;
import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.data.model.Track;
import com.m.elshenawy.musicapp.databinding.ActivityTracksBinding;
import com.m.elshenawy.musicapp.interactor.TracksInteractor;
import com.m.elshenawy.musicapp.presenter.TracksPresenter;
import com.m.elshenawy.musicapp.view.adapter.TracksAdapter;
import com.m.elshenawy.musicapp.view.fragment.PlayerFragment;
import com.m.elshenawy.musicapp.view.utils.BlurEffectUtils;

import java.lang.reflect.Type;
import java.util.List;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class TracksActivity extends AppCompatActivity implements TracksPresenter.View, AppBarLayout.OnOffsetChangedListener {
    public static final String EXTRA_REPOSITORY = "EXTRA_ARTIST";
    public static final String EXTRA_TRACK_POSITION = "EXTRA_TRACK_POSITION";
    public static final String EXTRA_TRACKS = "EXTRA_TRACKS";
    private ActivityTracksBinding binding;
    private TracksPresenter tracksPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTracksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpToolbar();
        setUpRecyclerView();
        setUpPresenter();
        Artist artist = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        initializeViews(artist);
        tracksPresenter.onSearchTracks(artist.id);
    }

    private void initializeViews(Artist artist) {
        if (artist.artistImages.size() > 0) {
            Glide.with(this)
                    .load(artist.artistImages.get(0).url)
                    .transform(new BlurEffectUtils(this, 20))
                    .into(binding.imageViewCollapsingArtist);
            Glide.with(this).load(artist.artistImages.get(0).url)
                    .into(binding.circularImageViewArtist);
        } else {
            final String imageHolder = "http://www.iphonemode.com/wp-content/uploads/2016/07/Spotify-new-logo.jpg";
            binding.circularImageViewArtist.setVisibility(View.GONE);
            Glide.with(this)
                    .load(imageHolder)
                    .transform(new BlurEffectUtils(this, 20))
                    .into(binding.imageViewCollapsingArtist);
        }

        binding.textViewTitleArtist.setText(artist.name);
        binding.textViewSubtitleArtist.setText(artist.name);
        String totalFollowers = getResources().getQuantityString(R.plurals.numberOfFollowers, artist.followers.totalFollowers, artist.followers.totalFollowers);
        binding.textViewFollowersArtist.setText(totalFollowers);
    }

    private void setUpPresenter() {
        tracksPresenter = new TracksPresenter(new TracksInteractor(new SpotifyClient()));
        tracksPresenter.setView(this);
    }

    private void setUpRecyclerView() {
        TracksAdapter adapter = new TracksAdapter();
        adapter.setItemClickListener((tracks, track, position) -> tracksPresenter.launchArtistDetail(tracks, track, position));
        binding.recycleViewTracks.setAdapter(adapter);
        binding.appBarArtist.addOnOffsetChangedListener(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        onOffsetChangedState(appBarLayout, verticalOffset);
    }

    private void hideAndShowTitleToolbar(int visibility) {
        binding.textViewLineTracks.setVisibility(visibility);
        binding.textViewSubtitleArtist.setVisibility(visibility);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onOffsetChangedState(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            hideAndShowTitleToolbar(View.GONE);
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            hideAndShowTitleToolbar(View.VISIBLE);
        } else {
            hideAndShowTitleToolbar(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        binding.progressBarTracks.setVisibility(View.VISIBLE);
        binding.imageViewTracks.setVisibility(View.GONE);
        binding.textViewLineTracks.setVisibility(View.GONE);
        binding.recycleViewTracks.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        binding.progressBarTracks.setVisibility(View.GONE);
        binding.recycleViewTracks.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTracksNotFoundMessage() {
        binding.progressBarTracks.setVisibility(View.GONE);
        binding.textViewLineTracks.setVisibility(View.VISIBLE);
        binding.imageViewTracks.setVisibility(View.VISIBLE);
        binding.textViewLineTracks.setText(getString(R.string.error_tracks_not_found));
        binding.imageViewTracks.setImageDrawable(ContextCompat.getDrawable(context(), R.mipmap.ic_not_found));

    }

    @Override
    public void renderTracks(List<Track> tracks) {
        TracksAdapter adapter = (TracksAdapter) binding.recycleViewTracks.getAdapter();
        adapter.setTracks(tracks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void launchTrackDetail(List<Track> tracks, Track track, int position) {
        PlayerFragment.newInstance(setTracks(tracks), position).show(getSupportFragmentManager(), "");

    }

    private String setTracks(List<Track> tracks) {
        Gson gson = new GsonBuilder().create();
        Type trackType = new TypeToken<List<Track>>() {
        }.getType();
        return gson.toJson(tracks, trackType);
    }

    @Override
    public void showConnectionErrorMessage() {
        binding.progressBarTracks.setVisibility(View.GONE);
        binding.textViewLineTracks.setVisibility(View.VISIBLE);
        binding.imageViewTracks.setVisibility(View.VISIBLE);
        binding.textViewLineTracks.setText(getString(R.string.error_internet_connection));
        binding.imageViewTracks.setImageDrawable(ContextCompat.getDrawable(context(), R.mipmap.ic_not_internet));
    }


    @Override
    public Context context() {
        return null;
    }
}
