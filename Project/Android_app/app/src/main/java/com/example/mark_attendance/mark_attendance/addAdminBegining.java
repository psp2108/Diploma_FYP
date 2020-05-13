package com.example.mark_attendance.mark_attendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ProjectCompletePackage.RuntimeDatabase;

/**
 * Created by Aritro Biswas on 04-10-2017.
 */

public class addAdminBegining extends AppCompatActivity{

    private EditText adminUsername;
    TextView checkUsername;
    private Button checkUsernameAvailability;
    private EditText adminPassword, adminConfirmPassword;
    private Button nextbutton;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_new_admin);

        nextbutton = (Button) findViewById(R.id.next_button_addemp);

        adminUsername = (EditText) findViewById(R.id.input_username_newadmin);
        checkUsername = (TextView) findViewById(R.id.username_status_newadmin);
        checkUsernameAvailability = (Button) findViewById(R.id.btn_getOTP);

        adminPassword = (EditText) findViewById(R.id.input_emailedOTP_admin);
        adminConfirmPassword = (EditText) findViewById(R.id.input_confirmpass_newadmin);
        nextbutton = (Button) findViewById(R.id.next_button_addemp);

        nextbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String pwd1, pwd2;
                pwd1 = adminPassword.getText().toString();
                pwd2 = adminConfirmPassword.getText().toString();
                if(pwd1.equals(pwd2))
                    new AddAdminCredentials().execute();
                else
                    Toast.makeText(getApplicationContext(), "Passwords didn't matched", Toast.LENGTH_LONG).show();
            }
        });
    }

    class AddAdminCredentials extends AsyncTask<Void, String, String> {

        String uid, pwd;

        @Override
        protected void onPreExecute() {
            uid = adminUsername.getText().toString();
            pwd = adminPassword.getText().toString();
        }
        @Override
        protected String doInBackground(Void... params) {
            try{
                return RuntimeDatabase.mainComClass.addNewAdminCredentials(uid, pwd);
            }
            catch(Exception ex){
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                if(!s.contains("Exception")){
                    if(s.equals("Record Inserted")){
                        RuntimeDatabase.NewAdminUsername = uid;
                        Intent openAddAdminDetails = new Intent(getApplicationContext(),addAdminDetailsBegining.class);
                        startActivity(openAddAdminDetails);
                        finish();
                    }
                }
            }
        }
    }

}
