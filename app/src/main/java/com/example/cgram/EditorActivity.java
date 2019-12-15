package com.example.cgram;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.cgram.adapter.ViewPagerAdapter;
import com.example.cgram.fragment.EditImageFragment;
import com.example.cgram.fragment.FilterImageFragment;
import com.example.cgram.utils.BitmapUtils;
import com.example.cgram.utils.EditImageFragmentListener;
import com.example.cgram.utils.FilterListFragmentListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditorActivity extends AppCompatActivity implements FilterListFragmentListener, EditImageFragmentListener {

    private PhotoEditorView photoEditorView;
    private PhotoEditor photoEditor;
    public static String IMAGE__EXTRA = "extra";
    public static String SELECTED__EXTRA = "extra_image";
    public static Bitmap bitmap, filteredBitmap, finalBitmap;
    FilterImageFragment filterImageFragment;
    EditImageFragment editImageFragment;
    private static int RESULT_LOAD_IMAGE = 1;
    private ConstraintLayout consEditor;
    public static String fileName;
    ImageButton ibEmoji, ibBrush;

    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float contrastFinal = 1.0f;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        photoEditorView = findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .build();
        ActionBar actionBar = getSupportActionBar();
        TabLayout editorTabLayout = findViewById(R.id.tab_view);
        ViewPager editorViewPager = findViewById(R.id.viewpager);
        ibBrush = findViewById(R.id.ibBrush);
        ibEmoji = findViewById(R.id.ibEmoji);
        consEditor = findViewById(R.id.ConsEditor);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Edit Image");
        }

        if (getIntent().getStringExtra(IMAGE__EXTRA) != null) {
            try {
                fileName = getIntent().getStringExtra(IMAGE__EXTRA);
                FileInputStream is = this.openFileInput(fileName);
                Bitmap temp = BitmapFactory.decodeStream(is);
                loadImage(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (getIntent().getStringExtra(SELECTED__EXTRA) != null) {
            try {
                fileName = getIntent().getStringExtra(SELECTED__EXTRA);
                FileInputStream is = this.openFileInput(fileName);
                Bitmap temp = BitmapFactory.decodeStream(is);
                loadImage(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ibEmoji.setOnClickListener(emojiListener);
        setupViewPager(editorViewPager);
        editorTabLayout.setupWithViewPager(editorViewPager);
    }

    private View.OnClickListener emojiListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private void loadImage(Bitmap temp) {
        bitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
        filteredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        finalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(bitmap);
    }

    private void setupViewPager(ViewPager editorViewPager) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bundle b = new Bundle();
        b.putByteArray("image", byteArray);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        filterImageFragment = new FilterImageFragment();
        filterImageFragment.setListener(this);
        filterImageFragment.setArguments(b);
        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);
        adapter.addFragment(filterImageFragment, getString(R.string.filters));
        adapter.addFragment(editImageFragment, getString(R.string.adjust));
        editorViewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal = brightness;
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onContrastChanged(float contrast) {
        contrastFinal = contrast;
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(contrast));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditComplete() {
        Bitmap map = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        filter.addSubFilter(new ContrastSubFilter(contrastFinal));
        filter.addSubFilter(new SaturationSubfilter(saturationFinal));
        finalBitmap = filter.processFilter(map);
    }

    @Override
    public void onFilterselected(Filter filter) {
        resetControl();
        filteredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    private void resetControl() {
        if (editImageFragment != null){
            editImageFragment.reset();
        }
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        contrastFinal = 1.0f;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.option_change) {
            openImage();
            return true;
        } else if (item.getItemId() == R.id.option_save) {
            saveImage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            try{
                                final String path = BitmapUtils.insertImage(getContentResolver(), finalBitmap, System.currentTimeMillis()+"_img.jpg", null);
                                if (!TextUtils.isEmpty(path)){
                                    Snackbar snackbar = Snackbar.make(consEditor, "Image Saved", Snackbar.LENGTH_LONG)
                                            .setAction("OPEN", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.setDataAndType(Uri.parse(path), "image/*");
                                                    startActivity(intent);
                                                }
                                            });
                                    snackbar.show();
                                }
                                else {
                                    Snackbar snackbar = Snackbar.make(consEditor, "Unable to Save Image", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Toast.makeText(EditorActivity.this, getString(R.string.gallery_denied), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).check();
    }

    private void openImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, RESULT_LOAD_IMAGE);
                        }
                        else{
                            Toast.makeText(EditorActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            Bitmap bit = BitmapUtils.getBitmapFromGallery(this, Objects.requireNonNull(data).getData(),800, 800);
            bitmap.recycle();
            finalBitmap.recycle();
            filteredBitmap.recycle();
            bitmap = bit.copy(Bitmap.Config.ARGB_8888, true);
            filteredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            photoEditorView.getSource().setImageBitmap(bitmap);
            bit.recycle();
            filterImageFragment.displayThubnail(bitmap);
            Toast.makeText(EditorActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
