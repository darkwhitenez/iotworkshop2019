package worskhop.iot.symdroid.models;

import java.io.Serializable;
import java.util.List;

public class SymbioteResource implements Serializable {
    private String platformId;
    private String platformName;
    private String owner;
    private String name;
    private String description;
    private String id;
    private String locationName;
    private String locationLatitude;
    private String locationLongitude;
    private String locationAltitude;
    private List<String> resourceType;
    private List<ObservedProperties> observedProperties;

    public SymbioteResource() {
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationAltitude() {
        return locationAltitude;
    }

    public void setLocationAltitude(String locationAltitude) {
        this.locationAltitude = locationAltitude;
    }

    public List<String> getResourceType() {
        return resourceType;
    }

    public void setResourceType(List<String> resourceType) {
        this.resourceType = resourceType;
    }

    public List<ObservedProperties> getObservedProperties() {
        return observedProperties;
    }

    public void setObservedProperties(List<ObservedProperties> observedProperties) {
        this.observedProperties = observedProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SymbioteResource resource = (SymbioteResource) o;

        return id.equals(resource.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SymbioteResource [platformId=" + platformId + ", platformName=" + platformName + ", owner=" + owner
                + ", name=" + name + ", description=" + description + ", id=" + id + ", locationName=" + locationName
                + ", locationLatitude=" + locationLatitude + ", locationLongitude=" + locationLongitude
                + ", locationAltitude=" + locationAltitude + ", resourceType=" + resourceType + ", observedProperties="
                + observedProperties + "]";
    }

}