package com.salesforce.android.hfxtransit.activities.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sthistle on 4/17/17.
 */

public class BusModel {
  private LatLng position;
  private String name;

  public BusModel(float lat, float lon, String name) {
    position = new LatLng(lat, lon);
    this.name = name;
  }

  public LatLng getPosition() {
    return position;
  }

  public String getName() {
    return name;
  }
}
