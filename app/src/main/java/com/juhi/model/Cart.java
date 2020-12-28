package com.juhi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Cart implements Serializable {
    private Date currentDate;
    private List<Product> selectedProducts;

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }
}
