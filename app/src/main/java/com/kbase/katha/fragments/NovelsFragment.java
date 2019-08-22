package com.kbase.katha.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kbase.katha.R;
import com.kbase.katha.adapter.StoryAdapter;
import com.kbase.katha.model.Customer;

import java.util.ArrayList;

public class NovelsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private StoryAdapter storyAdapter;
    private SearchView searchView;
    ArrayList<Customer> name;
    RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_novels, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        TextView textView = root.findViewById(R.id.text_gallery);
        textView.setText("This is short stories fragment");
        recyclerView = root.findViewById(R.id.recyclingView);
        searchView = root.findViewById(R.id.searchView);
        name = new ArrayList<>();
//        System.out.println("call");
//
//
//        for (int i = 0; i < 10; i++) {
//            Customer customer = new Customer();
//            customer.setName("novels name" + i);
//            customer.setDes("novels des" + i);
//            name.add(customer);
//        }
//
//        storyAdapter = new StoryAdapter(name);
//        recyclerView.setAdapter(storyAdapter);
//        searchView.setOnQueryTextListener(this);
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