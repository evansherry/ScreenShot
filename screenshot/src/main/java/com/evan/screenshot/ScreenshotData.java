package com.evan.screenshot;

/**
 * Created by Evan on 2018/1/19.
 */

public class ScreenshotData {
    private String fileName;
    private String path;

    public ScreenshotData(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }
}
