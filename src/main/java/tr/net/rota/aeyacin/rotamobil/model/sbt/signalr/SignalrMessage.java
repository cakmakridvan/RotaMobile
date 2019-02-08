package tr.net.rota.aeyacin.rotamobil.model.sbt.signalr;

import java.util.List;

/**
 * Created by ayacin on 17.03.2017.
 */

public class SignalrMessage {
    public String H; //hub
    public String M; //server message metod not connection custom metod
    public List<AClass> A; //message 0:name or id 1:message  string nesnesi parse edilecek

}
