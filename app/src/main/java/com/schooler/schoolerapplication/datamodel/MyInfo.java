package com.schooler.schoolerapplication.datamodel;

import io.realm.RealmObject;

/**
 * Created by boxfox on 2017-02-04.
 */
public class MyInfo extends RealmObject {
    public static final String BIRTHDAY = "생년월일";
    public static final String SCHOOL = "재학중인 학교";
    public static final String PHONE = "전화번호";
    public static final String SUBJECT = "학과 및 학급";

    private String profileImage;
    private String birthday;
    private String school;
    private String phone;
    private String subject;

    private String sessionKey;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
