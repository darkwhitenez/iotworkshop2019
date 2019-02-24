package worskhop.iot.symdroid.network;

import android.content.Context;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import worskhop.iot.symdroid.models.Search;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.settings.Settings;
import worskhop.iot.symdroid.utils.ApiException;

public class SymbIoTeCoreSensorQueryTask extends AsyncTask<Search, Void, Collection<SymbioteResource>> {

    private static final Logger LOG = LoggerFactory.getLogger(SymbIoTeCoreSensorQueryTask.class);

    private final QueryTaskCallback mCallback;
    private final WeakReference<Context> mCtx;


    public SymbIoTeCoreSensorQueryTask(Context ctx, QueryTaskCallback callback) {
        this.mCallback = callback;
        this.mCtx = new WeakReference<>(ctx);
    }


    @Override
    protected Collection<SymbioteResource> doInBackground(Search... queries) throws ApiException {
        @SuppressWarnings("unchecked") Collection<SymbioteResource> result;
        try {
            NetworkUtil.getInstance().createSecurityRequest(mCtx.get());

            result = NetworkUtil.getInstance().findResources(mCtx.get(), queries[0]);
        } catch (ApiException e) {
            mCallback.onError(e);
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Collection<SymbioteResource> sensorId) {
        super.onPostExecute(sensorId);
        if (sensorId != null) {
            List<SymbioteResource> results = new ArrayList<>(sensorId);
            if (results.size() > Settings.getMaxQueryItemsPerReq(mCtx.get())) {
                results = results.subList(0, Settings.getMaxQueryItemsPerReq(mCtx.get()));
            }
            mCallback.onSearchComplete(results);
        }
    }

    public interface QueryTaskCallback {
        void onSearchComplete(Collection<SymbioteResource> sensorId);

        void onError(ApiException e);
    }

}
