package org.hugh.loader.bean;


import java.io.File;
import java.io.Serializable;

/**
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class LoadInfo implements Serializable {
    public String url;//download url
    public File file;//download file
    public String action;//the action of the receiver

    public LoadInfo(String url, File file, String action) {
        this.url = url;
        this.file = file;
        this.action = action;
    }

    public String getId() {
        return url + file.getAbsolutePath();
    }
}
