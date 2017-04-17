package com.salesforce.android.hfxtransit.activities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.transit.realtime.TripUpdate;
import com.salesforce.android.hfxtransit.R;
import com.salesforce.android.hfxtransit.activities.model.TransitModel;
import com.salesforce.android.hfxtransit.csv.ConstantTransitModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by sthistle on 4/12/17.
 */

public class RouteListRvAdapter extends RecyclerView.Adapter<RouteListRvAdapter.BusHolder> {
  private static final int VIEW_TYPE_BUS = 0;
  private static final int VIEW_TYPE_SHOW_ALL = 1;
  private TransitModel mModel;
  private String mRouteId;
  private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss MM-dd-YY");

  public RouteListRvAdapter(TransitModel model, String routeId) {
    ConstantTransitModel ctm = ConstantTransitModel.getInstance();
    mModel = model;
    mRouteId = routeId;
  }

  @Override public RouteListRvAdapter.BusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View inflatedView;
    switch (viewType) {
      case VIEW_TYPE_BUS:
        inflatedView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.recycler_view_show_bus, parent, false);
        return new BusHolder(inflatedView);
      //case VIEW_TYPE_SHOW_ALL:
      //  inflatedView = LayoutInflater.from(parent.getContext())
      //      .inflate(R.layout.recycler_view_show_bus, parent, false);
      //  return new ShowAllHolder(inflatedView);
    }
    return null;
  }

  @Override public void onBindViewHolder(RouteListRvAdapter.BusHolder holder, int position) {
    Collection<String> buses = mModel.getBusesForRoute(mRouteId);
    String busId = (String) buses.toArray()[position];

    TripUpdate trip = mModel.getTripUpdate(busId);
    String name = ConstantTransitModel.getInstance().getTrip(busId).getTrip_headsign();

    int nextStopIndex = findNextStopIndex(trip);
    if (nextStopIndex < 0) {
      holder.bindBus(name, "Unknown", "Unknown", busId);
      return;
    }
    String nextStop = ConstantTransitModel.getInstance()
        .getStop(trip.stop_time_update.get(nextStopIndex).stop_id)
        .getStop_name();

    String date = "Unknown";
    if (trip.stop_time_update != null) {
      date = sdf.format(new Date(1000 * trip.stop_time_update.get(nextStopIndex).departure.time));
      for (int i = 0; i < trip.stop_time_update.size(); i++) {
        if (trip.stop_time_update.get(i).departure != null
            && trip.stop_time_update.get(i).departure.time != null) {
          Log.d("Trip time", "onBindViewHolder: " + i + " " + sdf.format(
              new Date(1000 * trip.stop_time_update.get(i).departure.time)));
        } else {
          Log.d("Trip time", "Skipping date for " + i);
        }
      }
      Log.d("Current time:", "current time is" + sdf.format(new Date()));
    }
    holder.bindBus(name, nextStop, date, busId);
  }

  private int findNextStopIndex(TripUpdate update) {
    long now = new Date().getTime();
    for (int i = 0; i < update.stop_time_update.size(); i++) {
      if (update.stop_time_update.get(i).departure != null
          && now < update.stop_time_update.get(i).departure.time * 1000) {
        return i;
      }
    }
    return -1;
  }

  @Override public int getItemCount() {
    if (mModel == null) {
      return 0;
    }
    return mModel.getBusesForRoute(mRouteId).size();
  }

  public void updateModel(TransitModel model, String routeId) {
    mModel = model;
    mRouteId = routeId;
  }

  public static class BusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mName;
    private TextView mTime;
    private TextView mNextStop;
    private String mId;

    private static final String KEY = "BUS";

    public BusHolder(View v) {
      super(v);

      mName = (TextView) v.findViewById(R.id.item_name);
      mTime = (TextView) v.findViewById(R.id.item_arrival_time);
      mNextStop = (TextView) v.findViewById(R.id.item_next_stop);
      v.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      Log.d("RecyclerView", "CLICK! Display route for bus " + mId);
    }

    public void bindBus(String name, String nextStop, String time, String id) {
      mName.setText(name);
      mNextStop.setText(nextStop);
      mTime.setText(time);
      mId = id;
    }
  }
}


