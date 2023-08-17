package com.wisepotato.wp_app;

public class queryResultModel {



    private String thumbnailPath;
    private int nodeId;
    private String PostTitle;
    private String PostAuthor;


    public queryResultModel(int nodeId , String thumbnailPath, String postTitle, String postAuthor ) {
        this.thumbnailPath = thumbnailPath;
        this.nodeId = nodeId;
        PostTitle = postTitle;
        PostAuthor = postAuthor;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
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


}
