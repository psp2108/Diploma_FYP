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

import java.util.Random;

import EmailPackage.SendEMail;
import ProjectCompletePackage.RuntimeDatabase;

/**
 * Created by Aritro Biswas on 02-10-2017.
 */

public class otpVerification extends AppCompatActivity{
    private Button GetOTPBtn, VerifyOTPBtn;
    private EditText adminUsername, otpInputField;
    private Random r;
    private String otp = null;
    public String Username;
    long startTime, endTime, difference;
    final private int OTPInterval = 3 * 60;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p2_otp_verification);

        r = new Random();
        adminUsername = (EditText) findViewById(R.id.input_username_newadmin);
        otpInputField = (EditText) findViewById(R.id.input_emailedOTP_admin);

        GetOTPBtn = (Button) findViewById(R.id.btn_getOTP);
        VerifyOTPBtn = (Button) findViewById(R.id.btn_verify_otpverification);

        GetOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendMail().execute();
            }
        });

        VerifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTPAndRedirect();
            }
        });
    }

    private void verifyOTPAndRedirect(){
        if(otp != null){
            endTime = System.currentTimeMillis();
            difference = (endTime - startTime)/1000;

            if(difference <= OTPInterval){
                String userOTP = otpInputField.getText().toString();

                if(userOTP.equals(otp)){
                    RuntimeDatabase.AdminUsername = Username;
                    Intent open_newAdminPage = new Intent(getApplicationContext(),changeForgottenPass.class);
                    startActivity(open_newAdminPage);
                }
                else{
                    Toast.makeText(otpVerification.this, "OTP you entered is incorrect", Toast.LENGTH_LONG).show();
                }
            }
            else{
                otp = null;
                Toast.makeText(otpVerification.this, "Your OTP has Expired", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(otpVerification.this, "You haven't received any OTP yet", Toast.LENGTH_LONG).show();
        }
    }

    class SendMail extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            Username = adminUsername.getText().toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            otp = String.valueOf(r.nextInt(999999));
            otp = "000000".substring(otp.length()) + otp;

            try {
                String email = RuntimeDatabase.mainComClass.getAdminEmail(Username);
                if(email != null){
                    if(!email.equals("")){
                        otp = String.valueOf(r.nextInt(999999));
                        otp = "000000".substring(otp.length()) + otp;

                        SendEMail.receiver = email;
                        SendEMail.subject = "Password Reset OTP";
                        SendEMail.body = "Hii " + Username + "\nYour OTP to reset password is: " + otp;
                        SendEMail.sendMail();
                        startTime = System.currentTimeMillis();
                    }
                    else{
                        otp = null;
                        return "Wrong Username";
                    }
                }
                else{
                    otp = null;
                    return "Wrong Username";
                }
            } catch (Exception e) {
                otp = null;
                return "Main Not Send:" + e.toString();
            }
            return "Main Send";
        }

        @Override
        protected void onPostExecute(String msg) {
            Toast.makeText(otpVerification.this, msg, Toast.LENGTH_LONG).show();
        }
    }
}
