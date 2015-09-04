package com.yuantiku.siphon.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yuantiku.siphon.mvp.imodel.IFileModel;

import java.io.File;

/**
 * Created by wanghb on 15/8/22.
 */
public class ApkHelper {
    public static void installApk(Context context, IFileModel fileModel) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(fileModel.getPath())),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
