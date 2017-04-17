package com.salesforce.android.hfxtransit.activities;

import android.support.annotation.NonNull;
import com.salesforce.android.hfxtransit.activities.model.TransitModel;
import com.salesforce.android.hfxtransit.utils.AbstractHandler;
import com.salesforce.android.service.common.utilities.control.Async;

/**
 * Created by sthistle on 4/11/17.
 */

public class RouteListPresenter {
  private TransitModel mModel;
  private RouteListActivity mActivity;

  public RouteListPresenter(RouteListActivity activity) {
    mActivity = activity;
  }

  public void requestRoutes() {
    if (mModel != null) {
      updateRoutes(mModel);
    } else {
      updateModel();
    }
  }

  private void updateRoutes(TransitModel model) {
    mActivity.notifyRoutesChanged(model.getBusRoutes());
  }

  private void updateModel() {
    TransitModel.updateModel().addHandler(new AbstractHandler<TransitModel>() {

      @Override public void handleResult(Async<?> async, @NonNull TransitModel transitModel) {
        mModel = transitModel;
        updateRoutes(transitModel);
      }
    });
  }

  public void requestAllBuses() {
    //  mActivity.updateBusList(mModel);
  }

  public void requestBuses(long id) {
    mActivity.updateBusList(mModel, id);
  }
}
