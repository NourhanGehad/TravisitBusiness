package com.travisit.travisitbusiness.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Offer implements Serializable {
    Integer id = null;
    String title;
    String description;
    String startDate;
    String endDate;
    ArrayList<Integer> tags = null;
    Integer branchID = null;
    String firstImage = null;
    String secondImage = null;
    String thirdImage = null;

    public Offer(Integer id, String title, String description, String startDate, String endDate, ArrayList<Integer> tags, Integer branchID, String firstImage, String secondImage, String thirdImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
        this.branchID = branchID;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
    }

    public Offer(int id, String title, String description, String startDate, String endDate, String firstImage, String secondImage, String thirdImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
    }
//TODO REMOVE BELOW
    public Offer(int id, String title, String description, String startDate, String endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Offer(String title, String description, String startDate, String endDate, ArrayList<Integer> tags, Integer branchID) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
        this.branchID = branchID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Integer> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Integer> tags) {
        this.tags = tags;
    }

    public Integer getBranchID() {
        return branchID;
    }

    public void setBranchID(Integer branchID) {
        this.branchID = branchID;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getSecondImage() {
        return secondImage;
    }

    public void setSecondImage(String secondImage) {
        this.secondImage = secondImage;
    }

    public String getThirdImage() {
        return thirdImage;
    }

    public void setThirdImage(String thirdImage) {
        this.thirdImage = thirdImage;
    }
}
