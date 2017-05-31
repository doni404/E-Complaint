package com.example.donisaurus.ecomplaint.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ArikAchmad on 12/18/2015.
 */
public class ResponOnly {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("data")
    @Expose
    private ModelStatusInsert data;


    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The pesan
     */
    public String getPesan() {
        return pesan;
    }

    /**
     * @param pesan The pesan
     */
    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    /**
     *
     * @return
     *     The data
     */
    public ModelStatusInsert getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(ModelStatusInsert data) {
        this.data = data;
    }


}
