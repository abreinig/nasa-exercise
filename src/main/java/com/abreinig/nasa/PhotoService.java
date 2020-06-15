package com.abreinig.nasa;

import com.abreinig.nasa.model.NasaPhotoModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PhotoService implements APIConfiguration{

    private PhotoInterface service;

    @Value("${nasaAPI.key}")
    private String nasaAPIKey;

    public PhotoService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PhotoInterface.class);
    }

    public String getNasaPhotoModelFromFile() throws IOException {
        List<String> roversList = new ArrayList<>(Arrays.asList("curiosity", "spirit", "opportunity"));
        List<String> datesFromFile = PhotoOperations.readDatesFromFile();
        //Make a call for each requested date. For each date we need to get photos from all 3 rovers if the exist

        for ( String date : datesFromFile) {
            for (String rover : roversList) {
                Call<NasaPhotoModel> retrofitCall = service.getNasaPhotoModel(rover, date, nasaAPIKey);
                Response<NasaPhotoModel> response = retrofitCall.execute();

                if (!response.isSuccessful()) {
                    throw new IOException(response.errorBody() != null
                            ? response.errorBody().string() : "Unknown Error");
                }

                if (!response.body().getPhotos().isEmpty()) {
                    PhotoOperations.generatePhotosUrlListAndDownload(response.body().getPhotos());
                }
            }
        }
        return "Success: Please check your /tmp directory.";
    }

    public String getNasaPhotoModelCustom(String rover_name, String earth_date) throws IOException {
        List<String> roversList = new ArrayList<>(Arrays.asList("curiosity", "spirit", "opportunity"));
        Call<NasaPhotoModel> retrofitCall = service.getNasaPhotoModel(rover_name, earth_date, nasaAPIKey);
        Response<NasaPhotoModel> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown Error");
        }

        if (!response.body().getPhotos().isEmpty()) {
            PhotoOperations.generatePhotosUrlListAndDownload(response.body().getPhotos());
        }
        return "Success: Please check your /tmp directory.";
    }
}
