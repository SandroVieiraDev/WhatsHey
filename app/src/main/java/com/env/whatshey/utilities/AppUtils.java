package com.env.whatshey.utilities;

import static com.env.whatshey.utilities.MaskEditUtils.unmask;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;

public class AppUtils {


    public static void openChatWhatsapp(Context context, String number) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("whatsapp://send?phone=+55" + unmask(number))));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void shareNumber(Context context, String number) {
        try {
            context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, unmask(number)), number));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void shareApplication(Context context) {
        String uri = (context.getPackageName());
        try {
            android.content.pm.PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(uri, android.content.pm.PackageManager.GET_ACTIVITIES);
            String apk = pi.applicationInfo.publicSourceDir;

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());

            context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND)
                    .setType("apk application/vnd.android.package-archive")
                    .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(apk))), "Whats Hey"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
