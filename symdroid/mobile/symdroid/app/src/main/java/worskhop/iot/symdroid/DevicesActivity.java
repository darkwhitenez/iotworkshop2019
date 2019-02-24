package worskhop.iot.symdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import worskhop.iot.symdroid.adaptors.SensorsDetailAdapter;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.utils.SharedPreference;

public class DevicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subscriptions");
        RecyclerView mRecyclerView = findViewById(R.id.sensors);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SensorsDetailAdapter mAdapter = new SensorsDetailAdapter(this, new ArrayList<SymbioteResource>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.getPositionClicks().subscribe((item) -> {

        });

        mAdapter.setData(SharedPreference.getInstance().getResources(this));
    }
}
