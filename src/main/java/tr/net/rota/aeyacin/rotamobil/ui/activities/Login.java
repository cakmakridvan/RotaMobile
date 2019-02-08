package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rampo.updatechecker.UpdateChecker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.func.PrefencesCrypt;
import tr.net.rota.aeyacin.rotamobil.model.DeviceMarker;
import tr.net.rota.aeyacin.rotamobil.model.sbt.GetLocation;
import tr.net.rota.aeyacin.rotamobil.model.sbt.LocationR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.SendVehicle;
import tr.net.rota.aeyacin.rotamobil.model.sbt.StatusR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.User;
import tr.net.rota.aeyacin.rotamobil.model.sbt.VehicleGroup;
import tr.net.rota.aeyacin.rotamobil.model.sbt.VehicleState;

import static tr.net.rota.aeyacin.rotamobil.Globals.MainCustomerAccountID;
import static tr.net.rota.aeyacin.rotamobil.data.server.GetLastDeviceMove;
import static tr.net.rota.aeyacin.rotamobil.data.server.GetListVehicleStatus;
import static tr.net.rota.aeyacin.rotamobil.data.server.GetUserVehicleRelation;
import static tr.net.rota.aeyacin.rotamobil.data.server.GetUsers;

public class Login extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Switch RememberMe;
    private TextView AlertText;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 20177;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AlertText = (TextView) findViewById(R.id.alertText);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        RememberMe = (Switch) findViewById(R.id.switchremember);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        try {
            if (!checkAndRequestPermissions()) {
                //  Toast.makeText(getApplicationContext(), "?zin Verilmedi", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
//        mEmailView.setText("fgursoy");
//        mPasswordView.setText("123");
    }

    @Override
    protected void onStart() {
        super.onStart();
        PrefencesCrypt saved = new PrefencesCrypt(this);
        if (!saved.getusermail().equalsIgnoreCase("") && !saved.getuserpass().equalsIgnoreCase("")) {
            RememberMe.setChecked(true);
            mEmailView.setText(saved.getusermail());
            mPasswordView.setText(saved.getuserpass());

        } else {
            RememberMe.setChecked(false);
        }
        try {


            UpdateChecker checker = new UpdateChecker(this);
            UpdateChecker.start();
        } catch (Exception e) {

        }


    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (RememberMe.isChecked()) {
            PrefencesCrypt saved = new PrefencesCrypt(this);
            saved.saveusermail(email);
            saved.saveuserpass(password);
        }

        boolean cancel = false;
        View focusView = null;
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        /*else if (!isEmailValid( email )) {
            mEmailView.setError( getString( R.string.error_invalid_email ) );
            focusView = mEmailView;
            cancel = true;
        }
*/
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            HideKeyboard(Login.this);
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private int stateResultError = 0;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                String webservice = GetUsers(mEmail, mPassword);
                if (!webservice.trim().equalsIgnoreCase("false")) {
                    Globals.user = new Gson().fromJson(webservice, User.class);

                    if (Globals.user.UserID == 0) {
                        stateResultError = 0;
                        return false;
                    }
                    if (!Globals.user.EntranceAuthority.equalsIgnoreCase("true")) {
                        stateResultError = 1;
                        return false;
                    }

                    List<VehicleState> VehicleStatelist = null;
                    try {
                        webservice = GetListVehicleStatus(Globals.user.CompanyID);
                        if (!webservice.equalsIgnoreCase("false")) {
                            Type collectionType_VehicleState = new TypeToken<List<VehicleState>>() {
                            }.getType();
                            VehicleStatelist = new Gson().fromJson(webservice, collectionType_VehicleState);

                        }
                    } catch (Exception ex) {

                    }

                    webservice = GetUserVehicleRelation(Globals.user.UserID, Globals.user.UserType, Globals.user.CompanyID);
                    Type collectionType_VehicleGroup = new TypeToken<List<VehicleGroup>>() {
                    }.getType();
                    Globals.groups = new Gson().fromJson(webservice, collectionType_VehicleGroup);


                    for (int i = 0; i < Globals.groups.size(); i++) {

                        for (int xx = 0; xx < Globals.groups.get(i).Vehicle.size(); xx++) {
                            if (VehicleStatelist != null)
                                for (int zz = 0; zz < VehicleStatelist.size(); zz++) {
                                    if (Globals.groups.get(i).Vehicle.get(xx).VehicleID == VehicleStatelist.get(zz).VehicleId) {
                                        Globals.groups.get(i).Vehicle.get(xx).IsAvaibleEngineBlokage = true;
                                        break;
                                    }
                                }

                            Globals.SendList.add(new SendVehicle(Globals.groups.get(i).Vehicle.get(xx).VehicleID));

                            DeviceMarker tt = new DeviceMarker();
                            tt.device = Globals.groups.get(i).Vehicle.get(xx);
                            tt.GroupID = Globals.groups.get(i).GroupID;
                            Globals.DevicesVehicleMap.put(Globals.groups.get(i).Vehicle.get(xx).VehicleID, tt);

                        }
                    }

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

                    webservice = GetLastDeviceMove(new Gson().toJson(Globals.SendList), Globals.user.CompanyID);
                    if (!webservice.equalsIgnoreCase("false")) {


                        Type collectionType_LocationR = new TypeToken<List<GetLocation>>() {
                        }.getType();
                        List<GetLocation> locationlist = gson.fromJson(webservice, collectionType_LocationR);

                    /*
                    webservice = GetLastMotion( new Gson().toJson( Globals.SendList ) );

                    Type collectionType_Status = new TypeToken<List<StatusR>>() {
                    }.getType();

                    List<StatusR> statuslist = gson.fromJson( webservice, collectionType_Status );
*/
                        //   Globals.LastLocationList = locationlist;
                        for (int yy = 0; yy < locationlist.size(); yy++) {
                      /*  DeviceMarker tmp = Globals.DevicesVehicleMap.get( locationlist.get( yy ).VehicleID );
                        tmp.location = locationlist.get( yy );
                        Globals.DevicesVehicleMap.put( locationlist.get( yy ).VehicleID, tmp );
                        */

                            LocationR loc = new LocationR();
                            StatusR stat = new StatusR();
                            loc.Speed = locationlist.get(yy).Speed;
                            loc.Angle = locationlist.get(yy).Angle;
                            loc.VehicleID = locationlist.get(yy).VehicleID;
                            loc.DeviceDateTime = locationlist.get(yy).DeviceDateTime;
                            loc.Latitude = locationlist.get(yy).Latitude;
                            loc.Longitude = locationlist.get(yy).Longitude;
                            loc.SatelliteCount = locationlist.get(yy).SatelliteCount;

                            stat.BatteryCharging = true;
                            stat.DateTime = locationlist.get(yy).StatusDateTime;
                            stat.GPSState = true;
                            stat.Ignition = locationlist.get(yy).Ignition;
                            stat.VehicleID = locationlist.get(yy).VehicleID;

                            if (Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID) != null) {
                                Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).VehicleImage = locationlist.get(yy).VehicleImagePath;
                                Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).LastLocation = locationlist.get(yy);
                                Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).location = loc;
                                Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).status = stat;
                            }


                        }
                    }


                    return true;
                }
                //  Thread.sleep( 2000 );
            } catch (Exception e) {
                return false;
            }
/*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split( ":" );
                if (pieces[0].equals( mEmail )) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals( mPassword );
                }
            }
*/
            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;


            if (success) {
                MainCustomerAccountID = Globals.user.CompanyID;

                //startActivity(new Intent(getApplicationContext(), Main.class));
                Intent go_main = new Intent(getApplicationContext(),Main.class);
                //send Customer AccountID
                Bundle c = new Bundle();
                c.putInt("CustomerID",MainCustomerAccountID);
                go_main.putExtras(c);
                startActivity(go_main);
                finish();

                //  showProgress( false );
            } else {
                if (stateResultError == 1) {
                    AlertText.setText(Globals.user.EntranceAuthority);
                } else if (stateResultError == 0) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }


                showProgress(false);
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean checkAndRequestPermissions() {
        int permissionINTERNET = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
        int permissionACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionACCESS_VIBRATE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.VIBRATE);
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int permissionACCESS_LOCATION_EXTRA_COMMANDS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        int permissionACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionACCESS_WIFI_STATE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE);
        int permissionCHANGE_WIFI_STATE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CHANGE_WIFI_STATE);
        int permissionREAD_PHONE_STATE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        int permissionRECEIVE_BOOT_COMPLETED = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED);
        int permissionBLUETOOTH_ADMIN = ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN);
        int permissionBLUETOOTH = ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH);
        List<String> listPermissionsNeeded = new ArrayList<String>();
        if (permissionINTERNET != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
        }
        if (permissionACCESS_NETWORK_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (permissionACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            //      listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionACCESS_VIBRATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.VIBRATE);
        }
        if (permissionACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionACCESS_LOCATION_EXTRA_COMMANDS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        }
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    public static void HideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
