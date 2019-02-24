package worskhop.iot.symdroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import worskhop.iot.symdroid.models.Search;
import worskhop.iot.symdroid.utils.SensorSearchCallback;

public class BottomSearchFragment extends BottomSheetDialogFragment {
    private static BottomSheetBehavior bottomSheetBehavior;
    private static View bottomSheetInternal;
    private static BottomSearchFragment INSTANCE;
    private SensorSearchCallback callback;
    private CoordinatorLayout coordinatorLayout;

    public BottomSearchFragment() {
    }

    public void setCallback(SensorSearchCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            coordinatorLayout = (CoordinatorLayout) d.findViewById(R.id.locUXCoordinatorLayout);
            bottomSheetInternal = d.findViewById(R.id.locUXView);
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInternal);
            bottomSheetBehavior.setHideable(false);
            BottomSheetBehavior.from((View) coordinatorLayout.getParent()).setPeekHeight(bottomSheetInternal.getHeight());
            bottomSheetBehavior.setPeekHeight(bottomSheetInternal.getHeight());
            coordinatorLayout.getParent().requestLayout();
        });
        INSTANCE = this;

        final View view = inflater.inflate(R.layout.bottom_search, container, false);

        Button searchBtn = view.findViewById(R.id.searchBtn);
        EditText platform_name = view.findViewById(R.id.platform_name);
        EditText name = view.findViewById(R.id.name);
        EditText owner = view.findViewById(R.id.owner);
        EditText observed_property = view.findViewById(R.id.observed_property);
        EditText type = view.findViewById(R.id.type);
        EditText location_name = view.findViewById(R.id.location_name);


        searchBtn.setOnClickListener((e) -> {
            Search search = new Search();
            search.setPlatformName(platform_name.getText().toString());
            search.setName(name.getText().toString());
            search.setOwner(owner.getText().toString());
            search.setObservedProperty(observed_property.getText().toString());
            search.setType(type.getText().toString());
            search.setLocationName(location_name.getText().toString());
            callback.sensorSearchCallback(search);
            dismiss();
        });

        return view;
    }

}
