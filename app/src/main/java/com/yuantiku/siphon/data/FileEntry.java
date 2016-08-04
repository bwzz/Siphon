package com.yuantiku.siphon.data;

import android.support.annotation.NonNull;

import com.yuantiku.siphon.data.apkconfigs.ApkConfig;
import com.yuantiku.siphon.data.apkconfigs.ApkType;
import com.yuantiku.siphon.factory.SingletonFactory;

import proguard.annotation.KeepClassMemberNames;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.PrimaryKey;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * @author wanghb
 * @date 15/8/15.
 */
@KeepClassMemberNames
@Table(FileEntry.FileEntries)
public class FileEntry extends Model {

    public final static String FileEntries = "FileEntries";
    public final static String ApkConfigId = "apkConfigId";
    public final static String ApkConfigType = "apkConfigType";

    private ApkConfig apkConfig;

    @Column(ApkConfigId)
    private int apkConfigId;

    @Column(ApkConfigType)
    private String apkConfigType;

    @PrimaryKey
    @Column("href")
    public String href;

    @Column("name")
    public String name;

    @Column("date")
    public String date;

    public ApkConfig getApkConfig() {
        if (apkConfig == null) {
            ApkType type = ApkType.valueOf(apkConfigType);
            apkConfig = SingletonFactory.get().getApkConfigModel()
                    .getByIdAndType(apkConfigId, type);
        }
        return apkConfig;
    }

    public void setApkConfig(@NonNull ApkConfig apkConfig) {
        this.apkConfig = apkConfig;
        apkConfigId = apkConfig.getId();
        apkConfigType = apkConfig.getType().name();
    }

    @Override
    protected void beforeSave() {
        super.beforeSave();
        apkConfigId = apkConfig.getId();
        apkConfigType = apkConfig.getType().name();
    }

}
