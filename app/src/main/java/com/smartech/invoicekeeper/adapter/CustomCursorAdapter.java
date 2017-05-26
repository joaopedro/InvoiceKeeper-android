package com.smartech.invoicekeeper.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class CustomCursorAdapter extends SimpleCursorAdapter{
    protected int[] mTo;
    protected int[] mFrom;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mTo = to;
        findColumns(c, from);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        final ViewBinder binder = super.getViewBinder();
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound) {
                    String text = cursor.getString(from[i]);
                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        renderImage(text, (ImageView) v,context);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleCursorAdapter");
                    }
                }
            }
        }
    }

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    private void renderImage(String imageFile, ImageView invoiceImageView, Context context) {
        if( imageFile != null && !TextUtils.isEmpty(imageFile)){
            try {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeStream(context.openFileInput(imageFile));
                invoiceImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException();
            }
        }
    }


}