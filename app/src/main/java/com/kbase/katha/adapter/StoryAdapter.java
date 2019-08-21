package com.kbase.katha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kbase.katha.R;
import com.kbase.katha.model.Customer;

import java.util.ArrayList;
import java.util.Locale;

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Customer> nameArrayList;
    ArrayList<Customer> arryList;

    public StoryAdapter(ArrayList<Customer> name) {
        this.nameArrayList = name;
        arryList = new ArrayList<>();
        arryList.addAll(name);
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
        Customer name = nameArrayList.get(position);
        System.out.println(position);
        ((TextViewHolder) holder).name.setText(name.getName());
        ((TextViewHolder) holder).des.setText(name.getDes());
    }

    @Override
    public int getItemCount() {
        System.out.println(nameArrayList.size());
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
        System.out.println(charText);
        charText = charText.toLowerCase(Locale.getDefault());
        nameArrayList.clear();
        if (charText.length() == 0) {
            nameArrayList.addAll(arryList);
        } else {
            for (Customer response : arryList) {
                if (response.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    System.out.println("call name");
                    nameArrayList.add(response);
                } else if (response.getDes().toLowerCase(Locale.getDefault()).contains(charText)) {
                    System.out.println("call des");
                    nameArrayList.add(response);
                }
            }
        }
        StoryAdapter.this.notifyDataSetChanged();
    }


}
