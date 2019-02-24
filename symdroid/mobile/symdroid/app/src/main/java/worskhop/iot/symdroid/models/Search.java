package worskhop.iot.symdroid.models;

import java.io.Serializable;

public class Search implements Serializable {
    public String platformName;
    public String name;
    public String owner;
    public String observedProperty;
    public String type;
    public String locationName;

    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        if (!platformName.isEmpty()) {
            sb.append("platform_name=").append(platformName).append("&");
        }
        if (!name.isEmpty()) {
            sb.append("name=").append(name).append("&");
        }
        if (!owner.isEmpty()) {
            sb.append("owner=").append(owner).append("&");
        }
        if (!observedProperty.isEmpty()) {
            sb.append("observed_property=").append(observedProperty).append("&");
        }
        if (!type.isEmpty()) {
            sb.append("type=").append(type).append("&");
        }
        if (!locationName.isEmpty()) {
            sb.append("location_name=").append(locationName).append("&");
        }
        String query = sb.toString();
        if (!query.isEmpty()) {
            query = query.substring(0, query.length() - 1);
        }
        return query;
    }


    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getObservedProperty() {
        return observedProperty;
    }

    public void setObservedProperty(String observedProperty) {
        this.observedProperty = observedProperty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
