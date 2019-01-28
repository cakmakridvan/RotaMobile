package tr.net.rota.aeyacin.rotamobil.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;

import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.func.Geography;
import tr.net.rota.aeyacin.rotamobil.model.sbt.GetLocation;
import tr.net.rota.aeyacin.rotamobil.model.sbt.LocationR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.StatusR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.Vehicle;

/**
 * Created by ayacin on 9.03.2017.
 */

public class DeviceMarker {
    public Marker marker;
    public GetLocation LastLocation = new GetLocation();
    //  public Marker markerbaloon;
    public Vehicle device = new Vehicle();
    public int GroupID;
    public LocationR location = new LocationR();
    public StatusR status = new StatusR();
    public String VehicleImage = "araba";
    public String VehicleAdress = "";
    public Date LastTime = new Date();
    public double LastLatitude = 0;
    public double LastLongitude = 0;



/*
    public Marker getMarkerbaloon() {
        return markerbaloon;
    }

    public void setMarkerbaloon(Marker markerbaloon, Activity activity) {
        IconGenerator iconFactory = new IconGenerator( activity );
        iconFactory.setStyle( IconGenerator.STYLE_ORANGE );
        //  marker.setIcon( BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(device.Tag)) );
        //  marker.setAnchor( iconFactory.getAnchorU(), iconFactory.getAnchorV() );

        View multiProfile = activity.getLayoutInflater().inflate( R.layout.map_icon_with_title, null );

        IconGenerator mClusterIconGenerator = new IconGenerator( activity.getApplicationContext() );
        mClusterIconGenerator.setContentView( multiProfile );
        Bitmap icon = mClusterIconGenerator.makeIcon( String.valueOf( "merhaba" ) );
        markerbaloon.setIcon( BitmapDescriptorFactory.fromBitmap( icon ) );

        this.markerbaloon = markerbaloon;
    }


*/

//signalr ekleme
    public void AddLocation(LocationR newLocation) {
        //yeni lokasyon ve konum g?ncellemesi ayr?ca animasyon ?a??rma ve ?zel hesap yap?lacak yer

        try {
            this.location = newLocation;
        } catch (Exception e) {
            e.toString();
        }

      //  if (Globals.DevicesVehicleMap.size() < 51)
        {

            UpdateAdress(newLocation);


        }

    }

    //veritabnından ekleme
    public void AddLocation(GetLocation getloc) {
        //yeni lokasyon ve konum g?ncellemesi ayr?ca animasyon ?a??rma ve ?zel hesap yap?lacak yer


        LocationR newLocation = new LocationR();
        newLocation.Angle = getloc.Angle;
        newLocation.DeviceDateTime = getloc.DeviceDateTime;
        newLocation.Latitude = getloc.Latitude;
        newLocation.Longitude = getloc.Longitude;
        newLocation.SatelliteCount = getloc.SatelliteCount;
        newLocation.Speed = getloc.Speed;
        newLocation.VehicleID = getloc.VehicleID;


        try {
            this.location = newLocation;
            this.status.DateTime=getloc.StatusDateTime;
        } catch (Exception e) {
            e.toString();
        }

       // if (Globals.DevicesVehicleMap.size() < 51)
        {

            UpdateAdress(newLocation);


        }

    }

    public synchronized void UpdateAdress(LocationR newLocation) {

        if (this.VehicleAdress.trim().equalsIgnoreCase("")) {
            String a = Globals.GetLocationAdress(new LatLng(newLocation.Latitude, newLocation.Longitude));
            if (!a.equalsIgnoreCase("")) {
                this.VehicleAdress = a;
            }
        } else {
            long Fark = new Date().getTime() - this.LastTime.getTime();
            if (Fark > 60000) {  //Eger 60 saniyeden fazla ise yap

                double distance = Geography.distancemeter(this.LastLatitude, this.LastLongitude, newLocation.Latitude, newLocation.Longitude);
                if (distance > 50) {
                    String a = Globals.GetLocationAdress(new LatLng(newLocation.Latitude, newLocation.Longitude));
                    if (!a.equalsIgnoreCase("")) {
                        this.VehicleAdress = a;
                    }
                }

            }
        }
    }

    public synchronized void UpdateAdress() {

        String a = Globals.GetLocationAdress(new LatLng(this.LastLocation.Latitude, this.LastLocation.Longitude));
        if (!a.equalsIgnoreCase("")) {
            this.VehicleAdress = a;
        }

    }

    public void AddStatus(StatusR newStatus) {
        //yeni lokasyon ve konum g?ncellemesi ayr?ca animasyon ?a??rma ve ?zel hesap yap?lacak yer
        this.status = newStatus;

    }


}
