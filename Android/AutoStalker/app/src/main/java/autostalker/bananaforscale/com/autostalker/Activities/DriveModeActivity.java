package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.view.View.OnTouchListener;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

//import com.example.android.apis.R;

import autostalker.bananaforscale.com.autostalker.R;
import autostalker.bananaforscale.com.autostalker.Utils.CommonUtils;


public class DriveModeActivity extends Activity implements InputManager.InputDeviceListener  {

    private VrVideoView mVrVideoView;
    private static final String TAG = "GameControllerInput";
    private InputManager mInputManager;

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            getDirectionPressed(event);
            return true;
        } else {
            return false;
        }
    }

    // Implementation of InputManager.InputDeviceListener.onInputDeviceRemoved()
    @Override
    public void onInputDeviceRemoved(int deviceId) {
    }
    // Implementation of InputManager.InputDeviceListener.onInputDeviceAdded()
    @Override
    public void onInputDeviceAdded(int deviceId) {

    }
    // Implementation of InputManager.InputDeviceListener.onInputDeviceChanged()
    @Override
    public void onInputDeviceChanged(int deviceId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.drive_mode);

        //initJoystick();
        initViews();
    }

    public int getDirectionPressed(InputEvent event) {

        // If the input event is a MotionEvent, check its hat axis values.
        if (event instanceof MotionEvent) {

            // Use the hat axis value to find the D-pad direction
            MotionEvent motionEvent = (MotionEvent) event;

            /*if(motionEvent.getY() > 0) {
                CommonUtils.showMessage("Accion:", "Freno", this);
                return 0;
            }*/

//            CommonUtils.showMessage("Axis: Report", motionEvent.getX() + "|" + motionEvent.getY(), this.getContext());
            Double angle = CommonUtils.getAngleByXYAxis(motionEvent.getX(), motionEvent.getY() * -1);
            Double power = CommonUtils.getPowerPressByXYAxis(motionEvent.getX(), motionEvent.getY() * -1);

            CommonUtils.showMessage("Angle and power ", angle.toString() + "|" + power.toString(), this);
        }

        return 0; //directionPressed;
    }

    private void initViews() {
        mVrVideoView = (VrVideoView) findViewById(R.id.video_view);

        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        //String vidAddress = "http://192.168.0.114:8200/MediaItems/21.mp4";
        //String vidAddress = "http://192.168.0.114:8090?action=stream";
        //String vidAddress = "http://192.168.0.114:8081";
        //Uri vidUri = Uri.parse(vidAddress);

        try {
            VrVideoView.Options options = new VrVideoView.Options();
            options.inputType = VrVideoView.Options.TYPE_MONO;
            //mVrVideoView.loadVideo(vidUri, options);
            //mVrVideoView.loadVideoFromAsset("seaturtle.mp4", options);
        } catch( Exception /*IOException */ e ) {
            //Handle exception
        }

        // Mensaje

    }

}
