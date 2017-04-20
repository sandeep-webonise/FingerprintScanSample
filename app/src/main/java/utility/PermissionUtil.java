package utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {

    public static final int FINGER_PRINT_SCANNER = 1;

    public static boolean checkPermissionForFingerPrint(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT);
        if (result == PackageManager.PERMISSION_GRANTED) {
            result = ContextCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT);
        }
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
