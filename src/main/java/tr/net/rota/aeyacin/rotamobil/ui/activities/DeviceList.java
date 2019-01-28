package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.ui.adapters.GroupListAdapter;

public class DeviceList extends AppCompatActivity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    GroupListAdapter listAdapter;
    ExpandableListView expListView;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_device_list );
       // getActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setTitle("Hello world App");  // provide compatibility to all the versions

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.grouplist);
        // preparing list data
        prepareListData();

        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.searchList);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);

    }

    public void prepareListData(){
     //   listAdapter = new GroupListAdapter(this, Globals.groups, Globals.listGroupDevices);
        listAdapter = new GroupListAdapter(this, Globals.groups );
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.filterData(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.filterData(newText);

        return false;
    }

    @Override
    public boolean onClose() {
        listAdapter.filterData("");

        return false;
    }


    ///////////////////   Menu ///////////////////////////


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask( this );
                return true;
            case R.id.action_vehicle_list_menu_refresh:
                prepareListData();
                return true;

            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        getMenuInflater().inflate( R.menu.vehicle_list, menu );
/*
        MenuItem nextItem=menu.findItem(R.id.menu_item_next);
        nextItem.setVisible(false);

*/


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu( menu );

        //   menu.getItem(1).setEnabled(false);

        return true;
    }

    public void ExitApp() {
        android.os.Process.killProcess( android.os.Process.myPid() );
        System.exit( 1 );
    }
}
