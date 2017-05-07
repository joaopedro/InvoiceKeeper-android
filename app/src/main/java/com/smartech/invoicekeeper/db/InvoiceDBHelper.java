package com.smartech.invoicekeeper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InvoiceOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "invoice.db";

    private static final String SQL_CREATE_INVOICE =
            "CREATE TABLE " + InvoiceContract.Invoice.TABLE_NAME + " (" +
                    InvoiceContract.Invoice.COLUMN_NAME_TITLE + " TEXT, " +
                    InvoiceContract.Invoice.COLUMN_NAME_TYPE + " TEXT, " +
                    InvoiceContract.Invoice.COLUMN_NAME_WARRANTY_PERIOD + " INTEGER, " +
                    InvoiceContract.Invoice.COLUMN_NAME_IMAGE_FILE + " TEXT, " +
                    InvoiceContract.Invoice.COLUMN_NAME_DATE + " TEXT);";

    private static final String SQL_DELETE_INVOICES =
            "DROP TABLE IF EXISTS " + InvoiceContract.Invoice.TABLE_NAME;

    InvoiceOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INVOICE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_INVOICES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}