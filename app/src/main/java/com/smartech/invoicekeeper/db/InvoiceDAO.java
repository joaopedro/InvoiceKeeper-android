package com.smartech.invoicekeeper.db;

import android.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by jpedro on 21/05/2017.
 */

public class InvoiceDAO {

    private long id;
    private String title;
    private String date;
    private String warrantyPeriod;
    private String type;
    private String imageFile;

    public InvoiceDAO(long id, String title, String date, String warrantyPeriod, String type, String imageFile){
        this.id = id;
        this.title = title;
        this.type = type;
        this.warrantyPeriod = warrantyPeriod;
        this.date = date;
        this.imageFile = imageFile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

}
