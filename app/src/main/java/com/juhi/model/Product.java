package com.juhi.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    private String mTitle;
    private String mActualPrice;
    private int mCount;
    private String mOriginalPrice;
    private String mOfferPercentage;
    private boolean isBeautican;

    public String getmActualPrice() {
        return mActualPrice;
    }

    public void setmActualPrice(String mActualPrice) {
        this.mActualPrice = mActualPrice;
    }

    public boolean isBeautican() {
        return isBeautican;
    }

    public void setBeautican(boolean beautican) {
        isBeautican = beautican;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public String getmOriginalPrice() {
        return mOriginalPrice;
    }

    public void setmOriginalPrice(String mOriginalPrice) {
        this.mOriginalPrice = mOriginalPrice;
    }

    public String getmOfferPercentage() {
        return mOfferPercentage;
    }

    public void setmOfferPercentage(String mOfferPercentage) {
        this.mOfferPercentage = mOfferPercentage;
    }
}
