package org.hugh.loader;

/**
 * @author hugh
 * @since 2017/11/21 16:16
 */

public class Constant {
    //download status.
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_PREPARE = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_PAUSE = 3;
    public static final int STATUS_COMPLETE = 4;
    public static final int STATUS_FAIL = 5;

    //receiver data.
    public static final String DOWNLOAD_EXTRA = "downloadExtra";
}
