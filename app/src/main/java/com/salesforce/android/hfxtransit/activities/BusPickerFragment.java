package com.salesforce.android.hfxtransit.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.salesforce.android.hfxtransit.R;
import com.salesforce.android.hfxtransit.activities.model.BusModel;
import com.salesforce.android.hfxtransit.csv.ConstantTransitModel;
import java.util.HashMap;
import java.util.List;

public class BusPickerFragment extends Fragment {
  MapView mapView;
  GoogleMap map;
  final int MY_PERM_REQUEST = 12367;
  private HashMap<String, Marker> markerHashMap = new HashMap<>();
  String mRoute;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_bus_picker, container, false);

    // Gets the MapView from the XML layout and creates it
    mapView = (MapView) v.findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);

    // Gets to GoogleMap from the MapView and does initialization stuff
    map = mapView.getMap();
    map.getUiSettings().setMyLocationButtonEnabled(false);
    try {

      if (!isPermitted()) {
        String[] requiredPerms = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this.getActivity(), requiredPerms, MY_PERM_REQUEST);
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return v;
      } else {
        activateMap();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return v;
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    if (requestCode == MY_PERM_REQUEST && isPermitted()) {
      activateMap();
    }
  }

  private boolean isPermitted() {
    return !(ActivityCompat.checkSelfPermission(this.getContext(),
        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this.getContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
  }

  private void activateMap() {
    try {
      map.setMyLocationEnabled(true);
      // Updates the location and zoom of the MapView
      CameraUpdate cameraUpdate =
          CameraUpdateFactory.newLatLngZoom(new LatLng(44.6488, -63.5752), 10);
      map.animateCamera(cameraUpdate);
      // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
      MapsInitializer.initialize(this.getActivity());
    } catch (SecurityException e) {
      e.printStackTrace();
    }
  }

  @Override public void onResume() {
    mapView.onResume();
    super.onResume();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  public void updateMap(final List<BusModel> model, final String route) {

    this.getActivity().runOnUiThread(new Runnable() {
      @Override public void run() {
        if (mRoute != route) {
          mRoute = route;
          for (Marker m : markerHashMap.values()) {
            m.remove();
          }
          markerHashMap.clear();
        }
        for (BusModel b : model) {
          if (markerHashMap.containsKey(b.getName())) {
            Marker m = markerHashMap.get(b.getName());
            animateMarker(markerHashMap.get(b.getName()), b.getPosition());
          } else {
            String name =
                ConstantTransitModel.getInstance().getTrip(b.getName()).getTrip_headsign();
            Marker m = map.addMarker(new MarkerOptions().position(b.getPosition()).title(name));
            m.showInfoWindow();
            markerHashMap.put(b.getName(), m);
          }
        }
      }
    });
  }

  private void animateMarker(final Marker marker, final LatLng toPosition) {
    final Handler handler = new Handler(Looper.getMainLooper());
    final long start = SystemClock.uptimeMillis();
    Projection proj = map.getProjection();
    Point startPoint = proj.toScreenLocation(marker.getPosition());
    final LatLng startLatLng = proj.fromScreenLocation(startPoint);
    final long duration = 2000;

    final Interpolator interpolator = new LinearInterpolator();

    handler.post(new Runnable() {
      @Override public void run() {
        long elapsed = SystemClock.uptimeMillis() - start;
        float t = interpolator.getInterpolation((float) elapsed / duration);
        double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
        double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
        marker.setPosition(new LatLng(lat, lng));

        if (t < 1.0) {
          // Post again 16ms later.
          handler.postDelayed(this, 16);
        }
      }
    });
  }
}
