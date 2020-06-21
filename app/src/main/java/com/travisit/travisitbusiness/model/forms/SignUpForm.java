package com.travisit.travisitbusiness.model.forms;

import java.io.Serializable;

public class SignUpForm extends EmailForm {
    private String name;
    private String password;

    public SignUpForm(String name, String email, String password) {
        super(email);
        this.name = name;
        this.password = password;
    }
}
