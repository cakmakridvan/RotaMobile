package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import tr.net.rota.aeyacin.rotamobil.R;

public class SplashActivity extends Activity {


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 20177;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
     //   supportRequestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView( R.layout.activity_splash );

        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );

     //   FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        try {
            if (!checkAndRequestPermissions()) {
                //  Toast.makeText(getApplicationContext(), "?zin Verilmedi", Toast.LENGTH_LONG).show();
            }
        //    Crashlytics.getInstance().crash(); // Force a crash

        }catch (Exception e){

        }
        new task().execute( "" );
    }


    private boolean checkAndRequestPermissions() {
        int permissionINTERNET = ContextCompat.checkSelfPermission( this, android.Manifest.permission.INTERNET );
        int permissionACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_NETWORK_STATE );
        int permissionACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION );
        int permissionACCESS_VIBRATE = ContextCompat.checkSelfPermission( this, android.Manifest.permission.VIBRATE );
        int permissionCAMERA = ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA );
        int permissionACCESS_LOCATION_EXTRA_COMMANDS = ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS );
        int permissionACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION );
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission( this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE );
        int permissionACCESS_WIFI_STATE = ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_WIFI_STATE );
        int permissionCHANGE_WIFI_STATE = ContextCompat.checkSelfPermission( this, android.Manifest.permission.CHANGE_WIFI_STATE );
        int permissionREAD_PHONE_STATE = ContextCompat.checkSelfPermission( this, android.Manifest.permission.READ_PHONE_STATE );
        int permissionRECEIVE_BOOT_COMPLETED = ContextCompat.checkSelfPermission( this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED );
        int permissionBLUETOOTH_ADMIN = ContextCompat.checkSelfPermission( this, android.Manifest.permission.BLUETOOTH_ADMIN );
        int permissionBLUETOOTH = ContextCompat.checkSelfPermission( this, android.Manifest.permission.BLUETOOTH );
        List<String> listPermissionsNeeded = new ArrayList<String>();
        if (permissionINTERNET != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( android.Manifest.permission.INTERNET );
        }
        if (permissionACCESS_NETWORK_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( android.Manifest.permission.ACCESS_NETWORK_STATE );
        }
        if (permissionACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( android.Manifest.permission.ACCESS_FINE_LOCATION );
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            //      listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionACCESS_VIBRATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( android.Manifest.permission.VIBRATE );
        }
        if (permissionACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( android.Manifest.permission.ACCESS_COARSE_LOCATION );
        }

        if (permissionACCESS_LOCATION_EXTRA_COMMANDS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS );
        }
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( android.Manifest.permission.WRITE_EXTERNAL_STORAGE );
        }
        if (permissionACCESS_WIFI_STATE != PackageManager.PERMISSION_GRANTED) {
            //     listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (permissionCHANGE_WIFI_STATE != PackageManager.PERMISSION_GRANTED) {
            //    listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE);
        }
        if (permissionREAD_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
            //     listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionRECEIVE_BOOT_COMPLETED != PackageManager.PERMISSION_GRANTED) {
            //    listPermissionsNeeded.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        }
        if (permissionBLUETOOTH_ADMIN != PackageManager.PERMISSION_GRANTED) {
            //     listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (permissionBLUETOOTH != PackageManager.PERMISSION_GRANTED) {
            //    listPermissionsNeeded.add(Manifest.permission.BLUETOOTH);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions( this, listPermissionsNeeded.toArray( new String[listPermissionsNeeded.size()] ), REQUEST_ID_MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext( newBase );
        MultiDex.install( this );
    }


    class task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                Thread.sleep( 2000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute( s );
            startActivity( new Intent( getApplicationContext(), Login.class ) );
            finish();
        }
    }
}
