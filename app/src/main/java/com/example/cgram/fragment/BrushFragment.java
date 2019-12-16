package com.example.cgram.fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.cgram.R;
import com.example.cgram.adapter.ColorAdapter;
import com.example.cgram.utils.BrushFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrushFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {

    int colorSelected = Color.BLACK;
    private RecyclerView rvColor;
    static BrushFragment instance;
    private SeekBar sbWeight, sbOpacity;
    BrushFragmentListener listener;

    public void setListener(BrushFragmentListener listener) {
        this.listener = listener;
    }

    public static BrushFragment getInstance() {
        if (instance == null) {
            instance = new BrushFragment();
        }
        return instance;
    }

    public BrushFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brush, container, false);
    }

    @Override
    public void onColorSelected(int color) {
        colorSelected = color;
    }
}
