package com.example.safsaf.inventoryapp;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.safsaf.inventoryapp.data.ProductContract.productEntry;
/**
 * Created by Safsaf on 8/14/2017.
 */

/**
 * Allows user to create a new product or  edit an existing one.
 */


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the pet data loader */
    private static final int EXISTING_PRODUCT_LOADER = 0;

    /** Content URI for the existing pet (null if it's a new pet) */
    private Uri mCurrentProductUri;
    /** EditText field to enter the product's name */
    private EditText mNameEditText;

    /** EditText field to enter the product's price */
    private EditText mPriceEditText;

    /** EditText field to enter the product's quantity */
    private EditText mQuantityEditText;
    /**  buttob to enter the product's Image */
    private Button mButtonImage;
     private ImageView mImageView;




    final int REQUEST_CODE_GALLERY = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        // مشر فاهمة ليه مش عاوز يعمل انت
        // Examine the intent that was used to launch this activity,
                // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.
        if (mCurrentProductUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(getString(R.string.editor_activity_title_new_product));
        } else {
            // Otherwise this is an existing pet, so change app bar to say "Edit Pet"
            setTitle(getString(R.string.editor_activity_title_edit_product));

            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
                    }


        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mButtonImage=(Button)findViewById(R.id.chooseButton);
        mImageView = (ImageView) findViewById(R.id.imageView);






        // ATTACH A CLICK  LISTENER TO BUTTONCHOOSE  AND GET IMAGE FROM GALLERY TO IMAGE VIEW.
        mButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EditorActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }//   public void onClick(View v)
        });//  buttonChoose.setOnClickListener(new View.OnClickListener()

    }//   protected void onCreate(Bundle savedInstanceState) {





    // imageViewToByte() method
    private byte[] imageViewToByte(ImageView imageView) {

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        return byteArray;
    }//  private  byte [] imageViewToByte(){




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }// if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            else {
                Toast.makeText(getApplicationContext(), "You don't have no permission to access file location!", Toast.LENGTH_SHORT).show();

            }// else {
            return;

        }// if (requestCode==REQUEST_CODE_GALLERY)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }// public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mImageView.setImageBitmap(bitmap);
            } //try {
            catch (FileNotFoundException e) {

                e.printStackTrace();
            }//  catch (FileNotFoundException e){


        }// if (requestCode==REQUEST_CODE_GALLERY&& requestCode== RESULT_OK && data != null )

        super.onActivityResult(requestCode, resultCode, data);
    }// protected void onActivityResult(int requestCode, int resultCode, Intent data) {







    /**
          * Get user input from editor and save new pet into database.
          */
    private void insertProduct() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        int price;
        try {
            price = Integer.parseInt(priceString);
        }catch (NumberFormatException e){
           price=0; // I set the weight to 0 because this is the default value in the database so..
        }
        String quantityString = mQuantityEditText.getText().toString().trim();
        int quantity;
        try {
           quantity = Integer.parseInt(quantityString);
        }catch (NumberFormatException e){
            quantity=0; // I set the weight to 0 because this is the default value in the database so..
        }
        byte[] image= imageViewToByte(mImageView);


        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(productEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(productEntry.COLUMN_PRODUCT_PRICE,price);
        values.put(productEntry.COLUMN_PRODUCT_QUANTITY,quantity);
        values.put(productEntry.COLUMN_PRODUCT_IMAGE,image);




        // Insert a new pet into the provider, returning the content URI for the new pet.
        Uri newUri = getContentResolver().insert(productEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:

                // Save pet to database
                insertProduct();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                productEntry._ID,
                productEntry.COLUMN_PRODUCT_NAME,
                productEntry.COLUMN_PRODUCT_PRICE,
                productEntry.COLUMN_PRODUCT_QUANTITY,
                productEntry.COLUMN_PRODUCT_IMAGE};
        // This loader will execute the ContentProvider's query method on a background thread

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                productEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,
                // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
                if (cursor == null || cursor.getCount() < 1) {

                    return;
                            }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_QUANTITY);
            final int imageColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_IMAGE);
            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(nameColumnIndex);
            String productPrice = cursor.getString(priceColumnIndex);
            final String productQuantity = cursor.getString(quantityColumnIndex);
            byte[] productImage = cursor.getBlob(imageColumnIndex);
            Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
            // Update the views on the screen with the values from the database
            mNameEditText.setText(productName);
            mPriceEditText.setText(productPrice);
            mQuantityEditText.setText(productQuantity);
            mImageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mImageView.setImageBitmap(null);

    }
}// public class EditorActivity extends AppCompatActivity {
