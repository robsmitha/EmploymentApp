package edu.fsu.cs.mobile.hw5;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getCanonicalName();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_login, container, false);

        Button buttonLogin = v.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onLogin();
            }
        });

        return v;
    }

    private void onLogin(){
        EditText editTextEmployeeID = getView().findViewById(R.id.editTextEmployeeID);
        EditText editTextAccessCode = getView().findViewById(R.id.editTextAccessCode);
        //reset errors
        editTextEmployeeID.setError(null);
        editTextAccessCode.setError(null);

        if(!editTextEmployeeID.getText().toString().isEmpty()){
            if(!editTextAccessCode.getText().toString().isEmpty()){
                //set up content values
                ContentValues values = new ContentValues();
                values.put(EmployeeContract.TransactionEntry.EMPLOYEE_ID, editTextEmployeeID.getText().toString());
                values.put(EmployeeContract.TransactionEntry.PASSWD, editTextAccessCode.getText().toString());

                ((MainActivity)getActivity()).onQuery(values);
            }else{
                editTextAccessCode.setError("Please enter an access code");
            }
        }else{
            editTextEmployeeID.setError("Please enter a valid employee ID");
        }
    }

}
