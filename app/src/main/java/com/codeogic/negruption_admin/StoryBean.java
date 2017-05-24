package com.codeogic.negruption_admin;

import java.io.Serializable;

/**
 * Created by Admin on 24-05-2017.
 */

public class StoryBean implements Serializable {
    int UserId;
    int storyId;
    String storyTitle;
    String department;
    String place;
    String storyDesc;
    String imageProof;
    String audioProof;
    String videoProof;
    int views;
    String username;
    String privacy;

    public StoryBean() {
    }

    public StoryBean(int userId, int storyId, String storyTitle, String department, String place, String storyDesc, String imageProof, String audioProof, String videoProof, int views, String username, String privacy) {
        UserId = userId;
        this.storyId = storyId;
        this.storyTitle = storyTitle;
        this.department = department;
        this.place = place;
        this.storyDesc = storyDesc;
        this.imageProof = imageProof;
        this.audioProof = audioProof;
        this.videoProof = videoProof;
        this.views = views;
        this.username = username;
        this.privacy = privacy;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStoryDesc() {
        return storyDesc;
    }

    public void setStoryDesc(String storyDesc) {
        this.storyDesc = storyDesc;
    }

    public String getImageProof() {
        return imageProof;
    }

    public void setImageProof(String imageProof) {
        this.imageProof = imageProof;
    }

    public String getAudioProof() {
        return audioProof;
    }

    public void setAudioProof(String audioProof) {
        this.audioProof = audioProof;
    }

    public String getVideoProof() {
        return videoProof;
    }

    public void setVideoProof(String videoProof) {
        this.videoProof = videoProof;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    @Override
    public String toString() {
        return "StoryBean{" +
                "UserId=" + UserId +
                ", storyId=" + storyId +
                ", storyTitle='" + storyTitle + '\'' +
                ", department='" + department + '\'' +
                ", place='" + place + '\'' +
                ", storyDesc='" + storyDesc + '\'' +
                ", imageProof='" + imageProof + '\'' +
                ", audioProof='" + audioProof + '\'' +
                ", videoProof='" + videoProof + '\'' +
                ", views=" + views +
                ", username='" + username + '\'' +
                ", privacy='" + privacy + '\'' +
                '}';
    }
}
