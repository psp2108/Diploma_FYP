package com.example.mark_attendance.mark_attendance;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ProjectCompletePackage.RuntimeDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class addNewAdminIDFragment extends Fragment {
    private View addnewadminview;

    private EditText adminUsername;
    TextView checkUsername;
    private Button checkUsernameAvailability;
    private EditText adminPassword, adminConfirmPassword;
    private Button nextbutton;
    private String uid, pwd;

    public addNewAdminIDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addnewadminview = inflater.inflate(R.layout.fragment_add_new_admin, container, false);

        adminUsername = (EditText) addnewadminview.findViewById(R.id.input_username_newadmin);
        checkUsername = (TextView) addnewadminview.findViewById(R.id.username_status_newadmin);
        checkUsernameAvailability = (Button) addnewadminview.findViewById(R.id.btn_getOTP);

        adminPassword = (EditText) addnewadminview.findViewById(R.id.input_emailedOTP_admin);
        adminConfirmPassword = (EditText) addnewadminview.findViewById(R.id.input_confirmpass_newadmin);
        nextbutton = (Button) addnewadminview.findViewById(R.id.next_button_addemp);
        nextbutton.setEnabled(false);
        checkUsernameAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckAdminUsernameAvailability().execute();
            }
        });

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
                    Toast.makeText(getActivity(), "Passwords didn't matched", Toast.LENGTH_LONG).show();
            }
        });
        return addnewadminview;
    }

    public void addNewAdminMethod(){
        ((drawerMain) getActivity()).setActionBarTitle("Add Admin Details");
        addAdminDetailsFragment addadmindetails = new addAdminDetailsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                addadmindetails,
                addadmindetails.getTag()
        ).addToBackStack("fragBack").commit();
    }

    class AddAdminCredentials extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
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
                        addNewAdminMethod();
                    }
                }
            }
        }
    }

    class CheckAdminUsernameAvailability extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            uid = adminUsername.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                if(uid.trim().equals("")){
                    return "Username Invalid";
                }
                boolean Available = RuntimeDatabase.mainComClass.ifUsernameAvailable(uid);
                if(Available)
                    return "Username is available";
                else
                    return "Username Taken";
            }
            catch(Exception ex){
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.contains("Exception")){
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                nextbutton.setEnabled(false);
            }
            else{
                checkUsername.setText(s);
                if(s.equals("Username is available")){
                    nextbutton.setEnabled(true);
                }
                else{
                    nextbutton.setEnabled(false);
                }
            }
        }
    }
}
