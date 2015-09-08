package com.yuantiku.siphon.mvp.model;

import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.helper.ZhenguanyuPathHelper;
import com.yuantiku.siphon.mvp.imodel.IFileModel;
import com.yuantiku.siphon.mvp.imodel.IFileModelFactory;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by wanghb on 15/9/3.
 */
public class FileModelFactory implements IFileModelFactory {

    @Inject
    FileModelFactory() {

    }

    @Override
    public IFileModel createFileModel(String path) {
        return new DefaultFileModel(path);
    }

    @Override
    public IFileModel createFileModel(FileEntry fileEntry) {
        return createFileModel(ZhenguanyuPathHelper.createCachePath(fileEntry));
    }

    private static class DefaultFileModel implements IFileModel {

        private File file;

        public DefaultFileModel(String path) {
            file = new File(path);
        }

        @Override
        public boolean exists() {
            return file.exists();
        }

        @Override
        public String getPath() {
            return file.getPath();
        }

        @Override
        public boolean delete() {
            return file.delete();
        }
    }
}
