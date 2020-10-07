package com.travisit.travisitbusiness.model.forms;

import java.util.ArrayList;

public class EditProfileForm/* extends EmailForm*/ {
    private String name;
    private String governmentIssuedNumber;
    private ArrayList<Integer> businessesCategories;

    public EditProfileForm(String name, String email, String governmentIssuedNumber, ArrayList<Integer> categories) {
       // super(email);
        this.name = name;
        this.governmentIssuedNumber = governmentIssuedNumber;
        this.businessesCategories = categories;
    }
}
