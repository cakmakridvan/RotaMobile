package tr.net.rota.aeyacin.rotamobil;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by ayacin on 25.04.2017.
 */

public class Constants {

    //web servisle gonderilecek loglarin tutuldugu klasor
    public static String dirWebservislog = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Rota"
            + "/Rota STS" + "/DataWebServis/";

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sortDateFormat = new SimpleDateFormat( "dd.MM.yyyy" );

    @SuppressLint("SimpleDateFormat")
    public static final Format formatter = new SimpleDateFormat( "HH:mm:ss" );

}
