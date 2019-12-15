package com.example.cgram;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cgram.utils.BitmapUtils;

import java.io.FileOutputStream;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int GALLERY_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button takePicture = findViewById(R.id.btnTake);
        Button selectPicture = findViewById(R.id.btnSelect);
        takePicture.setOnClickListener(takePictureListener);
        selectPicture.setOnClickListener(selectPictureListener);
    }

    private View.OnClickListener takePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    };

    private View.OnClickListener selectPictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, getString(R.string.granted), Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.denied), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, getString(R.string.gallery_granted), Toast.LENGTH_SHORT).show();
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.gallery_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String filename =  ts+".jpeg";
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap photo = (Bitmap) (data).getExtras().get("data");
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();
                photo.recycle();
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(EditorActivity.IMAGE__EXTRA, filename);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "Image taken", Toast.LENGTH_SHORT).show();
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap picked = BitmapUtils.getBitmapFromGallery(this, data.getData(),800, 800);
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                picked.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();
                picked.recycle();
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(EditorActivity.SELECTED__EXTRA, filename);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainActivity.this, getString(R.string.press_exit), Toast.LENGTH_SHORT).show();
    }
}
