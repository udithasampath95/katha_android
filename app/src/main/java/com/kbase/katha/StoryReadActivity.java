package com.kbase.katha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.kbase.katha.model.Story;

public class StoryReadActivity extends AppCompatActivity {
    Story story;
    private TextView titleTextView, contentTextView, uploadedByTextView, uploadedDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_read);
        getExtras();
        initViews();
        setValues();
    }

    private void setValues() {
        titleTextView.setText(story.getStoryTitleSinhala());
        contentTextView.setText(story.getStoryContent());
        uploadedByTextView.setText("Uploaded By: " + story.getStoryUploadedBy());
        uploadedDateTextView.setText("Uploaded Date: " + story.getStoryUploadedDate());
    }

    private void initViews() {
        titleTextView = findViewById(R.id.storyTitle);
        contentTextView = findViewById(R.id.storyContetnt);
        uploadedByTextView = findViewById(R.id.storyAddedBy);
        uploadedDateTextView = findViewById(R.id.storyAddedDate);
    }

    private void getExtras() {
        story = (Story) getIntent().getExtras().getSerializable("story");
    }

}
