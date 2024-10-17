package com.videoaianalyzier.video_ai_analyzer.model;

import java.util.List;

public class User {
    private String name;
    private String username;

    private String email;

    private String password;

    private String confirmPassword;
    private List<VideoIdea> videoIdeas;

    private int tokens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<VideoIdea> getVideoIdeas() {
        return videoIdeas;
    }

    public void setVideoIdeas(List<VideoIdea> videoIdeas) {
        this.videoIdeas = videoIdeas;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
}
