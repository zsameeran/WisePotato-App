package com.wisepotato.wp_app;

public class modelPost {

    private String postTitle , imageName , parentName ,tag1, tag2 ,tag3, tag4, tag5 , dateTimeStamp , author;
    private int nodeId , parentId ,postUpvotes;



    public modelPost(String postTitle, String imageName, String parentName
            , String tag1, String tag2, String tag3, String tag4, String tag5, String author, String dateTimeStamp,
                     int nodeId, int parentId , int postUpvotes) {
        this.postTitle = postTitle;
        this.imageName = imageName;
        this.parentName = parentName;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.author = author;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.tag5 = tag5;
        this.dateTimeStamp = dateTimeStamp;
        this.nodeId = nodeId;
        this.parentId = parentId;
        this.postUpvotes = postUpvotes;
    }

    public modelPost(String postTitle, String imageName, String parentName,
                     String tag1, String tag2, String tag3, String tag4, String tag5,String author,
                     int nodeId, int parentId ,int postUpvotes) {
        this.postTitle = postTitle;
        this.imageName = imageName;
        this.parentName = parentName;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.author = author;
        this.tag5 = tag5;
        this.nodeId = nodeId;
        this.parentId = parentId;
        this.postUpvotes = postUpvotes;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getImageName() {
        return imageName;
    }



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTag4() {
        return tag4;
    }

    public void setTag4(String tag4) {
        this.tag4 = tag4;
    }

    public String getTag5() {
        return tag5;
    }

    public void setTag5(String tag5) {
        this.tag5 = tag5;
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    public int getPostUpvotes() {
        return postUpvotes;
    }

    public void setPostUpvotes(int postUpvotes) {
        this.postUpvotes = postUpvotes;
    }
}
