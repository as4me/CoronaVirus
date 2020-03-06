package me.as4.coronavirus.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class RealTimeDatabase {

    public int confirmed;
    public int death;
    public int recover;
    public String data;
    public Double lat;
    public Double lon;

    public RealTimeDatabase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public int getConfirmed() {
        return confirmed;
    }

    public int getDeath() {
        return death;
    }

    public int getRecover() {
        return recover;
    }

    public String getData() {
        return data;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public RealTimeDatabase(int confirmed, int death, int recover, String data, Double lat, Double lon) {
        this.confirmed = confirmed;
        this.death = death;
        this.recover = recover;
        this.data = data;
        this.lat = lat;
        this.lon = lon;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("confirmed", confirmed);
        result.put("death", death);
        result.put("recover", recover);
        result.put("lat", lat);
        result.put("lon", lon);

        return result;
    }

}
