package com.example.mark_attendance.mark_attendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ProjectCompletePackage.RuntimeDatabase;

/**
 * Created by Aritro Biswas on 02-10-2017.
 */

public class changeForgottenPass extends AppCompatActivity {
    private Button PasswordChangeBtn;
    private EditText PasswordText, ConfirmPasswordText;
    String password, confirmPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p3_change_forgotten_pass);

        PasswordText = (EditText) findViewById(R.id.pass_edittext_changeforgottenpass);
        ConfirmPasswordText = (EditText) findViewById(R.id.pass_edittext_confchangeforgottenpass);

        PasswordChangeBtn = (Button) findViewById(R.id.btn_change_password);
        PasswordChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = PasswordText.getText().toString();
                confirmPassword = ConfirmPasswordText.getText().toString();

                if(password.equals(confirmPassword)){
                    new ChangePassword().execute();
                }
                else{
                    Toast.makeText(changeForgottenPass.this, "Passwords Not Matched", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    class ChangePassword extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                return RuntimeDatabase.mainComClass.updatePassword(RuntimeDatabase.AdminUsername, password);
            } catch (Exception e) {
                return "Password Not Changed:" + e.toString();
            }
        }

        @Override
        protected void onPostExecute(String msg) {

            if(msg.equals("Password Updated")) {
                Toast.makeText(changeForgottenPass.this, "Password Changed", Toast.LENGTH_LONG).show();
                Intent open_newAdminPage = new Intent(getApplicationContext(), mainActivity.class);
                startActivity(open_newAdminPage);
            }
            else{
                Toast.makeText(changeForgottenPass.this, "Password not rested: " + msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
