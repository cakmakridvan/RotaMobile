package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.data.server;
import tr.net.rota.aeyacin.rotamobil.model.sbt.GetArchives;
import tr.net.rota.aeyacin.rotamobil.utils.Geography;

import static tr.net.rota.aeyacin.rotamobil.Globals.GetLocationAdress;

public class VehicleHistory extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private List<History> histories = new ArrayList<>();
    private GoogleMap mMap;
    private TextView lblBilgi;
    private TextView lblAdress;
    private SeekBar seekBar;
    private float mapzoomlevel = 20;
    private int selecteditem = 0;
    private int Lastselecteditem = -1;

    private SegmentedGroup segment;
    private RadioButton btnseg1;
    private int SumDistance = 0;
    private List<Marker> lstmMarkers = new ArrayList<Marker>();
    private Toolbar toolbar;

    private String Plate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        checkAndRequestPermissions();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        lblBilgi = (TextView) findViewById(R.id.txtBilgiVec);
        lblAdress = (TextView) findViewById(R.id.txtAdress);
        seekBar = (SeekBar) findViewById(R.id.seekBarV);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                   try {
                                                       selecteditem = progress;
                                                       mapcenter(histories.get(selecteditem), selecteditem);

                                                   } catch (Exception e) {
                                                   }

                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {

                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {
                                                   try {
                                                       selecteditem = seekBar.getProgress();
                                                       mapcenter(histories.get(selecteditem), selecteditem);
                                                   } catch (Exception e) {
                                                   }

                                               }
                                           }
        );
        segment = (SegmentedGroup) findViewById(R.id.seg);
        btnseg1 = (RadioButton) findViewById(R.id.button21);
        btnseg1.setChecked(true);

        segment.setSelected(true);
        segment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.button21:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.button22:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    default:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null) {
            int VehicleID = b.getInt("VehicleID");
            String StartDate = b.getString("StartDate");
            String EndDate = b.getString("EndDate");
            Plate = b.getString("Plate");

            new getHistoryBackground(VehicleID, StartDate, EndDate).execute();

        } else {

            new AlertDialog.Builder(this).setCancelable(false).setMessage("Hata")
                    .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle("Araç Geçmiş - " + this.Plate);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void ButtonNext(View v) {
        if (selecteditem < histories.size() - 1) {
            selecteditem++;
            seekBar.setProgress(selecteditem);
            mapcenter(histories.get(selecteditem), selecteditem);

        } else if (selecteditem == histories.size()) {
            selecteditem = histories.size();

            seekBar.setProgress(selecteditem);
            mapcenter(histories.get(selecteditem), selecteditem);
        }
    }

    public void ButtonPrevious(View v) {
        if (selecteditem > 0) {
            selecteditem--;

            seekBar.setProgress(selecteditem);
            mapcenter(histories.get(selecteditem), selecteditem);
        } else if (selecteditem == 0) {
            selecteditem = 0;

            seekBar.setProgress(selecteditem);
            mapcenter(histories.get(selecteditem), selecteditem);
        }

    }

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 20177;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //    NavUtils.navigateUpFromSameTask( this );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mapcenter(final History item, final int selecteditem) {

        lblBilgi.setText(item.DeviceDateTime + " Toplam Yol:" + SumDistance / 1000 + " KM " + item.Speed + " KM/S");

        LatLng pozisyonlar = new LatLng(item.Latitude, item.Longitude);
        lblAdress.setText(GetLocationAdress(pozisyonlar));


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(item.Latitude, item.Longitude))
                .zoom(16).bearing(360).tilt(45).build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        if (selecteditem < 1) {
            if (Lastselecteditem > 0) {
                histories.get(Lastselecteditem).marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_yesil));

            }
            histories.get(selecteditem).marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_kirmizi));

        } else if (Lastselecteditem == selecteditem) {

            histories.get(selecteditem).marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_kirmizi));


        } else {
            if (Lastselecteditem > -1) {
                histories.get(selecteditem).marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_kirmizi));

                histories.get(Lastselecteditem).marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_yesil));

            } else {
                histories.get(selecteditem).marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_kirmizi));

            }


        }
        Lastselecteditem = selecteditem;






/*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pozisyonlar);

        for (Marker markers : lstmMarkers) {
            markers.remove();
        }
        lstmMarkers.clear();

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTitle(String.valueOf(item.Speed) + " km/sn " + " -- " +
                String.valueOf(item.DeviceDateTime));
        marker.setRotation((float) item.Angle);


        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_kirmizi));

        lstmMarkers.add(marker);
        */
/*
//            @Override
            public boolean onMarkerClick(Marker marker) {



                for (Marker markers : lstmMarkers) {
                    markers.remove();
                }
                lstmMarkers.clear();

//                marker.setTitle(String.valueOf(item.Speed) + " km/sn " + " -- " +
//                        String.valueOf(item.DeviceDateTime));
//                marker.setRotation((float) item.Angle);

                marker.setTitle(String.valueOf(histories.get(selecteditem).Speed) + " km/sn " + " -- " +
                        String.valueOf(histories.get(selecteditem).DeviceDateTime));
                marker.setRotation((float) histories.get(selecteditem).Angle);

                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_kirmizi));

                lstmMarkers.add(marker);

                return false;
            }
        });
*/


      /*  mMap.addMarker(new MarkerOptions().position(pozisyonlar)
                .title(String.valueOf(item.Speed) + " km/sn " + " -- " +
                        String.valueOf(item.DeviceDateTime))
                .rotation((float) item.Angle)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_kirmizi)));
*/
    }


    class getHistoryBackground extends AsyncTask<Object, Object, Boolean> {
        ProgressDialog pDialog;

        int VehicleID;
        String StartDate;
        String EndDate;

        public getHistoryBackground(int vehicleID, String startDate, String endDate) {
            this.VehicleID = vehicleID;
            this.StartDate = startDate;
            this.EndDate = endDate;
        }

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(VehicleHistory.this);
            pDialog.setMessage("Araç Geçmişi Getiriliyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {

                String result = server.GetVehicleArchive(this.VehicleID, this.StartDate, this.EndDate);

                if (!result.trim().equalsIgnoreCase("false")) {
                    Type collectionType = new TypeToken<List<GetArchives>>() {
                    }.getType();

                    List<GetArchives> results = new Gson().fromJson(result, collectionType);
                    histories.clear();
                    for (GetArchives item : results) {

                        History tmp = new History();
                        tmp.Latitude = item.Latitude;
                        tmp.Longitude = item.Longitude;
                        tmp.Angle = item.Angle;
                        tmp.Speed = item.Speed;


                        tmp.setDeviceDateTime(item.DeviceDateTime);

                        histories.add(tmp);

                    }

                    Collections.sort(histories, new Comparator<History>() {
                        public int compare(History m1, History m2) {
                            return m1.getDeviceDateTime().compareTo(m2.getDeviceDateTime());
                        }
                    });
                    return true;
                } else {
                    return false;
                    //   finish();
                }
                // int a=histories.size();
            } catch (Exception e) {
                return false;
            }
            //return null;
        }

        protected void onPostExecute(Boolean state) { //Posttan sonra
            try {


                SumDistance = 0;
                if (state == true) {
                    LatLngBounds.Builder mapcenter = new LatLngBounds.Builder();
                    for (int i = 0; i < histories.size(); i++) {

                        LatLng pozisyonlar = new LatLng(histories.get(i).Latitude, histories.get(i).Longitude);
                        if (i > 0) {
                            SumDistance += Geography.HaversineInM(histories.get(i).Latitude, histories.get(i).Longitude, histories.get(i - 1).Latitude, histories.get(i - 1).Longitude);
                        }

                        histories.get(i).marker = mMap.addMarker(new MarkerOptions().position(pozisyonlar)
                                //   .title(String.valueOf(histories.get(i).Speed) + " km/sn " + " -- " + String.valueOf(histories.get(i).DeviceDateTime))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_direction_yesil))
                                .rotation((float) histories.get(i).Angle)
                                .flat(true)// marker harita ile beraber dönsün
                        );
                        histories.get(i).marker.setTag(i);

                        if (pozisyonlar.longitude != 0 || pozisyonlar.latitude != 0) {
                            mapcenter.include(pozisyonlar);
                        }

                    }

                    mMap.setOnMarkerClickListener(VehicleHistory.this);


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
                        e.toString();
                    }
                    seekBar.setMax(histories.size() - 1);
                    //    seekBar.setProgress(0);
                    lblBilgi.setText(histories.get(0).DeviceDateTime + " Toplam Yol:" + SumDistance / 1000 + " km");
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    new AlertDialog.Builder(VehicleHistory.this).setTitle("Dikkat").setMessage("Geçmiş Bulunamadı")
                            .setCancelable(false)
                            .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                }

            } catch (Exception e) {
                pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
                new AlertDialog.Builder(VehicleHistory.this).setTitle("Dikkat").setMessage("Geçmiş Bulunamadı")
                        .setCancelable(false)
                        .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }

        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        String adres = GetLocationAdress(marker.getPosition());
        marker.setTitle(adres);// + " \n\r " + marker.getTitle());
        marker.showInfoWindow();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).marker.equals(marker)) {
                selecteditem = i;
                seekBar.setProgress(selecteditem);
                mapcenter(histories.get(selecteditem), selecteditem);
            }
        }
    /*    String adres = GetLocationAdress(marker.getPosition());
        marker.setTitle(adres);// + " \n\r " + marker.getTitle());
        marker.showInfoWindow();
*/
        return false;
    }


    //aşağıdaki kullanım      Collections.sort(myList);
    public static class History {

        public float Latitude;
        public float Longitude;
        public int Speed;
        public int Adress;
        public Marker marker;

        public void setDeviceDateTime(String deviceDateTime) {
            DeviceDateTime = deviceDateTime;
            DeviceTime = stringToDate(this.DeviceDateTime.replace("T", " "), "yyyy-MM-dd HH:mm:ss");
        }

        public String DeviceDateTime;
        public int Angle;
        public Date DeviceTime;


        public Date getDeviceDateTime() {

            return DeviceTime;
        }

        private Date stringToDate(String aDate, String aFormat) {

            if (aDate == null) return null;
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
            Date stringDate = simpledateformat.parse(aDate, pos);
            return stringDate;

        }

    }

}
