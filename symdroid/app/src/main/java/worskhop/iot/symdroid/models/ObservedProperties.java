package worskhop.iot.symdroid.models;


import java.io.Serializable;
import java.util.Arrays;

public class ObservedProperties implements Serializable {
    private String name;
    private String iri;
    private String[] description;

    public ObservedProperties() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ObservedProperties [name=" + name + ", iri=" + iri + ", description=" + Arrays.toString(description)
                + "]";
    }
}