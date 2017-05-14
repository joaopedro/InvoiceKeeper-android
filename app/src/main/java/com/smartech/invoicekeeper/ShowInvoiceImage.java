package com.smartech.invoicekeeper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.FileNotFoundException;

public class ShowInvoiceImage extends AppCompatActivity {

    private String imagePath;
    private ImageView bigImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoice_image);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra(AddInvoiceActivity.IMAGE_FILE_EXTRA);

        bigImageView = (ImageView) findViewById(R.id.bigInvoiceImageView);

        if(!TextUtils.isEmpty(imagePath)){
            try {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeStream(openFileInput(imagePath));
                bigImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException();
            }
        }
    }
}
