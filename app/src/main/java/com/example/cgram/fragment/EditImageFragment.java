package com.example.cgram.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.cgram.R;
import com.example.cgram.utils.EditImageFragmentListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    private SeekBar sb_brightness, sb_contrast, sb_saturation;

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public EditImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sb_brightness = view.findViewById(R.id.sb_brightness);
        sb_contrast = view.findViewById(R.id.sb_contrast);
        sb_saturation = view.findViewById(R.id.sb_saturation);
        sb_brightness.setMax(200);
        sb_brightness.setProgress(100);
        sb_contrast.setMax(20);
        sb_contrast.setProgress(0);
        sb_saturation.setMax(30);
        sb_saturation.setProgress(10);

        sb_brightness.setOnSeekBarChangeListener(this);
        sb_saturation.setOnSeekBarChangeListener(this);
        sb_contrast.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {
            float value;
            switch (seekBar.getId()) {
                case R.id.sb_brightness:
                    listener.onBrightnessChanged(progress-100);
                    break;
                case R.id.sb_contrast:
                    progress+=10;
                    value = .10f*progress;
                    listener.onContrastChanged(value);
                    break;
                case R.id.sb_saturation:
                    value = .10f*progress;
                    listener.onSaturationChanged(value);
                    break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener != null) {
            listener.onEditStarted();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null) {
            listener.onEditComplete();
        }
    }

    public void reset() {
        sb_brightness.setProgress(100);
        sb_contrast.setProgress(0);
        sb_saturation.setProgress(10);
    }
}
