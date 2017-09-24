package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.view.View.OnTouchListener;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

//import com.example.android.apis.R;

import TCP.TcpClient;
import autostalker.bananaforscale.com.autostalker.Protocol.Movement;
import autostalker.bananaforscale.com.autostalker.R;
import autostalker.bananaforscale.com.autostalker.Utils.CommonUtils;


public class DriveModeActivity extends Activity implements InputManager.InputDeviceListener  {

    private VrVideoView mVrVideoView;
    private static final String TAG = "GameControllerInput";
    //private InputManager mInputManager;
    private TcpClient mTcpClient;

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


        new ConnectTask().execute("");

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
            double angle =  CommonUtils.getAngleByXYAxis(motionEvent.getX(), motionEvent.getY() *
                    -1);
            double power =  CommonUtils.getPowerPressByXYAxis(motionEvent.getX(), motionEvent.getY
                    () *
                    -1);

            Log.d("Angle and power ", String.valueOf( angle) + "|" + String.valueOf( power));

            if(mTcpClient.isConnected()) {
                if(angle > 135)
                    angle = 135;
                if(angle < 45)
                    angle = 45;

                power = power* 100;
                if(power > 100)
                    power = 100;

                Movement movement = new Movement();
                movement.angle = (int)angle;
                movement.power = (int)power;

                String message = movement.toJson();
                Log.d("sending",message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
            }
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

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    Log.d("TCP",message);
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }

                @Override
                public void connectionStablished() {
                    Log.d("TCP","Connection stablished");
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Log.d("progress","progressuptdate");
            //in the arrayList we add the messaged received from server
//            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
//            mAdapter.notifyDataSetChanged();
        }
    }

}
