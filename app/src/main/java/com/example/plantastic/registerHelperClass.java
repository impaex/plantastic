package com.example.plantastic;

public class registerHelperClass {

    String firstnameString, lastnameString,emailString,password1String,password2String;

    public registerHelperClass(String firstnameString, String lastnameString, String emailString, String password1String, String password2String) {
        this.firstnameString = firstnameString;
        this.lastnameString = lastnameString;
        this.emailString = emailString;
        this.password1String = password1String;
        this.password2String = password2String;
    }

    public registerHelperClass(){}

    public String getFirstnameString() {
        return firstnameString;
    }

    public void setFirstnameString(String firstnameString) {
        this.firstnameString = firstnameString;
    }

    public String getLastnameString() {
        return lastnameString;
    }

    public void setLastnameString(String lastnameString) {
        this.lastnameString = lastnameString;
    }

    public String getEmailString() {
        return emailString;
    }

    public void setEmailString(String emailString) {
        this.emailString = emailString;
    }

    public String getPassword1String() {
        return password1String;
    }

    public void setPassword1String(String password1String) {
        this.password1String = password1String;
    }

    public String getPassword2String() {
        return password2String;
    }

    public void setPassword2String(String password2String) {
        this.password2String = password2String;
    }
}
