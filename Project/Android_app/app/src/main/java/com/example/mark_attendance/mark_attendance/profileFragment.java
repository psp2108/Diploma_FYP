package com.example.mark_attendance.mark_attendance;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ProjectCompletePackage.RuntimeDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {
    private Button changepass;
    private Button editprofile;
    private View profileview;
    private TextView FirstName, LastName, Email, DOB, Gender, Department;
    public static String whiteSpace = "      ";
    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        profileview = inflater.inflate(R.layout.fragment_profile, container, false);
        changepass = (Button)profileview.findViewById(R.id.changepass_btn_profile);
        editprofile = (Button)profileview.findViewById(R.id.editdetails_btn_profile);

        FirstName = (TextView) profileview.findViewById(R.id.fname_data_profile);
        LastName = (TextView) profileview.findViewById(R.id.lname_data_profile);
        Email = (TextView) profileview.findViewById(R.id.email_data_profile);
        DOB = (TextView) profileview.findViewById(R.id.dob_data_profile);
        Gender = (TextView) profileview.findViewById(R.id.gender_data_profile);
        Department = (TextView) profileview.findViewById(R.id.dept_data_profile);

        new GetAdminProfile().execute();

        changepass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeExistingPassMethod();
            }
        });
        editprofile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editProfileMethod();
            }
        });

        return profileview;
    }
    public void changeExistingPassMethod(){
        ((drawerMain) getActivity()).setActionBarTitle("Change Password");
        changeExistingPassFragment changeexistingpass = new changeExistingPassFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                changeexistingpass,
                changeexistingpass.getTag()
        ).addToBackStack("fragBack").commit();
    }
    public void editProfileMethod(){
        ((drawerMain) getActivity()).setActionBarTitle("Edit Admin Details");
        editAdminDetailsFragment editadmin = new editAdminDetailsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                editadmin,
                editadmin.getTag()
        ).addToBackStack("fragBack").commit();
    }

    class GetAdminProfile extends AsyncTask<Void, String, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                return RuntimeDatabase.mainComClass.getAdminProfile(RuntimeDatabase.AdminUsername);
            }
            catch(Exception ex){
                String s[] = new String[1];
                s[0] = ex.toString();
                return s;
            }
        }

        @Override
        protected void onPostExecute(String[] list) {
            if(!list[0].contains("Exception")){
                FirstName.setText(whiteSpace + list[0]);
                LastName.setText(whiteSpace + list[1]);
                Email.setText(whiteSpace + list[2]);
                DOB.setText(whiteSpace + list[3]);
                Gender.setText(whiteSpace + list[4]);
                Department.setText(whiteSpace + list[list.length - 1]);
            }
        }
    }

}
