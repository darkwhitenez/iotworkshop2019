package worskhop.iot.symdroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import worskhop.iot.symdroid.settings.SettingsActivity;
import worskhop.iot.symdroid.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView exploreView = findViewById(R.id.explore);
        TextView devicesView = findViewById(R.id.devices);
        TextView settingsView = findViewById(R.id.settings);

        exploreView.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, ExploreActivity.class);
            startActivity(i);
        });
        devicesView.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, DevicesActivity.class);
            startActivity(i);
        });
        settingsView.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }
}
