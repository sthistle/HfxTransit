package com.salesforce.android.hfxtransit.csv;

/**
 * Created by sthistle on 4/12/17.
 */
public class Stop {

  private static final int MAX_INDEX = 6;
  private static final int ID_INDEX = 0;
  private static final int NAME_INDEX = 2;
  private static final int LAT_INDEX = 4;
  private static final int LON_INDEX = 5;

  private String stop_id;
  //@CsvBindByName() private String stop_code;
  private String stop_name;
  //@CsvBindByName() private String stop_desc;
  private String stop_lat;
  private String stop_lon;
  //@CsvBindByName() private String zone_id;
  //@CsvBindByName() private String stop_url;
  //@CsvBindByName() private String location_type;
  //@CsvBindByName() private String parent_station;
  //@CsvBindByName() private String stop_timezone;
  //@CsvBindByName() private String wheelchair_boarding;

  public Stop(String id, String name, String lat, String lon) {
    this.stop_id = id;
    this.stop_name = name;
    this.stop_lat = lat;
    this.stop_lon = lon;
  }

  public String getStop_id() {
    return stop_id;
  }

  //public String getStop_code() {
  //  return stop_code;
  //}

  public String getStop_name() {
    return stop_name;
  }

  //public String getStop_desc() {
  //  return stop_desc;
  //}

  public String getStop_lat() {
    return stop_lat;
  }

  public String getStop_lon() {
    return stop_lon;
  }

  //public String getZone_id() {
  //  return zone_id;
  //}
  //
  //public String getStop_url() {
  //  return stop_url;
  //}
  //
  //public String getLocation_type() {
  //  return location_type;
  //}
  //
  //public String getParent_station() {
  //  return parent_station;
  //}
  //
  //public String getStop_timezone() {
  //  return stop_timezone;
  //}
  //
  //public String getWheelchair_boarding() {
  //  return wheelchair_boarding;
  //}

  public static Stop fromLine(String line) {
    String[] tokens = line.split(",");
    if (line.length() > MAX_INDEX) {
      Stop s = new Stop(tokens[ID_INDEX], tokens[NAME_INDEX], tokens[LAT_INDEX], tokens[LON_INDEX]);
      return s;
    }
    return null;
  }
}
