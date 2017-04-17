package com.salesforce.android.hfxtransit.csv;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by sthistle on 4/12/17.
 */

public class Trip {

  private static final int MIN_SIZE = 4;
  private static final int ROUTE_ID_INDEX = 0;
  private static final int TRIP_ID_INDEX = 2;
  private static final int DISPLAY_NAME_INDEX = 3;

  @CsvBindByName() private String route_id;
  // @CsvBindByName() private String service_id;
  @CsvBindByName() private String trip_id;
  @CsvBindByName() private String trip_headsign;

  public Trip(String route, String trip, String sign) {
    route_id = route;
    trip_id = trip;
    trip_headsign = sign;
  }
  //@CsvBindByName() private String trip_short_name;
  //@CsvBindByName() private String direction_id;
  //@CsvBindByName() private String block_id;
  //@CsvBindByName() private String shape_id;
  //@CsvBindByName() private String wheelchair_accessible;
  //@CsvBindByName() private String bikes_allowed;

  public String getRoute_id() {
    return route_id;
  }

  //public String getService_id() {
  //  return service_id;
  //}

  public String getTrip_id() {
    return trip_id;
  }

  public String getTrip_headsign() {
    return trip_headsign;
  }

  //public String getTrip_short_name() {
  //  return trip_short_name;
  //}
  //
  //public String getDirection_id() {
  //  return direction_id;
  //}
  //
  //public String getBlock_id() {
  //  return block_id;
  //}

  //public String getShape_id() {
  //  return shape_id;
  //}
  //
  //public String getWheelchair_accessible() {
  //  return wheelchair_accessible;
  //}
  //
  //public String getBikes_allowed() {
  //  return bikes_allowed;
  //}
  public static Trip fromLine(String line) {
    String[] tokens = line.split(",");
    if (line.length() > MIN_SIZE) {
      Trip s = new Trip(tokens[ROUTE_ID_INDEX], tokens[TRIP_ID_INDEX], tokens[DISPLAY_NAME_INDEX]);
      return s;
    }
    return null;
  }
}
