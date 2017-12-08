package com.hexaenna.drchella.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 12/8/2017.
 */

public class MapDetails {
    @SerializedName("results")
    @Expose
    ArrayList<MapResults> results = new ArrayList<>();

    public ArrayList<MapResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<MapResults> results) {
        this.results = results;
    }

    public class MapResults {
        Geometry  geometry;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
    }

    public class Geometry{
        LocationLat location;

        public LocationLat getLocation() {
            return location;
        }

        public void setLocation(LocationLat location) {
            this.location = location;
        }
    }

    public class LocationLat{
        double lat,lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
