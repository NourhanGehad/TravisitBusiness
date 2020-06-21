package com.travisit.travisitbusiness.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Category  implements Serializable {
    Integer id = null;
    String name;
    Boolean isSelected = false;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public static Comparator<Category> categoryComparator = new Comparator<Category>() {
        @Override
        public int compare(Category c1, Category c2) {
            String category1Name = c1.getName().toUpperCase();
            String category2Name = c2.getName().toUpperCase();

            return category1Name.compareTo(category2Name);
        }
    };
}
