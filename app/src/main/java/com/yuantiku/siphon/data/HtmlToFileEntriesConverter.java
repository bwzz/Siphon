package com.yuantiku.siphon.data;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * @author wanghb
 * @date 15/8/15.
 */
public class HtmlToFileEntriesConverter implements Converter {

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        List<FileEntry> fileEntryList = new ArrayList<>();
        try {
            Document document = Jsoup.parse(body.in(), "UTF-8",
                    "https://app.zhenguanyu.com/");
            Elements items = document.select(".link-box");
            for (int i = 0; i < items.size(); ++i) {
                if (i < 2) {
                    continue;
                }
                Element element = items.get(i);
                FileEntry fileEntry = new FileEntry();
                fileEntry.href = element.attr("href");
                Elements divs = element.select("div");
                fileEntry.name = divs.get(0).data();
                fileEntry.date = divs.get(1).data();
                fileEntryList.add(fileEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileEntryList;
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }

}
