package com.crop.companion.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crop.companion.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder> {
    public List<T> list = new ArrayList<>();

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public View mView;

        public SimpleViewHolder (View view) {
            super(view);
            name = view.findViewById(R.id.item_text);
            mView = view;
        }
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleRecyclerAdapter.SimpleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
