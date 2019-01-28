package tr.net.rota.aeyacin.rotamobil.model.sbt;

/**
 * Created by aeyacin on 3.10.2017.
 */

public class MonthlyRoadReportObj {

    // ilk hareket
    public String FirstMotionDateTime;  //DateTime
    public double FirstMotionLatitude;
    public double FirstMotionLongitude;

    // son hareket
    public String LastMotionDateTime; //DateTime
    public double LastMotionLatitude;
    public double LastMotionLongitude;

    // günlük yapılan toplam yol
    public double DayTotalKm;

    // gün içi en yüksek hız
    public int HighestSpeed;
    public double HighestSpeedLatitude;
    public double HighestSpeedLongitude;

    // gün içi ortalama hız
    public int AvarageSpeed;

    // hız limiti üzeri gidiş toplam süresi
    public String OverTheSpeedLimitMinute;

    // park süresi
    public String TotalParkTime;
    // park sayısı
    public int TotalParkNumber;

    // sefer süresi
    public String ExpeditionTime;

    // sefer sayısı
    public int ExpeditionNumber;

    // rolanti süresi
    public String TotalIdlingTime;

    // ilk konum ile son konum aynımı
    public String FirstMovementLastMovementIdentical;

    // mesai saatleri içerisi yapılan km
    public double AtworkKm;

    // mesai saatleri dışarısı yapılan km
    public double OutOfWorkKm;
    public String DayTime; //DateTime



}
