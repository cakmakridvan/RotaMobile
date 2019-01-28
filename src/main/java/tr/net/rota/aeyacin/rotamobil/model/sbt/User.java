package tr.net.rota.aeyacin.rotamobil.model.sbt;

import java.util.List;

/**
 * Created by ayacin on 19.03.2017.
 */

public class User {
    public int UserID;
    public String NameSurname;
    public String CompanyName;
    public int CompanyID;
    public Boolean UserType;
    public String StaffTask;
    public String EntranceAuthority;
    public List<Company> CompanyList;
}
