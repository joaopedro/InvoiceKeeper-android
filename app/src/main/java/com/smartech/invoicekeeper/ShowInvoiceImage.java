package com.smartech.invoicekeeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.smartech.invoicekeeper.MainActivity.DBID;

public class ShowInvoiceImage extends AppCompatActivity {

    public static final String IMAGE_FILE_EXTRA = "imageFile";
    private String imagePath;
    private ImageView bigImageView;
    private static final int SELECT_PICTURE = 2;
    private String imageFile;
    private boolean imageChanged;
    private long dbid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoice_image);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra(AddInvoiceActivity.IMAGE_FILE_EXTRA);
        dbid = intent.getLongExtra(DBID, 0);

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

    @Override
    public void onBackPressed() {
        if(imageChanged && dbid > 0){
            Intent intent = new Intent(this, AddInvoiceActivity.class);
            intent.putExtra(DBID, dbid);
            intent.putExtra(IMAGE_FILE_EXTRA, imageFile);
            startActivity(intent);
        }
        else
            super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(imageChanged && dbid > 0){
                    Intent intent = new Intent(this, AddInvoiceActivity.class);
                    intent.putExtra(DBID, dbid);
                    intent.putExtra(IMAGE_FILE_EXTRA, imageFile);
                    startActivity(intent);
                }
                else
                    super.onBackPressed();

                return true;

            case R.id.action_edit_image:

                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_PICK);

                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Intent chooserIntent = Intent.createChooser(pickIntent, getString(R.string.pickActionTitle));
                chooserIntent.putExtra
                        (
                                Intent.EXTRA_INITIAL_INTENTS,
                                new Intent[] { takePhotoIntent }
                        );
                startActivityForResult(chooserIntent, SELECT_PICTURE);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            Bitmap invoiceImage = null;
            if(data.hasExtra("data")){
                invoiceImage = (Bitmap) data.getExtras().get("data");
                bigImageView.setImageBitmap(invoiceImage);
                imageChanged =true;
            }else{
                Uri selectedimg = data.getData();
                try {
                    invoiceImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                    bigImageView.setImageBitmap(invoiceImage);
                } catch (IOException e) {
                    throw new RuntimeException("Error Getting Image.");
                }
                imageChanged =true;
            }

            if(invoiceImage != null)
            {
                FileOutputStream fos = null;
                String uuidFilePath = UUID.randomUUID().toString();
                try {
                    fos = openFileOutput(uuidFilePath, Context.MODE_PRIVATE);
                    invoiceImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                imageFile = uuidFilePath;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_image_menu, menu);
        return true;
    }
}
