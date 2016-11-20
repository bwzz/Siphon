package com.yuantiku.siphon.webservice;

import com.yuantiku.siphon.data.AppVersion;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by wanghb on 16/11/20.
 */

public interface FirService {
    String BASE_URL = "http://api.fir.im";

    @GET("/apps/latest/{bundle}")
    Observable<AppVersion> getAppVersion(@Path("bundle") String bundle,
            @Query("api_token") String apiToken, @Query("type") String type);
}
