package com.example.mark_attendance.mark_attendance;


import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import ProjectCompletePackage.MessageDetails;
import ProjectCompletePackage.MessageQueueCustomClass;
import ProjectCompletePackage.RuntimeDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class markAttendenceFragment extends Fragment {

    private ImageView imageView;
    private View markattendence_view;
    private TextView ArduinoMsgStatus;
    private static final String TAG = "markAttendenceFragment";
    private MarkEmployeeAttendanceAsync markEmployeeAttendanceAsync = null;

    private static Handler myHandler;

    public markAttendenceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        markattendence_view = inflater.inflate(R.layout.fragment_mark_attendence, container, false);
        markEmployeeAttendanceAsync = new MarkEmployeeAttendanceAsync();
        markEmployeeAttendanceAsync.execute();

        ArduinoMsgStatus = (TextView)  markattendence_view.findViewById(R.id.attendance_Status_TextView);


        return markattendence_view;

    }
    //Call this method for verification of employee Biometrics
    /*private void createAndShowAlertDialog(String empPs,String empName) {
        String empDetails = "Are you: "+empName+" with PS No.: "+empPs;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(empDetails);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO OK
                //new MarkEmployeeAttendance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                new Thread(new MarkEmployeeAttendance2()).start();
//                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO NO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "On Pause called");

        if(markEmployeeAttendanceAsync != null) {
            markEmployeeAttendanceAsync.cancel(true);
            markEmployeeAttendanceAsync.cancel(false);
            markEmployeeAttendanceAsync = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "On resume called");

        if(markEmployeeAttendanceAsync == null){
            markEmployeeAttendanceAsync = new MarkEmployeeAttendanceAsync();
            markEmployeeAttendanceAsync.execute();
        }
    }

    class MarkEmployeeAttendanceAsync extends AsyncTask<Void, String, String> {
        String msg;
        boolean status;

        @Override
        protected void onPreExecute() {
            MessageQueueCustomClass.clearAll();
            mainActivity.usbService.write(MessageDetails.EXIT_CURRENTMODE.getBytes());
        }

        @Override
        protected String doInBackground(Void... params) {
            boolean flag = true;
            try{
                publishProgress("MSG","Getting sensor ready..");
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

                mainActivity.usbService.write(MessageDetails.GET_FINGERPRINT_ID.getBytes());
                publishProgress("MSG","Place your finger..");

                while(!isCancelled()) {
                    if (MessageQueueCustomClass.isNewMessageArrived()) {
                        msg = MessageQueueCustomClass.getFirstMessage();

                        if(msg.length() > 1){
                            int id = Integer.parseInt(msg);
                            RuntimeDatabase.EmployeeFingerprintID = id;
                            String empDetails[];
                            //empDetails = RuntimeDatabase.mainComClass.getEmployeeDetails(RuntimeDatabase.AdminUsername, RuntimeDatabase.EmployeeFingerprintID);

                            //publishProgress("ID",empDetails[0],empDetails[1],empDetails[2], msg);

                            status = RuntimeDatabase.mainComClass.markAttendance(RuntimeDatabase.AdminUsername, RuntimeDatabase.EmployeeFingerprintID);
                            if(status){
                                publishProgress("S","Attendance Marked");
                            }
                            else{
                                publishProgress("S","Attendance not Marked, Try Again");
                            }
                            //publishProgress(msg);
                        }
                        else if(msg.equals(MessageDetails.NO_MATCH_FOUND)){
                            publishProgress("MSG","Finger Print Not Matched");
                            Thread.sleep(1000);
                            publishProgress("MSG","Place your finger..");
                        }
                        else if(msg.equals("")) {

                        }
                        else{
                            publishProgress("MSG","Unknown Error");
                            Thread.sleep(1000);
                            publishProgress("MSG","Place your finger..");
                        }
                    }
                    //Thread.sleep(800);
                }
                return "Done";
            }
            catch(Exception ex){
                return ex.toString();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].equals("MSG")){
                ArduinoMsgStatus.setText(values[1]);
            }
            else if(values[0].equals("S")){
                Toast.makeText(getActivity(), values[1], Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(), values[0], Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            mainActivity.usbService.write(MessageDetails.EXIT_CURRENTMODE.getBytes());
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mainActivity.usbService.write(MessageDetails.EXIT_CURRENTMODE.getBytes());
        }
    }

}
