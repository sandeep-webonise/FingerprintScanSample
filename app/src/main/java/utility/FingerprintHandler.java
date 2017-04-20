package utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.applications.sample.fingerprintautenticationsample.R;


@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private CancellationSignal cancellationSignal;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    public void stopListening() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Fingerprint Authentication error")
                .append("\n")
                .append(errString);

        switch (errMsgId) {
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                stringBuffer.append("\n\n")
                        .append("Clear your app cache and try again");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT
                    | FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS
                    | FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                stringBuffer.append("\n\n")
                        .append("Please Clean the sensor and your finger and try again");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                stringBuffer.append("\n\n")
                        .append("Partial Finger print acquired. Please press firmly on sensor");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                stringBuffer.append("\n \n")
                        .append("Close the app and open again");
                break;
            default:
                break;

        }
        this.update(stringBuffer.toString(), false);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.\n\n Please try again!", false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }

    private void update(String e, Boolean success) {
        TextView textView = (TextView) ((Activity) context).findViewById(R.id.tvMessage);
        textView.setText(e);
        if (success) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.textPrimaryDark));
        }
    }
}
