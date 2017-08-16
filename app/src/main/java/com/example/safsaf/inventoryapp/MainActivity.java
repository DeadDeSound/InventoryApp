package com.example.safsaf.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static com.example.safsaf.inventoryapp.data.ProductContract.productEntry;

/**
 * Displays list of products that were entered and stored in the app.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();

    }
    /**
     * +     * Temporary helper method to display information in the onscreen TextView about the state of
     * +     * the pets database.
     * +
     */
    private void displayDatabaseInfo() {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                productEntry._ID,
                productEntry.COLUMN_PRODUCT_NAME,
                productEntry.COLUMN_PRODUCT_PRICE,
               productEntry.COLUMN_PRODUCT_QUANTITY,
                productEntry.COLUMN_PRODUCT_IMAGE };

        // Perform a query on the pets table
        Cursor cursor = getContentResolver().query(
               productEntry.CONTENT_URI,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                                        // Don't filter by row groups
                null);                   // The sort order
        TextView displayView = (TextView) findViewById(R.id.text_view_product);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The products table contains <number of rows in Cursor> products.
            // _id - name - price - quantity - image
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The products table contains " + cursor.getCount() + " Products.\n\n");
            displayView.append(productEntry._ID + " - " +
                    productEntry.COLUMN_PRODUCT_NAME + " - " +
                    productEntry.COLUMN_PRODUCT_PRICE + " - " +
                    productEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    productEntry.COLUMN_PRODUCT_IMAGE + "\n");
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(productEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_QUANTITY);
            int imageColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_IMAGE);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                byte[] currentImage = cursor.getBlob(imageColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentImage));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }






























}
