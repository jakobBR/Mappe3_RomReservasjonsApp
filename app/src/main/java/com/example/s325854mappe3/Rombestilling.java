package com.example.s325854mappe3;

public class Rombestilling {


    public String getTil() {
        return til;
    }

    public void setTil(String til) {
        this.til = til;
    }

    public String getFra() {
        return fra;
    }

    public void setFra(String fra) {
        this.fra = fra;
    }

    public Rombestilling(String til, String fra) {
        this.til = til;
        this.fra = fra;
    }

    public String til;
    public String fra;

}
