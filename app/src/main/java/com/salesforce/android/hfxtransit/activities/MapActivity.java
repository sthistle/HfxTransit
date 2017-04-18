package com.salesforce.android.hfxtransit.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.transit.realtime.VehiclePosition;
import com.salesforce.android.hfxtransit.R;
import com.salesforce.android.hfxtransit.activities.model.BusModel;
import com.salesforce.android.hfxtransit.activities.model.TransitModel;
import com.salesforce.android.hfxtransit.utils.AbstractHandler;
import com.salesforce.android.service.common.utilities.control.Async;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

  private Spinner mRoute;
  private Spinner mUpdateTime;
  private BusPickerFragment mBusMap;
  private TransitModel mCompleteModel;
  private String routeSelected;

  Handler handler = new Handler();
  // Define the code block to be executed
  private Runnable positionUpdater = new Runnable() {
    @Override public void run() {
      TransitModel.updateModel().addHandler(new AbstractHandler<TransitModel>() {

        @Override public void handleResult(Async<?> async, @NonNull TransitModel transitModel) {
          mCompleteModel = transitModel;
          onPositionUpdate();
        }
      });
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.map_fragment, new BusPickerFragment())
          .commit();
    }

    mBusMap = (BusPickerFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

    mRoute = (Spinner) this.findViewById(R.id.map_route_spinner);
    mRoute.setOnItemSelectedListener(new RouteSelectedListener(this));

    mUpdateTime = (Spinner) this.findViewById(R.id.map_time_spinner);
    mUpdateTime.setOnItemSelectedListener(new UpdateTimeUpdateListener());

    //Update route model.
    TransitModel.updateModel().addHandler(new AbstractHandler<TransitModel>() {

      @Override public void handleResult(Async<?> async, @NonNull TransitModel transitModel) {
        mCompleteModel = transitModel;
        List<String> busRoutes = transitModel.getBusRoutes();
        Collections.sort(busRoutes);
        final ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>(MapActivity.this,
            android.R.layout.simple_spinner_dropdown_item, busRoutes);
        MapActivity.this.runOnUiThread(new Runnable() {
          @Override public void run() {
            mRoute.setAdapter(routeAdapter);
          }
        });
      }
    });
  }

  public void notifyRouteSelected(String route) {
    routeSelected = route;
    if (mCompleteModel == null) {
      return;
    }
    onPositionUpdate();
  }

  public void onPositionUpdate() {
    Log.e("MapActivity", "onPositionUpdate: ");
    handler.removeCallbacks(positionUpdater);
    handler.postDelayed(positionUpdater, 30000);
    Collection<String> busses = mCompleteModel.getBusesForRoute(routeSelected);
    ArrayList<BusModel> toDisplay = new ArrayList<BusModel>(busses.size());
    for (String bus : busses) {
      VehiclePosition p = mCompleteModel.getBusPostion(bus);
      if (p != null
          && p.position != null
          && p.position.latitude != null
          && p.position.longitude != null) {
        toDisplay.add(new BusModel(p.position.latitude, p.position.longitude, bus));
      } else {
        Log.e("MapActivity", "NO POSITION FOUND: ");
      }
    }
    mBusMap.updateMap(toDisplay, routeSelected);
  }

  private class RouteSelectedListener implements AdapterView.OnItemSelectedListener {
    private MapActivity mParent;

    public RouteSelectedListener(MapActivity mapActivity) {
      mParent = mapActivity;
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String routeNumber = (String) mRoute.getItemAtPosition(position);
      mParent.notifyRouteSelected(routeNumber);
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
      //INTENTIONALLY LEFT BLANK.
    }
  }

  private class UpdateTimeUpdateListener implements AdapterView.OnItemSelectedListener {
    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      //Schedule updates

    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
      //INTENTIONALLY LEFT BLANK
    }
  }
}
