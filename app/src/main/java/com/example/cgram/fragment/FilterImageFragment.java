package com.example.cgram.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cgram.EditorActivity;
import com.example.cgram.R;
import com.example.cgram.adapter.FilterAdapter;
import com.example.cgram.utils.BitmapUtils;
import com.example.cgram.utils.FilterListFragmentListener;
import com.example.cgram.utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterImageFragment extends Fragment implements FilterListFragmentListener{

    private RecyclerView rvFilter;
    private FilterAdapter adapter;
    ArrayList<ThumbnailItem> thumbnailItems;
    FilterListFragmentListener listener;

    public void setListener(FilterListFragmentListener listener) {
        this.listener = listener;
    }

    public FilterImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        thumbnailItems = new ArrayList<>();
        adapter = new FilterAdapter(thumbnailItems, this, getActivity());
        rvFilter = view.findViewById(R.id.rv_filters);
        rvFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvFilter.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        rvFilter.addItemDecoration(new SpacesItemDecoration(space));
        rvFilter.setAdapter(adapter);
        displayThubnail(null);
    }

    public void displayThubnail(final Bitmap bitmap) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bitmap thubm;
                if (bitmap == null) {
                    byte[] byteArray = getArguments().getByteArray("image");
                    thubm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                } else {
                    thubm = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                }

                if (thubm == null){
                    return;
                }
                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                ThumbnailItem item = new ThumbnailItem();
                item.image = thubm;
                item.filterName = "Normal";
                ThumbnailsManager.addThumb(item);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());

                for (Filter filter : filters){
                    ThumbnailItem ti = new ThumbnailItem();
                    ti.image = thubm;
                    ti.filter = filter;
                    ti.filterName = filter.getName();
                    ThumbnailsManager.addThumb(ti);
                }

                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(r).start();
    }

    @Override
    public void onFilterselected(Filter filter) {
        if (listener != null){
            listener.onFilterselected(filter);
        }
    }
}
