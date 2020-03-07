package me.as4.coronavirus.models;

public class RecyclerModel {
    private String name, confirmed, deaths, recovered;
    public  RecyclerModel(){

    }

    public  RecyclerModel(String name, String confirmed,String deaths,String recovered){
            this.name = name;
            this.confirmed = confirmed;
            this.deaths = deaths;
            this.recovered = recovered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
}
