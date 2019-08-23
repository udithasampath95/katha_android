package com.kbase.katha.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kbase.katha.R;
import com.kbase.katha.model.Story;
import com.kbase.katha.utill.StoryAdapterInterface;

import java.util.ArrayList;
import java.util.Locale;

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Story> nameArrayList;
    ArrayList<Story> arryList;
    StoryAdapterInterface storyAdapterInterface;


    public StoryAdapter(ArrayList<Story> story, Context context) {
        this.nameArrayList = story;
        arryList = new ArrayList<>();
        arryList.addAll(story);
        this.storyAdapterInterface = (StoryAdapterInterface) context;
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
        ((TextViewHolder) holder).title.setText(story.getStoryTitleSinhala());
        ((TextViewHolder) holder).content.setText(story.getStoryContent());
        ((TextViewHolder) holder).storyUploadedDate.setText("");
        ((TextViewHolder) holder).bind(nameArrayList.get(position), storyAdapterInterface);
    }



    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title, content, storyUploadedDate;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            storyUploadedDate = view.findViewById(R.id.storyUploadedDate);
            title = view.findViewById(R.id.storyTitle);
            content = view.findViewById(R.id.storyContent);
        }

        public void bind(final Story story, final StoryAdapterInterface storyAdapterInterface) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storyAdapterInterface.navigate(story);
                }
            });
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
