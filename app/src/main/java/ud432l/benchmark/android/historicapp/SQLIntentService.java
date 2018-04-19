package ud432l.benchmark.android.historicapp;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by andyf on 4/4/2018.
 */

public class SQLIntentService extends IntentService {

    private static final String TAG = SQLIntentService.class.getSimpleName();
    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String QRC_EXTRA = "qrc";
    public static final String URL_RESULT_EXTRA = "url";

    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;

    public static final String serverIP = "http://flyinghistory.com";
    public static final String pathToPHP = "/geturl.php";

    public SQLIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        try {
            try {
                //setup connection to php script
                URL url = new URL(serverIP + pathToPHP);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); //may need to convert to GET
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //write request param
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(URLEncoder.encode("code", "UTF-8")
                            + "="
                            + URLEncoder.encode(intent.getStringExtra(QRC_EXTRA), "UTF-8")
                );
                writer.flush();
                writer.close();
                os.close();

                String response = "";
                String urlstr = "";

                //parse json response
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    response = br.readLine();
                    JSONArray jArray = new JSONArray(response);
                    JSONObject jObject = jArray.getJSONObject(0);
                    urlstr = jObject.getString("url");
//                    Log.d(TAG, "URL Recieved: '" + urlstr + "'");
                }

                Intent result = new Intent();
                result.putExtra(URL_RESULT_EXTRA, urlstr);
//                Log.d(TAG, "Sending back result");
                reply.send(this, RESULT_CODE, result);

            } catch (MalformedURLException e) {
//                Log.e(TAG, "Error getting url", e);
                reply.send(INVALID_URL_CODE);
            } catch (Exception e) {
//                Log.e(TAG, "Error getting url", e);
                reply.send(ERROR_CODE);
            }

        } catch (PendingIntent.CanceledException c) {
//            Log.i(TAG, "reply cancelled", c);
        }
    }

}
