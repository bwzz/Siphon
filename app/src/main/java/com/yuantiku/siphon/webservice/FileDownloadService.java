package com.yuantiku.siphon.webservice;

import java.io.File;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by wanghb on 15/8/16.
 */
public interface FileDownloadService {
    @GET("/list")
    Observable<File> get(@Query("dir") String dir);
}
