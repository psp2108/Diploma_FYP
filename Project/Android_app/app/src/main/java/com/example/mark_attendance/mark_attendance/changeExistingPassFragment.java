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
import android.widget.EditText;
import android.widget.Toast;

import ProjectCompletePackage.RuntimeDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class changeExistingPassFragment extends Fragment {

    private Button changebtn;
    private EditText oldPassword, newPassword, confirmNewPassword;

    public String oldPass, newPass, confirmNewPass;

    private View changeexistingpass_view;
    public changeExistingPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        changeexistingpass_view = inflater.inflate(R.layout.fragment_change_existing_pass, container, false);

        oldPassword = (EditText) changeexistingpass_view.findViewById(R.id.existingpass_edittext_changeexistingpass);
        newPassword = (EditText) changeexistingpass_view.findViewById(R.id.pass_edittext_changeexistingpass);
        confirmNewPassword = (EditText) changeexistingpass_view.findViewById(R.id.confpass_edittext_changeexistingpass);

        changebtn = (Button) changeexistingpass_view.findViewById(R.id.change_btn_changeexistingpass);
        changebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                oldPass = oldPassword.getText().toString();
                newPass = newPassword.getText().toString();
                confirmNewPass = confirmNewPassword.getText().toString();
                createAndShowAlertDialog();
            }
        });
        return changeexistingpass_view;
    }
    private void createAndShowAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Change Password");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO

                dialog.dismiss();

                if(newPass.equals(confirmNewPass)){
                    new ChangeAdminPassword().execute();
                }
                else{
                    Toast.makeText(getActivity(), "New Password doesn't matched", Toast.LENGTH_LONG).show();
                }


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
    public void openProfileMethod(){
        ((drawerMain) getActivity()).setActionBarTitle("My Profile");
        profileFragment profilefragment = new profileFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                profilefragment,
                profilefragment.getTag()
        ).commit();
    }

    class ChangeAdminPassword extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            try{
                String credentials = RuntimeDatabase.mainComClass.checkAdminCredentials(RuntimeDatabase.AdminUsername, oldPass);
                if(!credentials.equals("WRONG CREDENTIALS")){
                    String ifUpdated = RuntimeDatabase.mainComClass.updatePassword(RuntimeDatabase.AdminUsername, newPass);
                    if(ifUpdated.equals("Password Updated")){
                        return "Password Updated Successfully";
                    }
                    else{
                        return "Failed to update password";
                    }
                }
                else
                {
                    return "Wrong old password entered";
                }
            }
            catch(Exception ex) {
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                if(!s.equals("")){
                    Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

                    if(!s.contains("Exception")){
                        if(s.equals("Password Updated Successfully")){
                            openProfileMethod();
                        }
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Unknown Error", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "Unknown Error", Toast.LENGTH_LONG).show();
            }

        }
    }

}
