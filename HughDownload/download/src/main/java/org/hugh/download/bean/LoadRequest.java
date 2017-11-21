package org.hugh.download.bean;

import android.support.annotation.IntRange;

import java.io.Serializable;

/**
 * download request
 *
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class LoadRequest implements Serializable {
    public static final int LOAD_REQUEST = 0;//download
    public static final int PAUSE_REQUEST = 1;//pause

    @IntRange(from = LOAD_REQUEST, to = PAUSE_REQUEST)
    public int dictate;//you can use this dictate to control the download service.(load or pause)
    public LoadInfo downloadInfo;
}
