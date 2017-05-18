package com.smartech.invoicekeeper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartech.invoicekeeper.db.InvoiceContract;
import com.smartech.invoicekeeper.db.InvoiceDBHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class AddInvoiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    public static final String IMAGE_FILE_EXTRA = "imageFile";
    private TextView date;
    private TextView title;
    private Calendar calendar ;
    private DatePickerDialog datePickerDialog ;
    private int Year, Month, Day ;

    private Spinner invoiceType;
    private Spinner warrantyPeriod;

    private static final int CAMERA_REQUEST = 1888;
    private ImageView invoiceImageView;
    private boolean imageTaken = false;

    private static final int SELECT_PICTURE = 1;
    private String imageFile;
    private InvoiceDBHelper invoiceDBHelper;

    private long dbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        invoiceDBHelper = new InvoiceDBHelper(getApplicationContext());
        Intent intent = getIntent();
        dbID = intent.getIntExtra(MainActivity.DBID, 0);

        //Instantiate a calendar do fill the date textbox and the DatePickerDialog
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        //Handle the data text box and the datePickerDialog
        datePickerDialog = new DatePickerDialog(AddInvoiceActivity.this, AddInvoiceActivity.this, Year, Month, Day);
        datePickerDialog.setTitle(R.string.datePickerTitle);

        title = (TextView) findViewById(R.id.invoiceTitle);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ContentValues values = new ContentValues();
                values.put(InvoiceContract.Invoice.COLUMN_NAME_TITLE, s.toString());
                if(dbID==0){
                    dbID = invoiceDBHelper.insertInvoice(s.toString(),null,null,null,null);
                }else{
                 //   invoiceDBHelper.updateInvoice(dbID, s.toString(), invoiceType.getSelectedItem().toString(),Integer.valueOf(warrantyPeriod.getSelectedItem().toString())
                 //           ,imageFile, null);
                    invoiceDBHelper.updateInvoice(dbID, s.toString(), null ,null ,null, null);
                }
            }
        });



        date = (TextView) findViewById(R.id.dateText);
        date.setText(Day+"/"+Month+"/"+Year);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //handle the invoice type Spinner
        invoiceType = (Spinner) findViewById(R.id.invoiceType);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> invoiceTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.invoice_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        invoiceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        invoiceType.setAdapter(invoiceTypeAdapter);


        //handle the warranty period Spinner
        warrantyPeriod = (Spinner) findViewById(R.id.warrantyPeriod);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> warrantyAdapter = ArrayAdapter.createFromResource(this,
                R.array.warranty_periods, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        warrantyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        warrantyPeriod.setAdapter(warrantyAdapter);

        //Handle the invoice image capture
        this.invoiceImageView = (ImageView)this.findViewById(R.id.invoiceImageView);

        invoiceImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!imageTaken){
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
                }else if (!TextUtils.isEmpty(imageFile)){
                    Intent intent = new Intent(AddInvoiceActivity.this, ShowInvoiceImage.class);
                    intent.putExtra(IMAGE_FILE_EXTRA, imageFile);
                    startActivity(intent);
                }
            }
        });

        if(dbID>MainActivity.INITIALDB_ID){
            title.setText(invoiceDBHelper.getInvoiceById(dbID));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            Bitmap invoiceImage = null;
            if(data.hasExtra("data")){
                 invoiceImage = (Bitmap) data.getExtras().get("data");
                invoiceImageView.setImageBitmap(invoiceImage);
                imageTaken = true;
            }else{
                Uri selectedimg = data.getData();
                try {
                    invoiceImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                    invoiceImageView.setImageBitmap(invoiceImage);
                    imageTaken = true;
                } catch (IOException e) {
                    throw new RuntimeException("Error Getting Image.");
                }
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
    protected void onDestroy() {
        invoiceDBHelper.close();
        super.onDestroy();
    }

}
