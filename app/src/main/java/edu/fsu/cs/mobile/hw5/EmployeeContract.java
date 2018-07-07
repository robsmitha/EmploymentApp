package edu.fsu.cs.mobile.hw5;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Locale;

public class EmployeeContract {
    /*
        Built on lecture 10 examples.
        provided at http://ww2.cs.fsu.edu/~yannes/examples/lect10examples.zip
     */
    private static final String TAG = EmployeeContract.class.getCanonicalName();
    public final static String DBNAME = "mydata.db";
    public final static int DBVERSION = 1;

    public static final int URI_TRANSACTIONS = 1;
    public static final int URI_TRANSACTION_ID = 2;

    public static final String AUTHORITY = "edu.fsu.cs.mobile.hw5.transactionprovider";

    public class TransactionEntry implements BaseColumns {
        public static final String TABLE = "employees";

        static final String _ID = "_id";
        static final String EMPLOYEE_ID = "employee_id";
        static final String NAME = "name";
        static final String EMAIL = "email";
        static final String GENDER = "gender";
        static final String PASSWD = "passwd";
        static final String DEPARTMENT = "department";
    }

    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/" + TransactionEntry.TABLE);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/edu.fsu.cs.mobile.provider/" + TransactionEntry.TABLE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/edu.fsu.cs.mobile.provider/" + TransactionEntry.TABLE;

    public static final String SQL_CREATE_TRANSACTION_TABLE =
            String.format(Locale.US,
                    "CREATE TABLE %s (" // TransactionEntry.TABLE
                            + " %s INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + " %s TEXT, "
                            + " %s TEXT,"
                            + " %s TEXT,"
                            + " %s TEXT,"
                            + " %s TEXT,"
                            + " %s TEXT);",
                    TransactionEntry.TABLE, TransactionEntry._ID, TransactionEntry.EMPLOYEE_ID, TransactionEntry.NAME,
                    TransactionEntry.EMAIL, TransactionEntry.GENDER, TransactionEntry.PASSWD, TransactionEntry.DEPARTMENT);

    public static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating: " + SQL_CREATE_TRANSACTION_TABLE);
            db.execSQL(SQL_CREATE_TRANSACTION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(String.format(Locale.US,
                    "DROP TABLE IF EXISTS %s", TransactionEntry.TABLE));
            onCreate(db);
        }
    }
}
