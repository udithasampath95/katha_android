package com.kbase.katha;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbase.katha.model.Story;
import com.kbase.katha.sharedpreferences.SharedPreference;


import java.util.ArrayList;
import java.util.List;

public class StoryReadActivity extends AppCompatActivity {
    Story story;
    private ImageView imageView;
    private TextView titleTextView, contentTextView, uploadedByTextView, uploadedDateTextView, textShare;
    ImageButton saveButton, removeSave;
    private ArrayList<Story> Listtasks;
    String imagePath;
    private String singlishName;
    private String save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_read);

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


            }
        });
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
