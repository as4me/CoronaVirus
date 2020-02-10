package me.as4.coronavirus.service;

import me.as4.coronavirus.models.APIModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppCoronaService {

    // GET
    @GET("arcgis/rest/services/ncov_cases/FeatureServer/2/query?f=json&where=1%3D1&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=Confirmed%20desc&resultOffset=0&resultRecordCount=500&cacheHint=true")
    Call<APIModel> getData();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://services1.arcgis.com/0MSEUqKaxRlEPj5g/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}
