package com.example.bookmamagement;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserProfile implements Serializable {
    private String id, hoten, sdt, email, diachi, ngaysinh, fileNameAvatar;

    public UserProfile() {
    }

    public UserProfile(String id, String hoten, String sdt, String email, String diachi, String ngaysinh, String fileNameAvatar) {
        this.id = id;
        this.hoten = hoten;
        this.sdt = sdt;
        this.email = email;
        this.diachi = diachi;
        this.ngaysinh = ngaysinh;
        this.fileNameAvatar = fileNameAvatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }


    public String getFileNameAvatar() {
        return fileNameAvatar;
    }

    public void setFileNameAvatar(String fileNameAvatar) {
        this.fileNameAvatar = fileNameAvatar;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("hoten", hoten);
        result.put("sdt", sdt);
        result.put("ngaysinh", ngaysinh);
        result.put("diachi", diachi);
        result.put("fileNameAvatar", fileNameAvatar);
        return result;
    }
}
