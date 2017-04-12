package com.salesforce.android.hfxtransit.http;

import android.util.Log;
import com.google.transit.realtime.FeedEntity;
import com.google.transit.realtime.FeedMessage;
import com.google.transit.realtime.TripUpdate;
import com.google.transit.realtime.VehiclePosition;
import com.salesforce.android.service.common.utilities.control.Async;
import com.salesforce.android.service.common.utilities.control.BasicAsync;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.salesforce.android.hfxtransit.http.HttpUtils.REAL_TIME;
import static com.salesforce.android.hfxtransit.http.HttpUtils.TRIP_UPDATES;
import static com.salesforce.android.hfxtransit.http.HttpUtils.VEHICLE_POS;

/**
 * Created by sthistle on 4/11/17.
 */
public class TripUpdateRequest {

  private static String TAG = "TripUpdateRequest";
  final BasicAsync<List<TripUpdate>> mResult;

  private TripUpdateRequest() {
    mResult = BasicAsync.create();
  }

  public static Async<List<TripUpdate>> send(HttpUrl baseUrl, OkHttpClient client) {
    final TripUpdateRequest r = new TripUpdateRequest();
    HttpUrl url =
        baseUrl.newBuilder().addPathSegment(REAL_TIME).addPathSegment(TRIP_UPDATES).build();

    Request request = new Request.Builder().url(url).build();
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        Log.e(TAG, "onFailure: " + e.getMessage());
        r.mResult.setError(e);
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        try {
          FeedMessage posFeed = FeedMessage.ADAPTER.decode(response.body().byteStream());
          List<TripUpdate> list = new ArrayList<TripUpdate>();
          for (FeedEntity e : posFeed.entity) {
            list.add(e.trip_update);
          }
          r.mResult.setResult(list);
        } catch (Exception e) {
          r.mResult.setError(e);
        }
      }
    });
    return r.mResult;
  }
}
