package com.kbase.katha.model;

public class Customer {
    private String name;
    private String des;

    public Customer(String name, String des) {
        this.name = name;
        this.des = des;
    }

    public Customer() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
