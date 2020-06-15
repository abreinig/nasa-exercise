
package com.abreinig.nasa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NasaPhotoModel implements Serializable
{

    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    private final static long serialVersionUID = -3594424240862173836L;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

}
