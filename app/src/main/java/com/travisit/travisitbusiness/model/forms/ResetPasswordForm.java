package com.travisit.travisitbusiness.model.forms;


public class ResetPasswordForm{

    private String resetCode;
    private String password;

    public ResetPasswordForm(String resetCode, String password) {
        this.resetCode = resetCode;
        this.password = password;
    }
}
