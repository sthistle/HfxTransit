package com.salesforce.android.hfxtransit;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.transit.realtime.FeedMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    try {
      AssetManager mngr = getAssets();

      InputStream posStream = mngr.open("initVehiclePositions.pb");
      FeedMessage posFeedInit = FeedMessage.ADAPTER.decode(posStream);
      posStream.close();

      InputStream tripUpdateStream = mngr.open("initTripUpdates.pb");
      FeedMessage tripUpdatesInit = FeedMessage.ADAPTER.decode(tripUpdateStream);
      tripUpdateStream.close();

      InputStream alertsStream = mngr.open("initAlerts.pb");
      FeedMessage alertsInit = FeedMessage.ADAPTER.decode(alertsStream);
      alertsStream.close();

      posStream = mngr.open("VehiclePositions.pb");
      FeedMessage posFeed = FeedMessage.ADAPTER.decode(posStream);
      posStream.close();

      tripUpdateStream = mngr.open("TripUpdates.pb");
      FeedMessage tripUpdates = FeedMessage.ADAPTER.decode(tripUpdateStream);
      tripUpdateStream.close();

      alertsStream = mngr.open("Alerts.pb");
      FeedMessage alerts = FeedMessage.ADAPTER.decode(alertsStream);
      alertsStream.close();

      Log.e(TAG, "Read data");
      //BufferedReader reader = new BufferedReader(new FileReader("assets/VehiclePositions.pb"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
