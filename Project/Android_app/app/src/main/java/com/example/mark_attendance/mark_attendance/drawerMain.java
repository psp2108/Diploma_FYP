package com.example.mark_attendance.mark_attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ProjectCompletePackage.RuntimeDatabase;

public class drawerMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView adminFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p4_many_in_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Get Nav Header to change name and email id

        View header=navigationView.getHeaderView(0);
        adminFullName = (TextView)header.findViewById(R.id.username_textview_navheader);
        adminFullName.setText(RuntimeDatabase.adminFullName);


        markAttendenceMethod();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } /*else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_markattendance) {
            markAttendenceMethod();
        } else if (id == R.id.nav_newemployee) {
            newEmployeeMethod();
        } else if (id == R.id.nav_profile) {
            profileMethod();
        }else if (id == R.id.nav_removeempbio) {
            removeBioMethod();
        }else if (id == R.id.nav_newadmin) {
            addNewAdminMethod();
        }else if (id == R.id.nav_logout) {
            logoutMethod();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /************************/
//    public void markAttendenceMethod(){
//        getSupportActionBar().setTitle("Mark Attendence");
//        markAttendenceFragment markattendence = new markAttendenceFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(
//                R.id.relativelayout_content_drawer,
//                markattendence,
//                markattendence.getTag()
//        ).addToBackStack("fragBack").commit();
//    }
//
//
//    public void profileMethod(){
//        getSupportActionBar().setTitle("My Profile");
//        profileFragment profilefragment = new profileFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        fragmentManager.beginTransaction().replace(
//                R.id.relativelayout_content_drawer,
//                profilefragment,
//                profilefragment.getTag()
//        ).addToBackStack("fragBack").commit();
//    }
//    public void newEmployeeMethod(){
//        getSupportActionBar().setTitle("Add Student");
//        addEmployeeFragment addemployee = new addEmployeeFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(
//                R.id.relativelayout_content_drawer,
//                addemployee,
//                addemployee.getTag()
//        ).addToBackStack("fragBack").commit();
//    }
//    public void removeBioMethod(){
//        getSupportActionBar().setTitle("Remove Biometrics");
//        removeBiometricFragment removeBio = new removeBiometricFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(
//                R.id.relativelayout_content_drawer,
//                removeBio,
//                removeBio.getTag()
//        ).addToBackStack("fragBack").commit();
//    }
//    public void addNewAdminMethod(){
//        getSupportActionBar().setTitle("Add New Admin");
//        addNewAdminIDFragment addnewadminid = new addNewAdminIDFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(
//                R.id.relativelayout_content_drawer,
//                addnewadminid,
//                addnewadminid.getTag()
//        ).addToBackStack("fragBack").commit();
//    }
    /******************/

        public void markAttendenceMethod(){
        getSupportActionBar().setTitle("Mark Attendence");
        markAttendenceFragment markattendence = new markAttendenceFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                markattendence,
                markattendence.getTag()
        ).commit();
    }


    public void profileMethod(){
        getSupportActionBar().setTitle("My Profile");
        profileFragment profilefragment = new profileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                profilefragment,
                profilefragment.getTag()
        ).commit();
    }
    public void newEmployeeMethod(){
        getSupportActionBar().setTitle("Add Student");
        addEmployeeFragment addemployee = new addEmployeeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                addemployee,
                addemployee.getTag()
        ).commit();
    }
    public void removeBioMethod(){
        getSupportActionBar().setTitle("Remove Biometrics");
        removeBiometricFragment removeBio = new removeBiometricFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                removeBio,
                removeBio.getTag()
        ).commit();
    }
    public void addNewAdminMethod(){
        getSupportActionBar().setTitle("Add New Admin");
        addNewAdminIDFragment addnewadminid = new addNewAdminIDFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.relativelayout_content_drawer,
                addnewadminid,
                addnewadminid.getTag()
        ).commit();
    }
    /*******************/
    public void logoutMethod(){

//        Intent open_loginpage = new Intent(getApplicationContext(),mainActivity.class);
//        startActivity(open_loginpage);
        finish();
    }
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

}
