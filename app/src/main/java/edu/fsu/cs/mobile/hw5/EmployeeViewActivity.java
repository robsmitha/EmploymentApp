package edu.fsu.cs.mobile.hw5;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmployeeViewActivity extends AppCompatActivity {
    /*
        Built on lecture 10 examples.
        provided at http://ww2.cs.fsu.edu/~yannes/examples/lect10examples.zip
     */
    private static final String TAG = EmployeeViewActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_view);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        if(uri == null) {
            throw new IllegalArgumentException("Did not include URI");
        }
        Log.i(TAG, "EmployeeViewActivity:onCreate(): uri = " + uri);
        setupUI(uri);



    }
    public void setupUI(Uri uri) {
        final Cursor cursor = getContentResolver().query(uri,
                null, null, null, null);

        Log.i(TAG, "EmployeeViewActivity:setUI()");
        if (cursor == null) {
            return;
        }

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Log.i(TAG, "EmployeeViewActivity:setUI(): cursor = " + cursor.getCount());

        if (cursor.getCount() > 0) {
            cursor.moveToNext();

            final String ID = cursor.getString(cursor.getColumnIndex(EmployeeContract.TransactionEntry._ID));

            String employeeID = cursor.getString(cursor.getColumnIndex(EmployeeContract.TransactionEntry.EMPLOYEE_ID));
            String name = cursor.getString(cursor.getColumnIndex(EmployeeContract.TransactionEntry.NAME));
            String email = cursor.getString(cursor.getColumnIndex(EmployeeContract.TransactionEntry.EMAIL));
            String gender = cursor.getString(cursor.getColumnIndex(EmployeeContract.TransactionEntry.GENDER));
            String department = cursor.getString(cursor.getColumnIndex(EmployeeContract.TransactionEntry.DEPARTMENT));

            TextView textViewEmployeeID = findViewById(R.id.textViewEmployeeID);
            TextView textViewName = findViewById(R.id.textViewName);
            TextView textViewEmail = findViewById(R.id.textViewEmail);
            TextView textViewGender = findViewById(R.id.textViewGender);
            TextView textViewDepartment = findViewById(R.id.textViewDepartment);

            textViewEmployeeID.setText(employeeID);
            textViewName.setText(name);
            textViewEmail.setText(email);
            textViewGender.setText(gender);
            textViewDepartment.setText(department);

            Button buttonDelete = findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("ID", ID);  //set pk argument for delete
                    startActivity(intent);


                }
            });

        }
        cursor.close();
    }
}
