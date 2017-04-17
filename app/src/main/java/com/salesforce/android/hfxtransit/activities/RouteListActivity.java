package com.salesforce.android.hfxtransit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.salesforce.android.hfxtransit.R;
import com.salesforce.android.hfxtransit.activities.model.TransitModel;
import java.util.Collections;
import java.util.List;

public class RouteListActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {

  private RouteListPresenter mPresenter;
  private Spinner mSpinner;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLinearLayoutManager;
  private RouteListRvAdapter mRouteListAdapter;
  private String mRouteId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_route_list);
    mPresenter = new RouteListPresenter(this);

    mSpinner = (Spinner) this.findViewById(R.id.routePicker);
    mSpinner.setOnItemSelectedListener(this);

    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    mLinearLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLinearLayoutManager);
    mRecyclerView.setHasFixedSize(true);

    mRouteListAdapter = new RouteListRvAdapter(null, null);
    mRecyclerView.setAdapter(mRouteListAdapter);
    DividerItemDecoration dividerItemDecoration =
        new DividerItemDecoration(mRecyclerView.getContext(),
            mLinearLayoutManager.getOrientation());
    mRecyclerView.addItemDecoration(dividerItemDecoration);

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

  @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String routeNumber = (String) mSpinner.getItemAtPosition(position);
    mPresenter.requestBuses(Long.parseLong(routeNumber));
  }

  @Override public void onNothingSelected(AdapterView<?> parent) {
    // mPresenter.requestAllBuses();
  }

  public void updateBusList(TransitModel mModel, Long id) {
    mRouteListAdapter.updateModel(mModel, id.toString());
    mRouteListAdapter.notifyDataSetChanged();
  }
}
