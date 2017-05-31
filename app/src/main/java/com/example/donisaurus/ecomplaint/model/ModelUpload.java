package com.example.donisaurus.ecomplaint.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Donisaurus on 1/8/2017.
 */

public class ModelUpload {
    @SerializedName("id_post")
    @Expose
    private Integer idPost;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;
    @SerializedName("kategori")
    @Expose
    private Integer kategori;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("waktu")
    @Expose
    private String waktu;
    @SerializedName("diskripsi")
    @Expose
    private String diskripsi;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("nama_lokasi")
    @Expose
    private String namaLokasi;
    @SerializedName("areaid")
    @Expose
    private Integer areaid;
    @SerializedName("suka")
    @Expose
    private Integer suka;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("komentar")
    @Expose
    private Integer komentar;
    @SerializedName("id_like")
    @Expose
    private Object idLike;

    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getDiskripsi() {
        return diskripsi;
    }

    public void setDiskripsi(String diskripsi) {
        this.diskripsi = diskripsi;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public Integer getSuka() {
        return suka;
    }

    public void setSuka(Integer suka) {
        this.suka = suka;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getKomentar() {
        return komentar;
    }

    public void setKomentar(Integer komentar) {
        this.komentar = komentar;
    }

    public Object getIdLike() {
        return idLike;
    }

    public void setIdLike(Object idLike) {
        this.idLike = idLike;
    }

    public Integer getKategori() {
        return kategori;
    }

    public void setKategori(Integer kategori) {
        this.kategori = kategori;
    }

    public Integer getAreaid() {
        return areaid;
    }

    public void setAreaid(Integer areaid) {
        this.areaid = areaid;
    }
}
