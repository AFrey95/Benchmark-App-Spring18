package ud432l.benchmark.android.historicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide title on action bar
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

        // Buttons on activity
        Button BtnFH = (Button) findViewById(R.id.mainBtnFH);
        Button BtnQR = (Button) findViewById(R.id.mainBtnQR);
        Button BtnEOF = (Button) findViewById(R.id.mainBtnEOF);
        // Add webListener
        View.OnClickListener webListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent for Screen

                Intent i = new Intent(MainActivity.this, FullscreenActivity.class);
                String SiteURL = getResources().getString(R.string.FlyingHistoryLink);
                i.putExtra("SiteURL", SiteURL);
3333
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
        };

        // Add qrListener
        View.OnClickListener qrListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent for Screen

                // Implement QR Scanner
                String SiteURL = getResources().getString(R.string.EOFLink);
                Intent i = new Intent(MainActivity.this, QRActivity.class);
                i.putExtra("SCAN_MODE", "QR_CODE_MODE");
                i.putExtra("SiteURL", SiteURL);
                startActivity(i);
            }
        };

        // Assign webListener to Flying History Button
        BtnFH.setOnClickListener(webListener);
        BtnEOF.setOnClickListener(webEOFListener);

        // Assign qrListener to QR Scan Button
        BtnQR.setOnClickListener(qrListener);

        reportFullyDrawn();
    }
}
