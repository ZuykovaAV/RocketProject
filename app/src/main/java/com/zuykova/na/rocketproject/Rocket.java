package com.zuykova.na.rocketproject;

import java.util.UUID;

public class Rocket {
    private UUID mId;
    private String mRocketName;
    private Long mRocketTime;
    private String mRocketIcon;
    private String mRocketDesc;
    private String mArticleLink;

    public Rocket(){
        mId =UUID.randomUUID();
}

    public UUID getId() {
        return mId;
    }

    public String getRocketName() {
        return mRocketName;
    }

    public void setRocketName(String rocketName) {
        mRocketName = rocketName;
    }

    public Long getRocketTime() {
        return mRocketTime;
    }

    public void setRocketTime(Long rocketTime) {
        mRocketTime = rocketTime;
    }

    public String getRocketIcon() {
        return mRocketIcon;
    }

    public void setRocketIcon(String rocketIcon) {
        mRocketIcon = rocketIcon;
    }

    public String getRocketDesc() {
        return mRocketDesc;
    }

    public void setRocketDesc(String rocketDesc) {
        mRocketDesc = rocketDesc;
    }

    public String getArticleLink() {
        return mArticleLink;
    }

    public void setArticleLink(String articleLink) {
        mArticleLink = articleLink;
    }
}
