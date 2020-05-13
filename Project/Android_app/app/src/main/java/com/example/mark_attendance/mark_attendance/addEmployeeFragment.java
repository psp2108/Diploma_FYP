package com.example.mark_attendance.mark_attendance;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.*;
import java.text.*;

import ProjectCompletePackage.RuntimeDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class addEmployeeFragment extends Fragment {
    private static final String tag= "Emp Details Fragment";

    //for datepicker
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private View addempview;

    private EditText empFirstName, empLastName, empPSNumber, empEmailId;
    private Spinner dept_spinner;
    ArrayAdapter<String> adapter;
    private RadioGroup empGender;
    private EditText empDOB, empHiringDate, empHireTillDate, commonObject;
    private Button nextbuttonaddemployee;


    public addEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addempview = inflater.inflate(R.layout.fragment_add_employee, container, false);

        empFirstName = (EditText) addempview.findViewById(R.id.firstname_edittext_addemp);
        empLastName = (EditText) addempview.findViewById(R.id.lastname_edittext_addemp);
        empPSNumber = (EditText) addempview.findViewById(R.id.psno_edittext_addemp);
        empEmailId = (EditText) addempview.findViewById(R.id.email_edittext_addemp);

        empGender = (RadioGroup) addempview.findViewById(R.id.gender_radiogroup_addemp);

        dept_spinner = (Spinner) addempview.findViewById(R.id.dept_spinner_addemp);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        dept_spinner.setAdapter(adapter);
        new GetAndFillDepartmentList().execute();


        empDOB= (EditText) addempview.findViewById (R.id.dob_editdext_addemp);
        empHiringDate= (EditText) addempview.findViewById (R.id.hiredate_editdext_addemp);
        empHireTillDate= (EditText) addempview.findViewById (R.id.hiredtill_editdext_addemp);

        nextbuttonaddemployee = (Button) addempview.findViewById(R.id.next_button_addemp);
        nextbuttonaddemployee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AddEmployee().execute();
            }
        });

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

        empDOB.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    datePickerMethod(empDOB);
            }
        });
        empDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerMethod(empDOB);
            }
        });
        empHiringDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    datePickerMethod(empHiringDate);
            }
        });
        empHiringDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerMethod(empHiringDate);
            }
        });
        empHireTillDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    datePickerMethod(empHireTillDate);
            }
        });
        empHireTillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerMethod(empHireTillDate);
            }
        });

        return addempview;
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
        int selectedId = empGender.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton commonEmpGender = (RadioButton) addempview.findViewById(selectedId);
        return commonEmpGender.getText().toString();
    }

    public void enrollBiometricsMethod(){
        ((drawerMain) getActivity()).setActionBarTitle("Enroll Biometrics");
        enrollBiometricsFragment enrollbiometrics = new enrollBiometricsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                enrollbiometrics,
                enrollbiometrics.getTag()
        ).commit();
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

    class AddEmployee extends AsyncTask<Void, String, String> {

        String FirstName, LastName, PSNo, Email, Department, Gender;
        Date DOB, HireFrom, HireTill;

        @Override
        protected void onPreExecute() {
            FirstName = empFirstName.getText().toString();
            LastName = empLastName.getText().toString();
            PSNo = empPSNumber.getText().toString();
            Email = empEmailId.getText().toString();
            Department = dept_spinner.getSelectedItem().toString();
            Gender = getSelectedRadioButtonText();
            DOB = getDateFromEditText(empDOB);
            HireFrom = getDateFromEditText(empHiringDate);
            HireTill = getDateFromEditText(empHireTillDate);
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                int fpid = RuntimeDatabase.mainComClass.addEmployee(
                        FirstName,
                        LastName,
                        PSNo,
                        Department,
                        Email,
                        DOB,
                        Gender,
                        HireFrom,
                        HireTill,
                        RuntimeDatabase.AdminUsername);

                return fpid + "";
            }
            catch(Exception ex){
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                if(!s.contains("Exception")){
                    int fpid = Integer.parseInt(s);
                    if(fpid != -1){
                        RuntimeDatabase.EmployeeFingerprintID = fpid;
                        enrollBiometricsMethod();
                    }
                }
            }
        }
    }
}
