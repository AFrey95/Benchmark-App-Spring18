package ud432l.benchmark.android.historicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide title on action bar
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

        // Buttons on activity
        /*Button BtnFH = (Button) findViewById(R.id.mainBtnFH);*/
        Button BtnQR = (Button) findViewById(R.id.mainBtnQR);
        /*Button BtnEOF = (Button) findViewById(R.id.mainBtnEOF);*/

        //Spinner on Activity
        spinner = (Spinner) findViewById(R.id.catagorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.catagories_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

       /* // Add webListener
        View.OnClickListener webListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent for Screen

                Intent i = new Intent(MainActivity.this, FullscreenActivity.class);
                String SiteURL = getResources().getString(R.string.FlyingHistoryLink);
                i.putExtra("SiteURL", SiteURL);

                // start a new activity with Intent i
                startActivity(i);
            }
        };

        // Add webListener
        View.OnClickListener webEOFListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent for Screen

                Intent i = new Intent(MainActivity.this, FullscreenActivity.class);
                String SiteURL = getResources().getString(R.string.EOFLink);
                i.putExtra("SiteURL", SiteURL);

                // start a new activity with Intent i
                startActivity(i);
            }
        };*/

        // Add qrListener
        View.OnClickListener qrListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent for Screen

                // Implement QR Scanner
                String SiteURL = getResources().getString(R.string.EOFLink);
                Intent i = new Intent(MainActivity.this, QRActivity.class);
                i.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivity(i);
            }
        };

        // Assign webListener to Flying History Button
        /*BtnFH.setOnClickListener(webListener);
        BtnEOF.setOnClickListener(webEOFListener);*/

        // Assign qrListener to QR Scan Button
        BtnQR.setOnClickListener(qrListener);

        reportFullyDrawn();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedCatagory = null;
        if(spinner.getSelectedItem().equals("Music")){
            selectedCatagory = "music5";
        }

        else if(spinner.getSelectedItem().equals("Faith Tradition")){
            selectedCatagory = "faith1";
        }

        else if(spinner.getSelectedItem().equals("World")){
            selectedCatagory = "world2";
        }

        else if(spinner.getSelectedItem().equals("History of Flight")){
            selectedCatagory = "flyinghistory";
        }
        if(selectedCatagory!= null)
            startMainSiteActivity(selectedCatagory);
    }

    protected void startMainSiteActivity(String selectedCatagory){
        String SiteURL = getResources().getString(R.string.googleSiteLink);
        Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
        intent.putExtra("url", SiteURL+selectedCatagory);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
