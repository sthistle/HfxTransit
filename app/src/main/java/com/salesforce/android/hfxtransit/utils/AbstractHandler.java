package com.salesforce.android.hfxtransit.utils;

/**
 * Created by sthistle on 4/11/17.
 */

import android.support.annotation.NonNull;
import android.util.Log;
import com.salesforce.android.service.common.utilities.control.Async;

public abstract class AbstractHandler<T>
    implements Async.ResultHandler<T>, Async.ErrorHandler, Async.CompletionHandler {

  private static String TAG = "AbstractHandler";

  @Override public void handleComplete(Async<?> async) {
    Log.d(TAG, "handleComplete: ");
  }

  @Override public void handleError(Async<?> async, @NonNull Throwable throwable) {
    Log.e(TAG, "Error handling async result " + throwable.getMessage());
  }
}
