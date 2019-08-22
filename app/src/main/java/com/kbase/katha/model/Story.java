package com.kbase.katha.model;

public class Story {
    private String storyId;
    private String storyTitleSinhala;
    private String storyTitleSinglish;
    private String storyContent;
    private String storyUploadedDate;
    private String storyUploadedBy;

    public Story() {
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getStoryTitleSinhala() {
        return storyTitleSinhala;
    }

    public void setStoryTitleSinhala(String storyTitleSinhala) {
        this.storyTitleSinhala = storyTitleSinhala;
    }

    public String getStoryTitleSinglish() {
        return storyTitleSinglish;
    }

    public void setStoryTitleSinglish(String storyTitleSinglish) {
        this.storyTitleSinglish = storyTitleSinglish;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    public String getStoryUploadedDate() {
        return storyUploadedDate;
    }

    public void setStoryUploadedDate(String storyUploadedDate) {
        this.storyUploadedDate = storyUploadedDate;
    }

    public String getStoryUploadedBy() {
        return storyUploadedBy;
    }

    public void setStoryUploadedBy(String storyUploadedBy) {
        this.storyUploadedBy = storyUploadedBy;
    }
}