package autostalker.bananaforscale.com.autostalker.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import java.lang.Math;
import java.lang.ref.Reference;

import static android.R.attr.angle;
import static android.os.Build.VERSION_CODES.M;

public class CommonUtils {

    public static void showMessage(String title, String msg, Context context){

        //AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this.getContext());
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static double getAngleByXYAxis(float x, float y) {
        double angle = Math.atan(y/x);
        angle = Math.toDegrees(angle);

        if (x<0)
             angle += 180;
        if (y<0 && x>0)
            angle += 360;

        return angle;
        //return Math.toDegrees(angle);
    }

    public static double getPowerPressByXYAxis(float x, float y) {

        if(y < 0) {
            y = y * -1;
        }
        if(x < 0) {
            x = x * -1;
        }
        double hip = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        return hip; // / 1.41; // 1.41 the max  hip for 1,1 offset
    }
}
