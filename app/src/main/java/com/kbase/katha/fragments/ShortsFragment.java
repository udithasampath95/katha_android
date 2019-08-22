package com.kbase.katha.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.kbase.katha.APIService;
import com.kbase.katha.R;
import com.kbase.katha.RetrofitInstance;
import com.kbase.katha.adapter.StoryAdapter;
import com.kbase.katha.model.Story;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShortsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private StoryAdapter storyAdapter;
    private SearchView searchView;
    RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shorts, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText("This is short stories fragment");
        recyclerView = root.findViewById(R.id.recyclingView);
        searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        getAllShortStories();
    }

    private void getAllShortStories() {
        APIService getNoticeDataService = RetrofitInstance.getRetrofitInstance().create(APIService.class);
        Call<ArrayList<Story>> call = getNoticeDataService.getAllShortStories();
        call.enqueue(new Callback<ArrayList<Story>>() {
            @Override
            public void onResponse(Call<ArrayList<Story>> call, Response<ArrayList<Story>> response) {
                ArrayList<Story> stories = new ArrayList<>();
                stories = response.body();
                for (Story story : stories) {
                    System.out.println(story.getStoryContent());
                }

                storyAdapter = new StoryAdapter(stories);
                recyclerView.setAdapter(storyAdapter);


            }

            @Override
            public void onFailure(Call<ArrayList<Story>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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