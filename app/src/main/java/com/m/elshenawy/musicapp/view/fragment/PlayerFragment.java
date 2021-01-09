package com.m.elshenawy.musicapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.m.elshenawy.musicapp.data.model.Track;
import com.m.elshenawy.musicapp.databinding.FragmentPlayerBinding;
import com.m.elshenawy.musicapp.interactor.PlayerInteractor;
import com.m.elshenawy.musicapp.presenter.AudioPlayerPresenter;
import com.m.elshenawy.musicapp.view.activity.TracksActivity;
import com.m.elshenawy.musicapp.view.service.AudioPlayerService;
import com.m.elshenawy.musicapp.view.utils.ServiceUtils;

import java.lang.reflect.Type;
import java.util.List;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class PlayerFragment extends DialogFragment implements AudioPlayerPresenter.View, SeekBar.OnSeekBarChangeListener {
    AudioPlayerService audioPlayerService;
    private AudioPlayerPresenter audioPlayerPresenter;
    private final boolean isPlayerPlaying = false;
    private int trackCurrentPosition;
    private List<Track> trackList;
    private int trackPosition;
    private FragmentPlayerBinding binding;

    public static PlayerFragment newInstance(String tracks, int position) {
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TracksActivity.EXTRA_TRACKS, tracks);
        bundle.putInt(TracksActivity.EXTRA_TRACK_POSITION, position);
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trackList = getTrackList(getArguments().getString(TracksActivity.EXTRA_TRACKS));
        trackPosition = getArguments().getInt(TracksActivity.EXTRA_TRACK_POSITION);
        setUpPresenter();
        setUpUi();
    }

    private void setUpUi() {
        binding.imageButtonPreviewPlayer.setOnClickListener(v -> audioPlayerPresenter.onPreviewTrack());
        binding.imageButtonNextPlayer.setOnClickListener(v -> audioPlayerPresenter.onNextTrack());
        binding.imageButtonPlayPlayer.setOnClickListener(v -> audioPlayerPresenter.onPlayPauseTrack());
    }

    private void setUpPresenter() {
        audioPlayerPresenter = new AudioPlayerPresenter(new PlayerInteractor(trackList, getContext()));
        audioPlayerPresenter.setView(this);
        audioPlayerPresenter.onSetInfoTrackPlayer(trackPosition);
        audioPlayerPresenter.onStartAudioService(trackList.get(trackPosition).previewUrl);
    }

    private List<Track> getTrackList(String tracks) {
        Gson gson = new GsonBuilder().create();
        Type trackType = new TypeToken<List<Track>>() {
        }.getType();
        return gson.fromJson(tracks, trackType);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        audioPlayerPresenter.terminate();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        audioPlayerPresenter.terminate();
        super.onDestroy();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        binding.textViewTimeStart.setText("00:" + String.format("%020d", progress));
        trackCurrentPosition = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (isPlayerPlaying) {
            audioPlayerService.noUpdateUI();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        trackCurrentPosition = seekBar.getProgress();
        if (audioPlayerService != null) {
            boolean isPlayerPaused = false;
            audioPlayerService.toSeekTrack(trackCurrentPosition, isPlayerPaused);
        }

    }

    @Override
    public void onStartAudioService(String trackUrl, ServiceConnection serviceConnection) {
        Intent intent = new Intent(getActivity(), AudioPlayerService.class);
        intent.putExtra(AudioPlayerService.EXTRA_TRACK_PREVIEW_URL, trackUrl);

        if (ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService.class, requireActivity()) && !isPlayerPlaying) {
            trackCurrentPosition = 0;
            getActivity().getApplicationContext().stopService(intent);
            getActivity().getApplicationContext().startActivity(intent);
        } else if (!ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService.class, requireActivity())) {
            trackCurrentPosition = 0;
            getActivity().getApplicationContext().startService(intent);
        }
        if (audioPlayerService == null) {
            getActivity().getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void setInfoTrackPlayer(int trackPosition) {
        binding.textViewTrackTitlePlayer.setText(trackList.get(trackPosition).name);
        binding.textViewAlbumTitlePlayer.setText(trackList.get(trackPosition).album.albumName);

        if (trackList.get(trackPosition).album.trackImages.size() > 0) {
            for (int i = 0; i < trackList.get(trackPosition).album.trackImages.size(); i++) {
                if (trackList.get(i) != null && trackList.get(trackPosition).album.trackImages.size() > 0) {
                    Glide.with(getActivity())
                            .load(trackList.get(trackPosition).album.trackImages.get(0).url)
                            .into(binding.imageViewAlbumPlayer);
                }
            }
        } else {
            Glide.with(getActivity())
                    .load("http://www.iphonemode.com/wp-content/uploads/2016/07/Spotify-new-logo.jpg")
                    .into(binding.imageViewAlbumPlayer);
        }
    }

    @Override
    public void isPlay() {
        binding.imageButtonPlayPlayer.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void isPause() {
        binding.imageButtonPlayPlayer.setImageResource(android.R.drawable.ic_media_pause);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void setTimeStart(int trackCurrentPosition) {
        binding.seekBarTimeProgressPlayer.setProgress(trackCurrentPosition);
        binding.textViewTimeStart.setText("00:" + String.format("%02d", trackCurrentPosition));
    }

    @Override
    public void setTimeFinished(AudioPlayerService audioPlayerService) {
        binding.seekBarTimeProgressPlayer.setMax(audioPlayerService.getTrackDuration());
        binding.textViewTimeEnd.setText(audioPlayerService.getTrackDurationString());
    }

    @Override
    public void onResetTrackDuration() {
        binding.seekBarTimeProgressPlayer.setProgress(0);
        binding.textViewTimeStart.setText("");
        binding.textViewTimeEnd.setText("");
    }

    @Override
    public Context context() {
        return getActivity();
    }
}