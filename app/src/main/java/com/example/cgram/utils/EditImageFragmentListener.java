package com.example.cgram.utils;

public interface EditImageFragmentListener {
    void onBrightnessChanged(int brightness);
    void onContrastChanged(float contrast);
    void onSaturationChanged(float saturation);
    void onEditStarted();
    void onEditComplete();
}
