package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.model.sbt.MonthlyRoadReportObj;
import tr.net.rota.aeyacin.rotamobil.ui.adapters.MonthlyListAdapter;

public class MonthlyRoadReportActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MonthlyListAdapter mAdapter;
    private int VehicleID = -1;

    private String Plate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_road_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        Bundle b = getIntent().getExtras();
        if (b != null) {
             VehicleID = b.getInt("VehicleID");
            String values = b.getString("datas");
            if (values == null) {
                GotoFinish("");
                return;
            }
            Type collectionType = new TypeToken<List<MonthlyRoadReportObj>>() {
            }.getType();
            List<MonthlyRoadReportObj> list = new Gson().fromJson(values, collectionType);
            if (list == null || list.size() < 1) {
                GotoFinish("Veri Bulunamadı.");
                return;
            }
            initViews(list, VehicleID);

        } else {
            GotoFinish("");

        }


    }

    private void GotoFinish(String msg) {
        new AlertDialog.Builder(this).setCancelable(false).setMessage("Hata")
                .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }


    private void initViews(List<MonthlyRoadReportObj> delivery, int vehicleID) {
        mRecyclerView = (RecyclerView) findViewById(R.id.monthly_recycle_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MonthlyListAdapter(MonthlyRoadReportActivity.this, delivery, vehicleID,Plate);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void openPoupDate(View v) {
        if (VehicleID < 1) {
            Toast.makeText(getApplicationContext(),"Hatalı Araç ID",Toast.LENGTH_LONG).show();

        } else {
            popupDialog(VehicleID);

        }

    }

    public void popupDialog(final int VehicleID) {

        try {

            final Dialog dialog = new Dialog(MonthlyRoadReportActivity.this);
            dialog.setContentView(R.layout.time_picker_popup);
            dialog.setTitle("Zaman aralığı seçiniz");
            //   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            Button btntarih = (Button) dialog.findViewById(R.id.btnTarih);
            Button btnzaman1 = (Button) dialog.findViewById(R.id.btn1_Zaman);
            Button btnzaman2 = (Button) dialog.findViewById(R.id.btn2_Zaman);
            Button btnSave = (Button) dialog.findViewById(R.id.SaveButton);

            final TextView txtTarih = (TextView) dialog.findViewById(R.id.txtTarih);
            final TextView txt1Zaman1 = (TextView) dialog.findViewById(R.id.txt1_Zaman);
            final TextView txtZaman2 = (TextView) dialog.findViewById(R.id.txt2_Zaman);

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
                        timePicker = new TimePickerDialog(MonthlyRoadReportActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

                        timePicker = new TimePickerDialog(MonthlyRoadReportActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        datePicker = new DatePickerDialog(MonthlyRoadReportActivity.this, new DatePickerDialog.OnDateSetListener() {

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
                        Intent intent = new Intent(MonthlyRoadReportActivity.this, VehicleHistory.class);
                        Bundle b = new Bundle();
                        b.putInt("VehicleID", VehicleID); //Your id
                        b.putString("StartDate", txtTarih.getText().toString() + " " + txt1Zaman1.getText().toString() + ":00");
                        b.putString("EndDate", txtTarih.getText().toString() + " " + txtZaman2.getText().toString() + ":50");
                        b.putString("Plate",Plate);
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });


//    Button CloseButon = (Button) dialog.findViewById( R.id.btn_dialog_close );
            dialog.show();
        } catch (Exception e) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //    NavUtils.navigateUpFromSameTask( this );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    private BarChart mChart;
//
//    private  void setchart(){
//        mChart = (BarChart) findViewById(R.id.chart1);
//        mChart.getDescription().setEnabled(false);
//
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//        for (int i = (int) 0; i < 10 + 1; i++) {
//            float val = (float) (Math.random());
//            yVals1.add(new BarEntry(i, val));
//        }
//
//
//        BarDataSet set1 = new BarDataSet(yVals1, "The year 2017");
//        set1.setColors(ColorTemplate.MATERIAL_COLORS);
//
//        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.addAll(getDataSet());
//        //  dataSets.add(set1);
//
//        // BarData data = new BarData(dataSets);
//        BarData data = new BarData(dataSets);
//
//
//        //   BarData data = new BarData(getXAxisValues(), new ArrayList<BarEntry>());
//
//        mChart.setData(data);
//
//        // Description titile=new Description();
//        //   titile.setText("Aylık tablo");
//        //     mChart.setDescription(titile);
//        mChart.animateXY(2000, 2000);
//        mChart.invalidate();
//
//
////        mChart.setDrawBorders(true);
//
//        // scaling can now only be done on x- and y-axis separately
//        mChart.setPinchZoom(true);
//
//        //   mChart.setDrawBarShadow(false);
//
//        //   mChart.setDrawGridBackground(false);
//
//
//        /*
//
//        // create a custom MarkerView (extend MarkerView) and specify the layout
//        // to use for it
//     //   MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//     //   mv.setChartView(mChart); // For bounds control
//     //   mChart.setMarker(mv); // Set the marker to the chart
//
//      //  mSeekBarX.setProgress(10);
//      //  mSeekBarY.setProgress(100);
//
//        Legend l = mChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(true);
//    //    l.setTypeface(mTfLight);
//        l.setYOffset(0f);
//        l.setXOffset(10f);
//        l.setYEntrySpace(0f);
//        l.setTextSize(8f);
//
//        XAxis xAxis = mChart.getXAxis();
//       // xAxis.setTypeface(mTfLight);
//        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return String.valueOf((int) value);
//            }
//        });
//
//        YAxis leftAxis = mChart.getAxisLeft();
//    //    leftAxis.setTypeface(mTfLight);
//        leftAxis.setValueFormatter(new LargeValueFormatter());
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setSpaceTop(35f);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        mChart.getAxisRight().setEnabled(false);
//      //  setdata();
//    }
//
//    public void setdata() {
//
//        float groupSpace = 0.08f;
//        float barSpace = 0.03f; // x4 DataSet
//        float barWidth = 0.2f; // x4 DataSet
//        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"
//
//        int startYear = 1980;
//        int endYear = startYear + 30;
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
//        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
//        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
//
//        float randomMultiplier = 30 * 100000f;
//
//        for (int i = startYear; i < endYear; i++) {
//            yVals1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//            yVals2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//            yVals3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//            yVals4.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//        }
//
//        BarDataSet set1, set2, set3, set4;
//
//        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
//
//            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
//            set2 = (BarDataSet) mChart.getData().getDataSetByIndex(1);
//            set3 = (BarDataSet) mChart.getData().getDataSetByIndex(2);
//            set4 = (BarDataSet) mChart.getData().getDataSetByIndex(3);
//            set1.setValues(yVals1);
//            set2.setValues(yVals2);
//            set3.setValues(yVals3);
//            set4.setValues(yVals4);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//
//        } else {
//            // create 4 DataSets
//            set1 = new BarDataSet(yVals1, getString(R.string.chart_total_road));
//            set1.setColor(Color.rgb(104, 241, 175));
//            set2 = new BarDataSet(yVals2, getString(R.string.chart_max_speed));
//            set2.setColor(Color.rgb(164, 228, 251));
//            set3 = new BarDataSet(yVals3, "Company C");
//            set3.setColor(Color.rgb(242, 247, 158));
//            set4 = new BarDataSet(yVals4, "Company D");
//            set4.setColor(Color.rgb(255, 102, 0));
//
//            BarData data = new BarData(set1, set2, set3, set4);
//            data.setValueFormatter(new LargeValueFormatter());
//          //  data.setValueTypeface(mTfLight);
//
//            mChart.setData(data);
//        }
//
//        // specify the width each bar should have
//        mChart.getBarData().setBarWidth(barWidth);
//
//        // restrict the x-axis range
//        mChart.getXAxis().setAxisMinimum(startYear);
//
//        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
//        mChart.getXAxis().setAxisMaximum(startYear + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * 30);
//        mChart.groupBars(startYear, groupSpace, barSpace);
//        mChart.invalidate();
//        */
//    }
//
//    protected float getRandom(float range, float startsfrom) {
//        return (float) (Math.random() * range) + startsfrom;
//    }
//
//
//    public void SnakebarUndoExample() {
//        View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Geri alma i?lemi
//            }
//        };
//
//        Snackbar.make(findViewById(android.R.id.content), "Had a snack at Snackbar", Snackbar.LENGTH_LONG)
//                .setAction("Undo", mOnClickListener)
//                .setActionTextColor(Color.RED)
//                .show();
//    }
//
//    private ArrayList<BarDataSet> getDataSet() {
//        ArrayList<BarDataSet> dataSets = null;
//
//        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
//        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//        valueSet1.add(v1e1);
//        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
//        valueSet1.add(v1e2);
//        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
//        valueSet1.add(v1e3);
//        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
//        valueSet1.add(v1e4);
//        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
//        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
//        valueSet1.add(v1e6);
//
//        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
//        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
//        valueSet2.add(v2e1);
//        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
//        valueSet2.add(v2e2);
//        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
//        valueSet2.add(v2e3);
//        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
//        valueSet2.add(v2e4);
//        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
//        valueSet2.add(v2e5);
//        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
//        valueSet2.add(v2e6);
//
//        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
//        barDataSet1.setColor(Color.rgb(0, 155, 0));
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        dataSets = new ArrayList<>();
//        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
//        return dataSets;
//    }
//
//    private ArrayList<String> getXAxisValues() {
//        ArrayList<String> xAxis = new ArrayList<>();
//        xAxis.add("JAN");
//        xAxis.add("FEB");
//        xAxis.add("MAR");
//        xAxis.add("APR");
//        xAxis.add("MAY");
//        xAxis.add("JUN");
//        return xAxis;
//    }
//
//


}
