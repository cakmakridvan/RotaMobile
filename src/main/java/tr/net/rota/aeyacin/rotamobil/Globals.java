package tr.net.rota.aeyacin.rotamobil;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import tr.net.rota.aeyacin.rotamobil.model.DeviceMarker;
import tr.net.rota.aeyacin.rotamobil.model.sbt.SendVehicle;
import tr.net.rota.aeyacin.rotamobil.model.sbt.User;
import tr.net.rota.aeyacin.rotamobil.model.sbt.VehicleGroup;
import tr.net.rota.aeyacin.rotamobil.utils.SmsStruct;

/**
 * Created by ayacin on 9.03.2017.
 */

public class Globals {


    public static User user = new User();
    public static List<VehicleGroup> groups = new ArrayList<VehicleGroup>(); //Grup Adı,Grup Id Ve Gruba Bağlı VehicleListesi
    public static HashMap<Integer, DeviceMarker> DevicesVehicleMap = new HashMap<>();//vehicleID,DeviceList
    public static List<SendVehicle> SendList = new ArrayList<SendVehicle>(); //Her sorguda Gönderilecek Liste
    public static Boolean isConnectionSignalr = false;  //signalr için bağlantı tekrarı engellemek için
    public static int selectedVehicleID = -1;  //takip edilecek araç
    public static int MainCustomerAccountID ;  //Ana Hesap IDsi

    public static Context context;
    public static List<SmsStruct> smsStructs = new ArrayList<SmsStruct>();
    public static boolean isReChangeAccount=false;


    //  public static List<GetLocation> LastLocationList = new ArrayList<>();

    //  public static HashMap<Integer, Integer> VehicleListMap = new HashMap<>();//Listedeki vehcielID,İndex değerini tutar

    //   public static List<DeviceMarker> VehicleMarkerList=new ArrayList<>(  );
    //  public static List<DeviceMarker> devicelist = new ArrayList<>();


    //  public static HashMap<Integer, List<DeviceMarker>> listGroupDevices = new HashMap<>();//groupID,DeviceList
    public static Date stringToDate(String aDate, String aFormat) {

        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    public static String GetNowDate(Date date) {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);


    }

    public static synchronized String GetLocationAdress(LatLng location) {

        String LocationGeocoder = "";
        if (context == null) {
            return "";
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            // 40.822580, 29.313966
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);

            // addresses =
            // geocoder.getFromLocation(40.8478685111462,29.2955463993147, 4);

            LocationGeocoder = "";

            if (addresses.get(0).getCountryName() != null) {
                // LocationGeocoder +=
                // addresses.get(0).getCountryName()+"\\\\";
            }

            if (addresses.get(0).getAdminArea() != null) {
                LocationGeocoder += addresses.get(0).getAdminArea().toUpperCase() + "\\";
            } else if (addresses.size() > 1 && addresses.get(1).getAdminArea() != null) {
                LocationGeocoder += addresses.get(1).getAdminArea().toUpperCase() + "\\";
            } else if (addresses.size() > 2 && addresses.get(2).getAdminArea() != null) {
                LocationGeocoder += addresses.get(2).getAdminArea().toUpperCase() + "\\";
            } else if (addresses.size() > 3 && addresses.get(3).getAdminArea() != null) {
                LocationGeocoder += addresses.get(3).getAdminArea().toUpperCase() + "\\";

            }

            if (addresses.get(0).getSubAdminArea() != null) {
                LocationGeocoder += addresses.get(0).getSubAdminArea().toUpperCase() + "\\";
            } else if (addresses.size() > 1 && addresses.get(1).getSubAdminArea() != null) {
                LocationGeocoder += addresses.get(1).getSubAdminArea().toUpperCase() + "\\";
            } else if (addresses.size() > 2 && addresses.get(2).getSubAdminArea() != null) {
                LocationGeocoder += addresses.get(2).getSubAdminArea().toUpperCase() + "\\";
            } else if (addresses.size() > 3 && addresses.get(3).getSubAdminArea() != null) {
                LocationGeocoder += addresses.get(3).getSubAdminArea().toUpperCase() + "\\";
            }

            if (addresses.get(0).getSubLocality() != null) {
                LocationGeocoder += addresses.get(0).getSubLocality() + "\\";
            } else if (addresses.size() > 1 && addresses.get(1).getSubLocality() != null) {
                LocationGeocoder += addresses.get(1).getSubLocality() + "\\";
            } else if (addresses.size() > 2 && addresses.get(2).getSubLocality() != null) {
                LocationGeocoder += addresses.get(2).getSubLocality() + "\\";
            } else if (addresses.size() > 3 && addresses.get(3).getSubLocality() != null) {
                LocationGeocoder += addresses.get(3).getSubLocality() + "\\";
            }

            if (addresses.get(0).getThoroughfare() != null) {
                LocationGeocoder += addresses.get(0).getThoroughfare();
            } else if (addresses.size() > 1 && addresses.get(1).getThoroughfare() != null) {
                LocationGeocoder += addresses.get(1).getThoroughfare();
            } else if (addresses.size() > 2 && addresses.get(2).getThoroughfare() != null) {
                LocationGeocoder += addresses.get(2).getThoroughfare();
            } else if (addresses.size() > 3 && addresses.get(3).getThoroughfare() != null) {
                LocationGeocoder += addresses.get(3).getThoroughfare();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return LocationGeocoder;
    }

  //private static  BitmapDescriptor icon_carBlack = BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_black);


    public static  synchronized BitmapDescriptor getCarImage( String type, Boolean ignition, Boolean ismove, Boolean LimitOverTime){

        return  null;
    }
    public static synchronized int getcaricon(String type, Boolean ignition, Boolean ismove, Boolean LimitOverTime) {
        int car = R.mipmap.ic_car_black;



        if(type == null){

            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_car_red;
                } else if (ismove) {
                    car = R.mipmap.ic_car_green;
                } else {
                    car = R.mipmap.ic_car_purple;
                }
            } else {
                car = R.mipmap.ic_car_black;
            }

            return car;
        }
        if (type.equalsIgnoreCase("araba")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_car_red;
                } else if (ismove) {
                    car = R.mipmap.ic_car_green;
                } else {
                    car = R.mipmap.ic_car_purple;
                }
            } else {
                car = R.mipmap.ic_car_black;
            }

        } else if (type.equalsIgnoreCase("otobus")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_bus_red;
                } else if (ismove) {
                    car = R.mipmap.ic_bus_green;
                } else {
                    car = R.mipmap.ic_bus_purple;
                }
            } else {
                car = R.mipmap.ic_bus_black;
            }

        } else if (type.equalsIgnoreCase("kamyon")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_car_red;
                } else if (ismove) {
                    car = R.mipmap.ic_car_green;
                } else {
                    car = R.mipmap.ic_car_purple;
                }
            } else {
                car = R.mipmap.ic_car_black;
            }
        } else if (type.equalsIgnoreCase("moto")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_car_red;
                } else if (ismove) {
                    car = R.mipmap.ic_car_green;
                } else {
                    car = R.mipmap.ic_car_purple;
                }
            } else {
                car = R.mipmap.ic_car_black;
            }
        }


        return car;

    }

    public static synchronized int getcariconview(String type, Boolean ignition, Boolean ismove, Boolean LimitOverTime) {
        int car = R.mipmap.ic_car1_icon;
        if(type == null){
            return  car;
        }
        if (type.equalsIgnoreCase("araba")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_car1_icon;
                } else if (ismove) {
                    car = R.mipmap.ic_car1_icon;
                } else {
                    car = R.mipmap.ic_car1_icon;
                }
            } else {
                car = R.mipmap.ic_car1_icon;
            }

        } else if (type.equalsIgnoreCase("otobus")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_bus_icon;
                } else if (ismove) {
                    car = R.mipmap.ic_bus_icon;
                } else {
                    car = R.mipmap.ic_bus_icon;
                }
            } else {
                car = R.mipmap.ic_bus_icon;
            }

        } else if (type.equalsIgnoreCase("kamyon")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_truck_icon;
                } else if (ismove) {
                    car = R.mipmap.ic_truck_icon;
                } else {
                    car = R.mipmap.ic_truck_icon;
                }
            } else {
                car = R.mipmap.ic_truck_icon;
            }
        } else if (type.equalsIgnoreCase("moto")) {
            if (ignition) {
                if (LimitOverTime) {
                    car = R.mipmap.ic_motor_icon;
                } else if (ismove) {
                    car = R.mipmap.ic_motor_icon;
                } else {
                    car = R.mipmap.ic_motor_icon;
                }
            } else {
                car = R.mipmap.ic_motor_icon;
            }
        }


        return car;

    }

}
