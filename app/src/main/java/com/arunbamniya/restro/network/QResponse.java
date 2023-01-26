package com.arunbamniya.restro.network;

public class QResponse {
    String id , qr_data , qr_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQr_data() {
        return qr_data;
    }

    public void setQr_data(String qr_data) {
        this.qr_data = qr_data;
    }

    public String getQr_image() {
        return qr_image;
    }

    public void setQr_image(String qr_image) {
        this.qr_image = qr_image;
    }

    @Override
    public String toString() {
        return "QResponse{" +
                "id='" + id + '\'' +
                ", qr_data='" + qr_data + '\'' +
                ", qr_image='" + qr_image + '\'' +
                '}';
    }
}
