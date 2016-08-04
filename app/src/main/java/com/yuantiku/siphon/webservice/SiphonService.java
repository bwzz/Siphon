package com.yuantiku.siphon.webservice;

import com.yuantiku.siphon.data.FileEntry;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * @author wanghb
 * @date 16/8/4.
 */
public interface SiphonService {
    @GET("/android/{id}/{type}")
    Observable<List<FileEntry>> listFiles(@Path("id") String id, @Path("type") String type);
}
