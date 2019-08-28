package com.kbase.katha.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kbase.katha.R;
import com.kbase.katha.adapter.StoryAdapter;
import com.kbase.katha.model.Story;
import com.kbase.katha.sharedpreferences.SharedPreference;

import java.util.ArrayList;


public class SaveFragment extends Fragment implements SearchView.OnQueryTextListener {
    private StoryAdapter storyAdapter;
    private SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Story> stories;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_save, container, false);
        recyclerView = root.findViewById(R.id.recyclingView);
        searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        getAllShortStories();
    }

    private void getAllShortStories() {
        SharedPreference sharedPreference = new SharedPreference();
        stories = new ArrayList<>();
        stories = sharedPreference.getFavorites(getContext());
        if (stories == null) {
            System.out.println("Null");
        } else {
            storyAdapter = new StoryAdapter(stories, getContext(), "save");
            recyclerView.setAdapter(storyAdapter);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (storyAdapter == null) {
            System.out.println("No filters");
        } else {
            storyAdapter.filter(s);
        }
        return false;
    }
}