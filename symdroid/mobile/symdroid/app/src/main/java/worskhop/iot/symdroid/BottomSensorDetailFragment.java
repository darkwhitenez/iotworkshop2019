package worskhop.iot.symdroid;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.utils.SensorSearchCallback;
import worskhop.iot.symdroid.utils.SharedPreference;

public class BottomSensorDetailFragment extends BottomSheetDialogFragment {
    private SensorSearchCallback callback;
    private SharedPreference sharedPreference = SharedPreference.getInstance();

    public BottomSensorDetailFragment() {
    }

    public void setCallback(SensorSearchCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.bottom_sensor_detail, container, false);

        TextView mSensorName;
        TextView mPlatformName;
        TextView mLocationName;
        TextView mDescription;
        Button mSubscribeBtn;
        Button mDataBtn;

        mSensorName = view.findViewById(R.id.sensorName);
        mPlatformName = view.findViewById(R.id.platformName);
        mLocationName = view.findViewById(R.id.locationName);
        mDescription = view.findViewById(R.id.description);
        mSubscribeBtn = view.findViewById(R.id.subscribeBtn);
        mDataBtn = view.findViewById(R.id.dataBtn);


        SymbioteResource resource = ((SymbioteResource) getArguments().get("data"));


        mSensorName.setText(resource.getName());
        mPlatformName.setText(resource.getPlatformName());
        mLocationName.setText(resource.getLocationName());
        mDescription.setText(resource.getDescription());

        List<SymbioteResource> resources = sharedPreference.getResources(getContext());

        if (resources.contains(resource)) {
            Drawable img = getContext().getResources().getDrawable(R.drawable.checked_success_32);
            mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        } else {
            Drawable img = getContext().getResources().getDrawable(R.drawable.checked_empty_32);
            mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }


        mDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("resource", resource);
                Intent i = new Intent(getContext(), SensorActivity.class);
                i.putExtras(bundle);
                getActivity().startActivity(i);
            }
        });

        mSubscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resources.contains(resource)) {
                    Drawable img = getContext().getResources().getDrawable(R.drawable.checked_empty_32);
                    mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    sharedPreference.removeResource(getContext(), resource);
                } else {
                    Drawable img = getContext().getResources().getDrawable(R.drawable.checked_success_32);
                    mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    sharedPreference.addResource(getContext(), resource);
                }
            }
        });


        return view;
    }

}
