package com.example.mark_attendance.mark_attendance;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.*;
import java.util.*;

import ProjectCompletePackage.*;

//import static com.example.mark_attendance.mark_attendance.R.id.textView;

public class mainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";


    public static UsbService usbService;
    private MyHandler mHandler;
    private static final int PERMS_REQUEST_CODE = 10;

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        RuntimeDatabase.AdminUsername = "";

        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<mainActivity> mActivity;

        public MyHandler(mainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    Toast.makeText(mActivity.get(), "MESSAGE:-> " + data, Toast.LENGTH_LONG).show();
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.SYNC_READ:
                    String buffer = (String) msg.obj;
                    mActivity.get().onArduinoMessageReceived(buffer);
                    break;
            }
        }
    }

    private boolean hasPermissions(String[] permissions){
        int res = 0;

        for(String perms: permissions){
            res = checkCallingOrSelfPermission(perms);
            if(!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(String[] permissions){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch(requestCode){
            case PERMS_REQUEST_CODE:

                for(int res: grantResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;

            default:
                allowed = false;
                break;
        }

        if(allowed){
            //TODO: On Permission Granted
            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)){
                    Toast.makeText(getApplicationContext(),"Internet Permissions Denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onArduinoMessageReceived(String message){
        //TODO: on arduino message received
        //o.b(TAG,message);

        MessageQueueCustomClass.sendMessage(message);
    }

    private EditText username, password;
    private Button loginButton;
    private TextView ForgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_login);

        File dataFiles = this.getExternalFilesDir(null);
        RuntimeDatabase.mainComClass = new MainCommunicatingClass(dataFiles.getPath());

//        final String[] Permission = new String[]{
//                Manifest.permission.ACCESS_NETWORK_STATE,
//                Manifest.permission.ACCESS_WIFI_STATE,
//                Manifest.permission.INTERNET
//        };

//        if (!hasPermissions(Permission)){
//            requestPerms(Permission);
//        }

        username = (EditText) findViewById(R.id.userName_EditText_loginPage);
        password = (EditText) findViewById(R.id.pass_EditText_loginPage);
        loginButton = (Button) findViewById(R.id.login_button_loginPage);
        ForgetPassword = (TextView) findViewById(R.id.forgotpass_textView_loginpage);

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_newAdminPage = new Intent(getApplicationContext(),otpVerification.class);
                startActivity(open_newAdminPage);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CredentialCheck().execute();
            }
        });

        mHandler = new MyHandler(this);
        new CheckIfAdminAvailable().execute();
    }

    public void addNewAdminMethod(){
        getSupportActionBar().setTitle("Add New Admin");
        addNewAdminIDFragment addnewadminid = new addNewAdminIDFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.constrintlayout_login,
                addnewadminid,
                addnewadminid.getTag()
        ).addToBackStack("fragBack").commit();
    }

    class CredentialCheck extends AsyncTask<Void, String, String>{

        String uid, pwd;

        @Override
        protected void onPreExecute() {
            uid = username.getText().toString();
            pwd = password.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            String status = "", details[];
            try {
                status =  RuntimeDatabase.mainComClass.checkAdminCredentials(uid, pwd);
                details = RuntimeDatabase.mainComClass.getAdminProfile(uid);
                RuntimeDatabase.adminFullName = details[0] + " " + details[1];

            } catch (Exception e) {
                status = e.toString();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                if(!s.contains("Exception")){
                    if(!s.equals("WRONG CREDENTIALS")){
                        username.setText("");
                        password.setText("");
                        RuntimeDatabase.AdminUsername = uid;
                        Intent open_newAdminPage = new Intent(getApplicationContext(),drawerMain.class);
                        startActivity(open_newAdminPage);
                    }
                    else{
                        //Wrong Credentials
                        RuntimeDatabase.adminFullName = "";
                        Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    //Exception
                    RuntimeDatabase.adminFullName = "";
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
            else{
                //Unknown Error
                RuntimeDatabase.adminFullName = "";
                Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    class CheckIfAdminAvailable extends AsyncTask<Void, String, String>{
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                return RuntimeDatabase.mainComClass.checkIfAdminTableIsEmpty();
            }
            catch(Exception ex){
                return  ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                if(!s.contains("Exception")){
                    if(s.equals("true")){
                        //TODO: call Add admin page
                        Intent open_newAdminPage = new Intent(getApplicationContext(),addAdminBegining.class);
                        startActivity(open_newAdminPage);
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    }
                    else if(s.equals("false")){
                        Toast.makeText(getApplicationContext(),"Admin available",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Connection unknown error",Toast.LENGTH_LONG).show();
            }
        }
    }


}
