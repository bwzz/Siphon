package com.yuantiku.siphon.webservice;

import com.yuantiku.siphon.data.FileConverter;
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

    public static FileDownloadService createDownloadService(String targetFilePath) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(EndPoint)
                .setLogLevel(LogLevel.BASIC)
                .setConverter(new FileConverter(targetFilePath))
                .build();
        return restAdapter.create(FileDownloadService.class);
    }
}
