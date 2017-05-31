package com.example.donisaurus.ecomplaint.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * berisikan table post
 */

@Table(name = "ikimara_post")
public class ModelPostDB extends Model implements Serializable {

    @Column(name = "id_post")
    private int id_post;

    @Column(name = "id_user")
    private int id_user;

    @Column(name = "kategori")
    private int kategori;

    @Column(name = "user")
    private String user;

    @Column(name = "gambar")
    private String gambar;

    @Column(name = "waktu")
    private String waktu;

    @Column(name = "diskripsi")
    private String diskripsi;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "nama_lokasi")
    private String nama_lokasi;

    @Column(name = "areaid")
    private int areaid;

    @Column(name = "suka_count")
    private int suka;

    @Column(name = "komentar_count")
    private int komentar;


    public static List<ModelPostDB> getALl() {
        return new Select().from(ModelPostDB.class).execute();
    }

    public static void deleteRow(int id) {
        new Delete().from(ModelPostDB.class).where("id_post = ?", id).execute();
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNama_lokasi() {
        return nama_lokasi;
    }

    public void setNama_lokasi(String nama_lokasi) {
        this.nama_lokasi = nama_lokasi;
    }

    public int getSuka() {
        return suka;
    }

    public void setSuka(int suka) {
        this.suka = suka;
    }

    public int getKomentar() {
        return komentar;
    }

    public void setKomentar(int komentar) {
        this.komentar = komentar;
    }
}
