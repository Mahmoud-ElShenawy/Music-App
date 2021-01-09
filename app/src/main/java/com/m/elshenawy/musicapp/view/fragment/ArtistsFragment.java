package com.m.elshenawy.musicapp.view.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;

import com.m.elshenawy.musicapp.R;
import com.m.elshenawy.musicapp.data.api.client.SpotifyClient;
import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.databinding.FragmentArtisitsBinding;
import com.m.elshenawy.musicapp.interactor.ArtistsInteractor;
import com.m.elshenawy.musicapp.presenter.ArtistsPresenter;
import com.m.elshenawy.musicapp.view.activity.TracksActivity;
import com.m.elshenawy.musicapp.view.adapter.ArtistsAdapter;

import java.util.List;
import java.util.Objects;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ArtistsFragment extends Fragment implements ArtistsPresenter.View, SearchView.OnQueryTextListener {
    private FragmentArtisitsBinding binding;
    private ArtistsPresenter artistsPresenter;

    public static ArtistsFragment newInstance() {
        return new ArtistsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtisitsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpPresenter();
        setUpToolbar();
        setUpRecyclerView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_music, menu);
        setUpSearchView(menu);
    }


    private void setUpSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setMaxWidth(binding.toolbar.toolbarItem.getWidth());
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroy() {
        artistsPresenter.terminate();
        super.onDestroy();
    }

    @Nullable
    @Override
    public Context getContext() {
        return getActivity();
    }

    private void setUpRecyclerView() {
        ArtistsAdapter adapter = new ArtistsAdapter();
        adapter.setItemClickListener((Artist artist, int position) -> artistsPresenter.launchArtistDetail(artist));
        binding.recycleViewArtists.setAdapter(adapter);
    }

    private void setUpToolbar() {
        getParentActivity().setSupportActionBar(binding.toolbar.toolbarItem);
        ActionBar actionBar = getParentActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_navigation_menu);
        }
    }

    private void setUpPresenter() {
        artistsPresenter = new ArtistsPresenter(new ArtistsInteractor(new SpotifyClient()));
        artistsPresenter.setView(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        artistsPresenter.onSearchArtist(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void showLoading() {
        binding.progressBarArtists.setVisibility(View.VISIBLE);
        binding.imageViewArtists.setVisibility(View.GONE);
        binding.textViewLineArtists.setVisibility(View.GONE);
        binding.recycleViewArtists.setVisibility(View.GONE);
        binding.textViewSubLineArtists.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        binding.progressBarArtists.setVisibility(View.GONE);
        binding.recycleViewArtists.setVisibility(View.VISIBLE);
    }

    @Override
    public void showArtistNotFoundMessage() {
        binding.progressBarArtists.setVisibility(View.GONE);
        binding.textViewLineArtists.setVisibility(View.GONE);
        binding.imageViewArtists.setVisibility(View.VISIBLE);
        binding.textViewLineArtists.setText(getString(R.string.error_artist_not_found));
        binding.imageViewArtists.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_not_found));
    }

    @Override
    public void renderArtists(List<Artist> artists) {
        ArtistsAdapter adapter = (ArtistsAdapter) binding.recycleViewArtists.getAdapter();
        adapter.setArtists(artists);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void launchArtistDetail(Artist artist) {
        Intent intent = new Intent(getContext(), TracksActivity.class);
        intent.putExtra(TracksActivity.EXTRA_REPOSITORY, artist);
        startActivity(intent);
    }

    @Override
    public void showConnectionErrorMessage() {
        binding.progressBarArtists.setVisibility(View.GONE);
        binding.textViewLineArtists.setVisibility(View.VISIBLE);
        binding.imageViewArtists.setVisibility(View.VISIBLE);
        binding.textViewLineArtists.setText(getString(R.string.error_internet_connection));
        binding.imageViewArtists.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_not_internet));
    }

    @Override
    public void showServerError() {
        binding.progressBarArtists.setVisibility(View.GONE);
        binding.textViewLineArtists.setVisibility(View.VISIBLE);
        binding.imageViewArtists.setVisibility(View.VISIBLE);
        binding.textViewLineArtists.setText(getString(R.string.error_server_internal));
        binding.imageViewArtists.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_not_found));
    }

    @Override
    public Context context() {
        return null;
    }

    private AppCompatActivity getParentActivity() {
        return (AppCompatActivity) getActivity();
    }
}