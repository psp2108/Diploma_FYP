package com.example.mark_attendance.mark_attendance;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ProjectCompletePackage.MessageDetails;
import ProjectCompletePackage.MessageQueueCustomClass;
import ProjectCompletePackage.RuntimeDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class removeBiometricFragment extends Fragment {

    private Spinner emp_spinner ;
    ArrayAdapter<String> adapter;

    private GetAndFillEmployeeList getAndFillEmployeeList = null;
    private View removeBiometricView;
    private Button removebiometricsbtn;
    public removeBiometricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        removeBiometricView = inflater.inflate(R.layout.fragment_remove_biometric, container, false);
        removebiometricsbtn = (Button) removeBiometricView.findViewById(R.id.remove_button_removebio);
        removebiometricsbtn.setEnabled(false);
        removebiometricsbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createAndShowAlertDialog();
            }
        });


        emp_spinner = (Spinner) removeBiometricView.findViewById(R.id.emp_spinner_removebio);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        emp_spinner.setAdapter(adapter);
        getAndFillEmployeeList = new GetAndFillEmployeeList();
        getAndFillEmployeeList.execute();

        return removeBiometricView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // emp_spinner = (Spinner) getView().findViewById(R.id.emp_spinner_removebio);

    }

    private void createAndShowAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Remove Biometrics");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                new RemoveEmployeeBiometric().execute();
                dialog.dismiss();
                //markAttendenceMethod();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void fillEmpSpinner(String ...values){
        adapter.clear();
        for(String value: values){
            adapter.add(value);
        }
    }


    class GetAndFillEmployeeList extends AsyncTask<Void, String, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                return RuntimeDatabase.mainComClass.getEmployeesList(RuntimeDatabase.AdminUsername);
            }
            catch(Exception ex){
                String s[] = new String[1];
                s[0] = ex.toString();
                return s;
            }
        }

        @Override
        protected void onPostExecute(String[] list) {
            removebiometricsbtn.setEnabled(false);
            adapter.clear();

            if(list != null){
                if(list.length > 0) {
                    if (!list[0].contains("Exception")) {
                        removebiometricsbtn.setEnabled(true);
                        fillEmpSpinner(list);
                    } else {
                        Toast.makeText(getActivity(), list[0], Toast.LENGTH_LONG).show();
                    }
                }
            }
            getAndFillEmployeeList = null;
        }
    }

    class RemoveEmployeeBiometric extends AsyncTask<Void, String, Boolean> {
        String empDetails;

        @Override
        protected void onPreExecute() {
            empDetails = emp_spinner.getSelectedItem().toString();
            MessageQueueCustomClass.clearAll();
            mainActivity.usbService.write(MessageDetails.EXIT_CURRENTMODE.getBytes());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean finalStatus = false;

            try {
                Thread.sleep(800);
                while (true) {
                    if (MessageQueueCustomClass.isNewMessageArrived()) {
                        if (MessageQueueCustomClass.getFirstMessage().equals(MessageDetails.READY_FOR_COMMANDS)) {
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        return false;
                    }
                }

                mainActivity.usbService.write(MessageDetails.DELETE_FINGER_PRINT.getBytes());
                Thread.sleep(800);
                while (true) {
                    if (MessageQueueCustomClass.isNewMessageArrived()) {
                        if (MessageQueueCustomClass.getFirstMessage().equals(MessageDetails.ENTER_FPID_TODELETE)) {
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        return false;
                    }
                }

                mainActivity.usbService.write(RuntimeDatabase.mainComClass.getEmployeeSensorID(empDetails).getBytes());
                Thread.sleep(800);
                while (true) {
                    if (MessageQueueCustomClass.isNewMessageArrived()) {
                        if (MessageQueueCustomClass.getFirstMessage().equals(MessageDetails.FINGERPRINT_DELETED)) {
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        return false;
                    }
                }

                finalStatus = RuntimeDatabase.mainComClass.removeEmployeeBiometrics(empDetails, RuntimeDatabase.AdminUsername);

            } catch (Exception ex) {
                return false;
            }

            /*try {
                if (finalStatus) {
                    String newList[] = RuntimeDatabase.mainComClass.getEmployeesList(RuntimeDatabase.AdminUsername);
                    publishProgress(newList);
                }
            }
            catch (Exception ex) {
                publishProgress("ERROR","Failed to update List: " + ex.toString());
            }*/
            return finalStatus;
        }
/*
        @Override
        protected void onProgressUpdate(String[] list) {
            removebiometricsbtn.setEnabled(false);
            if(list != null){
                if(list.length > 0) {
                    if (!list[0].contains("ERROR")) {
                        removebiometricsbtn.setEnabled(true);
                        fillEmpSpinner(list);
                    } else {
                        Toast.makeText(getActivity(), list[0], Toast.LENGTH_LONG).show();
                    }
                }
            }
        }*/

        @Override
        protected void onPostExecute(Boolean status) {
            if(status){
                Toast.makeText(getActivity(), "Biometrics Removed", Toast.LENGTH_LONG).show();
                getAndFillEmployeeList = new GetAndFillEmployeeList();
                getAndFillEmployeeList.execute();
            }
            else{
                Toast.makeText(getActivity(), "Failed to remove Biometric", Toast.LENGTH_LONG).show();
            }
        }
    }
}
