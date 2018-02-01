package org.hugh.loader.bean;

import android.support.annotation.IntRange;

import java.io.Serializable;

import static org.hugh.loader.Constant.STATUS_FAIL;
import static org.hugh.loader.Constant.STATUS_PAUSE;
import static org.hugh.loader.Constant.STATUS_WAIT;


/**
 * download file.
 * you can get the download information from this.
 *
 * @author hugh
 * @since 2017/11/21 14:28
 */

public class LoadFile implements Serializable {
    public String id;//id.
    public String downloadUrl;//download url.
    public String filePath;//download file path.
    public long size;//file size.
    public long downloadMark;//record download location.
    @IntRange(from = STATUS_WAIT, to = STATUS_FAIL)
    public int downloadStatus = STATUS_PAUSE;//the status of the download information.
}