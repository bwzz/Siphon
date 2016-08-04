package com.yuantiku.siphon.webservice;

import com.yuantiku.siphon.data.HtmlToFileEntriesConverter;
import com.yuantiku.siphon.helper.ZhenguanyuPathHelper;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

/**
 * @author wanghb
 * @date 15/8/15.
 */
public class ServiceFactory {
    private final static String EndPoint = ZhenguanyuPathHelper.getEndpoint();

    public static ZhenguanyuService createZhenguanyuService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(EndPoint)
                .setLogLevel(LogLevel.BASIC)
                .setConverter(new HtmlToFileEntriesConverter())
                .build();
        return restAdapter.create(ZhenguanyuService.class);
    }

    public static SiphonService createSiphonService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(EndPoint + ":3000")
                .setLogLevel(LogLevel.BASIC)
                .build();
        return restAdapter.create(SiphonService.class);
    }

}
