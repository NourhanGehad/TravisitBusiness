package com.travisit.travisitbusiness.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Business implements Serializable {
    @SerializedName("token")
    private String token = null;
    @SerializedName("id")
    private Integer id = null;
    @SerializedName("name")
    private String name = null;
    @SerializedName("email")
    private String email = null;
    @SerializedName("logo")
    private String logo = null;
    @SerializedName("passwordResetCode")
    private String passwordResetCode = null;
    @SerializedName("approvementStatus")
    private String approvementStatus = null;
    @SerializedName("governmentIssuedNumber")
    private String governmentIssuedNumber = null;
    @SerializedName("governmentIssuedNumberImage")
    private String governmentIssuedNumberImage = null;
    @SerializedName("categories")
    private ArrayList<Category> businessesCategories = null;
    @SerializedName("offersCount")
    private Integer offersCount = null;
    @SerializedName("branchesCount")
    private Integer branchesCount = null;

    public Business(String token, Integer id, String name, String email, String logo, String approvementStatus, String governmentIssuedNumber, String governmentIssuedNumberImage, ArrayList<Category> businessesCategories) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.logo = logo;
        this.approvementStatus = approvementStatus;
        this.governmentIssuedNumber = governmentIssuedNumber;
        this.governmentIssuedNumberImage = governmentIssuedNumberImage;
        this.businessesCategories = businessesCategories;
    }

    public Business(String token) {
        this.token = token;
    }

    public Business(String token, String name, String email) {
        this.token = token;
        this.name = name;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public String getApprovementStatus() {
        return approvementStatus;
    }

    public void setApprovementStatus(String approvementStatus) {
        this.approvementStatus = approvementStatus;
    }

    public String getGovernmentIssuedNumber() {
        return governmentIssuedNumber;
    }

    public void setGovernmentIssuedNumber(String governmentIssuedNumber) {
        this.governmentIssuedNumber = governmentIssuedNumber;
    }

    public String getGovernmentIssuedNumberImage() {
        return governmentIssuedNumberImage;
    }

    public void setGovernmentIssuedNumberImage(String governmentIssuedNumberImage) {
        this.governmentIssuedNumberImage = governmentIssuedNumberImage;
    }

    public ArrayList<Category> getBusinessesCategories() {
        return businessesCategories;
    }

    public void setBusinessesCategories(ArrayList<Category> businessesCategories) {
        this.businessesCategories = businessesCategories;
    }

    public Integer getOffersCount() {
        return offersCount;
    }

    public void setOffersCount(Integer offersCount) {
        this.offersCount = offersCount;
    }

    public Integer getBranchesCount() {
        return branchesCount;
    }

    public void setBranchesCount(Integer branchesCount) {
        this.branchesCount = branchesCount;
    }
}
