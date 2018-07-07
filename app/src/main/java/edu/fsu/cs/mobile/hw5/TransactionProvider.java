package edu.fsu.cs.mobile.hw5;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Locale;

public class TransactionProvider extends ContentProvider {
    /*
        Built on lecture 10 examples.
        provided at http://ww2.cs.fsu.edu/~yannes/examples/lect10examples.zip
     */
    private static final String TAG = TransactionProvider.class.getCanonicalName();

    private MainDatabaseHelper mOpenHelper;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(EmployeeContract.AUTHORITY, EmployeeContract.TransactionEntry.TABLE, EmployeeContract.URI_TRANSACTIONS);
        sUriMatcher.addURI(EmployeeContract.AUTHORITY, EmployeeContract.TransactionEntry.TABLE + "/#", EmployeeContract.URI_TRANSACTION_ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());

        return (mOpenHelper.getWritableDatabase() != null);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        // Check for incorrect uri
        if(sUriMatcher.match(uri) != EmployeeContract.URI_TRANSACTIONS) {
            throw new IllegalArgumentException("Invalid insert URI: " + uri);
        }

        long rowId = db.insert(EmployeeContract.TransactionEntry.TABLE, "", values);

        // rowId > 0 if successful
        if(rowId <= 0) {
            throw new SQLException("Failed to insert into " + uri);
        }

        // Create new uri with rowId appended
        Uri insertedUri = ContentUris.withAppendedId(EmployeeContract.CONTENT_URI, rowId);
        // Notify ContentResolver of insertedUri
        getContext().getContentResolver().notifyChange(insertedUri, null);


        Log.i(TAG, "TransactionProvider:insert(): insertedUri = " + insertedUri);

        // Return insertedUri
        return insertedUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.i(TAG, "TransactionProvider:query(): uri = " + uri);

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(EmployeeContract.TransactionEntry.TABLE);

        Log.i(TAG, "TransactionProvider:query(): sUriMatcher.match(uri) = " + sUriMatcher.match(uri));

        Log.i(TAG, "TransactionProvider:query(): EmployeeContract.URI_TRANSACTION_ID = " + EmployeeContract.URI_TRANSACTION_ID);
        switch(sUriMatcher.match(uri)) {
            case EmployeeContract.URI_TRANSACTIONS:
                // Querying many transactions
                break;
            case EmployeeContract.URI_TRANSACTION_ID:
                Log.i(TAG, "TransactionProvider:query(): uri.getLastPathSegment() = " +uri.getLastPathSegment());
                // Querying specific id
                builder.appendWhere(EmployeeContract.TransactionEntry._ID + " = " +
                        uri.getLastPathSegment());
                break;
        }

        Log.i(TAG, "TransactionProvider:query(): uri.getLastPathSegment() = " +uri.getLastPathSegment());
        return builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated;
        switch(sUriMatcher.match(uri)) {
            case EmployeeContract.URI_TRANSACTIONS:
                numUpdated = db.update(EmployeeContract.TransactionEntry.TABLE, values,
                        selection, selectionArgs);
                break;
            case EmployeeContract.URI_TRANSACTION_ID:
                String where = EmployeeContract.TransactionEntry._ID + " = " +
                        uri.getLastPathSegment();
                if(!selection.isEmpty()) {
                    where += " AND " + selection;
                }
                numUpdated = db.update(EmployeeContract.TransactionEntry.TABLE, values, where,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid update URI: " + uri);
        }

        return numUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numDeleted;
        switch(sUriMatcher.match(uri)) {
            case EmployeeContract.URI_TRANSACTIONS:
                numDeleted = db.delete(EmployeeContract.TransactionEntry.TABLE, selection,
                        selectionArgs);
                break;
            case EmployeeContract.URI_TRANSACTION_ID:
                String where = EmployeeContract.TransactionEntry._ID + " = " +
                        uri.getLastPathSegment();
                if(!selection.isEmpty()) {
                    where += " AND " + selection;
                }
                numDeleted = db.delete(EmployeeContract.TransactionEntry.TABLE, where,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid delete URI: " + uri);
        }

        return numDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case EmployeeContract.URI_TRANSACTIONS:
                return EmployeeContract.CONTENT_TYPE;
            case EmployeeContract.URI_TRANSACTION_ID:
                return EmployeeContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context) {
            super(context, EmployeeContract.DBNAME, null, EmployeeContract.DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating: " + EmployeeContract.SQL_CREATE_TRANSACTION_TABLE);
            db.execSQL(EmployeeContract.SQL_CREATE_TRANSACTION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(String.format(Locale.US,
                    "DROP TABLE IF EXISTS %s", EmployeeContract.TransactionEntry.TABLE));
            onCreate(db);
        }
    }
}
