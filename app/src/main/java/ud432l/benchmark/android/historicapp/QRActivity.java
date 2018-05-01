/***********************************************************************
*
* File: QRActivity.java
* Authors: Andy Frey, Hassan Alnasser, Almuaayad AlMaskari
* Date: 5/1/18
*
* This Activity handles the QR scanner screen. QRCs are interpreted
* using the JourneyApps and ZXing libraries. When a QRC is scanned
* the Activiy starts SQLIntentService to retrieve the corresponding
* URL from the translation database. It then starts FullscreenActivity
* with the URL received.
*
***********************************************************************/


package ud432l.benchmark.android.historicapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class QRActivity extends Activity implements DecoratedBarcodeView.TorchListener {
    private static final String TAG = QRActivity.class.getSimpleName();
    private static final int URL_REQUEST_CODE = 0;
    private static boolean loading = false;
    private static int count = 0;

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private Button switchFlashlightButton;
//    private String qrcText;

    /* Create callback function for barcode scanning */
    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

        	// Get URL from QR Code
            String qrcText = result.getText();

            PendingIntent pendingResult = createPendingResult(
                    URL_REQUEST_CODE, new Intent(), 0);
            Intent intent = new Intent(QRActivity.this, SQLIntentService.class);
            intent.putExtra(SQLIntentService.QRC_EXTRA, qrcText);
            intent.putExtra(SQLIntentService.PENDING_RESULT_EXTRA, pendingResult);
            loading = true;=
            startService(intent);
		}

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case SQLIntentService.INVALID_URL_CODE:
                //handle invalid url
                break;
            case SQLIntentService.ERROR_CODE:
                //handle error
                break;
            case SQLIntentService.RESULT_CODE:
                openSite(data);
                break;
            default:
//                Log.e(TAG, "No result code match.");
                break;
        }
        return;
    }

	// Start the web view Activity
    private void openSite(Intent data) {
        String url = data.getStringExtra(SQLIntentService.URL_RESULT_EXTRA);
        Intent i = new Intent(QRActivity.this, FullscreenActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    /* When activity is initially created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        /* Set up QR Scanner to be a continuous read */
        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);
        barcodeScannerView.decodeContinuous(callback);

        /* Set up Flashlight Button */
        switchFlashlightButton = (Button)findViewById(R.id.switch_flashlight);

        /* If devices doesn't have flashlight in camera, remove flashlight button */
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        /* Used to capture images to scan QR code */
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }
    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setText(R.string.turn_on_flashlight);
    }

}
