package com.example.mark_attendance.mark_attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ProjectCompletePackage.RuntimeDatabase;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Aritro Biswas on 04-10-2017.
 */

public class addAdminDetailsBegining extends AppCompatActivity{
    private static final String TAG = "addAdminDetailsBegining";

    private TextView adminFirstName, adminLastName, adminEmailID, commonObject;
    private RadioGroup adminGender;
    private EditText adminDOB;
    private TextView dept_textview;

    private EditText adminDept, adminDeptDesc, adminDeptLoc;
    private Spinner dept_spinner;

    private Button submitbutton;

    //for datepicker
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_admin_details);

        adminFirstName = (EditText) findViewById(R.id.firstname_edittext_addadmin);
        adminLastName = (EditText) findViewById(R.id.lastname_edittext_addadmin);
        adminEmailID = (EditText) findViewById(R.id.email_edittext_addadmin);

        adminGender = (RadioGroup) findViewById(R.id.gender_radiogroup_addadmin);

        adminDOB = (EditText) findViewById(R.id.dob_edittext_addadmin);

        dept_textview = (TextView) findViewById(R.id.department_textview_addadmin);

        adminDept = (EditText) findViewById(R.id.deptname_edittext_addadmin);
        adminDeptDesc = (EditText) findViewById(R.id.deptdesc_edittext_addadmin);
        adminDeptLoc = (EditText) findViewById(R.id.deptlocation_edittext_addadmin);

        dept_spinner = (Spinner) findViewById(R.id.dept_spinner_addadmin);

        submitbutton = (Button) findViewById(R.id.submit_button_addadmin);

        setVisibility();

        //date picker
        mDateSetListener=new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year);
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

        submitbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AddAdminDetails().execute();
            }
        });

    }


    public void setVisibility(){
        adminDept.setVisibility(VISIBLE);
        adminDeptDesc.setVisibility(VISIBLE);
        adminDeptLoc.setVisibility(VISIBLE);

        dept_spinner.setVisibility(INVISIBLE);

        dept_textview.setText("");
    }

    public void datePickerMethod(final EditText obj){
        commonObject = obj;
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dailog=new DatePickerDialog(addAdminDetailsBegining.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
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
        RadioButton commonAdminGender = (RadioButton) findViewById(selectedId);
        return commonAdminGender.getText().toString();
    }

    class AddAdminDetails extends AsyncTask<Void, String, String> {

        String FirstName, LastName, Email, Gender, Department, DepartmentDesc, DepartmentLoc, PreviousDepartment;
        Date DOB;

        @Override
        protected void onPreExecute() {
            FirstName = adminFirstName.getText().toString();
            LastName = adminLastName.getText().toString();
            Email = adminEmailID.getText().toString();
            Gender = getSelectedRadioButtonText();
            Department = adminDept.getText().toString();
            DepartmentDesc = adminDeptDesc.getText().toString();
            DepartmentLoc = adminDeptLoc.getText().toString();
            DOB = getDateFromEditText(adminDOB);
        }
        @Override
        protected String doInBackground(Void... params) {
            try{
                boolean status;

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
                        Intent openLoginPage = new Intent(getApplicationContext(),mainActivity.class);
                        startActivity(openLoginPage);
                        finish();
                    }
                }
            }
        }
    }
}
