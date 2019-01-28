package tr.net.rota.aeyacin.rotamobil.utils;

import java.util.ArrayList;


public class SmsStruct {

    private String Numara;
    private String DeviceTypeCode;
    private String SmsSosNumaraTanim;
    private String SmsCenterNumaraTanim;
    private String SmsSosNumaraIptal;
    private String ConcoxMotorBloke;
    private String ConcoxMotorBlokeKaldirma;
    private String DeviceType;
    private String MotorDurum;


    private String Name;
    private ArrayList<SmsStruct> list;

    public SmsStruct(String numara, String deviceType) {
        Numara = numara;
        this.DeviceType = deviceType;
        list = new ArrayList<>();
        SmsStruct tmp = new SmsStruct(Numara);
        tmp.Name = "Concox";
        tmp.DeviceTypeCode = "Concox";
        tmp.SmsSosNumaraTanim = "SOS,A," + Numara + "#";
        tmp.SmsCenterNumaraTanim = "CENTER,A," + Numara + "#";
        tmp.SmsSosNumaraIptal = "SOS,D,1#";
        tmp.ConcoxMotorBloke = "RELAY,1#";
        tmp.ConcoxMotorBlokeKaldirma = "RELAY,0#";
        tmp.MotorDurum = "RELAY#";

        list.add(tmp);

    }

    public SmsStruct(String numara) {
        Numara = numara;
    }

    public String getMotorDurum() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).DeviceTypeCode.equalsIgnoreCase(DeviceType)) {
                result += list.get(i).MotorDurum;
            }
        }
        return result;
    }

    public String getSosNumaraTanim() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).DeviceTypeCode.equalsIgnoreCase(DeviceType)) {
                result += list.get(i).SmsSosNumaraTanim;
            }
        }

        return result;
    }

    public String getCenterNumaraTanim() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).DeviceTypeCode.equalsIgnoreCase(DeviceType)) {
                result += list.get(i).SmsCenterNumaraTanim;
            }
        }

        return result;
    }

    public String getSosNumaraIptal() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).DeviceTypeCode.equalsIgnoreCase(DeviceType)) {
                result += list.get(i).SmsSosNumaraIptal;
            }
        }

        return result;
    }

    public String getConcoxMotorBloke() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).DeviceTypeCode.equalsIgnoreCase(DeviceType)) {
                result += list.get(i).ConcoxMotorBloke;
            }
        }

        return result;
    }

    public String getConcoxMotorBlokeKaldirma() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).DeviceTypeCode.equalsIgnoreCase(DeviceType)) {
                result += list.get(i).ConcoxMotorBlokeKaldirma;
            }
        }

        return result;
    }

}
