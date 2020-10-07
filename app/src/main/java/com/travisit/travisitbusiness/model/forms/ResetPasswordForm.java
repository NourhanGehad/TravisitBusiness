package com.travisit.travisitbusiness.model.forms;


import java.io.Serializable;

public class ResetPasswordForm implements Serializable {

    private String resetCode;
    private String password;

    public ResetPasswordForm(String resetCode, String password) {
        this.resetCode = resetCode;
        this.password = password;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
