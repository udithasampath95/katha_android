package com.kbase.katha.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kbase.katha.R;
import com.kbase.katha.model.Story;
import com.kbase.katha.utill.StoryAdapterInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Story> nameArrayList;
    ArrayList<Story> arryList;
    StoryAdapterInterface storyAdapterInterface;
    Context context;
    String s="";

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;


    public StoryAdapter(ArrayList<Story> story, Context context, OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        this.nameArrayList = story;
        arryList = new ArrayList<>();
        arryList.addAll(story);
        this.storyAdapterInterface = (StoryAdapterInterface) context;
        this.context = context;
    }

    public StoryAdapter(ArrayList<Story> story, Context context, String s) {
        this.s = s;
        this.nameArrayList = story;
        arryList = new ArrayList<>();
        arryList.addAll(story);
        this.storyAdapterInterface = (StoryAdapterInterface) context;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_story_adapter, parent, false);
            vh = new TextViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            Story story = nameArrayList.get(position);

            System.out.println(nameArrayList.get(position));
            if (nameArrayList.get(position) == null) {
                System.out.println("null");
            } else {
                ((TextViewHolder) holder).title.setText(story.getStoryTitleSinhala());
                ((TextViewHolder) holder).content.setText(story.getStoryContent());
                ((TextViewHolder) holder).storyUploadedDate.setText("");
                ((TextViewHolder) holder).bind(nameArrayList.get(position), storyAdapterInterface);

                Glide.with(context)
                        .asBitmap()
                        .load(story.getImagePath())
                        .into(((TextViewHolder) holder).imageView);
            }
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }


    }


    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }

    public  class TextViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title, content, storyUploadedDate;
        ImageView imageView;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = view.findViewById(R.id.image);
            storyUploadedDate = view.findViewById(R.id.storyUploadedDate);
            title = view.findViewById(R.id.storyTitle);
            content = view.findViewById(R.id.storyContent);
        }

        public void bind(final Story story, final StoryAdapterInterface storyAdapterInterface) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(s.equalsIgnoreCase("save")){
                        storyAdapterInterface.navigate(story,"save");
                    }else {
                        storyAdapterInterface.navigate(story, "");
                    }
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

    public void showLoading() {
        if (isMoreLoading && nameArrayList != null && onLoadMoreListener != null) {
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    nameArrayList.add(null);
                    notifyItemInserted(nameArrayList.size() - 1);
                    onLoadMoreListener.onLoadMore();
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setMore(boolean isMore) {
        this.isMoreLoading = isMore;
    }

    public void dismissLoading() {
        if (nameArrayList != null && nameArrayList.size() > 0) {
            nameArrayList.remove(nameArrayList.size() - 1);
            notifyItemRemoved(nameArrayList.size());
        }
    }

    public void addItemMore(List<Story> lst) {
        int sizeInit = nameArrayList.size();
        nameArrayList.addAll(lst);
        notifyItemRangeChanged(sizeInit, nameArrayList.size());
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return nameArrayList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

}
