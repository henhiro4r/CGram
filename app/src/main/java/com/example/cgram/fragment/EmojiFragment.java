package com.example.cgram.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgram.R;
import com.example.cgram.adapter.EmojiAdapter;
import com.example.cgram.utils.EmojiFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener {

    private static EmojiFragment instance;
    private EmojiFragmentListener listener;

    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    public static EmojiFragment getInstance() {
        if (instance == null) {
            instance = new EmojiFragment();
        }
        return instance;
    }

    public EmojiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_emoji, container, false);
        RecyclerView rvEmoji = itemView.findViewById(R.id.rv_emoji);
        rvEmoji.setHasFixedSize(true);
        rvEmoji.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        EmojiAdapter adapter = new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()), this);
        rvEmoji.setAdapter(adapter);
        return itemView;
    }

    @Override
    public void onEmojiItemSelecter(String emoji) {
        listener.onEmojiSelected(emoji);
        dismiss();
    }
}
