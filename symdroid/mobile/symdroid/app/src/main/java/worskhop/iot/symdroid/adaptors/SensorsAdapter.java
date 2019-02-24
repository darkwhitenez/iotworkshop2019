package worskhop.iot.symdroid.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import worskhop.iot.symdroid.R;
import worskhop.iot.symdroid.SensorActivity;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.utils.SharedPreference;

public class SensorsAdapter extends RecyclerView.Adapter<SensorsAdapter.MyViewHolder> {
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();
    private List<SymbioteResource> mDataset;
    private LayoutInflater mInflater;
    private Context context;
    private SharedPreference sharedPreference = SharedPreference.getInstance();
    private List<SymbioteResource> resources = Collections.EMPTY_LIST;

    public SensorsAdapter(Context context, List<SymbioteResource> myDataset) {
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

    @NonNull
    @Override
    public SensorsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = mInflater.inflate(R.layout.sensor_row, parent, false);
        return new SensorsAdapter.MyViewHolder(view);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(position);
            }
        });

        holder.mSubscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resources.contains(resource)) {
                    Drawable img = context.getResources().getDrawable(R.drawable.checked_empty_32);
                    holder.mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    sharedPreference.removeResource(context, resource);
                } else {
                    Drawable img = context.getResources().getDrawable(R.drawable.checked_success_32);
                    holder.mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    sharedPreference.addResource(context, resource);
                }
            }
        });
        if (resources.contains(resource)) {
            Drawable img = context.getResources().getDrawable(R.drawable.checked_success_32);
            holder.mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        } else {
            Drawable img = context.getResources().getDrawable(R.drawable.checked_empty_32);
            holder.mSubscribeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }

        holder.mDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("resource", resource);
                Intent i = new Intent(context, SensorActivity.class);
                i.putExtras(bundle);
                context.startActivity(i);
            }
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

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mSensorName;
        TextView mPlatformName;
        TextView mLocationName;
        TextView mDescription;
        Button mSubscribeBtn;
        Button mDataBtn;

        MyViewHolder(View itemView) {
            super(itemView);
            mSensorName = itemView.findViewById(R.id.sensorName);
            mPlatformName = itemView.findViewById(R.id.platformName);
            mLocationName = itemView.findViewById(R.id.locationName);
            mDescription = itemView.findViewById(R.id.description);
            mSubscribeBtn = itemView.findViewById(R.id.subscribeBtn);
            mDataBtn = itemView.findViewById(R.id.dataBtn);
        }
    }
}