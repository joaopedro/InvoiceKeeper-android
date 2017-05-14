package com.smartech.invoicekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartech.invoicekeeper.db.InvoiceDBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private InvoiceDBHelper invoiceDBHelper;

    public static final String DBID = "dbid";
    public static final int INITIALDB_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        invoiceDBHelper = new InvoiceDBHelper(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(view.getContext(), AddInvoiceActivity.class);
                intent.putExtra(DBID, INITIALDB_ID);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.invoicesList);

        Cursor invoicesCursor = invoiceDBHelper.getAll();

        //TODO: startManagingCursor is depracated, use the correct method instead an finish using the database source for the list view.
        //        getLoaderManager().initLoader(0, null, this);

        startManagingCursor(invoicesCursor);
        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar8888");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        invoiceDBHelper.close();
        super.onDestroy();
    }
}
