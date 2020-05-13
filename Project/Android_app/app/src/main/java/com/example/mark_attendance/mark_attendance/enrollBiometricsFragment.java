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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ProjectCompletePackage.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class enrollBiometricsFragment extends Fragment {

    private Button submitbtn;
    private View enrollbiometrics_view;
    private TextView ArduinoMessageView;
    public enrollBiometricsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        enrollbiometrics_view = inflater.inflate(R.layout.fragment_enroll_biometrics, container, false);

        ArduinoMessageView = (TextView) enrollbiometrics_view.findViewById(R.id.addemp_arduino_msg_status);
        submitbtn = (Button) enrollbiometrics_view.findViewById(R.id.submit_button_enrollbiometrics);

        submitbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addEmployee();
                //createAndShowAlertDialog();
            }
        });

        new EnrollFingerPrint().execute();

        return enrollbiometrics_view;
    }

    private void createAndShowAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Submit Details");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                markAttendenceMethod();
                dialog.dismiss();

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

    public void markAttendenceMethod(){
        ((drawerMain) getActivity()).setActionBarTitle("Mark Attendence");
        markAttendenceFragment markattendence = new markAttendenceFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                markattendence,
                markattendence.getTag()
        ).addToBackStack("fragBack").commit();
    }

    public void addEmployee(){
        ((drawerMain) getActivity()).setActionBarTitle("Add Employee");
        addEmployeeFragment addemployee = new addEmployeeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                addemployee,
                addemployee.getTag()
        ).commit();
    }



    class EnrollFingerPrint extends AsyncTask<Void, String, String> {
        String msg;

        @Override
        protected void onPreExecute() {
            MessageQueueCustomClass.clearAll();
            mainActivity.usbService.write(MessageDetails.EXIT_CURRENTMODE.getBytes());
        }

        @Override
        protected String doInBackground(Void... params) {
            boolean flag = true;
            try{
                mainloop:
                while(true) {
                    while (true) {
                        Thread.sleep(80);
                        if (MessageQueueCustomClass.isNewMessageArrived()) {
                            if (MessageQueueCustomClass.getFirstMessage().equals(MessageDetails.READY_FOR_COMMANDS)) {
                                Thread.sleep(80);
                                break;
                            } else {
                                mainActivity.usbService.write(MessageDetails.EXIT_CURRENTMODE.getBytes());
                                continue;
                            }
                        }
                    }

                    mainActivity.usbService.write(MessageDetails.ENROLL_NEW_FINGERPRINT.getBytes());

                    while (true) {
                        Thread.sleep(160);
                        if (MessageQueueCustomClass.isNewMessageArrived()) {
                            if (MessageQueueCustomClass.getFirstMessage().equals(MessageDetails.ENTER_FPID_TOENROLL)) {
                                Thread.sleep(80);
                                break;
                            } else {
                                mainActivity.usbService.write(MessageDetails.ENROLL_NEW_FINGERPRINT.getBytes());
                                continue;
                            }
                        }
                    }

                    mainActivity.usbService.write((RuntimeDatabase.EmployeeFingerprintID + "").getBytes());

                    while (true) {
                        Thread.sleep(200);
                        if (MessageQueueCustomClass.isNewMessageArrived()) {
                            if (MessageQueueCustomClass.getFirstMessage().equals(MessageDetails.PLACE_FINGER)) {
                                break;
                            } else {
                                continue;
                            }
                        }
                        mainActivity.usbService.write((RuntimeDatabase.EmployeeFingerprintID + "").getBytes());
                    }

                    if(flag){
                        //update status to place finger
                        publishProgress("Place your finger.");
                        flag = false;
                    }
                    else{
                        //update status to retry
                        publishProgress("Failed to enroll! Place finger again.");
                    }

                    while (true) {
                        if (MessageQueueCustomClass.isNewMessageArrived()) {
                            msg = MessageQueueCustomClass.getFirstMessage();
                            if (msg.equals(MessageDetails.PLACE_SAME_FINGER)) {
                                break;
                            }
                            else if(msg.equals(MessageDetails.REMOVE_FINGER)){
                                publishProgress("Remove your finger.");
                            }
                        }
                    }
                    //update status to place same finger again
                    publishProgress("Place same finger again.");

                    finalSubLoop:
                    while (true) {
                        if (MessageQueueCustomClass.isNewMessageArrived()) {
                            msg = MessageQueueCustomClass.getFirstMessage();
                            if (msg.equals(MessageDetails.REMOVE_FINGER)){
                                publishProgress("Remove your finger.");
                                continue finalSubLoop;
                            }
                            else if (msg.equals(MessageDetails.READY_FOR_COMMANDS)){
                                continue finalSubLoop;
                            }
                            else if (msg.equals(MessageDetails.ENROLL_SUCCESSFUL)) {
                                //update status to enroll successful
                                publishProgress("Finger print enrolling successful.");
                                break mainloop;
                            } else {
                                //publishProgress(msg,"1");
                                continue mainloop;
                            }
                        }
                    }
                }
                return "Done";
            }
            catch(Exception ex){
                return ex.toString();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values.length == 1)
                ArduinoMessageView.setText(values[0]);
            else
                Toast.makeText(getActivity(), values[0], Toast.LENGTH_LONG).show();
        }
    }

}
