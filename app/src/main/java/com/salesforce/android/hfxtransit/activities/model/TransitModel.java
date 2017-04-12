package com.salesforce.android.hfxtransit.activities.model;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.transit.realtime.Alert;
import com.google.transit.realtime.TripUpdate;
import com.google.transit.realtime.VehiclePosition;
import com.salesforce.android.hfxtransit.http.HfxTransitAdapter;
import com.salesforce.android.hfxtransit.utils.AbstractHandler;
import com.salesforce.android.service.common.utilities.control.Async;
import com.salesforce.android.service.common.utilities.control.BasicAsync;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sthistle on 4/11/17.
 */

public class TransitModel {
  private Multimap<String, String> mRoutesToBuses = HashMultimap.create();
  private Map<String, VehiclePosition> mBusToLocationMap = new HashMap<>();
  private Map<String, TripUpdate> mBusToTripMap = new HashMap<>();

  public TransitModel(List<VehiclePosition> pos, List<TripUpdate> trips) {
    // Populate the bus to route map from trip data.
    for (TripUpdate update : trips) {
      String bus = update.trip.trip_id;
      String route = update.trip.route_id;
      mRoutesToBuses.put(route, bus);
      mBusToTripMap.put(bus, update);
    }

    for (VehiclePosition p : pos) {
      mBusToLocationMap.put(p.trip.route_id, p);
    }
  }

  public Collection<String> getBusesForRoute(String route) {
    return mRoutesToBuses.get(route);
  }

  public VehiclePosition getBusPostion(String busId) {
    return mBusToLocationMap.get(busId);
  }

  public TripUpdate getTripUpdate(String busId) {
    return mBusToTripMap.get(busId);
  }

  public List<String> getBusRoutes() {
    return Lists.newArrayList(mRoutesToBuses.keySet());
  }

  public static Async<TransitModel> updateModel() {
    BasicAsync<TransitModel> results = BasicAsync.create();
    final HfxTransitAdapter adapter = new HfxTransitAdapter();
    TransitModelUpdater updater = new TransitModelUpdater(results, adapter);
    adapter.getBusPostions().addHandler(updater);
    return results;
  }

  private static class TransitModelUpdater extends AbstractHandler<List<VehiclePosition>> {
    private BasicAsync<TransitModel> results;
    private List<VehiclePosition> positions;
    private List<TripUpdate> updates;
    private HfxTransitAdapter adapter;

    public TransitModelUpdater(BasicAsync<TransitModel> results, HfxTransitAdapter adapter) {
      super();
      this.results = results;
      this.adapter = adapter;
    }

    @Override
    public void handleResult(Async<?> async, @NonNull List<VehiclePosition> vehiclePosition) {
      positions = vehiclePosition;
      adapter.getTripUpdates().addHandler(new AbstractHandler<List<TripUpdate>>() {
        @Override public void handleResult(Async<?> async, @NonNull List<TripUpdate> tripUpdate) {
          updates = tripUpdate;
          TransitModel model = new TransitModel(positions, updates);
          results.setResult(model);
        }
      });
    }
  }
}
