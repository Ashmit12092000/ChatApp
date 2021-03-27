package com.example.letschat.Models;

public class User {
    public String userID;
    public String display_name;
    public String profile_pic;
    public String email;
    public String phone;
    public String age;
    public String gender;
    public String status="";
    public User(String userID, String display_Name, String profile_pic,String email,String phone,String age,String gender,String status) {
        this.userID = userID;
        this.display_name = display_Name;
        this.profile_pic = profile_pic;
        this.email=email;
        this.phone=phone;
        this.age=age;
        this.gender=gender;
        this.status=status;
    }
    public User(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setdisplay_name(String display_Name) {
        display_name = display_Name;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
