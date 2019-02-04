package tr.net.rota.aeyacin.rotamobil.data;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

/**
 * Created by ayacin on 9.03.2017.
 */

public class server {

    public static final String URL_RotaMobil = "http://ats2.rota.net.tr/Service/RotaService.asmx";//"http://ats2.rota.net.tr/RotaMobil/RotaMobilService.asmx";// "http://ats2.rota.net.tr/Service/RotaService.asmx";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final int WS_TIMEOUT = 60000;

    public static String GetUsers(String usermail, String password) {

        String METHOD_NAME = "GetUsers";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME);

        request.addProperty("Email", usermail);
        request.addProperty("Password", password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetMonthKm(String VehicleID) {

        String METHOD_NAME = "GetMonthKm";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME);

        request.addProperty("VehicleID", VehicleID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetUserVehicleRelation(int userID, boolean userType, int CompanyID) {

        String METHOD_NAME = "GetUserVehicleRelation";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME);

        request.addProperty("userID", userID);
        request.addProperty("userType", userType);
        request.addProperty("CompanyID", CompanyID);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetLastMotion(String VehicleList) {

        String METHOD_NAME = "GetLastMotion";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME);

        request.addProperty("VehicleID", VehicleList);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetLastLocation(String VehicleList) {

        String METHOD_NAME = "GetLastLocation";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME);

        request.addProperty("VehicleID", VehicleList);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetLastDeviceMove(String VehicleList, int CompanyID) {

        String METHOD_NAME = "GetLastDeviceMove";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME);

        request.addProperty("vehicleList", VehicleList);
        request.addProperty("CompanyID", CompanyID);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetVehicleArchive(int VehicleID, String StartDate, String EndDate) {

        String METHOD_NAME = "GetVehicleArchive";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("VehicleID", VehicleID);//770);//VehicleID );
        request.addProperty("StartDate", StartDate);//"16-5-2017 00:00:00");//
        request.addProperty("EndDate", EndDate);//"16-5-2017 23:00:00");//


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }
    public static String SendStopRunCommand(int vehicleId, String engineBlockage) {

        String METHOD_NAME = "SendStopRunCommand";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("vehicleId", vehicleId);//770);//VehicleID );
        request.addProperty("engineBlockage", engineBlockage);//"16-5-2017 00:00:00");//


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetVehicleStatus(int vehicleId) {

        String METHOD_NAME = "GetVehicleStatus";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("vehicleId", vehicleId);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }
    public static String GetListVehicleStatus(int companyId) {

        String METHOD_NAME = "GetListVehicleStatus";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("companyId", companyId);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(
                URL_RotaMobil,
                WS_TIMEOUT);
        SoapPrimitive result = null;
        String webReturn = "";

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            webReturn = result.toString();

        } catch (SocketTimeoutException e) {

            return "false";
        } catch (Exception e) {
            e.toString();

            webReturn = "false";
        } finally {
            return webReturn;
        }
    }

    public static String GetReport(String VehicleID){

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httppost = new HttpGet("http://178.18.200.116:90/api/RotaApi/BaseReportGet?VehicleID=" + VehicleID);
// Depends on your web service
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            // Oops
            return "false";
        }
        finally {
            try{

                if(inputStream != null)inputStream.close();

            }catch(Exception squish){
                return "false";
            }
        }
              return result;
    }
}
