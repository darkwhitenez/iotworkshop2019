package worskhop.iot.symdroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import worskhop.iot.symdroid.models.MapItem;
import worskhop.iot.symdroid.models.Search;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.network.SymbIoTeCoreSensorQueryTask;
import worskhop.iot.symdroid.utils.ApiException;
import worskhop.iot.symdroid.utils.SensorClickCallback;
import worskhop.iot.symdroid.utils.SensorSearchCallback;

public class ExploreActivity extends AppCompatActivity implements
        OnMapReadyCallback, SensorSearchCallback,
        ClusterManager.OnClusterItemClickListener<MapItem>,
        ClusterManager.OnClusterClickListener<MapItem> {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ExploreActivity.class);
    private GoogleMap mMap;
    private ArrayList<SymbioteResource> data = new ArrayList<>();
    private BottomSearchFragment bottomSearchFragment;
    private BottomSensorListFragment bottomSensorListFragment;
    private BottomSensorDetailFragment bottomSensorDetailFragment;
    private FloatingActionButton listFab;
    private CircularProgressBar progressBar;
    private Marker selectedMarker;
    private ClusterManager<MapItem> mClusterManager;
    private final SymbIoTeCoreSensorQueryTask.QueryTaskCallback mSymbIoTeQueryCallback = new SymbIoTeCoreSensorQueryTask.QueryTaskCallback() {
        @Override
        public void onSearchComplete(Collection<SymbioteResource> sensors) {
            data = new ArrayList<>(sensors);
            if (sensors.isEmpty()) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "No resources were found", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            updateListFabVisibility();

            runOnUiThread(() -> updateMap());
            progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onError(ApiException e) {
            runOnUiThread(() -> {
                Snackbar.make(getWindow().getDecorView().getRootView(), e.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                progressBar.setVisibility(View.INVISIBLE);
            });
        }
    };

    private void updateListFabVisibility() {
        if (data.isEmpty()) {
            listFab.setVisibility(View.INVISIBLE);
        } else {
            listFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("data")) {
                data = (ArrayList<SymbioteResource>) savedInstanceState.getSerializable("data");
            }
        }
        setContentView(R.layout.activity_explore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find resources");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton searchFab = findViewById(R.id.searchFab);
        listFab = findViewById(R.id.listFab);
        listFab.setVisibility(View.INVISIBLE);
        searchFab.setOnClickListener(view -> {
            bottomSearchFragment = new BottomSearchFragment();
            bottomSearchFragment.setCallback(ExploreActivity.this);
            bottomSearchFragment.show(getSupportFragmentManager(), "SEARCH");

        });
        listFab.setOnClickListener(view -> {
            bottomSensorListFragment = new BottomSensorListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", new ArrayList<>(data));
            bottomSensorListFragment.setArguments(bundle);
            bottomSensorListFragment.show(getSupportFragmentManager(), "LIST");
        });
        updateListFabVisibility();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear(); //clear old markers
        setUpClusterer();
        enableMyLocation();
        runOnUiThread(this::updateMap);
    }

    private void setUpClusterer() {

        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(this);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }


    private void updateMap() {
        if (mMap != null && data != null) {
            mClusterManager.clearItems();
            for (SymbioteResource resource : data) {
                MarkerOptions markerOptions = new MarkerOptions();
                try {
                    Double lat = Double.parseDouble(resource.getLocationLatitude());
                    Double lon = Double.parseDouble(resource.getLocationLongitude());
                    markerOptions.position(new LatLng(lat, lon));
                    markerOptions.title(resource.getDescription());
                    mClusterManager.addItem(new MapItem(lat, lon, resource.getDescription(), resource.getId(), resource.getId()));

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
            mClusterManager.cluster();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void sensorSearchCallback(Search query) {
        SymbIoTeCoreSensorQueryTask mCoreQueryTask = new SymbIoTeCoreSensorQueryTask(this, mSymbIoTeQueryCallback);
        mCoreQueryTask.execute(query);
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onClusterItemClick(MapItem mapItem) {
        SymbioteResource resource = data.stream().filter(item -> item.getId().equals(mapItem.getResourceId())).findFirst().get();
        bottomSensorDetailFragment = new BottomSensorDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", resource);
        bottomSensorDetailFragment.setArguments(bundle);
        bottomSensorDetailFragment.setCallback(ExploreActivity.this);
        bottomSensorDetailFragment.show(getSupportFragmentManager(), "DETAILS");
        return false;
    }

    @Override
    public boolean onClusterClick(Cluster<MapItem> cluster) {
        System.out.println("ON CLUSTER CLICK");
        LatLngBounds.Builder builder = LatLngBounds.builder();
        Collection<MapItem> items = cluster.getItems();

        for (MapItem item : items) {
            LatLng venuePosition = item.getPosition();
            builder.include(venuePosition);
        }

        final LatLngBounds bounds = builder.build();

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return true;
    }
}

