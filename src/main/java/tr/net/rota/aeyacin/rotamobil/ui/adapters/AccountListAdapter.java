package tr.net.rota.aeyacin.rotamobil.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.model.DeviceMarker;
import tr.net.rota.aeyacin.rotamobil.model.sbt.Company;
import tr.net.rota.aeyacin.rotamobil.model.sbt.GetLocation;
import tr.net.rota.aeyacin.rotamobil.model.sbt.LocationR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.SendVehicle;
import tr.net.rota.aeyacin.rotamobil.model.sbt.StatusR;
import tr.net.rota.aeyacin.rotamobil.model.sbt.VehicleGroup;
import tr.net.rota.aeyacin.rotamobil.model.sbt.VehicleState;

import static tr.net.rota.aeyacin.rotamobil.Globals.MainCustomerAccountID;
import static tr.net.rota.aeyacin.rotamobil.data.server.GetLastDeviceMove;
import static tr.net.rota.aeyacin.rotamobil.data.server.GetListVehicleStatus;
import static tr.net.rota.aeyacin.rotamobil.data.server.GetUserVehicleRelation;

/**
 * Created by aeyacin on 18.09.2017.
 */

public class AccountListAdapter extends BaseAdapter implements Filterable {

    private List<Company> originalData;
    private List<Company> filteredData;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    private Activity activity;

    public AccountListAdapter(Activity activity, List<Company> data) {
        this.filteredData = data;
        this.originalData = data;
        this.mInflater = LayoutInflater.from(activity.getApplicationContext());
        this.activity = activity;
    }

    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.accounts_item, null);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.Account_item_CompanyName);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetDevicesWithCompanyID(filteredData.get(position).CompanyID, filteredData.get(position).CompanyName);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(filteredData.get(position).CompanyName);
        holder.AccountID = filteredData.get(position).CompanyID;
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        int AccountID;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Company> list = originalData;

            int count = list.size();
            final ArrayList<Company> nlist = new ArrayList<>(count);

            Company filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.CompanyName.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Company>) results.values;
            notifyDataSetInvalidated();
            notifyDataSetChanged();

        }

    }

    @SuppressLint("StaticFieldLeak")
    protected class ServiceAsyncTask extends AsyncTask<String, Integer, String> { // Gönderilecek veri, İşlem durumu, Gelen veri
        private ProgressDialog progressDialog = null;
        private final boolean cancelable;
        private final boolean exitOnError;
        private String message = null;
        private int AccountID;

        ServiceAsyncTask(final boolean cancelable, final boolean exitOnError, final int companyid) {
            this.cancelable = cancelable;
            this.exitOnError = exitOnError;
            this.AccountID = companyid;
        }

        /**
         * İşlem öncesi yapılacaklar bu metod içinde gerçekleşir. UI thread içinde çalışır.
         */
        @Override
        protected void onPreExecute() {
            // İşlem göstergeçini devam eden işlemlerde kullanıcıyı bilgilendirmek için kullanacağız.
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Hesap İşleniyor...");
            progressDialog.setCancelable(cancelable);
            progressDialog.setIndeterminate(true);

            if (cancelable) {
                progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.cancel();
                    }
                });
            }

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ServiceAsyncTask.this.cancel(true);

                    if (exitOnError) {
                        activity.finish();
                    }
                }
            });

            progressDialog.show();
        }

        /**
         * Arkaplan işlemimiz burada gerçekleşir. Android bu işlem için bir thread açar bu sebeple burada direk UI içinde bir işlem yapmamalıyız.
         */
        @Override
        protected String doInBackground(String... strings) {
            try {


                String webservice;
                List<VehicleState> VehicleStatelist = null;
                try {
                    webservice = GetListVehicleStatus(this.AccountID);
                    if (!webservice.equalsIgnoreCase("false")) {
                        Type collectionType_VehicleState = new TypeToken<List<VehicleState>>() {
                        }.getType();
                        VehicleStatelist = new Gson().fromJson(webservice, collectionType_VehicleState);
                    }
                } catch (Exception ignored) {

                }

                webservice = GetUserVehicleRelation(Globals.user.UserID, Globals.user.UserType, Globals.user.CompanyID);
                Type collectionType_VehicleGroup = new TypeToken<List<VehicleGroup>>() {
                }.getType();
                Globals.DevicesVehicleMap.clear();
                Globals.SendList.clear();
                Globals.groups = new Gson().fromJson(webservice, collectionType_VehicleGroup);

                for (int i = 0; i < Globals.groups.size(); i++) {
                    for (int xx = 0; xx < Globals.groups.get(i).Vehicle.size(); xx++) {

                        if (VehicleStatelist != null)
                            for (int zz = 0; zz < VehicleStatelist.size(); zz++) {
                                if (Globals.groups.get(i).Vehicle.get(xx).VehicleID == VehicleStatelist.get(zz).VehicleId) {
                                    Globals.groups.get(i).Vehicle.get(xx).IsAvaibleEngineBlokage = true;
                                    break;
                                }
                            }
                        Globals.SendList.add(new SendVehicle(Globals.groups.get(i).Vehicle.get(xx).VehicleID));
                        DeviceMarker tt = new DeviceMarker();
                        tt.device = Globals.groups.get(i).Vehicle.get(xx);
                        tt.GroupID = Globals.groups.get(i).GroupID;
                        Globals.DevicesVehicleMap.put(Globals.groups.get(i).Vehicle.get(xx).VehicleID, tt);
                    }
                }

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                webservice = GetLastDeviceMove(new Gson().toJson(Globals.SendList), Globals.user.CompanyID);
                if (!webservice.equalsIgnoreCase("false")) {
                    Type collectionType_LocationR = new TypeToken<List<GetLocation>>() {
                    }.getType();
                    List<GetLocation> locationlist = gson.fromJson(webservice, collectionType_LocationR);

                    for (int yy = 0; yy < locationlist.size(); yy++) {

                        LocationR loc = new LocationR();
                        StatusR stat = new StatusR();
                        loc.Speed = locationlist.get(yy).Speed;
                        loc.Angle = locationlist.get(yy).Angle;
                        loc.VehicleID = locationlist.get(yy).VehicleID;
                        loc.DeviceDateTime = locationlist.get(yy).DeviceDateTime;
                        loc.Latitude = locationlist.get(yy).Latitude;
                        loc.Longitude = locationlist.get(yy).Longitude;
                        loc.SatelliteCount = locationlist.get(yy).SatelliteCount;
                        stat.BatteryCharging = true;
                        stat.DateTime = locationlist.get(yy).StatusDateTime;
                        stat.GPSState = true;
                        stat.Ignition = locationlist.get(yy).Ignition;
                        stat.VehicleID = locationlist.get(yy).VehicleID;

                        if (Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID) != null) {
                            Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).VehicleImage = locationlist.get(yy).VehicleImagePath;
                            Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).LastLocation = locationlist.get(yy);
                            Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).location = loc;
                            Globals.DevicesVehicleMap.get(locationlist.get(yy).VehicleID).status = stat;
                        }
                    }
                }
                return "true";
            } catch (Exception e) {

                Globals.user.CompanyID = MainCustomerAccountID;
                return null;
            }
        }

        /**
         * Arkaplan işlemimiz bittiğinde bu metod çalışır.
         */
        @Override
        protected void onPostExecute(String response) {
            progressDialog.setCancelable(true);
            progressDialog.dismiss();
            Globals.isReChangeAccount = true;
            activity.finish();
            if (null == message) {
                progressDialog.setTitle("İşlem Başarılı");

            } else {
                progressDialog.setTitle("Hata :(");
                progressDialog.setMessage(message);

                if (exitOnError) {
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            activity.finish();
                        }
                    });
                }
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }, null == message ? 700 : 3000);
        }

        /*
         Arkaplan işlemi iptal edildiğinde bu metod çalışır.
         */
        @Override
        protected void onCancelled() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (exitOnError && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    private void GetDevicesWithCompanyID(int CompanyID, String CompanyName) {

        Globals.user.CompanyID = CompanyID;
        Globals.user.CompanyName = CompanyName;

        new ServiceAsyncTask(false, true, Globals.user.CompanyID).execute();


    }

}