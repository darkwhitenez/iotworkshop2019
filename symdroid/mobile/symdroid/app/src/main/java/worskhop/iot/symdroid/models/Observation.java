package worskhop.iot.symdroid.models;


import java.io.Serializable;
import java.util.List;

public class Observation implements Serializable {
    private String resourceId;
    private List<Value> obsValues;
    private String samplingTime;

    public Observation() {
    }

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<Value> getObsValues() {
        return obsValues;
    }

    public void setObsValues(List<Value> obsValues) {
        this.obsValues = obsValues;
    }

    public Property getFirstObservedProperties() {
        return obsValues.get(0).getObsProperty();
    }

    @Override
    public String toString() {
        return "Observation{" +
                "resourceId='" + resourceId + '\'' +
                ", obsValues=" + obsValues +
                '}';
    }

    public static class Value {
        private String value;
        private Property obsProperty;
        private Uom uom;

        public Value() {
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Property getObsProperty() {
            return obsProperty;
        }

        public void setObsProperty(Property obsProperty) {
            this.obsProperty = obsProperty;
        }

        public Uom getUom() {
            return uom;
        }

        public void setUom(Uom uom) {
            this.uom = uom;
        }


    }

    public static class Property {
        private String name;
        private String iri;
        private List<String> description;

        public Property() {
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

        public List<String> getDescription() {
            return description;
        }

        public void setDescription(List<String> description) {
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Property property = (Property) o;

            if (!name.equals(property.name)) return false;
            return iri.equals(property.iri);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + iri.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Property{" +
                    "name='" + name + '\'' +
                    ", iri='" + iri + '\'' +
                    ", description=" + description +
                    '}';
        }
    }

    public static class Uom {
        private String symbol;
        private String name;
        private String iri;
        private List<String> description;

        public Uom() {
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
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

        public List<String> getDescription() {
            return description;
        }

        public void setDescription(List<String> description) {
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Uom uom = (Uom) o;

            if (!name.equals(uom.name)) return false;
            return iri.equals(uom.iri);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + iri.hashCode();
            return result;
        }
    }
}