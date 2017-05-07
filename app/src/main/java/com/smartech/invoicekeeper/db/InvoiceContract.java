package com.smartech.invoicekeeper.db;

import android.provider.BaseColumns;

public final class InvoiceContract {
    // To prevent someone from accidentally instantiating the Invoice class,
    // make the constructor private.
    private InvoiceContract() {}

    /* Inner class that defines the table contents */
    public static class Invoice implements BaseColumns {
        public static final String TABLE_NAME = "invoice";

        private static final String COLUMN_NAME_TITLE = "title";
        private static final String COLUMN_NAME_TYPE = "type";
        private static final String COLUMN_NAME_WARRANTY_PERIOD = "warranty_period";
        private static final String COLUMN_NAME_DATE = "date";
        private static final String COLUMN_NAME_IMAGE_FILE = "image_file";

    }
}
