package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.model.sbt.ReportData;
import tr.net.rota.aeyacin.rotamobil.ui.adapters.ReportListAdapter;


public class ReportActivity extends AppCompatActivity {

    private int VehicleID = -1;
    private RecyclerView mRecyclerView;
    private ReportListAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.report_toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        Bundle b = getIntent().getExtras();
        if (b != null) {
            VehicleID = b.getInt("report_VehicleID");
            String values = b.getString("report_datas");
            if (values == null) {
                GotoFinish("");
                return;
            }
            Type collectionType = new TypeToken<List<ReportData>>() {
            }.getType();
            List<ReportData> list = new Gson().fromJson(values, collectionType);
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

    private void initViews(List<ReportData> delivery, int vehicleID) {
        mRecyclerView = (RecyclerView) findViewById(R.id.report_recycle_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        rAdapter = new ReportListAdapter(ReportActivity.this, delivery, vehicleID);
        mRecyclerView.setAdapter(rAdapter);

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
}
