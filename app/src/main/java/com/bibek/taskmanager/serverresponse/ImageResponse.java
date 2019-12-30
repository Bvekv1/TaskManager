package com.bibek.taskmanager.serverresponse;

public class ImageResponse {
    private  String fileName;

    public ImageResponse(String fileName) {
        this.fileName = fileName;
    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String fileName) {
        this.fileName = fileName;
    }
}
