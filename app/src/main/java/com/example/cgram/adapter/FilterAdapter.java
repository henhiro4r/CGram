package com.example.cgram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgram.R;
import com.example.cgram.utils.FilterListFragmentListener;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private ArrayList<ThumbnailItem> thumbnailItems;
    private FilterListFragmentListener listFragmentListener;
    private Context context;
    private int selectedIndex = 0;

    public FilterAdapter(ArrayList<ThumbnailItem> thumbnailItems, FilterListFragmentListener listFragmentListener, Context context) {
        this.thumbnailItems = thumbnailItems;
        this.listFragmentListener = listFragmentListener;
        this.context = context;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_item, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, final int position) {
        final ThumbnailItem item = thumbnailItems.get(position);
        holder.filter_preview.setImageBitmap(item.image);
        holder.filter_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFragmentListener.onFilterselected(item.filter);
                selectedIndex = position;
                notifyDataSetChanged();
            }
        });
        holder.filterName.setText(item.filterName);

        if (selectedIndex == position) {
            holder.filterName.setTextColor(ContextCompat.getColor(context, R.color.selected_filter));
        } else {
            holder.filterName.setTextColor(ContextCompat.getColor(context, R.color.normal_filter));
        }
    }

    @Override
    public int getItemCount() {
        return thumbnailItems.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {

        ImageView filter_preview;
        TextView filterName;

        FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            filter_preview = itemView.findViewById(R.id.filter_preview);
            filterName = itemView.findViewById(R.id.tv_filter_name);
        }
    }
}
