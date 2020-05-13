package com.example.mark_attendance.mark_attendance;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ProjectCompletePackage.RuntimeDatabase;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class addAdminDetailsFragment extends Fragment {
    private static final String tag= "Admin Details Fragment";
    private String toSelectDept = "Select Institutee";
    private String toAddDept = "Add New Institute";

    private View addadmindetails_view;

    private TextView adminFirstName, adminLastName, adminEmailID, commonObject;
    private RadioGroup adminGender;
    private EditText adminDOB;
    private TextView dept_textview;

    private EditText adminDept, adminDeptDesc, adminDeptLoc;
    private Spinner dept_spinner;
    ArrayAdapter<String> adapter;

    private Button submitbutton;

    //for datepicker
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public addAdminDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addadmindetails_view = inflater.inflate(R.layout.fragment_add_admin_details, container, false);

        adminFirstName = (EditText) addadmindetails_view.findViewById(R.id.firstname_edittext_addadmin);
        adminLastName = (EditText) addadmindetails_view.findViewById(R.id.lastname_edittext_addadmin);
        adminEmailID = (EditText) addadmindetails_view.findViewById(R.id.email_edittext_addadmin);

        adminGender = (RadioGroup) addadmindetails_view.findViewById(R.id.gender_radiogroup_addadmin);

        adminDOB = (EditText) addadmindetails_view.findViewById(R.id.dob_edittext_addadmin);

        dept_textview = (TextView) addadmindetails_view.findViewById(R.id.department_textview_addadmin);

        adminDept = (EditText) addadmindetails_view.findViewById(R.id.deptname_edittext_addadmin);
        adminDeptDesc = (EditText) addadmindetails_view.findViewById(R.id.deptdesc_edittext_addadmin);
        adminDeptLoc = (EditText) addadmindetails_view.findViewById(R.id.deptlocation_edittext_addadmin);

        dept_spinner = (Spinner) addadmindetails_view.findViewById(R.id.dept_spinner_addadmin);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        dept_spinner.setAdapter(adapter);
        new GetAndFillDepartmentList().execute();

        submitbutton = (Button) addadmindetails_view.findViewById(R.id.submit_button_addadmin);

        //fillDeptSpinner();
        setVisibility(true, toAddDept);

        //date picker
        mDateSetListener=new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                Log.d(tag,"onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year);
                String date = year+ "-" + month + "-" + day;
                commonObject.setText(date);
            }
        };

        adminDOB.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    datePickerMethod(adminDOB);
            }
        });

        adminDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerMethod(adminDOB);
            }
        });

        dept_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(true, null);
            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AddAdminDetails().execute();
            }
        });
        return addadmindetails_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setVisibility(boolean changeText, String CurrentText){
        String currText;
        if(CurrentText == null) {
            currText = dept_textview.getText().toString();
        }
        else{
            currText = CurrentText;
        }

        if(changeText) {
            if (currText.equals(toSelectDept)) {
                adminDept.setVisibility(INVISIBLE);
                adminDeptDesc.setVisibility(INVISIBLE);
                adminDeptLoc.setVisibility(INVISIBLE);

                dept_spinner.setVisibility(VISIBLE);

                dept_textview.setText(toAddDept);
            } else {
                adminDept.setVisibility(VISIBLE);
                adminDeptDesc.setVisibility(VISIBLE);
                adminDeptLoc.setVisibility(VISIBLE);

                dept_spinner.setVisibility(INVISIBLE);

                dept_textview.setText(toSelectDept);
            }
        }
        else{
            if (currText.equals(toAddDept)) {
                adminDept.setVisibility(INVISIBLE);
                adminDeptDesc.setVisibility(INVISIBLE);
                adminDeptLoc.setVisibility(INVISIBLE);

                dept_spinner.setVisibility(VISIBLE);
            } else {
                adminDept.setVisibility(VISIBLE);
                adminDeptDesc.setVisibility(VISIBLE);
                adminDeptLoc.setVisibility(VISIBLE);

                dept_spinner.setVisibility(INVISIBLE);
            }
        }
    }
    public void openProfileMethod(){
        ((drawerMain) getActivity()).setActionBarTitle("My Profile");
        profileFragment profilefragment = new profileFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                profilefragment,
                profilefragment.getTag()
        ).addToBackStack("fragBack").commit();
    }

    public void fillDeptSpinner(String ...values){
        adapter.clear();
        for(String value: values){
            adapter.add(value);
        }
    }

    public void datePickerMethod(final EditText obj){
        commonObject = obj;
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dailog=new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
        dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailog.show();
    }
    private Date getDateFromEditText(EditText obj){
        Date date = new Date();
        String d = obj.getText().toString();
        DateFormat df = new SimpleDateFormat("yyyy-M-d");

        try {
            return df.parse(d);
        }
        catch(Exception ex){
            return null;
        }
    }

    private String getSelectedRadioButtonText(){
        int selectedId = adminGender.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton commonAdminGender = (RadioButton) addadmindetails_view.findViewById(selectedId);
        return commonAdminGender.getText().toString();
    }

    class GetAndFillDepartmentList extends AsyncTask<Void, String, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                return RuntimeDatabase.mainComClass.getDepartmentList();
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
                fillDeptSpinner(list);
            }
            else{

            }
        }
    }

    class AddAdminDetails extends AsyncTask<Void, String, String> {

        String FirstName, LastName, Email, Gender, Department, DepartmentDesc, DepartmentLoc, PreviousDepartment;
        Date DOB;
        boolean statusOfSpinner;

        @Override
        protected void onPreExecute() {
            FirstName = adminFirstName.getText().toString();
            LastName = adminLastName.getText().toString();
            Email = adminEmailID.getText().toString();
            Gender = getSelectedRadioButtonText();
            Department = adminDept.getText().toString();
            DepartmentDesc = adminDeptDesc.getText().toString();
            DepartmentLoc = adminDeptLoc.getText().toString();
            PreviousDepartment = dept_spinner.getSelectedItem().toString();
            DOB = getDateFromEditText(adminDOB);
            statusOfSpinner = dept_spinner.getVisibility() == VISIBLE;
        }
        @Override
        protected String doInBackground(Void... params) {
            try{
                boolean status;

                if(statusOfSpinner){
                    status = RuntimeDatabase.mainComClass.addNewAdminDetails(
                            FirstName,
                            LastName,
                            RuntimeDatabase.NewAdminUsername,
                            Email,
                            DOB,
                            Gender,
                            PreviousDepartment
                    );
                }
                else{
                    status = RuntimeDatabase.mainComClass.addNewAdminDetails(
                            FirstName,
                            LastName,
                            RuntimeDatabase.NewAdminUsername,
                            Email,
                            DOB,
                            Gender,
                            Department,
                            DepartmentDesc,
                            DepartmentLoc
                    );

                }
                if (status)
                    return "Done";
                else
                    return "Unknown Error";
            }
            catch(Exception ex){
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                if(!s.contains("Exception")){
                    if(s.equals("Done")){
                        openProfileMethod();
                    }
                }
            }
        }
    }
}
