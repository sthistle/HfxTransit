package com.salesforce.android.hfxtransit.activities;

import android.support.annotation.NonNull;
import com.salesforce.android.hfxtransit.activities.model.TransitModel;
import com.salesforce.android.hfxtransit.http.HfxTransitAdapter;
import com.salesforce.android.hfxtransit.utils.AbstractHandler;
import com.salesforce.android.service.common.utilities.control.Async;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
}
