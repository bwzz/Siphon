package com.yuantiku.siphon.helper;

import android.content.Context;

import com.yuantiku.siphon.data.FileEntry;

import java.util.List;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by wanghb on 15/10/11.
 */
public class SprinklesHelper {
    public static void init(Context context) {
        Sprinkles sprinkles = Sprinkles.init(context);
        Migration initialMigration = new Migration();
        initialMigration.createTable(FileEntry.class);
        sprinkles.addMigration(initialMigration);
    }

    public static void save(List<? extends Model> models) {
        if (models == null || models.isEmpty()) {
            return;
        }
        for (Model model : models) {
            model.save();
        }
    }

    public static void save(Model model) {
        if (model != null) {
            model.save();
        }
    }
}
