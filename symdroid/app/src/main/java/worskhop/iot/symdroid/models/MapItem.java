package worskhop.iot.symdroid.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapItem implements ClusterItem {
    private final LatLng mPosition;
    private String resourceId;
    private String mTitle;
    private String mSnippet;

    public MapItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
    }

    public MapItem(double lat, double lng, String title, String snippet, String resourceId) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        this.resourceId = resourceId;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set the title of the marker
     *
     * @param title string to be set as title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    /**
     * Set the description of the marker
     *
     * @param snippet string to be set as snippet
     */
    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSnippet() {
        return mSnippet;
    }

    public void setmSnippet(String mSnippet) {
        this.mSnippet = mSnippet;
    }

}