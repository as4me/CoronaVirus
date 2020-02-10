
package me.as4.coronavirus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("OBJECTID")
    @Expose
    private Long oBJECTID;
    @SerializedName("Country_Region")
    @Expose
    private String countryRegion;
    @SerializedName("Last_Update")
    @Expose
    private Long lastUpdate;
    @SerializedName("Lat")
    @Expose
    private Double lat;
    @SerializedName("Long_")
    @Expose
    private Double _long;
    @SerializedName("Confirmed")
    @Expose
    private Integer confirmed;
    @SerializedName("Deaths")
    @Expose
    private Integer deaths;
    @SerializedName("Recovered")
    @Expose
    private Integer recovered;

    public Long getOBJECTID() {
        return oBJECTID;
    }

    public void setOBJECTID(Long oBJECTID) {
        this.oBJECTID = oBJECTID;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

}
