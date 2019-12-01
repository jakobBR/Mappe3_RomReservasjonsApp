package com.example.s325854mappe3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rom implements Serializable {

    public String navn ;
    public String beskrivelse;
    public String latitude;
    public String longitude;
    public List<Rombestilling> romBestillinger = new ArrayList<>();

    public Rom(String navn, String beskrivelse, String latitude, String longitude) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Rom(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
