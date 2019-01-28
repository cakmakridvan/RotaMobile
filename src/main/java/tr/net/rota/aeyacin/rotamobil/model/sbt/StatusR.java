package tr.net.rota.aeyacin.rotamobil.model.sbt;

import java.util.Date;

/**
 * Created by ayacin on 21.03.2017.
 */

public class StatusR {
    public int VehicleID = 0;
    public Date DateTime = new Date();
    public boolean GPSState = true;
    public boolean BatteryCharging = true;
    public boolean Ignition = false;
}
