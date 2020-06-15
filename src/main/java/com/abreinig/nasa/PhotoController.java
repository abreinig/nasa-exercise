package com.abreinig.nasa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @GetMapping("/photos")
    public String getPhotos() throws IOException {
        return photoService.getNasaPhotoModelFromFile();
    }

    @GetMapping("/photos/{rover_name}/{earth_date}")
    public String getPhotos(@PathVariable("rover_name") String rover_name,
                                    @PathVariable("earth_date") String earth_date) throws IOException {
        return photoService.getNasaPhotoModelCustom(rover_name, earth_date);
    }


}
