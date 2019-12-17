package com.example.cgram.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgram.R;
import com.example.cgram.adapter.ColorAdapter;
import com.example.cgram.utils.AddTextFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener  {

    private int colorSelected = Color.BLACK;

    private AddTextFragmentListener listener;

    private EditText edit_add_text;

    public void setListener(AddTextFragmentListener listener){
        this.listener = listener;
    }

    private static AddTextFragment instance;

    public static AddTextFragment getInstance(){
        if(instance == null){
            instance = new AddTextFragment();
        }
        return instance;
    }

    public AddTextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_add_text, container, false);

        edit_add_text = itemView.findViewById(R.id.edit_add_text);
        Button btn_done = itemView.findViewById(R.id.btn_add_text);
        RecyclerView recycler_color = itemView.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ColorAdapter colorAdapter = new ColorAdapter(getContext(), this);
        recycler_color.setAdapter(colorAdapter);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddTextButtonClick(edit_add_text.getText().toString(), colorSelected);
                dismiss();
            }
        });

        return itemView;
    }

    @Override
    public void onColorSelected(int color) {
        colorSelected = color;
    }
}
