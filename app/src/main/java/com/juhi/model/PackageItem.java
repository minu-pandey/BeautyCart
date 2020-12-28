package com.juhi.model;

public class PackageItem {
    private int resourceId;
    private String packageTitle;
    private String collectionName;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public PackageItem(int resourceId, String packageTitle, String collectionName) {
        this.resourceId = resourceId;
        this.packageTitle = packageTitle;
        this.collectionName = collectionName;
    }
}
