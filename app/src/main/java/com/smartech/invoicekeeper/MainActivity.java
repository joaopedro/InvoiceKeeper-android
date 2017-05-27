package com.smartech.invoicekeeper;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.smartech.invoicekeeper.adapter.CustomCursorAdapter;
import com.smartech.invoicekeeper.db.InvoiceContract;
import com.smartech.invoicekeeper.db.InvoiceDBHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String CONTENT_INVOICE_LIST = "content://invoice_list";
    private ListView listView;
    private InvoiceDBHelper invoiceDBHelper;
    private CustomCursorAdapter dataAdapter;

    public static final String DBID = "dbid";
    public static final int INITIALDB_ID = 0;

    @Override
    protected void onResume() {
        getContentResolver().notifyChange(
                Uri.parse(CONTENT_INVOICE_LIST), null);
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.invoice_list_menu, menu);
    }

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
                Intent intent = new Intent(view.getContext(), AddInvoiceActivity.class);
                intent.putExtra(DBID, INITIALDB_ID);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.invoicesList);
        registerForContextMenu(listView);

        getLoaderManager().initLoader(0, null, this);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(), AddInvoiceActivity.class);
                long dbid = cursor.getLong(cursor.getColumnIndexOrThrow(InvoiceContract.Invoice._ID));
                intent.putExtra(DBID, dbid);
                startActivity(intent);
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
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_invoice) {
            if(invoiceDBHelper.deleteById(info.id)){
                getLoaderManager().restartLoader(0,null, this);

                return true;
            }
        }
        return super.onContextItemSelected(item);
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

    @Override
    public android.content.Loader onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(CONTENT_INVOICE_LIST);
        final String[] projection = new String[]{InvoiceContract.Invoice.COLUMN_NAME_TITLE};
        return new CursorLoader(this, uri, projection, null, null, null) {
            private final ForceLoadContentObserver mObserver = new ForceLoadContentObserver();

            @Override
            public Cursor loadInBackground() {
                Cursor c = invoiceDBHelper.getAll();
                if (c != null) {

                    c.getCount();
                    c.registerContentObserver(mObserver);
                }

                c.setNotificationUri(getContext().getContentResolver(), getUri());
                return c;
            }
        };
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if(dataAdapter==null){
            dataAdapter = new CustomCursorAdapter(
                    this, R.layout.invoice_list_element,
                    data,
                    new String[] {InvoiceContract.Invoice.COLUMN_NAME_TITLE,
                            InvoiceContract.Invoice.COLUMN_NAME_IMAGE_FILE},
                    new int[] {R.id.invoiceTitleListElement,
                    R.id.listImage},
                    0);
            listView.setAdapter(dataAdapter);
        }else{
            dataAdapter.swapCursor(data);
            dataAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader loader) {
        Toast.makeText(this,"Reset Made!!",Toast.LENGTH_LONG).show();
    }
}
