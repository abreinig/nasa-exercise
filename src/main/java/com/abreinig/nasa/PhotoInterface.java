package com.abreinig.nasa;

import com.abreinig.nasa.model.NasaPhotoModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PhotoInterface {

    @GET("rovers/{rovername}/photos")
    Call<NasaPhotoModel> getNasaPhotoModel(@Path("rovername") String rover_name,
                                           @Query("earth_date") String earth_date,
                                           @Query("api_key") String api_key);

  //todo: manifest?
}
