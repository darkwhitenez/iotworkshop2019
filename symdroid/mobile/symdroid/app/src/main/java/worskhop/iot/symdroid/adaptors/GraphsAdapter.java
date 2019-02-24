package worskhop.iot.symdroid.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import worskhop.iot.symdroid.R;
import worskhop.iot.symdroid.models.Observation;
import worskhop.iot.symdroid.models.ObservedProperties;

public class GraphsAdapter extends RecyclerView.Adapter<GraphsAdapter.MyViewHolder> {
    private Map<Observation.Property, List<Observation>> mDataset;
    private List<Observation.Property> properties = new LinkedList<>();
    private LayoutInflater mInflater;
    private Context context;

    public GraphsAdapter(Context context, List<ObservedProperties> observedProperties) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = new HashMap<>();
    }

    public void setData(Collection<Observation> data) {
        if (data != null && !data.isEmpty()) {
            mDataset.clear();
            prepareData(data);
            notifyDataSetChanged();
        }
    }

    public void prepareData(Collection<Observation> data) {
        mDataset = data.stream().collect(Collectors.groupingBy(item -> {
            return item.getObsValues().get(0).getObsProperty();
        }));
        properties.addAll(mDataset.keySet());
        System.out.println("DATA PREPARED " + +mDataset.keySet().size() + "\n" + mDataset.keySet());
    }

    @Override
    public GraphsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = mInflater.inflate(R.layout.observation_row, parent, false);
        return new GraphsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println(properties.size());
        List<Observation> observations = mDataset.get(properties.get(position));
        DataPoint[] points = new DataPoint[0];
        if (observations != null) {
            points = observations.stream().map(item -> {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date date = null;
                try {
                    date = format.parse(item.getSamplingTime().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = new Date();
                }
                return new DataPoint(date, Float.parseFloat(item.getObsValues().get(0).getValue()));
            }).sorted(Comparator.comparingDouble(DataPoint::getX)).toArray(DataPoint[]::new);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(12);
        series.setThickness(3);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date d = new java.sql.Date((long) dataPoint.getX());
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String formatted = format1.format(d.getTime());
                //Toast.makeText(context, formatted + " " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Info");
                alertDialog.setMessage("Time: " + formatted + "\nValue: " + dataPoint.getY());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        holder.chart.addSeries(series);

        // set date label formatter
        holder.chart.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context, new SimpleDateFormat("yyyy-MM-dd" + "\n" + "HH:mm:ss")));
        holder.chart.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        holder.chart.getGridLabelRenderer().setNumVerticalLabels(8); // only 8 because of the space


        // set manual x bounds to have nice steps
        holder.chart.getViewport().setXAxisBoundsManual(true);
        holder.chart.getViewport().setMinX(series.getLowestValueX());
        holder.chart.getViewport().setMaxX(series.getHighestValueX());

        // enable scaling and scrolling
        //holder.chart.getViewport().setScalable(true);
        //holder.chart.getViewport().setScalableY(true);


        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        holder.chart.getGridLabelRenderer().setHumanRounding(false);

        String sensorText = properties.get(position).getName() + " (" + observations.get(0).getObsValues().get(0).getUom().getSymbol() + ")";
        holder.sensorName.setText(sensorText);

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
        return properties.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public GraphView chart;
        public TextView sensorName;

        public MyViewHolder(View itemView) {
            super(itemView);
            chart = itemView.findViewById(R.id.chart);
            sensorName = itemView.findViewById(R.id.sensorName);
        }
    }
}