/************************************************************************
*
* File: FullscreenActivity.java
* Authors: Andy Frey, HassanAlnasser, Almuaayad Almaskari
* Date: 5/1/18
*
* This Activity handles the Web View for viewing webpages. It loads a
* fully qualified URL given in the Intent. If the URL does not begin
* with "http://" or "https://" the Activity will prepend "http://"
* before loading.
*
************************************************************************/

package ud432l.benchmark.android.historicapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class FullscreenActivity extends Activity {
    /* Global Variables */
    private static final String TAG = FullscreenActivity.class.getSimpleName();
    private WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get url from previous Activity */
        String url = null;
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if(url == null) {

        } else {

            /* Load the URL from url */
            setContentView(R.layout.activity_fullscreen);
            view =(WebView) this.findViewById(R.id.webView);
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = "http://" + url;
            }

			// Open the webview with the loaded URL.
            view.loadUrl(url);
        }

        /* Clear the cache and set settings */
        view.clearCache(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setUseWideViewPort(true);

        view.getSettings().setSupportZoom(true);
        view.getSettings().setBuiltInZoomControls(true);
        view.getSettings().setDisplayZoomControls(true);
        String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        view.getSettings().setUserAgentString(newUA);
        view.setWebViewClient(new WebViewClient());

        // Set up QR Scanner button
        Button btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.bringToFront();

        // Add event handler for QR Button
        View.OnClickListener qrListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Call QRActivity from here */
                Intent i = new Intent(FullscreenActivity.this, QRActivity.class);

                /* Set QR Code mode & pass along current SiteURL in this activity */
                i.putExtra("SCAN_MODE", "QR_CODE_MODE");

                /* Start the QR Activity */
                startActivity(i);
            }
        };

        /* Assign qrListener to button */
        btnQR.setOnClickListener(qrListener);

    }

    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    /* If back key is pressed */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    /* If we can go back to previous webpage */
                    if (view.canGoBack()) {
                        view.goBack();
                    }
                    /* Otherwise, exit the activity & return to previous activity */
                    else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
