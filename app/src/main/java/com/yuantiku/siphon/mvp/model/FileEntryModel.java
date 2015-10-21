package com.yuantiku.siphon.mvp.model;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.AbstractCursor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.reflect.TypeToken;
import com.yuantiku.siphon.data.FileEntry;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.helper.JsonHelper;
import com.yuantiku.siphon.helper.SprinklesHelper;
import com.yuantiku.siphon.mvp.imodel.IFileEntryModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by wanghb on 15/9/4.
 */
public class FileEntryModel implements IFileEntryModel {

    private static final String Authority = "siphon.yuantiku.com";
    private static final String FileEntriesType = "vnd.android.cursor.dir/fileEntries.siphon.yuantiku.com";
    private static final String FileEntryType = "vnd.android.cursor.item/fileEntry.siphon.yuantiku.com";

    private static final Uri FileEntryLatestUri = Uri.parse("content://" + Authority
            + "/fileEntries/latest");
    private static final Uri FileEntriesUri = Uri.parse("content://" + Authority + "/fileEntries");

    private static final String FileEntryContent = "FileEntryContent";
    private static final String FileEntriesContent = "FileEntriesContent";
    private static final String FileEntryApkConfig = "FileEntryApkConfig";

    private ContentResolver contentResolver;

    @Inject
    public FileEntryModel(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    public void registerContentObserver(ContentObserver observer) {
        contentResolver.registerContentObserver(FileEntriesUri, false, observer);
    }

    public void unregisterContentObserver(ContentObserver observer) {
        contentResolver.unregisterContentObserver(observer);
    }

    @Override
    public void updateAll(List<FileEntry> fileEntries, ApkConfig apkConfig) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FileEntryApkConfig, JsonHelper.json(apkConfig));
        contentValues.put(FileEntriesContent, JsonHelper.json(fileEntries));
        contentResolver.insert(FileEntriesUri, contentValues);
    }

    @Override
    public List<FileEntry> list(ApkConfig apkConfig) {
        List<FileEntry> fileEntries = new ArrayList<>();
        Cursor cursor = contentResolver.query(FileEntriesUri, null, getApkConfigSelection(),
                getApkConfigSelectionArgs(apkConfig), null);
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(1);
                FileEntry fileEntry = JsonHelper.json(content, FileEntry.class);
                fileEntries.add(fileEntry);
            } while (cursor.moveToNext());
        }
        return fileEntries;
    }

    @Override
    public FileEntry getLatest(ApkConfig apkConfig) {
        Cursor cursor = contentResolver.query(FileEntryLatestUri, null, getApkConfigSelection(),
                getApkConfigSelectionArgs(apkConfig), null);
        if (cursor.moveToFirst()) {
            String content = cursor.getString(1);
            FileEntry fileEntry = JsonHelper.json(content, FileEntry.class);
            return fileEntry;
        }
        return null;
    }

    private static String getApkConfigIdentify(ApkConfig apkConfig) {
        return String.format("%d-%s", apkConfig.getId(), apkConfig.getType().name());
    }

    private static String[] getApkConfigSelectionArgs(ApkConfig apkConfig) {
        return new String[] {
                String.valueOf(apkConfig.getId()), String.valueOf(apkConfig.getType())
        };
    }

    private static String getApkConfigSelection() {
        return String.format("select * from %s where %s = ? and %s = ? ", FileEntry.FileEntries,
                FileEntry.ApkConfigId, FileEntry.ApkConfigType);
    }

    // TODO : persist data with sqlite
    public static class FileEntryProvider extends ContentProvider {

        private final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        private List<FileEntry> fileEntries = new ArrayList<>();

        private ContentResolver contentResolver;

        @Override
        public boolean onCreate() {
            contentResolver = getContext().getContentResolver();
            uriMatcher.addURI(Authority, "fileEntries/latest", 1);
            uriMatcher.addURI(Authority, "fileEntries", 2);
            return true;
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                String sortOrder) {
            if (uriMatcher.match(uri) == 1) {
                FileEntry fileEntry = Query.one(FileEntry.class, selection, selectionArgs).get();
                List<FileEntry> fileEntries = new LinkedList<>();
                if (fileEntry != null) {
                    fileEntries.add(fileEntry);
                }
                return new FileEntryCursor(fileEntries);
            } else {
                List<FileEntry> fileEntries = Query.many(FileEntry.class, selection, selectionArgs)
                        .get().asList();
                return new FileEntryCursor(fileEntries);
            }
        }

        @Override
        public String getType(Uri uri) {
            switch (uriMatcher.match(uri)) {
                case 1:
                    return FileEntryType;
                case 2:
                    return FileEntriesType;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public Uri insert(Uri uri, ContentValues values) {
            if (values.containsKey(FileEntriesContent) && values.containsKey(FileEntryApkConfig)) {
                String content = values.getAsString(FileEntriesContent);
                ApkConfig apkConfig = JsonHelper.json(values.getAsString(FileEntryApkConfig),
                        ApkConfig.class);
                ModelList.from(
                        Query.many(FileEntry.class, getApkConfigSelection(),
                                getApkConfigSelectionArgs(apkConfig)).get()).deleteAll();

                List<FileEntry> fileEntries = JsonHelper.jsonList(content,
                        new TypeToken<List<FileEntry>>() {
                        }.getType());
                SprinklesHelper.save(fileEntries);
            }
            contentResolver.notifyChange(uri, null);
            return uri;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            return 0;
        }

        @Override
        public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            return 0;
        }
    }

    private static class FileEntryCursor extends AbstractCursor {

        private List<FileEntry> fileEntries;

        private FileEntry currentFileEntry;

        FileEntryCursor(List<FileEntry> fileEntries) {
            this.fileEntries = fileEntries;
        }

        @Override
        public boolean onMove(int oldPosition, int newPosition) {
            currentFileEntry = fileEntries.get(newPosition);
            return super.onMove(oldPosition, newPosition);
        }

        @Override
        public int getCount() {
            return fileEntries.size();
        }

        @Override
        public String[] getColumnNames() {
            return new String[] {
                    FileEntryContent
            };
        }

        @Override
        public String getString(int column) {
            return JsonHelper.json(currentFileEntry);
        }

        @Override
        public short getShort(int column) {
            return 0;
        }

        @Override
        public int getInt(int column) {
            return 0;
        }

        @Override
        public long getLong(int column) {
            return 0;
        }

        @Override
        public float getFloat(int column) {
            return 0;
        }

        @Override
        public double getDouble(int column) {
            return 0;
        }

        @Override
        public boolean isNull(int column) {
            return false;
        }
    }
}
