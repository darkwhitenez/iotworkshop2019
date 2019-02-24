package worskhop.iot.symdroid.network;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import worskhop.iot.symdroid.models.Observation;
import worskhop.iot.symdroid.utils.ApiException;

public class SymbIoTeSensorReadingTask extends AsyncTask<String, Void, List<Observation>> {

    private final WeakReference<Context> mCtx;
    private final SensorReaderCallback mCallback;


    public SymbIoTeSensorReadingTask(Context ctx, SensorReaderCallback callback) {
        this.mCtx = new WeakReference<>(ctx);
        this.mCallback = callback;
    }

    @Override
    protected List<Observation> doInBackground(String... sensorIds) {

        List<Observation> observations = Collections.emptyList();
        try {
            NetworkUtil.getInstance().createSecurityRequest(mCtx.get());
        } catch (ApiException e) {
            mCallback.onError(e);
            return observations;
        }

        try {
            String resourceUrl = NetworkUtil.getInstance().getResourceUrl(mCtx.get(), sensorIds[0]);
            observations = NetworkUtil.getInstance().getReadCurrentValue(resourceUrl, mCtx.get());
        } catch (ApiException e) {
            mCallback.onError(e);
        }

        return observations;
    }

    @Override
    protected void onPostExecute(List<Observation> rapResponse) {
        super.onPostExecute(rapResponse);
        mCallback.onSuccess(rapResponse);
    }

    public interface SensorReaderCallback {
        void onSuccess(List<Observation> observations);

        void onError(ApiException e);
    }

}
