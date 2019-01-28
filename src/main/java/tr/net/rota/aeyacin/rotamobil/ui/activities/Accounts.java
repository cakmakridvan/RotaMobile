package tr.net.rota.aeyacin.rotamobil.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import tr.net.rota.aeyacin.rotamobil.Globals;
import tr.net.rota.aeyacin.rotamobil.R;
import tr.net.rota.aeyacin.rotamobil.ui.adapters.AccountListAdapter;

public class Accounts extends AppCompatActivity {

    public ListView listview;
    public AccountListAdapter adapter;
    public EditText Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        Search = (EditText) findViewById(R.id.Account_Search_Edit_Text);
        listview = (ListView) findViewById(R.id.Account_List_View);

        adapter = new AccountListAdapter(this, Globals.user.CompanyList);
        listview.setAdapter(adapter);
        Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "]");

                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


}
