package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.android.segmented.SegmentedGroup;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.model.DeviceMarker;
import tr.net.rota.aeyacin.rotamobil.model.sbt.LocationR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.StatusR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.signalr.SignalrMessage;
import tr.net.rota.aeyacin.rotamobil.utils.LatLngInterpolator;
import tr.net.rota.aeyacin.rotamobil.utils.TransitionUtils;

import static tr.net.rota.aeyacin.rotamobil.utils.MarkerAnimation.animateMarkerToGB;


public class Main extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FloatingActionMenu menuRight;
    private Toolbar toolbar;
    public View rippleView;
    public TextView testview;
    private boolean launchedActivity;
    private int customerID = 0;
    boolean signal_con = false;
    public static String mConnectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customerID = Globals.user.CompanyID;
/*
        //get CustomerAccountID from Bundle
        Bundle c = getIntent().getExtras();
        if(c != null){

            customerID = c.getInt("CustomerID");
        }
*/
        SegmentedGroup seg_harita = findViewById(R.id.seg1);
        RadioButton radio_harita = findViewById(R.id.button23);
        radio_harita.setChecked(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //  startActivity( new Intent( getApplicationContext(), DeviceList.class ) );
                if (TransitionUtils.isAtLeastLollipop()) {
                    startRippleTransitionReveal();
                } else {
                    startActivity();
                }
            }
        });
        rippleView = findViewById(R.id.rippleView);

        seg_harita.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.button23:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.button24:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    default:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }

            }
        });


        initfloatmenu();
        //  initTestDataReal();


        testview = findViewById(R.id.testview);

        runhublistener();

      /*  Timer timer = new Timer();
         timer.schedule(doAsynchronousTask, 5000, 10000);
/*
        if (Globals.DevicesVehicleMap.size() < 1) {
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public void initfloatmenu() {
        menuRight = findViewById(R.id.menu_right);
        final com.github.clans.fab.FloatingActionButton
                OpenVehicleList = new com.github.clans.fab.FloatingActionButton(getApplicationContext());
        OpenVehicleList.setButtonSize(FloatingActionButton.SIZE_MINI);
        OpenVehicleList.setLabelText(getString(R.string.vehicle_list));
        OpenVehicleList.setButtonSize(com.github.clans.fab.FloatingActionButton.SIZE_NORMAL);
        OpenVehicleList.setColorPressed(R.color.colorAccent_rota_clicked);
        OpenVehicleList.setColorNormal(R.color.colorAccent_rota);
        OpenVehicleList.setImageResource(R.mipmap.ic_cehicle_list_icon);
        final com.github.clans.fab.FloatingActionButton NotTrack = new com.github.clans.fab.FloatingActionButton(getApplicationContext());
        NotTrack.setButtonSize(FloatingActionButton.SIZE_MINI);
        NotTrack.setLabelText(getString(R.string.not_track));
        NotTrack.setImageResource(R.drawable.ic_close);
        NotTrack.setButtonSize(com.github.clans.fab.FloatingActionButton.SIZE_NORMAL);
        NotTrack.setColorPressed(R.color.colorAccent_rota_clicked);
        NotTrack.setColorNormal(R.color.colorAccent_rota);

        final com.github.clans.fab.FloatingActionButton Accounts = new com.github.clans.fab.FloatingActionButton(getApplicationContext());
        Accounts.setButtonSize(FloatingActionButton.SIZE_MINI);
        Accounts.setLabelText(getString(R.string.accounts));
        Accounts.setImageResource(R.mipmap.ic_supervisor_account_black_24dp);
        Accounts.setButtonSize(com.github.clans.fab.FloatingActionButton.SIZE_NORMAL);
        Accounts.setColorPressed(R.color.colorAccent_rota_clicked);
        Accounts.setColorNormal(R.color.colorAccent_rota);

        menuRight.addMenuButton(OpenVehicleList);

        menuRight.addMenuButton(NotTrack);
        if (Globals.user.CompanyList != null) {
            if (Globals.user.CompanyList.size() > 1) {
                menuRight.addMenuButton(Accounts);
            }
        }


        Accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Accounts.class));
               /* ActivityCompat.startActivity(Main.class, new Intent(getApplicationContext(), Accounts.class),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(Main.class).toBundle());
                launchedActivity = true;
                */
            }
        });

        NotTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    NotTrack.setLabelColors( ContextCompat.getColor( getApplicationContext(), R.color.grey ),
                        ContextCompat.getColor( getApplicationContext(), R.color.light_grey ),
                        ContextCompat.getColor( getApplicationContext(), R.color.white_transparent ) );
                NotTrack.setLabelTextColor( ContextCompat.getColor( getApplicationContext(), R.color.black ) );
                Toast.makeText( getApplicationContext(), "tıklandı", Toast.LENGTH_LONG ).show();
                */
                if (Globals.selectedVehicleID > -1) {
                    Globals.selectedVehicleID = -1;
                    mapcenter();
                }


            }
        });
        OpenVehicleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OpenVehicleList.setLabelColors( ContextCompat.getColor( getApplicationContext(), R.color.grey ),
//                        ContextCompat.getColor( getApplicationContext(), R.color.light_grey ),
//                        ContextCompat.getColor( getApplicationContext(), R.color.white_transparent ) );
//               OpenVehicleList.setLabelTextColor( ContextCompat.getColor( getApplicationContext(), R.color.black ) );

                startActivity();
/*
                if (TransitionUtils.isAtLeastLollipop()) {
                    startRippleTransitionReveal();
                } else {
                }
                */
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle("ROTA - " + Globals.user.CompanyName);
        if (Globals.context == null) {
            Globals.context = this;
        }
        // initTestData();

        startsignalr();
        trackVehicle(true);
        /*
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
*/
        FirebaseUserActions.getInstance().start(getIndexApiAction());

        //     SmartScheduler jobScheduler = SmartScheduler.getInstance(this);


    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUserActions.getInstance().end(getIndexApiAction());
        stopsignalr();

    }

    @Override
    protected void onPause() {


        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        trackVehicle(true);

        if (Globals.isReChangeAccount) {
            mMap.clear();
            onMapReady(mMap);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && launchedActivity) {
            //   startRippleTransitionUnreveal();
            launchedActivity = false;
        }
        customerID = Globals.user.CompanyID;
        restartsignalr();


    }


    //#region Update Location On Webservice DB


//    final Handler handler = new Handler();
//    TimerTask doAsynchronousTask = new TimerTask() {
//        @Override
//        public void run() {
//            handler.post(
//                    new Runnable() {
//                        public void run() {
//                            GetLastMotionOnDB();
//                        }
//                    }
//            );
//        }
//    };

//    public void GetLastMotionOnDB() {
//        try {
//            // Simulate network access.
//
//
//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
//
//            /*String webservice = GetLastLocation(new Gson().toJson(Globals.SendList));
//
//            Type collectionType_LocationR = new TypeToken<List<LocationR>>() {
//            }.getType();
//            List<LocationR> locationlist = gson.fromJson(webservice, collectionType_LocationR);
//
//            webservice = GetLastMotion(new Gson().toJson(Globals.SendList));
//
//            Type collectionType_Status = new TypeToken<List<StatusR>>() {
//            }.getType();
//
//            List<StatusR> statuslist = gson.fromJson(webservice, collectionType_Status);
//
//            */
//            String webservice = GetLastDeviceMove(new Gson().toJson(Globals.SendList), Globals.user.CompanyID);
//            if (webservice.equalsIgnoreCase("false"))
//                return;
//
//            Type collectionType_LocationR = new TypeToken<List<GetLocation>>() {
//            }.getType();
//            final List<GetLocation> locationlist = gson.fromJson(webservice, collectionType_LocationR);
//
//            for (int yy = 0; yy < locationlist.size(); yy++) {
//
//                final GetLocation vehicle = locationlist.get(yy);
//
//                Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).AddLocation(locationlist.get(yy));
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        try {
//                            //   animateMarker(marker,new LatLng( newLocation.Latitude, newLocation.Longitude ),true,newLocation.Speed);
//
//                            Globals.DevicesVehicleMap.get(vehicle.VehicleID).marker.setPosition(new LatLng(vehicle.Latitude, vehicle.Longitude));
//                            Globals.DevicesVehicleMap.get(vehicle.VehicleID).marker.setRotation(vehicle.Angle);
//
//                            Globals.DevicesVehicleMap.get(vehicle.VehicleID).marker.setIcon(BitmapDescriptorFactory.fromResource(
//                                    Globals.getcaricon(Globals.DevicesVehicleMap.get(vehicle.VehicleID).VehicleImage,
//                                            Globals.DevicesVehicleMap.get(vehicle.VehicleID).LastLocation.Ignition, vehicle.Speed > 1, false)));
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                if (Globals.selectedVehicleID == locationlist.get(yy).VehicleID) {
//                    mapcenter(new LatLng(locationlist.get(yy).Latitude, locationlist.get(yy).Longitude), 15, false);
//
//
//                }
//
//            }
//
//
//        } catch (Exception e) {
//        }
//    }

    /*
    public class runmapmarker extends AsyncTask<Object, String, Boolean> {

        int VehicleID;
        LocationR newLocation;

        public runmapmarker(int vehicleID, LocationR location) {
            this.VehicleID = vehicleID;
            this.newLocation = location;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                //     Globals.DevicesVehicleMap.get( VehicleID ).marker.setPosition( new LatLng( newLocation.Latitude, newLocation.Longitude ) );
                //     Globals.DevicesVehicleMap.get( VehicleID ).marker.setRotation( newLocation.Angle );
                //     Globals.DevicesVehicleMap.get( VehicleID ).location = newLocation;

            } catch (Exception e) {
                e.toString();
            }

            return true;
        }
    }
*/

    //#endregion


    //#region Signalr


    HubConnection conn;
    HubProxy proxy;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    Type collectionSignalrMessage = new TypeToken<SignalrMessage>() {
    }.getType();

    SignalrMessage SignalR;

    float mapzoomlevel = 20;


    public void startsignalr() {
        try {
            if (conn != null)
                conn.start();
          /*
            if (!Globals.isConnectionSignalr || Globals.isReChangeAccount ) {
                Globals.isReChangeAccount = false;
                if (conn != null)
                    conn.stop();
                conn = null;
                proxy = null;
               runhublistener();
            }*/
        } catch (Exception e) {

        }
    }

    public void restartsignalr() {
        try {

            if (!Globals.isConnectionSignalr || Globals.isReChangeAccount) {
                Globals.isReChangeAccount = false;
                if (conn != null)
                    conn.stop();
                conn = null;
                proxy = null;
                runhublistener();
            }
        } catch (Exception e) {

        }
    }

    public void stopsignalr() {
        try {
            if (conn != null)
                conn.stop();
            /*
            if (Globals.isConnectionSignalr) {


            }*/
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("rawtypes")
    public void runhublistener() {
        try {


            Logger logger = new Logger() {

                @Override
                public void log(String message, LogLevel level) {
                    //  Log.i( "TümLogglar", "" + message );
                    //   testview.setText( testview.getText() + "\n " + message );

                    //  System.out.println( message );
                }
            };

            Map<String,String> a = new HashMap<String, String>();
            a.put("key1","key1");

            // Connect to the server
            //   HubConnection conn = new HubConnection( "http://cep.rota.net.tr/signalr/", "", true, logger );
            conn = new HubConnection("http://ats2.rota.net.tr/signalr/", "key="+ customerID , true, logger);

            // Create the hub proxy
            //  HubProxy proxy = conn.createHubProxy( "ChatHub" );
            proxy = conn.createHubProxy("RealData");

            proxy.subscribe(new Object() {
                @SuppressWarnings("unused")
                public void messageReceived(String name, String message) {
                    Log.i("STATE", "NAME:" + name + " MESAGGE" + message);

                }
            });

            // Subscribe to the error event
            conn.error(new ErrorCallback() {

                @Override
                public void onError(Throwable error) {
                    error.printStackTrace();
                }
            });

            // Subscribe to the connected event
            conn.connected(new Runnable() {

                @Override
                public void run() {
                    Log.i("STATE", "CONNECTED");
                    Globals.isConnectionSignalr = true;
                    //  System.out.println( "CONNECTED" );
                    signal_con = true;

                    mConnectionID = conn.getConnectionId();
                }
            });

      //Send CustomerID to Signal server
/*
try {
    proxy.invoke("connectID", customerID + "", "").done(new Action<Void>() {

        @Override
        public void run(Void obj) throws Exception {

            Log.i("STATE", "SENT!");


        }
    });
}catch (Exception e){
    e.printStackTrace();
}
*/

            // Subscribe to the closed event
            conn.closed(new Runnable() {

                @Override
                public void run() {
                    Log.i("STATE", "DISCONNECTED");
                    Globals.isConnectionSignalr = false;
                    //   System.out.println( "DISCONNECTED" );
                }
            });

            // Start the connection
            conn.start()
                    .done(new Action<Void>() {

                        @Override
                        public void run(Void obj) throws Exception {
                            Log.i("STATE", "Done Connecting!");
                            //   System.out.println( "Done Connecting!" );
                        }
                    });

            // Subscribe to the received event
            conn.received(new MessageReceivedHandler() {

                @Override
                public void onMessageReceived(JsonElement json) {
                    Log.i("STATE", "RAW received message: " + json.toString());
                    signalrReader(json);

                    //System.out.println( "RAW received message: " + json.toString() );
                }
            });



            // Read lines and send them as messages.
            //System.in
            Scanner inputReader = new Scanner("my device");
//updateLocation  //send
            String line = inputReader.nextLine();
            /*
            while (!"exit".equals(line)) {
                proxy.invoke("updateLocation1", "location=" + "fe", "connectID=" + "line").done(new Action<Void>() {

                    @Override
                    public void run(Void obj) throws Exception {
                        //    testview.setText( testview.getText() + "\n " + "SENT!" );
                        Log.i("STATE", "SENT!");

                        //   System.out.println( "SENT!" );
                    }
                });

                line = inputReader.next();
            }
*/
            inputReader.close();

            //  conn.stop();
        } catch (Exception e) {

            e.toString();
        }
    }

    private synchronized void animateMarker2(final Marker marker, final LatLng location, int angle, long timeDistance) {

        animateMarkerToGB(marker, location, new LatLngInterpolator.Linear());
/*
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 1000;
        final double bearing = bearingBetweenLocations( marker.getPosition(), location );
        final Interpolator interpolator = new LinearInterpolator();
        // float meter = distanceFrom( marker.getPosition().latitude, marker.getPosition().longitude, location.getLatitude(), location.getLongitude() );

        handler.post( new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation( (float) elapsed
                        / duration );

                double lng = t * location.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.latitude + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * bearing + (1 - t)
                        * startRotation);

                marker.setPosition( new LatLng( lat, lng ) );
                marker.setRotation( rotation );

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed( this, 16 );
                }
            }
        } );
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep( 501L );
                    marker.setPosition( new LatLng( location.latitude, location.longitude ) );
                    marker.setRotation( (float) bearing );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } );
        */
    }


    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker, final double spd) {

        float speed = (float) spd;// Float.parseFloat(spd);
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final float duration = 10 * speed;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation(((float) elapsed
                        / duration));
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    handler.postDelayed(this, 16);
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public synchronized void updateMarker(final LocationR newLocation, final int VehicleID) {
//ui çakışması olmaması için marker setposition gibi harita ve arayüz işlemleri aktivity içinde runOnUiThread ile yapılmalı
        try {
            //   new runmapmarker( VehicleID, newLocation ).execute( "" );


            //    final Marker marker = Globals.DevicesVehicleMap.get(VehicleID).marker;
            //   animateMarker(marker,new LatLng( newLocation.Latitude, newLocation.Longitude ),true,newLocation.Speed);


            //  new Thread() {
            //  public void run() {
            //uiThread arayüz çakışması için kullnılır
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //   animateMarker(marker,new LatLng( newLocation.Latitude, newLocation.Longitude ),true,newLocation.Speed);

                        Globals.DevicesVehicleMap.get(VehicleID).marker.setPosition(new LatLng(newLocation.Latitude, newLocation.Longitude));
                        Globals.DevicesVehicleMap.get(VehicleID).marker.setRotation(newLocation.Angle);

                        Globals.DevicesVehicleMap.get(VehicleID).marker.setIcon(BitmapDescriptorFactory.fromResource(
                                Globals.getcaricon(Globals.DevicesVehicleMap.get(VehicleID).VehicleImage,
                                        Globals.DevicesVehicleMap.get(VehicleID).LastLocation.Ignition, newLocation.Speed > 1, false)));


                        //  Globals.DevicesVehicleMap.get(VehicleID).marker = marker;
                        Globals.DevicesVehicleMap.get(VehicleID).AddLocation(newLocation);
                        if (Globals.selectedVehicleID > 0 && VehicleID == Globals.selectedVehicleID) {
                            trackVehicle(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            //     }
            //  }.start();


        } catch (Exception e) {
            e.toString();
        }

    }


    public void signalrReader(JsonElement json) {
        try {


            SignalR = gson.fromJson(json.toString(), collectionSignalrMessage);
            //   loc = SignalR.A;// gson.fromJson( gson.toJson(SignalR.A) , collectionAObject );

            if (SignalR.A.get(0).R20 != -1) {
                if (SignalR.A.get(0).R20 == 0)
                    return;

                if (SignalR.A.get(0).R20 != Globals.user.CompanyID)
                    return;
            }

            if (SignalR.M.equalsIgnoreCase("updateLocation1")) {

                //         if (Globals.user.CompanyID == SignalR.A.get(0).R5)
                {
                    //for (int i = 0; i < Globals.groups.size(); i++) {
                        //for (int bb = 0; bb < Globals.groups.get(i).Vehicle.size(); bb++) {

                            //if (SignalR.A.get(0).R2.equalsIgnoreCase(Globals.groups.get(i).Vehicle.get(bb).IMEI)) {

                                LocationR templocation = new LocationR();
                                //templocation.VehicleID = Globals.groups.get(i).Vehicle.get(bb).VehicleID;// Integer.parseInt( SignalR.A.R13 );
                                templocation.VehicleID = SignalR.A.get(0).VehicleID;
                                templocation.DeviceDateTime = (SignalR.A.get(0).R11);
                                templocation.SatelliteCount = (SignalR.A.get(0).R17);
                                templocation.Latitude = (SignalR.A.get(0).R14);
                                templocation.Longitude = (SignalR.A.get(0).R15);
                                templocation.Angle = (SignalR.A.get(0).R16);
                                templocation.Speed = (SignalR.A.get(0).R13);

                                Globals.DevicesVehicleMap.get( SignalR.A.get(0).VehicleID).VehicleImage = SignalR.A.get(0).R6;
                              //  Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).VehicleImage = SignalR.A.get(0).R6;
                                //Globals.DevicesVehicleMap.get(SignalR.A.get(0).VehicleID).VehicleImage = SignalR.A.get(0).R6;



                                String inf = "Plate: " + Globals.DevicesVehicleMap.get(SignalR.A.get(0).VehicleID).device.Plate + "  ID: " + Globals.DevicesVehicleMap.get(SignalR.A.get(0).VehicleID).device.VehicleID
                                        + "   IMEI: " + Globals.DevicesVehicleMap.get(SignalR.A.get(0).VehicleID).device.IMEI;
                               // String inf = "Plate: " + Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).device.Plate + "  ID: " + Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).device.VehicleID
                                 //       + "   IMEI: " + Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).device.IMEI;
                                Log.i("Taked Values:", inf + " => " + json.toString());
                                Globals.DevicesVehicleMap.get(SignalR.A.get(0).VehicleID).AddLocation(templocation);
                                updateMarker(templocation, SignalR.A.get(0).VehicleID);

                                //Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).AddLocation(templocation);
                                //updateMarker(templocation, Globals.groups.get(i).Vehicle.get(bb).VehicleID);


                                //break;
                            //}

                        //}
                    //}
                }
            } else if (SignalR.M.equalsIgnoreCase("updateStatus")) {

                {
                    for (int i = 0; i < Globals.groups.size(); i++) {
                        for (int bb = 0; bb < Globals.groups.get(i).Vehicle.size(); bb++) {

                            if (SignalR.A.get(0).R2.equalsIgnoreCase(Globals.groups.get(i).Vehicle.get(bb).IMEI)) {

                                StatusR templocation = new StatusR();
                                templocation.VehicleID = Globals.groups.get(i).Vehicle.get(bb).VehicleID;// Integer.parseInt( SignalR.A.R13 );
                                //      templocation.DateTime = SignalR.A.R7;
                                templocation.GPSState = Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).status.GPSState;
                                templocation.BatteryCharging = Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).status.BatteryCharging;
                                templocation.Ignition = SignalR.A.get(0).R8;
                                Globals.DevicesVehicleMap.get(Globals.groups.get(i).Vehicle.get(bb).VehicleID).AddStatus(templocation);


                                break;
                            }

                        }
                    }
                }
            }
            else{

                String a = "Deneme";
            }

        } catch (Exception e) {
            e.toString();
        }


    }

    //#endregion

    //#region  MAPS

    public void trackVehicle(Boolean zoom) {
        try {
            if (Globals.selectedVehicleID > 0) {
                if (Globals.DevicesVehicleMap.containsKey(Globals.selectedVehicleID)) {
                    //   mapcenter( Globals.VehicleMarkerList.get( Globals.VehicleListMap.get( Globals.selectedVehicleID ) ).marker.getPosition() );
                    mapcenter(Globals.DevicesVehicleMap.get(Globals.selectedVehicleID).marker.getPosition(), Globals.DevicesVehicleMap.get(Globals.selectedVehicleID).location.Angle, zoom);
                }
            }
        } catch (Exception e) {
            Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setBuildingsEnabled(true);
        mMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // mMap.setOnMyLocationChangeListener( this );
        // mMap.getUiSettings().setZoomControlsEnabled( true );
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setTrafficEnabled(true);

        mMap.setMyLocationEnabled(true);
        if (Globals.DevicesVehicleMap.size() > 0) {
            LatLngBounds.Builder mapcenter = new LatLngBounds.Builder();


            //ilk lokasyonları burada ekle
            //   int i = 0;
            for (Map.Entry<Integer, DeviceMarker> item : Globals.DevicesVehicleMap.entrySet()) {
                try {
                    LatLng testlocation = new LatLng(Globals.DevicesVehicleMap.get(item.getKey()).location.Latitude, Globals.DevicesVehicleMap.get(item.getKey()).location.Longitude);
                    Marker marker = mMap.addMarker(
                            new MarkerOptions().position(testlocation).title(Globals.DevicesVehicleMap.get(item.getKey()).device.Plate)
                                    //.icon( BitmapDescriptorFactory.fromResource( R.mipmap.ic_rota_car ) )
                                    .rotation((float) Globals.DevicesVehicleMap.get(item.getKey()).location.Angle)
                                    .flat(true) // marker harita ile beraber dönsün
                    );
                    marker.setTag(item.getKey());
                    marker.setIcon(BitmapDescriptorFactory.fromResource(
                            Globals.getcaricon(item.getValue().VehicleImage,
                                    item.getValue().LastLocation.Ignition, item.getValue().LastLocation.Speed > 3, false)
                    ));
                    Globals.DevicesVehicleMap.get(item.getKey()).marker = (marker);

                    if (Globals.DevicesVehicleMap.size() < 51) {

                        Globals.DevicesVehicleMap.get(item.getKey()).UpdateAdress();


                    }

                    //   DeviceMarker tmp = Globals.DevicesVehicleMap.get( item.getKey() );
                    //   tmp.marker = marker;
                    //    markersList.add( marker );
                    //     Globals.VehicleMarkerList.add( tmp );
                    //   Globals.VehicleListMap.put( item.getKey(), i );
               /*
                Globals.DevicesVehicleMap.get( item.getKey() ).setMarkerbaloon(
                        mMap.addMarker( new MarkerOptions().position( testlocation ).title( Globals.DevicesVehicleMap.get( item.getKey() ).device.Plate )
                                .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_action_name ) )
                        ),this ); */

                    //  i++;


                    if (testlocation.longitude != 0 || testlocation.latitude != 0) {
                        mapcenter.include(testlocation);
                    }
                } catch (Exception e) {
                    //  e.toString();
                }


            }

            try {
                LatLngBounds bounds = mapcenter.build();
                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        mapzoomlevel = cameraPosition.zoom;
                    }
                });
                CameraPosition cameraPosition = new CameraPosition.Builder().target(bounds.getCenter()).zoom(10).bearing(360).tilt(45).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (Exception e) {
                //  e.toString();
            }


        }
    }


    public void mapcenter(LatLng loc, float bearing, Boolean zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(loc)
                //    .bearing( bearing )
                .tilt(45)
                .zoom(mapzoomlevel).build();
        if (zoom) {
            cameraPosition = new CameraPosition.Builder().target(loc)
                    // .bearing( bearing )
                    .tilt(45)
                    .zoom(20).build();
        }
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void mapcenter() {
        if (Globals.DevicesVehicleMap.size() > 0) {
            LatLngBounds.Builder mapcenter = new LatLngBounds.Builder();
            for (Map.Entry<Integer, DeviceMarker> item : Globals.DevicesVehicleMap.entrySet()) {
                LatLng testlocation = new LatLng(Globals.DevicesVehicleMap.get(item.getKey()).location.Latitude, Globals.DevicesVehicleMap.get(item.getKey()).location.Longitude);

                if (testlocation.latitude != 0 || testlocation.longitude != 0)
                    mapcenter.include(testlocation);
            }

            LatLngBounds bounds = mapcenter.build();
            //mMap.setLatLngBoundsForCameraTarget( bounds );
            CameraPosition cameraPosition = new CameraPosition.Builder().target(bounds.getCenter()).zoom(12).tilt(45).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }


    }

    @SuppressLint("SetTextI18n")
    public void alermarkerpopup(String msgAdress, int vehicleID) {

        try {
            com.danimahardhika.cafebar.CafeBar.Builder builder = new com.danimahardhika.cafebar.CafeBar.Builder(Main.this)
                    .customView(R.layout.cafebar_custom_layout).duration(10000).floating(true)
                    .autoDismiss(true);

            final com.danimahardhika.cafebar.CafeBar cafeBar = builder.build();
            View v = cafeBar.getCafeBarView();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cafeBar.dismiss();
                }
            });
            TextView vehicle_plate = v.findViewById(R.id.vehicle_plate);
            TextView vehicle_speed = v.findViewById(R.id.txtHiz);
            vehicle_speed.setText(Globals.DevicesVehicleMap.get(vehicleID).location.Speed + " km/saat");
            vehicle_plate.setText(Globals.DevicesVehicleMap.get(vehicleID).device.Plate);
            TextView vehicle_tag = v.findViewById(R.id.vehicle_tag);
            vehicle_tag.setText(Globals.DevicesVehicleMap.get(vehicleID).device.Tag);
            int cartype = Globals.getcariconview(Globals.DevicesVehicleMap.get(vehicleID).VehicleImage,
                    Globals.DevicesVehicleMap.get(vehicleID).LastLocation.Ignition, Globals.DevicesVehicleMap.get(vehicleID).LastLocation.Speed > 3, false);
            vehicle_tag.setCompoundDrawablesWithIntrinsicBounds(cartype, 0, 0, 0);
            TextView text = v.findViewById(R.id.cafebar_text);
            text.setText(msgAdress);
            cafeBar.show();
        } catch (Exception ignored) {

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {

            int a = (int) marker.getTag();

            if (Globals.DevicesVehicleMap.get(a).VehicleAdress.length() > 3) {
                alermarkerpopup(Globals.DevicesVehicleMap.get(a).VehicleAdress, a);
            } else {
                String Query = Globals.GetLocationAdress(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                Globals.DevicesVehicleMap.get(a).VehicleAdress = Query;
                alermarkerpopup(Query, a);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
//#endregion

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRippleTransitionReveal() {

        menuRight.setVisibility(View.INVISIBLE);
        Animator animator = ViewAnimationUtils.createCircularReveal(rippleView,
                (int) menuRight.getX() + menuRight.getWidth() / 2,
                (int) menuRight.getY(), menuRight.getWidth() / 2, TransitionUtils.getViewRadius(rippleView) * 2);
        rippleView.setVisibility(View.VISIBLE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                //              recyclerView.animate().alpha(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity();
            }
        });
        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRippleTransitionUnreveal() {
        Animator animator = ViewAnimationUtils.createCircularReveal(rippleView,
                (int) menuRight.getX() + menuRight.getWidth() / 2,
                (int) menuRight.getY(), TransitionUtils.getViewRadius(rippleView) * 2, menuRight.getWidth() / 2);
        rippleView.setVisibility(View.VISIBLE);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                //   recyclerView.animate().alpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuRight.setVisibility(View.VISIBLE);
                rippleView.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    private void startActivity() {
        ActivityCompat.startActivity(this, new Intent(this, VehicleList.class),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        launchedActivity = true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public com.google.firebase.appindexing.Action getIndexApiAction() {
        return Actions.newView("Main", "http://[ENTER-YOUR-URL-HERE]");
    }


    ///////////////////   Menu ///////////////////////////


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_item_exit:
                ExitApp();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
/*
        MenuItem nextItem=menu.findItem(R.id.menu_item_next);
        nextItem.setVisible(false);



*/


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        //   menu.getItem(1).setEnabled(false);

        return true;
    }

    public void ExitApp() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_are_you_exit))
                .setContentText(getString(R.string.dialog_are_you_exit_info))
                .setCancelText(getString(R.string.dialog_cancel))
                .setConfirmText(getString(R.string.dialog_exit_app))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                    }
                })
                .show();

    }

}
