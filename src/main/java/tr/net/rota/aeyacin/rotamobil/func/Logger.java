package tr.net.rota.aeyacin.rotamobil.func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import tr.net.rota.aeyacin.rotamobil.Constants;

/**
 * Created by ayacin on 14.03.2017.
 */

public class Logger {

    public void newlog(String LogType,String FunctionName,String ClassName,String ErrorCode ){

    }


    public static void logwriter(String GelenLogBilgisi){
        Date now = new Date();
        try {
            File folder = new File(Constants.dirWebservislog);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File fname = new File(folder,
                    Constants.sortDateFormat.format(now) + "_Veri.txt");

            String YazilacakNot = WebServisread()
                    + Constants.formatter.format(now) + " : "
                    + GelenLogBilgisi;

            if (folder.canWrite()) {
                FileWriter fwriter = new FileWriter(fname);
                BufferedWriter out = new BufferedWriter(fwriter);
                out.write(YazilacakNot);
                out.close();
                fwriter.close();
            }
        } catch (IOException e) {
            e.toString();
        }


    }
    public static void logwriterAcil(String GelenLogBilgisi){



    }

    public static void logwriterBilgi(String GelenLogBilgisi){



    }



    private static String WebServisread() {
        Date now = new Date();
        String ret = "";
        try {
            File folder = new File( Constants.dirWebservislog);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File fname = new File(folder, Constants.sortDateFormat.format(now) + "_Veri.txt");

            FileInputStream fIn = new FileInputStream(fname);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(
                    fIn));
            String aDataRow = "";
            StringBuilder aBuffer = new StringBuilder();
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer.append(aDataRow);
                aBuffer.append("\n");
            }
            ret = (aBuffer.toString());
            myReader.close();

        } catch (Exception e) {

        }
        return ret;
    }

}
