package com.m.elshenawy.musicapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.m.elshenawy.musicapp.R;
import com.m.elshenawy.musicapp.databinding.ActivityArtistsBinding;
import com.m.elshenawy.musicapp.view.fragment.ArtistsFragment;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ArtistsActivity extends AppCompatActivity {
    ActivityArtistsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtistsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, ArtistsFragment.newInstance()).commit();
        }

    }
}