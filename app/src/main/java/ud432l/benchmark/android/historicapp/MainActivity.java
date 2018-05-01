/******************************************************************************
*
* File: MainActivity.java
* Authors: Andy Frey, Hassan Alnasser, Almuaayad AlMaskari
* Date: 5/1/18
*
* This Activiy handles the landing screen when the users opens the app. From
* the user can go to the QR screen or a webview screen, via the appropriate
* Activities.
*
******************************************************************************/

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
        Button BtnQR = (Button) findViewById(R.id.mainBtnQR);

        //Spinner on Activity
        spinner = (Spinner) findViewById(R.id.catagorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.catagories_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

        // Assign qrListener to QR Scan Button
        BtnQR.setOnClickListener(qrListener);

        reportFullyDrawn();
    }


	// Defines the drop down menu options.
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
