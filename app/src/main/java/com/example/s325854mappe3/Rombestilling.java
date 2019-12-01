package com.example.s325854mappe3;

import java.io.Serializable;

public class Rombestilling implements Serializable {


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

    public Rombestilling() {
    }

    public String til;
    public String fra;

}
