package com.abreinig.nasa;

import com.abreinig.nasa.model.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PhotoOperations {
    final static Logger LOGGER = LoggerFactory.getLogger(PhotoOperations.class);

    public static void generatePhotosUrlListAndDownload(List<Photo> photoList) throws IOException {
        String rover_name = photoList.get(0).getRover().getName();
        String earth_date = photoList.get(0).getEarthDate();
        List<String> imgSrcList = new ArrayList<>();
        //Generate a list of all the image urls
        for (int i=0; i<photoList.size(); i++) {
            String imgUrl = photoList.get(i).getImgSrc();
            // Fix the URL, get correct URL from Header 'Location'
            imgUrl = finalURL(imgUrl);
            imgSrcList.add(imgUrl);
        }
        if (imgSrcList.get(0) == null) { return; }
        downloadImages(imgSrcList, earth_date, rover_name);
    }

    //Check for a 301 redirect, if found return the new location.
    private static String finalURL(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setInstanceFollowRedirects(false);
        con.connect();
        int statusCode = con.getResponseCode();
        if (statusCode == 200 ) {return url;}
        return con.getHeaderField("Location");
    }

    private static void downloadImages(List<String> imgUrlList, String earth_date, String rover_name) throws IOException {
        for (String imgUrl : imgUrlList) {
            URL url = new URL(imgUrl);
            String destName = "/tmp/" + earth_date + '/' + rover_name + imgUrl.substring(imgUrl.lastIndexOf("/"));
            File myFile = new File(destName);
            //Check if file already exists
            if (myFile.exists()) {
                LOGGER.info("Skip download, file already exists:"+ destName);
                continue;
            }
            //Create file path if needed
            if(!myFile.getParentFile().exists()) {
                myFile.getParentFile().mkdirs();
            }
            LOGGER.info("Saving image:"+ destName);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(destName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        }
    }

    public static List<String> readDatesFromFile() throws IOException {
        List<String> datesList = new ArrayList<>();
        Resource resource = new ClassPathResource("/imageDates.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String line;
        while( (line = reader.readLine()) != null) {
            datesList.add(line);
        }

        //Format the dates YYYY-MM-DD
        for (int i = 0; i < datesList.size(); i++) {
            LocalDate ld = DateUtility.formatDate(datesList.get(i));
            datesList.set(i, ld.toString());
        }
        return datesList;
    }
}
