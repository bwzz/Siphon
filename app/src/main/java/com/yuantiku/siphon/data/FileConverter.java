package com.yuantiku.siphon.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by wanghb on 15/8/16.
 */
public class FileConverter implements Converter {
    private String targetFilePath;

    public FileConverter(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        File file = new File(targetFilePath);
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = body.in();
            outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw new ConversionException(e.getMessage());
        } finally {
            if (outputStream != null) {
                IOUtils.closeQuietly(outputStream);
            }
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
        return file;
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }
}
