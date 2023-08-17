package com.wisepotato.wp_app;



public class savedPostsModel {
    private String thumbnailPath;
    private String PostTitle;
    private String PostAuthor;
    private int postsUpvotes;



    private int postNodeId;

    public savedPostsModel(String thumbnailPath, String postTitle, String postAuthor, int postsUpvotes , int postNodeId) {
        this.thumbnailPath = thumbnailPath;
        PostTitle = postTitle;
        this.postNodeId = postNodeId;
        PostAuthor = postAuthor;
        this.postsUpvotes = postsUpvotes;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public String getPostAuthor() {
        return PostAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        PostAuthor = postAuthor;
    }

    public int getPostsUpvotes() {
        return postsUpvotes;
    }

    public void setPostsUpvotes(int postsUpvotes) {
        this.postsUpvotes = postsUpvotes;
    }
    public int getPostNodeId() {
        return postNodeId;
    }

    public void setPostNodeId(int postNodeId) {
        this.postNodeId = postNodeId;
    }
}
