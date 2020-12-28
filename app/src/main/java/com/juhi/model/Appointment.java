package com.juhi.model;

import java.util.Date;
import java.util.List;

public class Appointment {
    private String email;
    private String number;
    private String address;
    private Date date;
    private String timeSlot;
    private String time;
    private String username;
    private String path;
    private int status_code;
    private String total_amount;
    private String uniqueId;
    private List<Product> selected_Products;
    private String dateMonth;
    private boolean departureDate;

    public boolean isDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(boolean departureDate) {
        this.departureDate = departureDate;
    }

    public String getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(String dateMonth) {
        this.dateMonth = dateMonth;
    }

    private int orderId;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    private boolean isBeautican;

    public boolean isBeautican() {
        return isBeautican;
    }

    public void setBeautican(boolean beautican) {
        isBeautican = beautican;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public List<Product> getSelected_Products() {
        return selected_Products;
    }

    public void setSelected_Products(List<Product> selected_Products) {
        this.selected_Products = selected_Products;
    }
}
