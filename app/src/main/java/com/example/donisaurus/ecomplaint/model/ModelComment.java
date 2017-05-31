package com.example.donisaurus.ecomplaint.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Donisaurus on 1/8/2017.
 */

public class ModelComment {
    @SerializedName("id_komentar")
    @Expose
    private Integer idKomentar;
    @SerializedName("id_post")
    @Expose
    private Integer idPost;
    @SerializedName("id_user_komentar")
    @Expose
    private Integer idUserKomentar;
    @SerializedName("komentar")
    @Expose
    private String komentar;
    @SerializedName("tanggal_komentar")
    @Expose
    private String tanggalKomentar;
    @SerializedName("user_yg_komen")
    @Expose
    private String userYgKomen;

    public Integer getIdKomentar() {
        return idKomentar;
    }

    public void setIdKomentar(Integer idKomentar) {
        this.idKomentar = idKomentar;
    }

    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
    }

    public Integer getIdUserKomentar() {
        return idUserKomentar;
    }

    public void setIdUserKomentar(Integer idUserKomentar) {
        this.idUserKomentar = idUserKomentar;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getTanggalKomentar() {
        return tanggalKomentar;
    }

    public void setTanggalKomentar(String tanggalKomentar) {
        this.tanggalKomentar = tanggalKomentar;
    }

    public String getUserYgKomen() {
        return userYgKomen;
    }

    public void setUserYgKomen(String userYgKomen) {
        this.userYgKomen = userYgKomen;
    }
}
