package tr.net.rota.aeyacin.rotamobil.utils;

/**
 * Created by kkurtulus on 16.06.2017.
 */

public class Geography {



    // Region uzaklık hesapları

    // kullanımı haversine_km(40.993703, 29.133144,40.993962, 29.143444)

    final static double R = 6371.16;
    static double _eQuatorialEarthRadius = 6378.1370D;
    static double _d2r = (Math.PI / 180D);

    final static double d2r = (Math.PI / 180.0);
    private static double EARTH_CIRC_METERS = 40030218; // Radius = 6371007
    // (GRS80)

    public double haversine_km(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2.0), 2)
                + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r) * Math.pow(Math.sin(dlong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367 * c;

        return d;
    }

    // mil olarak
    public double haversine_mi(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * d2r;
        double dlat = (lat2 - lat1) * d2r;
        double a = Math.pow(Math.sin(dlat / 2.0), 2)
                + Math.cos(lat1 * d2r) * Math.cos(lat2 * d2r) * Math.pow(Math.sin(dlong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 3956 * c;

        return d;
    }

    public static int HaversineInM(double lat1, double long1, double lat2, double long2) {
        int a = (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
        if (a < 0) {
            a *= -1;
        }
        return a;
    }

    public int HaversineInMDouble(double lat1, double long1, double lat2, double long2) {
        return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
    }

    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D)
                + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r) * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;

        return d;
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {

        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        double d = Math.acos((Math.cos(radLat1) * Math.cos(radLat2))
                + (Math.sin(radLat1) * Math.sin(radLat2)) * (Math.cos(radLon1 - radLon2)));
        return (d * EARTH_CIRC_METERS);
    }

    // EndRegion
}
