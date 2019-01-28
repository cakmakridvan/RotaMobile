package tr.net.rota.aeyacin.rotamobil.func;

import android.app.Activity;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

/**
 * Created by ayacin on 19.03.2017.
 */

public class PrefencesCrypt {

    private EncryptedPreferences encryptedPreferences;
    private Activity activity;

    public PrefencesCrypt(Activity cntx) {
        activity = cntx;
        encryptedPreferences = new EncryptedPreferences.Builder( cntx ).withEncryptionPassword( cntx.getPackageName() ).build();//.withOnSharedPreferenceChangeListener(cntx).build();

    }

    public void save(String KEY, String Value) {
        encryptedPreferences.edit()
                .putString( KEY, Value )
                .apply();

    }

    public String getvalue(String KEY) {

        return encryptedPreferences.getString( KEY, "" );
    }

    public boolean saveusermail(String Value) {
        try {
            encryptedPreferences.edit()
                    .putString( "usermail", Value )
                    .apply();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public String getusermail() {
        return getvalue( "usermail" );

    }

    public boolean saveuserpass(String Value) {
        try {
            encryptedPreferences.edit()
                    .putString( "userpass", Value )
                    .apply();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public String getuserpass() {
        return getvalue( "userpass" );

    }


    public String getEncrypt(String pass, String data) {

        String password = pass;//"password";
        String message = data;//"hello world";
        try {
            //   if (BuildConfig.DEBUG) {
            //       AESCrypt.DEBUG_LOG_ENABLED = true;
            //   }
            String encryptedMsg = AESCrypt.decrypt( password, message );

            return encryptedMsg;
        } catch (GeneralSecurityException e) {
            //handle error
            e.toString();
        }
        return "";
    }

    public void getDecrypt() {
        String password = "password";
        String encryptedMsg = "2B22cS3UC5s35WBihLBo8w==";
        try {
            String messageAfterDecrypt = AESCrypt.decrypt(password, encryptedMsg);
        } catch (GeneralSecurityException e) {
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }
    }
}
