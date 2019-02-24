package worskhop.iot.symdroid.adaptors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import worskhop.iot.symdroid.R;
import worskhop.iot.symdroid.SensorActivity;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.utils.SharedPreference;

public class SensorsDetailAdapter extends RecyclerView.Adapter<SensorsDetailAdapter.MyViewHolder> {
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();
    private List<SymbioteResource> mDataset;
    private LayoutInflater mInflater;
    private Context context;
    private SharedPreference sharedPreference = SharedPreference.getInstance();
    private List<SymbioteResource> resources = Collections.EMPTY_LIST;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SensorsDetailAdapter(Context context, List<SymbioteResource> myDataset) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = myDataset;
    }

    public void setData(Collection<SymbioteResource> data) {
        if (data != null && !data.isEmpty()) {
            mDataset.clear();
            mDataset.addAll(data);
            notifyDataSetChanged();
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public SensorsDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View view = mInflater.inflate(R.layout.sensor_detail_row, parent, false);
        return new SensorsDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SymbioteResource resource = mDataset.get(position);

        List<SymbioteResource> subscribedResources = sharedPreference.getResources(context);
        if (subscribedResources != null) {
            resources = subscribedResources;
        }

        holder.mSensorName.setText(resource.getName());
        holder.mPlatformName.setText(resource.getPlatformName());
        holder.mLocationName.setText(resource.getLocationName());
        holder.mDescription.setText(resource.getDescription());

        try {
            Double lat = Double.parseDouble(resource.getLocationLatitude());
            Double lon = Double.parseDouble(resource.getLocationLongitude());

            holder.mapView.setTag(new LatLng(lat, lon));
            holder.setMapLocation();
        } catch (Exception ignore) {
        }


        holder.itemView.setOnClickListener(v -> onClickSubject.onNext(position));

        holder.mSubscribeBtn.setOnClickListener(view -> {
            sharedPreference.removeResource(context, resource);
            mDataset.remove(resource);
            notifyDataSetChanged();
        });

        holder.mDataBtn.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            System.out.println(resource);
            bundle.putSerializable("resource", resource);
            Intent i = new Intent(context, SensorActivity.class);
            i.putExtras(bundle);
            context.startActivity(i);
        });
    }

    public PublishSubject<Integer> getPositionClicks() {
        return onClickSubject;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        // each data item is just a string in this case
        TextView mSensorName;
        TextView mPlatformName;
        TextView mLocationName;
        TextView mDescription;
        Button mSubscribeBtn;
        Button mDataBtn;

        MapView mapView;
        GoogleMap map;

        MyViewHolder(View itemView) {
            super(itemView);
            mSensorName = itemView.findViewById(R.id.sensorName);
            mPlatformName = itemView.findViewById(R.id.platformName);
            mLocationName = itemView.findViewById(R.id.locationName);
            mDescription = itemView.findViewById(R.id.description);
            mSubscribeBtn = itemView.findViewById(R.id.subscribeBtn);
            mDataBtn = itemView.findViewById(R.id.dataBtn);
            mapView = itemView.findViewById(R.id.map_view);

            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.getUiSettings().setMapToolbarEnabled(false);
            setMapLocation();
        }

        private void setMapLocation() {
            if (map == null) return;

            LatLng data = (LatLng) mapView.getTag();
            if (data == null) return;

            // Add a marker for this item and set the camera
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(data, 13f));
            map.addMarker(new MarkerOptions().position(data));

            // Set the map type back to normal.
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
}