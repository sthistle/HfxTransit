package com.salesforce.android.hfxtransit.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by sthistle on 4/12/17.
 */

public class ConstantTransitModel {
  private HashMap<String, Trip> mTripsById;
  private HashMap<String, Stop> mStopsById;
  private static ConstantTransitModel INSTANCE = null;

  public static ConstantTransitModel getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ConstantTransitModel();
    }
    return INSTANCE;
  }

  private ConstantTransitModel() {
    String file = "res/raw/test.txt";
    InputStream stops = this.getClass().getClassLoader().getResourceAsStream("assets/stops.txt");
    InputStream trips = this.getClass().getClassLoader().getResourceAsStream("assets/trips.txt");
    mTripsById = new HashMap<>(13000);
    mStopsById = new HashMap<>(2500);

    readStops(stops);
    readTrips(trips);
  }

  private void readTrips(InputStream input) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      reader.readLine();
      String line = reader.readLine();
      while (line != null) {
        Trip t = Trip.fromLine(line);
        mTripsById.put(t.getTrip_id(), t);
        line = reader.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readStops(InputStream input) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      reader.readLine();
      String line = reader.readLine();
      while (line != null) {
        Stop s = Stop.fromLine(line);
        mStopsById.put(s.getStop_id(), s);
        line = reader.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Collection<Stop> getStops() {
    return mStopsById.values();
  }

  public Collection<Trip> getTrips() {
    return mTripsById.values();
  }

  public Trip getTrip(String id) {
    return mTripsById.get(id);
  }

  public Stop getStop(String id) {
    return mStopsById.get(id);
  }
}
