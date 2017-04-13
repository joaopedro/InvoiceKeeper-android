package com.smartech.invoicekeeper;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextClock;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Calendar;

public class AddInvoiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    TextView date;
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);


        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(AddInvoiceActivity.this, AddInvoiceActivity.this, Year, Month, Day);
        datePickerDialog.setTitle(R.string.datePickerTitle);

        date = (TextView) findViewById(R.id.dateText);
        date.setText(Day+"/"+Month+"/"+Year);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
