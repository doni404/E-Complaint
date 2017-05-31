package com.example.donisaurus.ecomplaint.model;

/**
 * Created by Donisaurus on 12/27/2016.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPostResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("data")
    @Expose
    private List<ModelPost> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<ModelPost> getData() {
        return data;
    }

    public void setData(List<ModelPost> data) {
        this.data = data;
    }
}
