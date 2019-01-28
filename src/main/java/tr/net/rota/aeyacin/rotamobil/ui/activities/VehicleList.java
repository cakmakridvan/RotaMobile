package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.github.leonardoxh.fakesearchview.FakeSearchView;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import org.jetbrains.annotations.NotNull;

import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.ui.adapters.GroupListAdapter;

public class VehicleList extends AppCompatActivity implements FakeSearchView.OnSearchListener, MenuItemCompat.OnActionExpandListener {

    //      implements        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    GroupListAdapter listAdapter;
    ExpandableListView expListView;
    FakeSearchView fakeSearchView;
    Toolbar toolbar;
    // private SearchView search;


    //  private SearchView mSearchView;
    //   private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //  if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        //    setSupportActionBar( toolbar );
        //   getSupportActionBar().setDisplayShowHomeEnabled(true);
        //    getSupportActionBar().setHomeButtonEnabled(true);

        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.grouplist);
        // preparing list data
        prepareListData();
/*
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.searchList);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void prepareListData() {
        //   listAdapter = new GroupListAdapter(this, Globals.groups, Globals.listGroupDevices);
        listAdapter = new GroupListAdapter(this, Globals.groups);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

/*
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
*/

    ///////////////////   Menu ///////////////////////////
 /*   SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            listAdapter.filterData( query );

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // newText is text entered by user to SearchView
            listAdapter.filterData( newText );

            Toast.makeText( getApplicationContext(), newText, Toast.LENGTH_LONG ).show();
            return false;
        }
    };
    SearchView.OnCloseListener listener_close = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            listAdapter.filterData( "" );
            return false;
        }
    };
*/

    @Override
    public void onSearch(@NotNull FakeSearchView fakeSearchView, @NotNull CharSequence constraint) {
        //The constraint variable here change every time user input data
        //     ((Filterable)listView.getAdapter()).getFilter().filter(constraint);
    /* Any adapter that implements a Filterable interface, or just extends the built in FakeSearchAdapter

       and implements the searchitem on your model to a custom filter logic */
        listAdapter.filterData(constraint.toString());


    }

    @Override
    public void onSearchHint(@NotNull FakeSearchView fakeSearchView, @NotNull CharSequence constraint) {
        //This is received when the user click in the search button on the keyboard
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //    NavUtils.navigateUpFromSameTask( this );
                onBackPressed();
                return true;
            case R.id.action_vehicle_list_menu_refresh:
                prepareListData();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.vehicle_list, menu);

        MenuItem menuItem = menu.findItem(R.id.action_vehicle_search);
        fakeSearchView = (FakeSearchView) MenuItemCompat.getActionView(menuItem);
        MenuItemCompat.setOnActionExpandListener(menuItem, this);
        fakeSearchView.setOnSearchListener(this);



    /*      searchMenuItem = menu.findItem( R.id.action_vehicle_search );


        mSearchView = (SearchView) searchMenuItem.getActionView();
    mSearchView.setOnQueryTextListener( listener );
        mSearchView.setOnCloseListener( listener_close );


        MenuItem nextItem=menu.findItem(R.id.menu_item_next);
        nextItem.setVisible(false);



*/
/*
        MenuItem searchItem = menu.findItem(R.id.action_vehicle_search);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
*/
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        //   menu.getItem(1).setEnabled(false);

        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("VehicleList", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        if (item.getItemId() == R.id.action_vehicle_search) {
            try {
                fakeSearchView.setSearchText("");
            } catch (Exception e) {

            }
        }

        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        if (item.getItemId() == R.id.action_vehicle_search) {
            listAdapter.filterData("");
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
