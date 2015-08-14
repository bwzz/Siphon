package com.yuantiku.siphon.webservice;

import com.yuantiku.siphon.data.FileEntry;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author wanghb
 * @date 15/8/14.
 */
public interface ZhenguanyuService {

    @GET("/list")
    Observable<List<FileEntry>> listFiles(@Query("dir") String dir);
}
