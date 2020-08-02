package com.example.cs446_group8.ui.project_details.crop_timelapse;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.emergency.EmergencyNumber;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.R;
import com.viewpagerindicator.LinePageIndicator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class timelapseActivity extends AppCompatActivity {

    String currentImagePath = null;
    private static final int IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION = 101;
    File cropsFolder;
    File parentFolder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelapse);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(timelapseActivity.this, permissions)) {
            ActivityCompat.requestPermissions(timelapseActivity.this, permissions, STORAGE_PERMISSION);
        } else{
            parentFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Crops");
            parentFolder.mkdir();

            String cropName = null;
            Bundle extras = getIntent().getExtras();
            cropName = null;
            if (extras != null) {
                cropName = extras.getString("cropName");
            }

            TextView titlebar = findViewById(R.id.title_bar);
            titlebar.setText(cropName + " Gallery");

            //Ensuring crop folder/album is created
            cropsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Crops/" + cropName);
            cropsFolder.mkdir();


            final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);
            final File[] listOfFiles = cropsFolder.listFiles();

            assert listOfFiles != null;
            Arrays.sort(listOfFiles, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

            final ViewPager viewPager = findViewById(R.id.image1);
            ImageAdapter adapter = new ImageAdapter(this, listOfFiles);
            viewPager.setAdapter(adapter);

            final LinePageIndicator linePageIndicator = findViewById(R.id.indicator);
            linePageIndicator.setViewPager(viewPager);

            refreshLayout.setOnRefreshListener(() -> {
                //finish();
                startActivity(getIntent());
                //viewPager.setCurrentItem(listOfFiles.length-1);
            });

            ImageView backBtn = findViewById(R.id.back_button);
            backBtn.setOnClickListener(view -> onBackPressed());

            ImageView uploadBtn = findViewById(R.id.uploadBtn);
            String finalCropName = cropName;
            uploadBtn.setOnClickListener(view -> {
                if (isExternalStorageReadable() && isExternalStorageWritable()) {
                    takePhotoIntent(finalCropName);
                } else {
                    //storage is full
                }
            });
        }
    }

    private static boolean hasPermissions(Context context, String... permissions){
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
    }

    public void takePhotoIntent(String cropName){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            File imageFile = null;
            try {
                imageFile = getImageFile(cropName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(imageFile != null){
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, IMAGE_REQUEST);
            }
        }
    }

    private File getImageFile(String cropName) throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Crops/" + cropName);
        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }




}