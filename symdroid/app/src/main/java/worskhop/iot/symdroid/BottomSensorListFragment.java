package worskhop.iot.symdroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;

import worskhop.iot.symdroid.adaptors.SensorsAdapter;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.utils.SimpleDividerItemDecoration;

/**
 * Created by Zvone on 10.1.2019..
 */

public class BottomSensorListFragment extends BottomSheetDialogFragment {
    public BottomSensorListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sensor_list, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.sensors);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mRecyclerView.getContext()));

        SensorsAdapter mAdapter = new SensorsAdapter(getActivity(), new ArrayList<SymbioteResource>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData((Collection<SymbioteResource>) getArguments().get("data"));
        mAdapter.notifyDataSetChanged();
        return view;
    }
}
