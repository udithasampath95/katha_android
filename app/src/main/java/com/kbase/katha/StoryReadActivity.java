package com.kbase.katha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbase.katha.model.Story;
import com.kbase.katha.sharedpreferences.SharedPreference;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryReadActivity extends AppCompatActivity {
    Story story;
    private ImageView imageView;
    private TextView titleTextView, contentTextView, uploadedByTextView, uploadedDateTextView, textShare;
    ImageButton saveButton, removeSave;
    private ArrayList<Story> Listtasks;
    String imagePath;
    private String singlishName;
    private String save;
    private Bitmap bitmap;
    private String photoPath;


    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int EXTERNAL_PERMISSION_REQUEST_CODE = 123;
    private int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_read);

        initAd();
        getExtras();
        initViews();
        setValues();
        setListners();

        if (checkSave(story)) {
            saveButton.setVisibility(View.GONE);
        } else {
            removeSave.setVisibility(View.GONE);
        }
    }

    private void initAd() {
        StartAppSDK.init(this, "208604706", true);
        StartAppAd.showAd(this);
    }

    public boolean checkSave(Story story) {
        SharedPreference sharedPreference = new SharedPreference();
        boolean check = false;
        String name = story.getStoryId();
        List<Story> favorites = sharedPreference.getFavorites(StoryReadActivity.this);
        if (favorites == null) {
            check = false;
        } else {
            for (int i = 0; i < favorites.size(); i++) {
                if (favorites.get(i).getStoryId().equalsIgnoreCase(name)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    private void setListners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    SharedPreference sharedPreference = new SharedPreference();
                    List<Story> favorites = sharedPreference.getFavorites(StoryReadActivity.this);
                    Story stories = new Story();
                    stories.setStoryId(story.getStoryId());
                    stories.setStoryTitleSinhala(titleTextView.getText().toString());
                    stories.setStoryContent(contentTextView.getText().toString());
                    stories.setStoryUploadedBy(uploadedByTextView.getText().toString());
                    stories.setStoryUploadedDate(uploadedDateTextView.getText().toString());
                    stories.setImagePath(imagePath);
                    stories.setStoryTitleSinglish(singlishName);
                    sharedPreference.addFavorite(StoryReadActivity.this, stories);
                    Toast.makeText(StoryReadActivity.this, "Story saved", Toast.LENGTH_SHORT).show();
                    removeSave.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);

            }
        });

        removeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String title = story.getStoryId();
                    SharedPreference sharedPreference = new SharedPreference();
                    List<Story> favorites = sharedPreference.getFavorites(StoryReadActivity.this);
                    for (int i = 0; i < favorites.size(); i++) {
                        if (favorites.get(i).getStoryId().equalsIgnoreCase(title)) {

                            sharedPreference.removeFavorite(StoryReadActivity.this,
                                    favorites.get(i));
                            removeSave.setVisibility(View.GONE);
                            saveButton.setVisibility(View.VISIBLE);
                            Toast.makeText(StoryReadActivity.this, "Story removed", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });

        textShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = getBitmapFromView(imageView);
                if (bitmap != null) {
                    getLocationPermission();
                }
            }
        });
    }

    private void getLocationPermission() {
        String[] permissions = {WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(StoryReadActivity.this,
                WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startShare(bitmap);
        } else {
            ActivityCompat.requestPermissions(StoryReadActivity.this,
                    permissions,
                    EXTERNAL_PERMISSION_REQUEST_CODE);
            Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }


    private void startShare(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        shareScreenshot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case EXTERNAL_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            startShare(bitmap);
//                            return;
                        } else {
                            startShare(bitmap);
                        }
                    }
                } else {
                    Toast.makeText(this, "Permission denied to read your external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void shareScreenshot() {
        photoPath = Environment.getExternalStorageDirectory() + "/saved_images" + "/Image-" + n + ".jpg";
        File F = new File(photoPath);
        //Uri U = Uri.fromFile(F);
        //  Uri U = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".my.package.name.provider", F);

        // TODO your package name as well add .fileprovider
        Uri U = FileProvider.getUriForFile(this, "com.kbase.katha.fileprovider", F);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        i.putExtra(Intent.EXTRA_COMPONENT_NAME, "aaaaa");
        i.putExtra(Intent.EXTRA_TEXT, titleTextView.getText().toString() + " \n " + contentTextView.getText().toString().substring(25) + " \n " + "" + "\n\n" + " https://play.google.com/store/apps/details?id=com.kbase.katha&hl=en");
        i.putExtra(Intent.EXTRA_STREAM, U);
        startActivityForResult(Intent.createChooser(i, "share via"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setValues() {
        singlishName = story.getStoryTitleSinglish();
        textShare = findViewById(R.id.textShare);
        titleTextView.setText(story.getStoryTitleSinhala());
        contentTextView.setText(story.getStoryContent());
        uploadedByTextView.setText("Uploaded By: " + story.getStoryUploadedBy());
        uploadedDateTextView.setText("Uploaded Date: " + story.getStoryUploadedDate());
        imagePath = story.getImagePath();
        Glide.with(this)
                .asBitmap()
                .load(story.getImagePath())
                .into(imageView);
    }

    private void initViews() {
        saveButton = findViewById(R.id.saveButton);
        removeSave = findViewById(R.id.saveRemoveButton);
        imageView = findViewById(R.id.image);
        titleTextView = findViewById(R.id.storyTitle);
        contentTextView = findViewById(R.id.storyContetnt);
        uploadedByTextView = findViewById(R.id.storyAddedBy);
        uploadedDateTextView = findViewById(R.id.storyAddedDate);
    }

    private void getExtras() {
        story = (Story) getIntent().getExtras().getSerializable("story");
        save = getIntent().getExtras().getString("save");
    }

}
