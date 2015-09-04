package bwzz.activityCallback;

import android.util.SparseArray;

import java.util.Random;

/**
 * Created by wanghb on 15/9/4.
 */
public class RequestCodeGenerator {
    public static int generateRequestCode() {
        // Can only use lower 16 bits for requestCode
        return (new Random(System.currentTimeMillis()).nextInt()) & 0xffff;
    }

    public static int generateRequestCode(SparseArray<?> sparseArray) {
        int requestCode = generateRequestCode();
        while (sparseArray.get(requestCode) != null) {
            requestCode = generateRequestCode();
        }
        return requestCode;
    }
}
