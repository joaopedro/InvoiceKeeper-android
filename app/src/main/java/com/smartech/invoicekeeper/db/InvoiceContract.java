package com.smartech.invoicekeeper.db;

import android.provider.BaseColumns;

public final class InvoiceContract {
    // To prevent someone from accidentally instantiating the Invoice class,
    // make the constructor private.
    private InvoiceContract() {}

    /* Inner class that defines the table contents */
    public static class Invoice implements BaseColumns {
        public static final String TABLE_NAME = "invoice";

        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_WARRANTY_PERIOD = "warranty_period";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_IMAGE_FILE = "image_file";

    }


}
