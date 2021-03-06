package com.example.safsaf.inventoryapp;

/**
 * Created by Safsaf on 8/17/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.safsaf.inventoryapp.R.id.price;
import static com.example.safsaf.inventoryapp.R.id.quantity;
import static com.example.safsaf.inventoryapp.data.ProductContract.productEntry;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    private Context mContext;

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }




    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public  void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(price);
        TextView quantityTextView = (TextView) view.findViewById(quantity);
        ImageView productImageView = (ImageView) view.findViewById(R.id.imageView);


        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_QUANTITY);
        final int imageColumnIndex = cursor.getColumnIndex(productEntry.COLUMN_PRODUCT_IMAGE);

        // Read the pet attributes from the Cursor for the current pet
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        byte[] productImage = cursor.getBlob(imageColumnIndex);
        Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);


        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);
        productImageView.setImageBitmap(bitmap);
/**   sellButton    ده الكود اللي الولد عامله بس ما شتغلش معايا
        Button sellButton = (Button) view.findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    Object obj = view.getTag();
                    String st = obj.toString();
                    ContentValues values = new ContentValues();
                    values.put(productEntry.COLUMN_PRODUCT_NAME, name);
                    values.put(productEntry.COLUMN_PRODUCT_PRICE, price);
                    values.put(productEntry.COLUMN_PRODUCT_QUANTITY, quantity >= 1? quantity-1: 0);
                    values.put(productEntry.COLUMN_PRODUCT_IMAGE, imageView);


                    Uri currentProductUri = ContentUris.withAppendedId(productEntry.CONTENT_URI, Integer.parseInt(st));

                    int rowsAffected = mContext.getContentResolver().update(currentProductUri, values, null, null);
                    if (rowsAffected == 0 || quantity == 0) {
                        Toast.makeText(mContext, mContext.getString(R.string.Toat), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Object obj = cursor.getInt(cursor.getColumnIndex(productEntry._ID));
        sellButton.setTag(obj);
*/
    }

}
