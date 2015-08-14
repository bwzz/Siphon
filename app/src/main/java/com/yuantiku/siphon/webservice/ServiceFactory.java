package com.yuantiku.siphon.webservice;

import com.yuantiku.siphon.data.HtmlToFileEntriesConverter;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

/**
 * @author wanghb
 * @date 15/8/15.
 */
public class ServiceFactory {
    public static ZhenguanyuService getService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://app.zhenguanyu.com")
                .setLogLevel(LogLevel.BASIC)
                .setConverter(new HtmlToFileEntriesConverter())
                .build();
        return restAdapter.create(ZhenguanyuService.class);
    }
}
