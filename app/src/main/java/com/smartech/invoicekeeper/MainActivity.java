package com.smartech.invoicekeeper;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.smartech.invoicekeeper.db.InvoiceContract;
import com.smartech.invoicekeeper.db.InvoiceDBHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private InvoiceDBHelper invoiceDBHelper;
    private SimpleCursorAdapter dataAdapter;

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
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.invoice_list_element,
                invoicesCursor,
                new String[] {InvoiceContract.Invoice.COLUMN_NAME_TITLE},
                new int[] {R.id.invoiceTitleListElement},
                0);


            //TODO: startManagingCursor is depracated, use the correct method instead an finish using the database source for the list view.
        //getLoaderManager().initLoader(0, null, this);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(), AddInvoiceActivity.class);
                int dbid = cursor.getInt(cursor.getColumnIndexOrThrow(InvoiceContract.Invoice._ID));
                intent.putExtra(DBID, dbid);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),
                        "Selected : "+dbid, Toast.LENGTH_SHORT).show();

            }
        });
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
