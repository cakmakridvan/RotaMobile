package tr.net.rota.aeyacin.rotamobil.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.model.sbt.MonthlyRoadReportObj;
import tr.net.rota.aeyacin.rotamobil.ui.activities.VehicleHistory;

/**
 * Created by aeyacin on 12.02.2018.
 */

public class MonthlyListAdapter extends RecyclerView.Adapter<MonthlyListAdapter.ViewHolder> {

    private List<MonthlyRoadReportObj> mArrayList;
    private Activity mActivity;
    private int VehicleID;
    private String Plate;

    public MonthlyListAdapter(Activity activity, List<MonthlyRoadReportObj> arrayList, int vehicleID, String plate) {

        this.Plate = plate;
        this.mArrayList = new ArrayList<>();
        this.mArrayList.addAll(arrayList);
        Collections.sort(this.mArrayList, new Comparator<MonthlyRoadReportObj>() {
            public int compare(MonthlyRoadReportObj o1, MonthlyRoadReportObj o2) {
                try {
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(o1.FirstMotionDateTime);
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(o2.FirstMotionDateTime);

                    if (date1.getTime() > date2.getTime()) {
                        return 1;
                    }
                    return -1;

                } catch (Exception ex) {
                    return 0;
                }
            }
        });
        Collections.reverse(this.mArrayList);
        this.mActivity = activity;
        this.VehicleID = vehicleID;
    }

    @Override
    public MonthlyListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.monthly_road_report_item, viewGroup, false);
        return new MonthlyListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthlyListAdapter.ViewHolder viewHolder, int i) {

        final MonthlyRoadReportObj item = mArrayList.get(i);

        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.avgSpeed.setText(String.valueOf(item.AvarageSpeed + " Km/s"));
        viewHolder.maxSpeed.setText(String.valueOf(item.HighestSpeed + " Km/s"));
        viewHolder.totalKm.setText(String.valueOf(item.DayTotalKm + " Km"));
        String title = "";
        try {
            Date readedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item.FirstMotionDateTime);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            title = outFormat.format(readedDate) + " (" + String.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(readedDate)) + ")";
            if (new SimpleDateFormat("dd.MM.yyyy").format(readedDate).toString().equalsIgnoreCase(new SimpleDateFormat("dd.MM.yyyy").format(new Date()))) {
                title = mActivity.getString(R.string.monthly_report_today) + " (" + String.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(readedDate)) + ")";

            }
        } catch (ParseException e) {
            e.printStackTrace();
            title = item.FirstMotionDateTime;
        }

        viewHolder.date.setText(String.valueOf(title));
        viewHolder.ditem_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(mActivity.getApplicationContext(), VehicleHistory.class);
                    Bundle b = new Bundle();
                    b.putInt("VehicleID", VehicleID); //Your id
                    Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item.FirstMotionDateTime);
                    Date finishDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item.LastMotionDateTime);
                    b.putString("StartDate", String.valueOf(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(startDate)));

                    b.putString("EndDate", String.valueOf(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(finishDate)));
                    b.putString("Plate", Plate); //Your id

                    intent.putExtras(b); //Put your id to your next Intent
                    mActivity.startActivity(intent);
                } catch (Exception Ex) {
                    Toast.makeText(MonthlyListAdapter.this.mActivity, R.string.alert_error_parse_data, Toast.LENGTH_LONG).show();

                }

            }
        });


/*
        viewHolder.ditem_DateValue.setText(item.Date);
        viewHolder.ditem_DistributionCodeValue.setText(item.DistributionCode);
        viewHolder.ditem_SerialNumberValue.setText(item.Serial + "-" + item.SerialNumber);
        viewHolder.ditem_SenderNameValue.setText(item.SenderName);
        viewHolder.ditem_ReceiverNameValue.setText(item.ReceiverName);
        if (item.ReceiverAddress2 == null || item.ReceiverAddress2.equals("")) {
            viewHolder.ditem_AddressValue.setText(item.ReceiverAddress1);
        } else {
            viewHolder.ditem_AddressValue.setText(item.ReceiverAddress1 + "/" + item.ReceiverAddress2);
        }
        // viewHolder.ditem_AddressValue.setText(mFilteredList.get(i).Date);

        viewHolder.ditem_CityValue.setText(item.ReceiverCity);
        viewHolder.ditem_DistrictValue.setText(item.ReceiverDistrict);

        if (item.ReceiverTelephone2 == null || item.ReceiverTelephone2.equals("")) {
            viewHolder.ditem_TelephoneValue.setText(item.ReceiverTelephone1);
        } else {
            viewHolder.ditem_TelephoneValue.setText(item.ReceiverTelephone1 + "/" + item.ReceiverTelephone2);
        }
        viewHolder.ditem_buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.sessionManager.data.deliveryItem = item;
                App.changeActivity(DeliverListAdapter.this.mActivity, DeliveryDetailActivity.class, false);


            }
        });
        viewHolder.ditem_buttonInstantDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        viewHolder.ditem_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                App.sessionManager.data.deliveryItem = item;
                App.changeActivity(DeliverListAdapter.this.mActivity, DeliveryInstantActivity.class, true);
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date,
                totalKm,
                maxSpeed,
                avgSpeed;

        private ImageView btnDetail;

        private View ditem_view;


        public ViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.main_item_date);
            totalKm = (TextView) view.findViewById(R.id.main_item_total_distance);
            maxSpeed = (TextView) view.findViewById(R.id.main_item_max_speed);
            avgSpeed = (TextView) view.findViewById(R.id.main_item_avg_speed);
            btnDetail = (ImageView) view.findViewById(R.id.monthly_detail);

            ditem_view = view;

        }
    }

}
