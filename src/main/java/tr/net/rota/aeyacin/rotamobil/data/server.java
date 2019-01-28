package tr.net.rota.aeyacin.rotamobil.data;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
}
