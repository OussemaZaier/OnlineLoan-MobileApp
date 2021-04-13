package com.example.pfe;

import android.app.Application;

public class CIN extends Application {


    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    private String CIN;
}
