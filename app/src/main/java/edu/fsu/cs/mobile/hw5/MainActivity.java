package edu.fsu.cs.mobile.hw5;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    public static final String VIEW_LOGIN = "login";
    public static final String VIEW_REGISTER = "register";
    public static final String VIEW_MAIN = "main";


    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "MainActivity.onCreate()");

        OnFragmentChanged(VIEW_MAIN);

        //intent for deletion
        if(getIntent().hasExtra("ID")){
            String pk = getIntent().getStringExtra("ID");
            if(!pk.isEmpty()){
                Log.i(TAG, "MainActivity.onCreate(): e = "+pk);
                ContentValues values = new ContentValues();
                values.put(EmployeeContract.TransactionEntry._ID, pk);
                onDelete(values);
            }
        }

    }

    public void OnFragmentChanged(String key){
        Log.i(TAG, "MainActivity.LaunchLoginFragment(): key = " + key);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (key){
            case VIEW_LOGIN:
                LoginFragment loginFragment = new LoginFragment();
                fragmentTransaction.replace(R.id.frame, loginFragment);
                fragmentTransaction.commit();
                break;
            case VIEW_REGISTER:
                RegisterFragment registerFragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.frame, registerFragment);
                fragmentTransaction.commit();
                break;
            case VIEW_MAIN:
                MainFragment mainFragment = new MainFragment();
                fragmentTransaction.replace(R.id.frame, mainFragment);
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_main:
                OnFragmentChanged(VIEW_MAIN);
                break;
            case R.id.option_login:
                OnFragmentChanged(VIEW_LOGIN);
                break;
            case R.id.option_register:
                OnFragmentChanged(VIEW_REGISTER);
                break;
        }

        return true;
    }

    public void onInsert(ContentValues values) {
        Log.i(TAG, "MainActivity.onInsert()");

        if(!values.containsKey(EmployeeContract.TransactionEntry.EMPLOYEE_ID))  return;
        if(!values.containsKey(EmployeeContract.TransactionEntry.NAME))  return;
        if(!values.containsKey(EmployeeContract.TransactionEntry.EMAIL))return;
        if(!values.containsKey(EmployeeContract.TransactionEntry.GENDER))return;
        if(!values.containsKey(EmployeeContract.TransactionEntry.PASSWD))return;
        if(!values.containsKey(EmployeeContract.TransactionEntry.DEPARTMENT))return;

        final Uri newUri = getContentResolver().insert(EmployeeContract.CONTENT_URI, values);

        assert(newUri != null);
        Log.i(TAG, "MainActivity.onInsert(): newUri = "+newUri);
        Intent intent = new Intent(Intent.ACTION_VIEW, newUri);
        startActivity(intent);
    }

    public void onQuery(ContentValues values) {
        Log.i(TAG, "MainActivity.onQuery()");
        String[] args = null;
        String where = null;

        if(values.containsKey(EmployeeContract.TransactionEntry.EMPLOYEE_ID)) {
            String employee_id = values.getAsString(EmployeeContract.TransactionEntry.EMPLOYEE_ID);
            where = EmployeeContract.TransactionEntry.EMPLOYEE_ID + " = ?";
            args = new String[]{employee_id};

            // If number is entered, update only that entry
            if (values.containsKey(EmployeeContract.TransactionEntry.PASSWD)) {
                String access_code = values.getAsString(EmployeeContract.TransactionEntry.PASSWD);
                where += "AND " + EmployeeContract.TransactionEntry.PASSWD + " = ?";
                args = new String[]{employee_id, access_code};
            }
        }

        mCursor = getContentResolver().query(EmployeeContract.CONTENT_URI,
                null, where, args, null);

        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                mCursor.moveToNext();
                int rowId = mCursor.getInt(mCursor.getColumnIndex(EmployeeContract.TransactionEntry._ID));
                Uri queriedUri = ContentUris.withAppendedId(EmployeeContract.CONTENT_URI, rowId);
                Log.i(TAG, "MainActivity.onQuery(): queriedUri = "+queriedUri);
                Intent intent = new Intent(Intent.ACTION_VIEW, queriedUri);
                startActivity(intent);
            }else{
                Toast.makeText(this,"The employee ID or access code is incorrect!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onLookup(ContentValues values) {
        Log.i(TAG, "MainActivity.onLookup()");
        String[] args = null;
        String where = null;
        if (values.containsKey(EmployeeContract.TransactionEntry.EMPLOYEE_ID)) {
            String employee_id = values.getAsString(EmployeeContract.TransactionEntry.EMPLOYEE_ID);
            where = EmployeeContract.TransactionEntry.EMPLOYEE_ID + " = ?";
            args = new String[]{employee_id};
        }
        mCursor = getContentResolver().query(EmployeeContract.CONTENT_URI,
                null, where, args, null);

        return mCursor != null && mCursor.getCount() > 0;
    }

    public void onDelete(ContentValues values) {
        Log.i(TAG, "MainActivity.onDelete()");
        String _id = values.getAsString(EmployeeContract.TransactionEntry._ID);

        String[] args = new String[]{_id};
        String where = EmployeeContract.TransactionEntry._ID + " = ?";


        int numDeletes = getContentResolver().delete(EmployeeContract.CONTENT_URI, where, args);
        if(numDeletes > 0){
            Log.i(TAG, "MainActivity.onDelete(): numDeletes = "+numDeletes);
            Toast.makeText(this,"Employee account deleted!", Toast.LENGTH_SHORT).show();
        }

    }

}
