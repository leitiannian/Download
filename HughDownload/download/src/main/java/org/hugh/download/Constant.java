package org.hugh.download;

/**
 * @author hugh
 * @since 2017/11/21 16:16
 */

public class Constant {
    //download status.
    public static final int STATUS_PREPARE = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_PAUSE = 2;
    public static final int STATUS_COMPLETE = 3;
    public static final int STATUS_FAIL = 4;

    //receiver data.
    public static final String DOWNLOAD_EXTRA = "downloadExtra";
}
