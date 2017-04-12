package com.salesforce.android.hfxtransit.http;

import com.google.transit.realtime.Alert;
import com.google.transit.realtime.TripUpdate;
import com.google.transit.realtime.VehiclePosition;
import com.salesforce.android.service.common.utilities.control.Async;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by sthistle on 4/11/17.
 */

public class HfxTransitAdapter {

  private final HttpUrl mBaseUrl;
  private final OkHttpClient mClient;

  public HfxTransitAdapter() {
    mBaseUrl = HttpUrl.parse("http://GTFS.halifax.ca/");
    mClient = new OkHttpClient.Builder().build();
  }

  public Async<List<VehiclePosition>> getBusPostions() {
    return VehiclePositionRequest.send(mBaseUrl, mClient);
  }

  public Async<List<Alert>> getAlerts() {
    return AlertRequest.send(mBaseUrl, mClient);
  }

  public Async<List<TripUpdate>> getTripUpdates() {
    return TripUpdateRequest.send(mBaseUrl, mClient);
  }
}
