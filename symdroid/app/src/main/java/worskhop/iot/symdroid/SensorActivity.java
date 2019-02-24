package worskhop.iot.symdroid;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.suke.widget.SwitchButton;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.List;
import java.util.stream.Collectors;

import worskhop.iot.symdroid.adaptors.GraphsAdapter;
import worskhop.iot.symdroid.models.Observation;
import worskhop.iot.symdroid.models.ObservedProperties;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.network.SymbIoTeActuatorCommand;
import worskhop.iot.symdroid.network.SymbIoTeSensorReadingTask;
import worskhop.iot.symdroid.utils.ApiException;

public class SensorActivity extends AppCompatActivity implements SymbIoTeSensorReadingTask.SensorReaderCallback
        , SymbIoTeActuatorCommand.ActuatorCommandCallback {
    private SymbioteResource resource;

    private TextView sensorName;
    private TextView platformId;
    private TextView platformName;
    private TextView locationName;
    private TextView latitude;
    private TextView longitude;
    private TextView description;
    private TextView observedProperties;
    private TextView resourceType;

    private SwitchButton switchButton;
    private IndicatorSeekBar indicatorSeekBarR;
    private IndicatorSeekBar indicatorSeekBarG;
    private IndicatorSeekBar indicatorSeekBarB;
    private Button rgbSendBtn;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GraphsAdapter graphsAdapter;
    private CircularProgressBar progressBar;
    private View actuatorViewOnOff;
    private View actuatorViewRGB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Resource");

        if (getIntent().hasExtra("resource")) {
            resource = (SymbioteResource) getIntent().getExtras().get("resource");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras: " + "resource");
        }

        sensorName = findViewById(R.id.sensorName);
        platformId = findViewById(R.id.platformId);
        platformName = findViewById(R.id.platformName);
        locationName = findViewById(R.id.locationName);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        description = findViewById(R.id.description);
        observedProperties = findViewById(R.id.observed_properties);
        resourceType = findViewById(R.id.resource_type);

        sensorName.setText(resource.getName());
        platformId.setText(resource.getPlatformId());
        platformName.setText(resource.getPlatformName());
        locationName.setText(resource.getLocationName());
        latitude.setText(resource.getLocationLatitude());
        longitude.setText(resource.getLocationLongitude());
        description.setText(resource.getDescription());
        observedProperties.setText(resource.getObservedProperties().stream().map(ObservedProperties::getName).collect(Collectors.joining(",")));
        resourceType.setText(resource.getResourceType().stream().map(item -> item.split("#")[1]).collect(Collectors.joining(",")));

        mRecyclerView = findViewById(R.id.graphs);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        graphsAdapter = new GraphsAdapter(this, resource.getObservedProperties());
        mRecyclerView.setAdapter(graphsAdapter);

        /* hardkodirani slučaji za jednostavan showcase */
        // aktuator on/off
        actuatorViewOnOff = findViewById(R.id.activity_sensor_actuator_onoff);
        switchButton = findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener((v, isChecked) -> {
            onSwitchButtonEvent(isChecked);
        });

        if (resource.getResourceType().stream().anyMatch(s ->
                s.contains("Actuator")
        ) && (resource.getDescription().toLowerCase().contains("switch") || (resource.getName().contains("AjOTi")))) {
            actuatorViewOnOff.setVisibility(View.VISIBLE);
        } else {
            actuatorViewOnOff.setVisibility(View.GONE);
        }

        // aktuator rgb
        actuatorViewRGB = findViewById(R.id.activity_sensor_actuator_rgb);
        indicatorSeekBarR = findViewById(R.id.indicatorR);
        indicatorSeekBarG = findViewById(R.id.indicatorG);
        indicatorSeekBarB = findViewById(R.id.indicatorB);
        rgbSendBtn = findViewById(R.id.rgbSendBtn);

        rgbSendBtn.setOnClickListener(v -> {
            onRGBCommand(indicatorSeekBarR.getProgress(), indicatorSeekBarG.getProgress(), indicatorSeekBarB.getProgress());
        });

        if (resource.getResourceType().stream().anyMatch(s ->
                s.contains("Actuator")
        ) && resource.getDescription().toLowerCase().contains("rgb")) {
            actuatorViewRGB.setVisibility(View.VISIBLE);
        } else {
            actuatorViewRGB.setVisibility(View.GONE);
        }

        // sensor
        if (resource.getResourceType().stream().anyMatch(s ->
                s.contains("Sensor")
        )) {
            progressBar.setVisibility(View.VISIBLE);
            new SymbIoTeSensorReadingTask(this, this).execute(resource.getId());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(List<Observation> observations) {
        graphsAdapter.setData(observations);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(ApiException e) {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            Snackbar.make(getWindow().getDecorView().getRootView(), e.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
    }

    public void onSwitchButtonEvent(boolean isChecked) {
        String command = "";
        if (isChecked) {
            command = "{\"onOffCapability\":[{\"on\":true}]}";
        } else {
            command = "{\"onOffCapability\":[{\"on\":false}]}";
        }
        System.out.println(command);
        new SymbIoTeActuatorCommand(this, this, command).execute(resource.getId());
    }

    public void onRGBCommand(int r, int g, int b) {
        String command = String.format("{\"RGBCapability\":[{\"r\":%d,\"g\":%d,\"b\":%d}]}", r, g, b);
        new SymbIoTeActuatorCommand(this, this, command).execute(resource.getId());
    }
}
