package com.kbase.katha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kbase.katha.R;
import com.kbase.katha.model.Customer;
import com.kbase.katha.model.Story;

import java.util.ArrayList;
import java.util.Locale;

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Story> nameArrayList;
    ArrayList<Story> arryList;

    public StoryAdapter(ArrayList<Story> story) {
        this.nameArrayList = story;
        arryList = new ArrayList<>();
        arryList.addAll(story);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_story_adapter, parent, false);
        viewHolder = new TextViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Story story = nameArrayList.get(position);
        ((TextViewHolder) holder).name.setText(story.getStoryId());
        ((TextViewHolder) holder).des.setText(story.getStoryTitleSinglish());
    }

    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView name;
        TextView des;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.nameText);
            des = view.findViewById(R.id.desText);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nameArrayList.clear();
        if (charText.length() == 0) {
            nameArrayList.addAll(arryList);
        } else {
            for (Story response : arryList) {
                if (response.getStoryTitleSinglish().toLowerCase(Locale.getDefault()).contains(charText)) {
                    nameArrayList.add(response);
                } else if (response.getStoryTitleSinhala().toLowerCase(Locale.getDefault()).contains(charText)) {
                    nameArrayList.add(response);
                }
            }
        }
        StoryAdapter.this.notifyDataSetChanged();
    }


}
