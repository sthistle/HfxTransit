package com.salesforce.android.hfxtransit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.salesforce.android.hfxtransit.R;
import java.util.Collections;
import java.util.List;

public class RouteListActivity extends AppCompatActivity {

  private RouteListPresenter mPresenter;
  private Spinner mSpinner;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_route_list);
    mPresenter = new RouteListPresenter(this);
    mSpinner = (Spinner) this.findViewById(R.id.routePicker);

    mPresenter.requestRoutes();
  }

  public void notifyRoutesChanged(List<String> busRoutes) {
    Collections.sort(busRoutes);
    final ArrayAdapter<String> routeAdapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, busRoutes);
    this.runOnUiThread(new Runnable() {
      @Override public void run() {
        mSpinner.setAdapter(routeAdapter);
      }
    });
  }
}
