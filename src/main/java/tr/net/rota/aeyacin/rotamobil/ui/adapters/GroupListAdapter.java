package tr.net.rota.aeyacin.rotamobil.ui.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.data.server;
import tr.net.rota.aeyacin.rotamobil.func.Logger;
import tr.net.rota.aeyacin.rotamobil.model.sbt.MonthlyRoadReportObj;
import tr.net.rota.aeyacin.rotamobil.model.sbt.ReportData;
import tr.net.rota.aeyacin.rotamobil.model.sbt.Vehicle;
import tr.net.rota.aeyacin.rotamobil.model.sbt.VehicleGroup;
import tr.net.rota.aeyacin.rotamobil.ui.activities.MonthlyRoadReportActivity;
import tr.net.rota.aeyacin.rotamobil.ui.activities.ReportActivity;
import tr.net.rota.aeyacin.rotamobil.ui.activities.VehicleHistory;
import tr.net.rota.aeyacin.rotamobil.ui.view.VerticalTextView3;
import tr.net.rota.aeyacin.rotamobil.utils.SmsStruct;

import static android.content.Context.TELEPHONY_SERVICE;

public class GroupListAdapter extends BaseExpandableListAdapter {

    int RQC_PICK_DATE_TIME_RANGE = 2365;
    private Context _context;
    private Activity _activity;


    private List<VehicleGroup> _listDataVehicleGroupWithListStatic;
    private List<VehicleGroup> _listDataVehicleGroupWithList = new ArrayList<>();
    /*
    private List<devicegroup> _listDataHeaderStatic;
    private List<devicegroup> _listDataHeader;
    // header titles
    // child data in format of header title, child title
    private HashMap<Integer, List<DeviceMarker>> _listDataChildStatic;
    private HashMap<Integer, List<DeviceMarker>> _listDataChild;
    */

    private List<SmsStruct> _listGelenMesajlar = new ArrayList<>();

    public GroupListAdapter(Activity context, List<VehicleGroup> datalist) {
        //    List<devicegroup> listDataHeader,HashMap<Integer, List<DeviceMarker>> listChildData) {
        this._context = context;
        this._activity = context;
        this._listDataVehicleGroupWithListStatic = datalist;//.addAll( datalist );
        this._listDataVehicleGroupWithList.addAll(this._listDataVehicleGroupWithListStatic);
   /*     this._listDataHeaderStatic = listDataHeader;
        this._listDataChildStatic = listChildData;
        this._listDataHeader = new ArrayList<>();
        this._listDataHeader.addAll( this._listDataHeaderStatic );
        this._listDataChild = new HashMap<>();
        this._listDataChild.putAll( this._listDataChildStatic );*/
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        //  return this._listDataChild.get( this._listDataHeader.get( groupPosition ).groupID ).get( childPosititon );
        return this._listDataVehicleGroupWithList.get(groupPosition).Vehicle.get(childPosititon);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
        //  return this._listDataVehicleGroupWithList.get( groupPosition).Vehicle.get( childPosition ).VehicleID;

    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Vehicle childitem = (Vehicle) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  try {
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.list_item, null);

            // } catch (Exception e) {
            //       e.toString();
            //   }
        }
        try {

            TextView vehicle_plate = convertView.findViewById(R.id.vehicle_plate);
            TextView vehicle_tag_type = convertView.findViewById(R.id.main_item_car_type_and_tag);
            TextView vehicle_speed = convertView.findViewById(R.id.main_item_Speed);
            VerticalTextView3 vehicle_ignition = convertView.findViewById(R.id.vehicle_ignition_state);
            TextView vehicle_time = convertView.findViewById(R.id.main_item_time);
            TextView vehicle_date = convertView.findViewById(R.id.main_item_date);
            TextView vehicle_adress = convertView.findViewById(R.id.main_item_adress);
            Button vehicle_track_button = convertView.findViewById(R.id.buttonTrack);
            Button vehicle_history_button = convertView.findViewById(R.id.buttonHistory);
            Button monthly_table_button = convertView.findViewById(R.id.buttonMonthlyTable);
            Button report_button = convertView.findViewById(R.id.buttonRapor);

            ImageButton popupmenu = convertView.findViewById(R.id.imageButtonMenu);

            popupmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v, childitem.VehicleID);
                }
            });

            vehicle_plate.setText(childitem.Plate);
            vehicle_plate.setTag(childitem.VehicleID);
            vehicle_tag_type.setText(childitem.Tag);
            int cartype = Globals.getcariconview(Globals.DevicesVehicleMap.get(childitem.VehicleID).VehicleImage,
                    Globals.DevicesVehicleMap.get(childitem.VehicleID).LastLocation.Ignition, Globals.DevicesVehicleMap.get(childitem.VehicleID).LastLocation.Speed > 3, false);

            vehicle_tag_type.setCompoundDrawablesWithIntrinsicBounds(cartype, 0, 0, 0);
            if (Globals.DevicesVehicleMap.get(childitem.VehicleID).status.Ignition) {
                if (Globals.DevicesVehicleMap.get(childitem.VehicleID).location.Speed > 3) {
                    vehicle_ignition.setText(R.string.ignition_moving_text);
                    vehicle_ignition.setBackgroundResource(R.color.ignition_open_and_speed);
                    vehicle_speed.setText(Globals.DevicesVehicleMap.get(childitem.VehicleID).location.Speed + " km/s");
                } else {
                    vehicle_ignition.setText(R.string.ignition_on_text);
                    vehicle_ignition.setBackgroundResource(R.color.ignition_open);
                    vehicle_speed.setText("0 km/s");
                }
            } else {
                vehicle_ignition.setText(R.string.ignition_off_text);
                vehicle_ignition.setBackgroundResource(R.color.ignition_close);
                vehicle_speed.setText("0 km/s");
            }

            DateFormat df = new SimpleDateFormat("HH:mm:ss"); //format time
            DateFormat df1 = new SimpleDateFormat("dd MMMM yyyy");//foramt date
            vehicle_time.setText(df.format(Globals.DevicesVehicleMap.get(childitem.VehicleID).status.DateTime.getTime()));
            vehicle_date.setText(df1.format(Globals.DevicesVehicleMap.get(childitem.VehicleID).status.DateTime.getTime()));
            vehicle_adress.setText(Globals.DevicesVehicleMap.get(childitem.VehicleID).VehicleAdress);
            vehicle_track_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globals.selectedVehicleID = childitem.VehicleID;
                    _activity.finish();
                }
            });

            monthly_table_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new getMonthlyReport(childitem.VehicleID, childitem.Plate).execute();
                }
            });
            vehicle_history_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*                    Intent pickerintent = new Intent( _activity.getApplicationContext(), DateTimeRangePickerActivity.class );
                    _activity.startActivityForResult( pickerintent, RQC_PICK_DATE_TIME_RANGE );
*/
                    popupDialog(childitem.VehicleID);

                }
            });
            report_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new getReport(childitem.VehicleID).execute();
                }
            });

            if (childitem.IsAvaibleEngineBlokage) {
                popupmenu.setVisibility(View.VISIBLE);
            } else {
                popupmenu.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {

        }


        return convertView;
    }

    class getMonthlyReport extends AsyncTask<Object, Object, Boolean> {
        ProgressDialog pDialog;

        int VehicleID;
        public String Plate;
        private List<MonthlyRoadReportObj> results = null;

        public getMonthlyReport(int vehicleID, String plate) {
            this.VehicleID = vehicleID;
            this.Plate = plate;
        }

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(GroupListAdapter.this._activity);
            pDialog.setMessage("Aylık Yol Raporu Getiriliyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {

                String result = server.GetMonthKm(this.VehicleID + "");

                if (!result.trim().equalsIgnoreCase("false")) {
                    Type collectionType = new TypeToken<List<MonthlyRoadReportObj>>() {
                    }.getType();

                    results = new Gson().fromJson(result, collectionType);

                    return true;
                } else {
                    return false;
                    //   finish();
                }
                // int a=histories.size();
            } catch (Exception e) {
                return false;
            }
            //return null;
        }

        protected void onPostExecute(Boolean state) { //Posttan sonra
            try {
                if (state == true) {
                    if (this.results == null) {
                        Toast.makeText(_activity.getApplicationContext(), "Veri Bulunamadı.", Toast.LENGTH_LONG).show();

                    } else if (this.results.size() < 1) {
                        Toast.makeText(_activity.getApplicationContext(), "Veri Bulunamadı.", Toast.LENGTH_LONG).show();

                    } else {
                        try {
                            Intent intent = new Intent(_activity.getApplicationContext(), MonthlyRoadReportActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("VehicleID", VehicleID); //Your id
                            b.putString("datas", new Gson().toJson(this.results));
                            b.putString("Plate", this.Plate);

                            intent.putExtras(b);
                            _activity.startActivity(intent);
                        } catch (Exception Ex) {
                            Toast.makeText(_activity.getApplicationContext(), "Veri İşleme Hatası", Toast.LENGTH_LONG).show();
                        }
                    }
                    pDialog.dismiss();

                } else {

                }
                pDialog.dismiss();  //ProgresDialog u kapatıyoruz.

            } catch (Exception e) {
                pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
            }

        }
    }


    class getReport extends AsyncTask<Object,Object,Boolean>{
        ProgressDialog pDialog_report;

        int VehicleID;
        private List<ReportData> reports_result = null;

        public getReport(int vehicleID){

            this.VehicleID = vehicleID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog_report = new ProgressDialog(GroupListAdapter.this._activity);
            pDialog_report.setMessage("Raporlar Getiriliyor...");
            pDialog_report.setIndeterminate(true);
            pDialog_report.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog_report.show();
        }

        @Override
        protected Boolean doInBackground(Object... objects) {

            try {

                String get_result = server.GetReport(this.VehicleID + "");

                if (!get_result.trim().equalsIgnoreCase("false")) {

                    Type collectionType_report = new TypeToken<List<ReportData>>() {
                    }.getType();

                    reports_result = new Gson().fromJson(get_result, collectionType_report);

                    return true;
                } else {
                    return false;
                }
            }catch (Exception e){

                return false;
            }


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            try {
                if (aBoolean == true) {
                    if (this.reports_result == null) {
                        Toast.makeText(_activity.getApplicationContext(), "Veri Bulunamadı.", Toast.LENGTH_LONG).show();

                    } else if (this.reports_result.size() < 1) {
                        Toast.makeText(_activity.getApplicationContext(), "Veri Bulunamadı.", Toast.LENGTH_LONG).show();

                    } else {
                        try {
                            Intent intent = new Intent(_activity.getApplicationContext(), ReportActivity.class);
                            Bundle report_act = new Bundle();
                            report_act.putInt("report_VehicleID", VehicleID); //Your id
                            report_act.putString("report_datas", new Gson().toJson(this.reports_result));


                            intent.putExtras(report_act);
                            _activity.startActivity(intent);
                        } catch (Exception Ex) {
                            Toast.makeText(_activity.getApplicationContext(), "Veri İşleme Hatası", Toast.LENGTH_LONG).show();
                        }
                    }
                    pDialog_report.dismiss();

                } else {

                }
                pDialog_report.dismiss();  //ProgresDialog u kapatıyoruz.

            } catch (Exception e) {
                pDialog_report.dismiss();  //ProgresDialog u kapatıyoruz.
            }
        }
    }

//    private void showPopupMenu(View view, final int vehicleID) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(_activity, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_main_items, popup.getMenu());
//        //  popup.setOnMenuItemClickListener(new FormItemAdapter.MyMenuItemClickListener());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_cihaz_tanimla:
//
//                        cihazTanimla(vehicleID);
//                        return true;
//                    case R.id.action_engine_kilitle:
//
//                        motorKilitle(vehicleID);
//                        return true;
//                    case R.id.action_engine_open:
//
//                        motorKilitAc(vehicleID);
//                        return true;
//                    case R.id.action_engine_state:
//                        engineState(vehicleID);
//                        return true;
//
//                    default:
//                }
//                return false;
//            }
//        });
//
//        popup.show();
//    }

    private void showPopupMenu(View view, final int vehicleID) {
        // inflate menu
        PopupMenu popup = new PopupMenu(_activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main_items_new, popup.getMenu());
        //  popup.setOnMenuItemClickListener(new FormItemAdapter.MyMenuItemClickListener());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_engine_kilitle:
                        new asyncSetMotorBlokajServer(vehicleID, false).execute();
                        return true;
                    case R.id.action_engine_open:

                        new asyncSetMotorBlokajServer(vehicleID, true).execute();
                        return true;
                    default:
                }
                return false;
            }
        });

        popup.show();
    }

    private void engineState(final int vehicleID) {
        try {

            if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {


//                SmsStruct tmp = new SmsStruct(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber,
//                        Globals.DevicesVehicleMap.get(vehicleID).LastLocation.DeviceType);

                TelephonyManager tm = (TelephonyManager) _activity.getSystemService(TELEPHONY_SERVICE);
                if (tm.getLine1Number() == null) {
                    new AlertDialog.Builder(_activity).setTitle("Hata")
                            .setMessage("Bu Cihazın Telefon Numarası ALınamıyor")
                            .setPositiveButton("TAMAM", null)
                            .show();
                    return;
                } else {
                    if (tm.getLine1Number().toString().trim().equalsIgnoreCase("") || tm.getLine1Number().length() < 11) {

                        try {
                            final Dialog dialog = new Dialog(_activity);
                            dialog.setContentView(R.layout.content_phone_number);
                            dialog.setTitle("Telefon Numaranızı Giriniz..");


                            final EditText edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
                            Button btnTamam = dialog.findViewById(R.id.btnPhoneTamam);

                            btnTamam.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    new AlertDialog.Builder(_activity).setTitle("Eminmisiniz")
                                            .setMessage("Numarayı Onaylıyormusunuz")
                                            .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogs, int which) {

                                                    dialog.dismiss();
//                                                    new asynEngineState(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();
                                                    new asynEngineStateYeniTel(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();

                                                }
                                            })
                                            .setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            });

                            dialog.show();

                        } catch (Exception e) {

                        }


                    } else {
                        new asynEngineState(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tm.getLine1Number()).execute();//değiştir
                    }
                }

            } else {
                checkAndRequestPermissions();
            }

        } catch (Exception e) {
            //  pDialog.dismiss(); new asynEngineState(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tm.getLine1Number()).execute();//değiştir
            Toast.makeText(_activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void cihazTanimla(final int vehicleID) {
        try {

            if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {


//                SmsStruct tmp = new SmsStruct(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber,
//                        Globals.DevicesVehicleMap.get(vehicleID).LastLocation.DeviceType);

                TelephonyManager tm = (TelephonyManager) _activity.getSystemService(TELEPHONY_SERVICE);
                if (tm.getLine1Number() == null) {
                    new AlertDialog.Builder(_activity).setTitle("Hata")
                            .setMessage("Bu Cihazın Telefon Numarası ALınamıyor")
                            .setPositiveButton("TAMAM", null)
                            .show();
                    return;
                } else {
                    if (tm.getLine1Number().toString().trim().equalsIgnoreCase("") || tm.getLine1Number().length() < 11) {

                        try {
                            final Dialog dialog = new Dialog(_activity);
                            dialog.setContentView(R.layout.content_phone_number);
                            dialog.setTitle("Telefon Numaranızı Giriniz..");


                            final EditText edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
                            Button btnTamam = dialog.findViewById(R.id.btnPhoneTamam);

                            btnTamam.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    new AlertDialog.Builder(_activity).setTitle("Eminmisiniz")
                                            .setMessage("Numarayı Onaylıyormusunuz")
                                            .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogs, int which) {
//                                                    String a= edtPhoneNumber.getText().toString().trim();
                                                    dialog.dismiss();
//                                                    new sendmessage(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();
                                                    new asynSendMessageYeniTel(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();

                                                }
                                            })
                                            .setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            });

                            dialog.show();

                        } catch (Exception e) {
                            e.toString();
                        }


                    } else {
                        new sendmessage(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tm.getLine1Number()).execute();
                    }
                }

            } else {
                checkAndRequestPermissions();
            }

        } catch (Exception e) {
            //  pDialog.dismiss(); new sendmessage(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tm.getLine1Number()).execute();
            Toast.makeText(_activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void motorKilitAc(final int vehicleID) {
        try {

            if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {


//                SmsStruct tmp = new SmsStruct(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber,
//                        Globals.DevicesVehicleMap.get(vehicleID).LastLocation.DeviceType);

                TelephonyManager tm = (TelephonyManager) _activity.getSystemService(TELEPHONY_SERVICE);
                if (tm.getLine1Number() == null) {
                    new AlertDialog.Builder(_activity).setTitle("Hata")
                            .setMessage("Bu Cihazın Telefon Numarası ALınamıyor")
                            .setPositiveButton("TAMAM", null)
                            .show();
                    return;
                } else

                {
                    if (tm.getLine1Number().toString().trim().equalsIgnoreCase("") || tm.getLine1Number().length() < 11) {

                        try {
                            final Dialog dialog = new Dialog(_activity);
                            dialog.setContentView(R.layout.content_phone_number);
                            dialog.setTitle("Telefon Numaranızı Giriniz..");


                            final EditText edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
                            Button btnTamam = dialog.findViewById(R.id.btnPhoneTamam);

                            btnTamam.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    new AlertDialog.Builder(_activity).setTitle("Eminmisiniz")
                                            .setMessage("Numarayı Onaylıyormusunuz")
                                            .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogs, int which) {

                                                    dialog.dismiss();
//                                                    new asynmotorKilitAc(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();
                                                    //new asynMotorKilitAcYeniTel(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();

                                                    SmsStruct tmp = new SmsStruct(edtPhoneNumber.getText().toString().trim(), "Concox");

                                                    messageGonder(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tmp.getConcoxMotorBlokeKaldirma());
                                                    new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("İşlem Başarılı")
                                                            .setContentText("Motor Kilitleme İsteği Gönderildi")
                                                            .setConfirmText("TAMAM")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismiss();
                                                                }
                                                            })
                                                            .show();

                                                }
                                            })
                                            .setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            });

                            dialog.show();

                        } catch (Exception e) {

                        }


                    } else {
                        new asynmotorKilitAc(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tm.getLine1Number()).execute();
                    }
                }

                //  pDialog.dismiss();new asynmotorKilitAc(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tm.getLine1Number()).execute();
            } else {
                checkAndRequestPermissions();
            }

        } catch (Exception e) {
            //  pDialog.dismiss();
            Toast.makeText(_activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void motorKilitle(final int vehicleID) {
        try {

            if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {


//                SmsStruct tmp = new SmsStruct(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber,
//                        Globals.DevicesVehicleMap.get(vehicleID).LastLocation.DeviceType);


                final TelephonyManager tm = (TelephonyManager) _activity.getSystemService(TELEPHONY_SERVICE);
                if (tm.getLine1Number() == null) {
                    new AlertDialog.Builder(_activity).setTitle("Hata")
                            .setMessage("Bu Cihazın Telefon Numarası ALınamıyor")
                            .setPositiveButton("TAMAM", null)
                            .show();
                    return;
                } else {
                    if (tm.getLine1Number().toString().trim().equalsIgnoreCase("") || tm.getLine1Number().length() < 11) {

                        try {
                            final Dialog dialog = new Dialog(_activity);
                            dialog.setContentView(R.layout.content_phone_number);
                            dialog.setTitle("Telefon Numaranızı Giriniz..");


                            final EditText edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
                            Button btnTamam = dialog.findViewById(R.id.btnPhoneTamam);

                            btnTamam.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    new AlertDialog.Builder(_activity).setTitle("Eminmisiniz")
                                            .setMessage("Numarayı Onaylıyormusunuz")
                                            .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogs, int which) {

                                                    dialog.dismiss();
//                                                    new asynMotorKilitle(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();
                                                    //   new asynMotorKilitleYeniTel(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, edtPhoneNumber.getText().toString().trim()).execute();
                                                    SmsStruct tmp = new SmsStruct(edtPhoneNumber.getText().toString().trim(), "Concox");

                                                    messageGonder(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tmp.getConcoxMotorBloke());
                                                    new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("İşlem Başarılı")
                                                            .setContentText("Motor Kilitleme İsteği Gönderildi")
                                                            .setConfirmText("TAMAM")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismiss();
                                                                }
                                                            })
                                                            .show();

                                                }
                                            })
                                            .setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            });

                            dialog.show();

                        } catch (Exception e) {

                        }


                    } else {
                        new asynMotorKilitle(Globals.DevicesVehicleMap.get(vehicleID).LastLocation.GsmNumber, tm.getLine1Number());
                    }
                }


                //  pDialog.dismiss();
            } else {
                checkAndRequestPermissions();
            }

        } catch (Exception e) {
            //  pDialog.dismiss();
            Toast.makeText(_activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class asynMotorKilitAcYeniTel extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;
        private Boolean statesmsreader = true;
        Integer state = 0;

        public asynMotorKilitAcYeniTel(String msjGonderilecekNumber, String bnmNumber) {
            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Motor Kilidi Açılıyor");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer ınteger) {
            super.onPostExecute(ınteger);

            pDialog.dismiss();
            if (ınteger == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (ınteger == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İşlem Başarılı Bir Şekilde Sonuçlanmıştır")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (ınteger == 2) {
//zaten
                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("Motor Kilidi Zaten Açık")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }

        }


        @Override
        protected Integer doInBackground(String... params) {

            try {

                Date start = new Date();

                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                messageGonder(msjgonderilecekNumber, tmp.getConcoxMotorBlokeKaldirma());

                while (statesmsreader) {

                    SmsRadar.initializeSmsRadarService(_activity, new SmsListener() {
                        @Override
                        public void onSmsSent(Sms sms) {

                        }

                        @Override
                        public void onSmsReceived(Sms sms) {

                            if (sms.getAddress().contains(msjgonderilecekNumber)) {

                                if (sms.getMsg().contains("Success!")) {
                                    statesmsreader = false;
                                    state = 1;
                                } else {
                                    statesmsreader = false;
                                    state = 2;
                                }
                            }
                        }
                    });

                    if ((new Date().getTime() - start.getTime()) > (1000 * 45)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);

                }


            } catch (Exception e) {
            }

            SmsRadar.stopSmsRadarService(_activity);
            return state;
        }
    }

    private class asynMotorKilitleYeniTel extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;
        private Boolean statesmsreader = true;
        Integer state = 0;

        public asynMotorKilitleYeniTel(String msjGonderilecekNumber, String bnmNumber) {
            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Motor Kilitleniyor");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer ınteger) {
            super.onPostExecute(ınteger);

            pDialog.dismiss();
            if (ınteger == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (ınteger == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İşlem Başarılı Bir Şekilde Sonuçlanmıştır")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (ınteger == 2) {
//zaten
                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("Motor Kilidi Zaten Açık")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }

        }


        @Override
        protected Integer doInBackground(String... params) {

            try {

                Date start = new Date();

                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                messageGonder(msjgonderilecekNumber, tmp.getConcoxMotorBloke());

                while (statesmsreader) {

                    SmsRadar.initializeSmsRadarService(_activity, new SmsListener() {
                        @Override
                        public void onSmsSent(Sms sms) {

                        }

                        @Override
                        public void onSmsReceived(Sms sms) {

                            if (sms.getAddress().contains(msjgonderilecekNumber)) {

                                if (sms.getMsg().contains("Success!")) {
                                    statesmsreader = false;
                                    state = 1;
                                } else {
                                    statesmsreader = false;
                                    state = 2;
                                }
                            }
                        }
                    });

                    if ((new Date().getTime() - start.getTime()) > (1000 * 45)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);

                }


            } catch (Exception e) {
            }

            SmsRadar.stopSmsRadarService(_activity);
            return state;
        }
    }

    private class asynEngineStateYeniTel extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;
        private Boolean statesmsreader = true;
        Integer state = 0;

        public asynEngineStateYeniTel(String msjGonderilecekNumber, String bnmNumber) {
            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Motor Kilit Durumuna Bakılıyor");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer ınteger) {
            super.onPostExecute(ınteger);

            pDialog.dismiss();
            if (ınteger == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (ınteger == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İşlem Başarılı Bir Şekilde Sonuçlanmıştır")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (ınteger == 2) {
//zaten
                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("Motor Kilidi Zaten Açık")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }

        }


        @Override
        protected Integer doInBackground(String... params) {

            try {

                Date start = new Date();

                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                messageGonder(msjgonderilecekNumber, tmp.getMotorDurum());


                while (statesmsreader) {

                    SmsRadar.initializeSmsRadarService(_activity, new SmsListener() {
                        @Override
                        public void onSmsSent(Sms sms) {

                        }

                        @Override
                        public void onSmsReceived(Sms sms) {

                            if (sms.getAddress().contains(msjgonderilecekNumber)) {

                                if (sms.getMsg().contains("RELAY:1")) {
                                    statesmsreader = false;
                                    state = 1;
                                } else {
                                    statesmsreader = false;
                                    state = 2;
                                }
                            }
                        }
                    });

                    if ((new Date().getTime() - start.getTime()) > (1000 * 45)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);

                }


            } catch (Exception e) {
            }

            SmsRadar.stopSmsRadarService(_activity);
            return state;
        }
    }

    private class asynSendMessageYeniTel extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;
        private Boolean statesmsreader = true;
        Integer state = 0;

        public asynSendMessageYeniTel(String msjGonderilecekNumber, String bnmNumber) {
            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Cihaz Tanımlanıyor Lütfen Bekleyiniz");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer ınteger) {
            super.onPostExecute(ınteger);

            pDialog.dismiss();
            if (ınteger == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (ınteger == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İşlem Başarılı Bir Şekilde Sonuçlanmıştır")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (ınteger == 2) {
//zaten
                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("Cihaz Tanımlanamadı.")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }
        }

        @Override
        protected Integer doInBackground(String... params) {

            try {

                Date start = new Date();

                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                messageGonder(msjgonderilecekNumber, tmp.getSosNumaraTanim());

                while (statesmsreader) {

                    SmsRadar.initializeSmsRadarService(_activity, new SmsListener() {
                        @Override
                        public void onSmsSent(Sms sms) {

                        }

                        @Override
                        public void onSmsReceived(Sms sms) {

                            if (sms.getAddress().contains(msjgonderilecekNumber)) {

                                if (sms.getMsg().contains("OK!")) {
                                    statesmsreader = false;
                                    state = 1;
                                } else {
                                    statesmsreader = false;
                                    state = 2;
                                }
                            }
                        }
                    });

                    if ((new Date().getTime() - start.getTime()) > (1000 * 45)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);
                }

                messageGonder(msjgonderilecekNumber, tmp.getCenterNumaraTanim());// Mesajı Gonderdik

                if (state == 1) {
                    statesmsreader = true;
                } else if (state == 0) {
                    statesmsreader = false;
                } else {
                    statesmsreader = false;
                }


                while (statesmsreader) {

                    SmsRadar.initializeSmsRadarService(_activity, new SmsListener() {
                        @Override
                        public void onSmsSent(Sms sms) {

                        }

                        @Override
                        public void onSmsReceived(Sms sms) {

                            if (sms.getAddress().contains(msjgonderilecekNumber)) {

                                if (sms.getMsg().contains("Center Number:")) {
                                    statesmsreader = false;
                                    state = 1;
                                } else {
                                    statesmsreader = false;
                                    state = 2;
                                }
                            }
                        }
                    });

                    if ((new Date().getTime() - start.getTime()) > (1000 * 90)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);
                }

                messageGonder(msjgonderilecekNumber, tmp.getSosNumaraIptal());// Mesajı Gonderdik

                if (state == 1) {
                    statesmsreader = true;
                } else if (state == 0) {
                    statesmsreader = false;
                } else {
                    statesmsreader = false;
                }

                while (statesmsreader) {

                    SmsRadar.initializeSmsRadarService(_activity, new SmsListener() {
                        @Override
                        public void onSmsSent(Sms sms) {

                        }

                        @Override
                        public void onSmsReceived(Sms sms) {

                            if (sms.getAddress().contains(msjgonderilecekNumber)) {

                                if (sms.getMsg().contains("SOS1: SOS2: SOS3:")) {
                                    statesmsreader = false;
                                    state = 1;
                                } else {
                                    statesmsreader = false;
                                    state = 2;
                                }
                            }
                        }
                    });

                    if ((new Date().getTime() - start.getTime()) > (1000 * 135)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);
                }


            } catch (Exception e) {
            }

            SmsRadar.stopSmsRadarService(_activity);
            return state;
        }
    }

    private class asynEngineState extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;

        public asynEngineState(String msjGonderilecekNumber, String bnmNumber) {
            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Motor Kilit Durumuna Bakılıyor");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            pDialog.dismiss();
            try {

                if (integer == 1) {
                    //başarılı

                    new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("MOTOR KAPALI")
                            .setContentText("MOTOR KİLİDİ AÇIK")
                            .setConfirmText("TAMAM")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();

                } else if (integer == 0) {
                    //zaman aşımı
                    new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Hata")
                            .setContentText("İşlem Zaman Aşımına Uğradı")
                            .setConfirmText("TAMAM")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    //motor durumu tanımlı değil
                    new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MOTOR AÇIK")
                            .setContentText("MOTOR KİLİDİ KAPALI")
                            .setConfirmText("TAMAM")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }

            } catch (Exception e) {
            }


        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer state = 0;
            try {

                Date start = new Date();
                Boolean statesmsreader = true;
                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                Uri uriSMSURI = Uri.parse("content://sms/inbox");
                int DAY_MILLISECONDS = 24 * 60 * 60 * 1000;
                Calendar cal = Calendar.getInstance();
                //        String[] projection = {"address", "body"};
                String whereAddress = "address = ?";
                String whereDate = "date BETWEEN " + start.getTime() +
                        " AND " + (start.getTime() + DAY_MILLISECONDS);

                String where = DatabaseUtils.concatenateWhere(whereAddress, whereDate);

                Cursor cur;

                String icerik = "";

                messageGonder(msjgonderilecekNumber, tmp.getMotorDurum());// Mesajı Gonderdik


                while (statesmsreader) {

                    try {
                        cur = _activity.getContentResolver().query(uriSMSURI,
                                null,//projection
                                where,
                                new String[]{msjgonderilecekNumber},
                                null);

                        while (cur.moveToNext()) {
                            icerik = cur.getString(13);
                            break;
                        }
                        if (icerik.length() > 2) {
                            if (icerik.contains("RELAY:1")) {
                                /*cur.getcount=1 dye sor dha saglıklı*/
                                statesmsreader = false;
                                state = 1;
                            } else {
                                statesmsreader = false;
                                state = 2;
                            }
                        }

                    } catch (Exception e) {
                    }
                    if ((new Date().getTime() - start.getTime()) > (1000 * 30)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);
                }


            } catch (Exception e) {
//                Log.i("LOGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG", e.toString());
            }

            return state;
        }
    }

    private class asynmotorKilitAc extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;

        public asynmotorKilitAc(String msjGonderilecekNumber, String bnmNumber) {
            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Motor Kilidi Açılıyor");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer aBoolean) {
            super.onPostExecute(aBoolean);

            pDialog.dismiss();
            if (aBoolean == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (aBoolean == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İşlem Başarılı Bir Şekilde Sonuçlanmıştır")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (aBoolean == 2) {
//zaten
                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("Motor Kilidi Zaten Açık")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }


        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer state = 0;
            try {
                Date start = new Date();
                Boolean statesmsreader = true;
                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                Uri uriSMSURI = Uri.parse("content://sms/inbox");
                int DAY_MILLISECONDS = 24 * 60 * 60 * 1000;
                Calendar cal = Calendar.getInstance();
                //        String[] projection = {"address", "body"};
                String whereAddress = "address = ?";
                String whereDate = "date BETWEEN " + start.getTime() +
                        " AND " + (start.getTime() + DAY_MILLISECONDS);

                String where = DatabaseUtils.concatenateWhere(whereAddress, whereDate);

                Cursor cur;

                messageGonder(msjgonderilecekNumber, tmp.getConcoxMotorBlokeKaldirma());// Mesajı Gonderdik
                pDialog.setMessage("Mesaj Gonderildi.\nCevap Bekleniyor...");

                String icerik = "";

                while (statesmsreader) {

                    try {
                        cur = _activity.getContentResolver().query(uriSMSURI,
                                null,//projection
                                where,
                                new String[]{msjgonderilecekNumber},
                                null);

                        while (cur.moveToNext()) {
                            icerik = cur.getString(13);
                            break;
                        }
                        if (icerik.length() > 2) {
                            if (icerik.contains("Success!")) {
                                /*cur.getcount=1 dye sor dha saglıklı*/
                                statesmsreader = false;
                                state = 1;
                            } else {
                                statesmsreader = false;
                                state = 2;
                            }
                        }

                    } catch (Exception e) {
                    }
                    if ((new Date().getTime() - start.getTime()) > (1000 * 25)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);
                }

            } catch (Exception e) {
            }


            return state;
        }
    }

    private class asynMotorKilitle extends AsyncTask<String, Integer, Integer> {

        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;

        public asynMotorKilitle(String msjGonderilecekNumber, String bnmNumber) {

            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Motor Kilitleniyor");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer aBoolean) {
            super.onPostExecute(aBoolean);

            pDialog.dismiss();
            if (aBoolean == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (aBoolean == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İşlem Başarılı Bir Şekilde Sonuçlanmıştır")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (aBoolean == 2) {
//zaten
                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("Motor Kilidi Zaten Kapalı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer state = 0;
            try {
                Date start = new Date();
                Boolean statesmsreader = true;
                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                Uri uriSMSURI = Uri.parse("content://sms/inbox");
                int DAY_MILLISECONDS = 24 * 60 * 60 * 1000;
                Calendar cal = Calendar.getInstance();
//                String[] projection = {"address", "body"};
                String whereAddress = "address = ?";
                String whereDate = "date BETWEEN " + start.getTime() +
                        " AND " + (start.getTime() + DAY_MILLISECONDS);

                String where = DatabaseUtils.concatenateWhere(whereAddress, whereDate);

                Cursor cur;

                messageGonder(msjgonderilecekNumber, tmp.getConcoxMotorBloke());// Mesajı Gonderdik
                pDialog.setMessage("Mesaj Gonderildi.\nCevap Bekleniyor...");
                String icerik = "";

                while (statesmsreader) {

                    try {
                        cur = _activity.getContentResolver().query(uriSMSURI,
                                null,
                                where,
                                new String[]{msjgonderilecekNumber},
                                null);

                        while (cur.moveToNext()) {
                            icerik = cur.getString(13);
                            break;
                        }
                        if (icerik.length() > 2) {
                            if (icerik.trim().contains("Success!")) {
                                statesmsreader = false;
                                state = 1;
                            } else {
                                statesmsreader = false;
                                state = 2;
                            }
                        }

                    } catch (Exception e) {
                    }
                    if ((new Date().getTime() - start.getTime()) > (1000 * 25)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);
                }


            } catch (Exception e) {
            }


            return state;
        }
    }

    private class sendmessage extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private String msjgonderilecekNumber;
        private String bnmNumaram;

        public sendmessage(String msjGonderilecekNumber, String bnmNumber) {

            this.msjgonderilecekNumber = msjGonderilecekNumber;
            this.bnmNumaram = bnmNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("Cihaz Tanımlanıyor Lütfen Bekleyiniz");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer aBoolean) {
            super.onPostExecute(aBoolean);

            pDialog.dismiss();
            if (aBoolean == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (aBoolean == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İşlem Başarılı Bir Şekilde Sonuçlanmıştır")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (aBoolean == 2) {
//zaten
                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("Cihaz Tanımlanamadı.")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer state = 0;
            try {
                Date start = new Date();
                Boolean statesmsreader = true;
                SmsStruct tmp = new SmsStruct(bnmNumaram, "Concox");

                Uri uriSMSURI = Uri.parse("content://sms/inbox");
                int DAY_MILLISECONDS = 24 * 60 * 60 * 1000;
                Calendar cal = Calendar.getInstance();
                // String[] projection = {"address", "body"};
                String whereAddress = "address = ?";
                String whereDate = "date BETWEEN " + start.getTime() +
                        " AND " + (start.getTime() + DAY_MILLISECONDS);

                String where = DatabaseUtils.concatenateWhere(whereAddress, whereDate);

                Cursor cur;

                messageGonder(msjgonderilecekNumber, tmp.getSosNumaraTanim());// Mesajı Gonderdik
                pDialog.setMessage("Mesaj Gonderildi.\nCevap Bekleniyor...");
                String icerik = "";
                while (statesmsreader) {

                    try {
                        cur = _activity.getContentResolver().query(uriSMSURI,
                                null,
                                where,
                                new String[]{msjgonderilecekNumber},
                                null);

                        while (cur.moveToNext()) {
                            icerik = cur.getString(13);
                            break;
                        }

                        if (icerik.length() > 2) {
                            if (icerik.trim().contains("OK!")) {
                                statesmsreader = false;
                                state = 1;
                            } else {
                                state = 2;
                                statesmsreader = false;
                            }
                        }

                    } catch (Exception e) {
                    }
                    if ((new Date().getTime() - start.getTime()) > (1000 * 20)) {
                        state = 0;
                        statesmsreader = false;
                    }
                    Thread.sleep(2000);
                }

                messageGonder(msjgonderilecekNumber, tmp.getCenterNumaraTanim());// Mesajı Gonderdik

                if (state == 1) {
                    statesmsreader = true;
                } else if (state == 0) {
                    statesmsreader = false;
                } else {
                    statesmsreader = false;
                }


                where = DatabaseUtils.concatenateWhere(whereAddress, whereDate);

                while (statesmsreader) {
                    try {
                        cur = _activity.getContentResolver().query(uriSMSURI,
                                null,
                                where,
                                new String[]{msjgonderilecekNumber},
                                null);

                        while (cur.moveToNext()) {
                            icerik = cur.getString(13);
                            break;
                        }

                        if (cur.getCount() > 1) {
                            if (icerik.trim().contains("Center Number:")) {
                                statesmsreader = false;
                                state = 1;
                            } else {
                                state = 2;
                                statesmsreader = false;
                            }
                        }

                    } catch (Exception e) {
                    }
                    if (statesmsreader == true) {
                        if ((new Date().getTime() - start.getTime()) > (1000 * 40)) {
                            state = 0;
                            statesmsreader = false;
                        }
                    }
                    Thread.sleep(2000);
                }

                messageGonder(msjgonderilecekNumber, tmp.getSosNumaraIptal());// Mesajı Gonderdik

                if (state == 1) {
                    statesmsreader = true;
                } else if (state == 0) {
                    statesmsreader = false;
                } else {
                    statesmsreader = false;
                }

                while (statesmsreader) {
                    try {
                        cur = _activity.getContentResolver().query(uriSMSURI,
                                null,
                                where,
                                new String[]{msjgonderilecekNumber},
                                null);

                        while (cur.moveToNext()) {
                            icerik = cur.getString(13);
                            break;
                        }

                        if (cur.getCount() > 2) {
                            if (icerik.trim().contains("SOS1: SOS2: SOS3:")) {
                                statesmsreader = false;
                                state = 1;
                            } else {
                                state = 2;
                                statesmsreader = false;
                            }
                        }

                    } catch (Exception e) {
                    }
                    if (statesmsreader == true) {
                        if ((new Date().getTime() - start.getTime()) > (1000 * 60)) {
                            state = 0;
                            statesmsreader = false;
                        }
                    }
                    Thread.sleep(2000);
                }

            } catch (Exception e) {
            }


            return state;

        }
    }

    private boolean messageGonder(String number, String messageIcerigi) {
        try {
            SmsManager sms = SmsManager.getDefault();
            if (number.contains("+90")) {
                sms.sendTextMessage("" + number, null,
                        messageIcerigi, null, null);
            } else {
                sms.sendTextMessage("+90" + number, null,
                        messageIcerigi, null, null);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }


    private class asyncSetMotorBlokajServer extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog pDialog;
        private int VehicleID;
        private boolean State;
        Integer state = 0;

        public asyncSetMotorBlokajServer(int vehicleID, boolean state) {
            this.VehicleID = vehicleID;
            this.State = state;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(_activity);
            pDialog.setTitle("Lütfen Bekleyiniz");
            pDialog.setMessage("İşlem yapılıyor...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer ınteger) {
            super.onPostExecute(ınteger);

            pDialog.dismiss();
            if (ınteger == 0) {

                new SweetAlertDialog(_activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Hata")
                        .setContentText("İşlem Zaman Aşımına Uğradı")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            } else if (ınteger == 1) {
                new SweetAlertDialog(_activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Başarılı")
                        .setContentText("İsteğiniz başarılı bir şekilde gönderildi")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (ınteger == 2) {

                new SweetAlertDialog(_activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Dikkat")
                        .setContentText("İstek gönderme başarısız. Lütfen tekrar deneyiniz.")
                        .setConfirmText("TAMAM")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }

        }


        @Override
        protected Integer doInBackground(String... params) {

            try {

                String result = server.SendStopRunCommand(this.VehicleID, (this.State == true ? "Aktif" : "Pasif"));

                if (result.equalsIgnoreCase("true")) {
                    state = 1;
                } else if (result.equalsIgnoreCase("false")) {
                    state = 2;
                } else {
                    state = 0;

                }


            } catch (Exception e) {
                state = 0;
            }

            return state;
        }
    }

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 20177;

    private boolean checkAndRequestPermissions() {
        int permissionINTERNET = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.INTERNET);
        int permissionACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionACCESS_VIBRATE = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.VIBRATE);
        int permissionCAMERA = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.CAMERA);
        int permissionACCESS_LOCATION_EXTRA_COMMANDS = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        int permissionACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionACCESS_WIFI_STATE = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_WIFI_STATE);
        int permissionCHANGE_WIFI_STATE = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.CHANGE_WIFI_STATE);
        int permissionREAD_PHONE_STATE = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.READ_PHONE_STATE);
        int permissionRECEIVE_BOOT_COMPLETED = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.RECEIVE_BOOT_COMPLETED);
        int permissionBLUETOOTH_ADMIN = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.BLUETOOTH_ADMIN);
        int permissionBLUETOOTH = ContextCompat.checkSelfPermission(_activity, android.Manifest.permission.BLUETOOTH);
        List<String> listPermissionsNeeded = new ArrayList<String>();
        if (permissionINTERNET != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
        }
        if (permissionACCESS_NETWORK_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (permissionACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            //      listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionACCESS_VIBRATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.VIBRATE);
        }
        if (permissionACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionACCESS_LOCATION_EXTRA_COMMANDS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        }
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionACCESS_WIFI_STATE != PackageManager.PERMISSION_GRANTED) {
            //     listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (permissionCHANGE_WIFI_STATE != PackageManager.PERMISSION_GRANTED) {
            //    listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE);
        }
        if (permissionREAD_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
            //     listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionRECEIVE_BOOT_COMPLETED != PackageManager.PERMISSION_GRANTED) {
            //    listPermissionsNeeded.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        }
        if (permissionBLUETOOTH_ADMIN != PackageManager.PERMISSION_GRANTED) {
            //     listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (permissionBLUETOOTH != PackageManager.PERMISSION_GRANTED) {
            //    listPermissionsNeeded.add(Manifest.permission.BLUETOOTH);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(_activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // return this._listDataChild.get( this._listDataHeader.get( groupPosition ).groupID ).size();
        return this._listDataVehicleGroupWithList.get(groupPosition).Vehicle.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        // return this._listDataHeader.get( groupPosition ).groupName;
        return this._listDataVehicleGroupWithList.get(groupPosition);

    }

    public boolean addGroup(VehicleGroup obje) {
        // return this._listDataHeader.get( groupPosition ).groupName;
        boolean state = true;
        try {
            for (VehicleGroup item : _listDataVehicleGroupWithListStatic) {
                if (item.GroupID == obje.GroupID) {
                    state = false;
                    break;
                }
            }
            if (state) {
                this._listDataVehicleGroupWithListStatic.add(obje);
            } else {
                state = false;
            }

        } catch (Exception e) {
            state = false;
        }
        return state;
    }

    @Override
    public int getGroupCount() {
        return this._listDataVehicleGroupWithList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        //  return this._listDataHeader.get( groupPosition ).groupID;//groupPosition;
        return this._listDataVehicleGroupWithList.get(groupPosition).GroupID;//groupPosition;

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = ((VehicleGroup) getGroup(groupPosition)).GroupName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header_item, null);
        }
        try {
            TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

        } catch (Exception e) {

        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    public void filterData(String query) {

        try {
            query = query.trim().toLowerCase();
            _listDataVehicleGroupWithList.clear();
//            _listDataChild.clear();
//            _listDataHeader.clear();

            if (query.isEmpty() || query.length() < 1) {
                _listDataVehicleGroupWithList.addAll(_listDataVehicleGroupWithListStatic);
//                _listDataChild.putAll( _listDataChildStatic );
//                _listDataHeader.addAll( _listDataHeaderStatic );

            } else {

                for (int i = 0; i < _listDataVehicleGroupWithListStatic.size(); i++) {
                    List<Vehicle> newlist = new ArrayList<>();
                    for (int h = 0; h < _listDataVehicleGroupWithListStatic.get(i).Vehicle.size(); h++) {

                        if (_listDataVehicleGroupWithListStatic.get(i).Vehicle.get(h).Plate.trim().toLowerCase().contains(query) ||
                                _listDataVehicleGroupWithListStatic.get(i).Vehicle.get(h).Tag.trim().toLowerCase().contains(query)) {
                            newlist.add(_listDataVehicleGroupWithListStatic.get(i).Vehicle.get(h));
                        }
                    }

                    if (newlist.size() > 0) {

                        VehicleGroup tmp = new VehicleGroup();
                        tmp.Vehicle = newlist;
                        tmp.GroupID = _listDataVehicleGroupWithListStatic.get(i).GroupID;
                        tmp.GroupName = _listDataVehicleGroupWithListStatic.get(i).GroupName;
                        _listDataVehicleGroupWithList.add(tmp);
                    }


                }
         /*       //  for (HashMap.Entry<Integer, List<DeviceMarker>> entry : _listDataChildStatic.entrySet()) {
                //  for(int i=0;i<_listDataChildStatic.size();i++){
                for (Integer keyitemold : _listDataChildStatic.keySet()) {
                    Integer keyitem = keyitemold;//entry.getKey();
                    List<DeviceMarker> newlist = new ArrayList<>();

                    //        entry.getValue()

                    for (DeviceMarker device : _listDataChildStatic.get( keyitem )) {
                        if (device.device.getPlate().trim().toLowerCase().contains( query ) ||
                                device.device.getTag().trim().toLowerCase().contains( query )) {
                            newlist.add( device );
                        }
                    }
                    if (newlist.size() > 0) {
                        _listDataChild.put( keyitem, newlist );
                        for (devicegroup group : _listDataHeaderStatic) {
                            if (group.groupID == keyitem) {
                                _listDataHeader.add( group );
                                break;
                            }
                        }

                    }

                }
*/
            }
        } catch (Exception e) {
//            _listDataChild.clear();
//            _listDataHeader.clear();
//            _listDataChild.putAll( _listDataChildStatic );
//            _listDataHeader.addAll( _listDataHeaderStatic );
            _listDataVehicleGroupWithList.clear();
            _listDataVehicleGroupWithList.addAll(_listDataVehicleGroupWithListStatic);
            new Logger().newlog("1", "filterData", "GroupListAdapter", e.toString());
        }


        notifyDataSetChanged();

    }


    public void popupDialog(final int VehicleID) {

        try {

            final Dialog dialog = new Dialog(_activity);
            dialog.setContentView(R.layout.time_picker_popup);
            dialog.setTitle("Zaman aralığı seçiniz");
            //   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            Button btntarih = dialog.findViewById(R.id.btnTarih);
            Button btnzaman1 = dialog.findViewById(R.id.btn1_Zaman);
            Button btnzaman2 = dialog.findViewById(R.id.btn2_Zaman);
            Button btnSave = dialog.findViewById(R.id.SaveButton);

            final TextView txtTarih = dialog.findViewById(R.id.txtTarih);
            final TextView txt1Zaman1 = dialog.findViewById(R.id.txt1_Zaman);
            final TextView txtZaman2 = dialog.findViewById(R.id.txt2_Zaman);

            Date simdikiZaman = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            txtTarih.setText(df.format(simdikiZaman));


            btnzaman2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Calendar mcurrentTime = Calendar.getInstance();//
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                        int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
                        TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

                        //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
                        timePicker = new TimePickerDialog(_context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                txtZaman2.setText(selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                            }
                        }, hour, minute, true);//true 24 saatli sistem için
                        timePicker.setTitle("Saat Seçiniz");
                        timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                        timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);


                        timePicker.show();
                    } catch (Exception e) {
                    }

                }
            });

            btnzaman1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Calendar mcurrentTime = Calendar.getInstance();//
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                        int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
                        TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk


                        //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
                        timePicker = new TimePickerDialog(_context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                txt1Zaman1.setText(selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                            }
                        }, hour, minute, true);//true 24 saatli sistem için
                        timePicker.setTitle("Saat Seçiniz");
                        timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                        timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

                        timePicker.show();
                    } catch (Exception e) {

                    }

                }
            });

            btntarih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                        int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz
                        DatePickerDialog datePicker;//Datepicker objemiz
                        datePicker = new DatePickerDialog(_context, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                txtTarih.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                        datePicker.setTitle("Tarih Seçiniz");
                        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);
                        datePicker.show();
                    } catch (Exception e) {

                    }
                }
            });


            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(_activity.getApplicationContext(), VehicleHistory.class);
                        Bundle b = new Bundle();
                        b.putInt("VehicleID", VehicleID); //Your id
                        b.putString("StartDate", txtTarih.getText().toString() + " " + txt1Zaman1.getText().toString() + ":00");
                        b.putString("EndDate", txtTarih.getText().toString() + " " + txtZaman2.getText().toString() + ":59");
                        b.putString("Plate", ""); //Your id

                        intent.putExtras(b); //Put your id to your next Intent
                        _activity.startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });


//    Button CloseButon = (Button) dialog.findViewById( R.id.btn_dialog_close );
            dialog.show();
        } catch (Exception e) {

        }

    }


}