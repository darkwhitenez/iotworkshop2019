package worskhop.iot.symdroid.network;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import worskhop.iot.symdroid.utils.ApiException;

public class SymbIoTeActuatorCommand extends AsyncTask<String, Void, Void> {

    private final WeakReference<Context> mCtx;
    private String command;
    private ActuatorCommandCallback callback;

    public SymbIoTeActuatorCommand(Context ctx, ActuatorCommandCallback callback, String command) {
        this.mCtx = new WeakReference<>(ctx);
        this.callback = callback;
        this.command = command;
    }

    @Override
    protected Void doInBackground(String... sensorIds) {

        try {
            NetworkUtil.getInstance().createSecurityRequest(mCtx.get());
        } catch (ApiException e) {
            callback.onError(e);
            return null;
        }

        try {
            String resourceUrl = NetworkUtil.getInstance().getResourceUrl(mCtx.get(), sensorIds[0]);
            NetworkUtil.getInstance().sendCommandToActuator(mCtx.get(), resourceUrl, command);
        } catch (ApiException e) {
            callback.onError(e);
        }
        return null;
    }

    public interface ActuatorCommandCallback {
        void onError(ApiException e);
    }
}
