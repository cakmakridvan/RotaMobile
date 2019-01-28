package tr.net.rota.aeyacin.rotamobil.model.sbt;

import java.util.Date;

/**
 * Created by ayacin on 24.03.2017.
 */

public class GetLocation {

    public String IMEI = "";
    public Date StatusDateTime = new Date();
    public boolean Ignition = false;
    public Date DeviceDateTime = new Date();
    public int Speed = 0;
    public double Latitude = 0;
    public double Longitude = 0;
    public int SatelliteCount = 0;
    public int Angle = 0;
    public String GroupName = "Lokasyon Yok";
    public String Plate = "";
    public String Tag = "";
    public int VehicleID = 0;
    public String VehicleImagePath = "araba";
    public String GsmNumber = "";
    public boolean EngineBlockage = false;
    public String DeviceType = "";

}
