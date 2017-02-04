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

    private String name;
    private String profileImage;
    private String birthday;
    private String school;
    private String phone;
    private String subject;
    private String nfc;

    private String sessionKey;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSubject() {
        if(subject==null)
            setSubject("");
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPhone() {

        if(phone==null)
            setPhone("");
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchool() {
        if(school==null)
        setSchool("");
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBirthday() {
        if(birthday==null)
            setBirthday("");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }
}
