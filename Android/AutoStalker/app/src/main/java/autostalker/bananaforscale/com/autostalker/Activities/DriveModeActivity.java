package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.hardware.input.InputManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View.OnTouchListener;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegInputStream;
import com.github.niqdev.mjpeg.MjpegSurfaceView;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

//import com.example.android.apis.R;

import java.io.InputStream;

import TCP.TcpClient;
import autostalker.bananaforscale.com.autostalker.Constants;
import autostalker.bananaforscale.com.autostalker.Protocol.BatteryLevel;
import autostalker.bananaforscale.com.autostalker.Protocol.Movement;
import autostalker.bananaforscale.com.autostalker.Protocol.ObstacleDetected;
import autostalker.bananaforscale.com.autostalker.Protocol.Ping;
import autostalker.bananaforscale.com.autostalker.R;
import autostalker.bananaforscale.com.autostalker.Utils.CommonUtils;
import autostalker.bananaforscale.com.autostalker.Utils.MyMjpegSurfaceView;
import autostalker.bananaforscale.com.autostalker.Utils.MyVideoView;
import rx.Observable;
import rx.functions.Action1;
import rx.plugins.RxJavaErrorHandler;


public class DriveModeActivity extends Activity implements InputManager.InputDeviceListener {

    private MyMjpegSurfaceView mVideoView1;
    private MyMjpegSurfaceView mVideoView2;
    private static final String TAG = "GameControllerInput";
    //private InputManager mInputManager;
    private TcpClient mTcpClient;
    private Context context;

    private Button panel1;
    private Button panel2;
    private Button panel3;
    private Button panel4;
    private float offsetAngle;
    private int returnButton;

    SharedPreferences settings ;
    SharedPreferences.Editor editor;


    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
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

    public void setTimeout(final Runnable runnable, final int delay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    runOnUiThread(runnable);
                    //runnable.run();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        offsetAngle = settings.getFloat(Constants.CONTROLLER_SETTINGS_UP,0);
        returnButton = settings.getInt(Constants.CONTROLLER_SETTINGS_RETURN,-1);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.drive_mode);
        panel1 = (Button) findViewById(R.id.panel1);
        panel2 = (Button) findViewById(R.id.panel2);
        panel3 = (Button) findViewById(R.id.panel3);
        panel4 = (Button) findViewById(R.id.panel4);


        new ConnectTask().execute("");
        initViews();

        //initJoystick();
    }


    private void ProcessKeyDown(int keyCode) {

        Log.d("keyCode",String.valueOf(keyCode));
        Log.d("return button",String.valueOf(returnButton));
        Log.d("angleoffset",String.valueOf(offsetAngle));

        if(keyCode == returnButton) {
            Log.d("retu","Returnbutton pressed");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Handle DPad keys and fire button on initial down but not on
        // auto-repeat.
        Log.d("aver","onKeyDown");

//        int deviceId = event.getDeviceId();
//        if (deviceId != -1) {

//        Log.d("aver","event.getRepeatCount() " + String.valueOf(event.getRepeatCount()));

        if (event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    super.onBackPressed();
                    break;
                default:
//                    Toast.makeText(this,"otra cosa: " + String.valueOf(keyCode),Toast.LENGTH_SHORT).show();
                    ProcessKeyDown(keyCode);

                    //                    if (isFireKey(keyCode)) {
                    //                        fire();
                    //                        handled = true;
                    //                    }
                    break;
            }
            return true;
        }


//        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("aver","onKeyUp");

        int deviceId = event.getDeviceId();
        if (deviceId != -1) {
            //Do stuff
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }


    private boolean processing = false;

    public int getDirectionPressed(InputEvent event) {

        // If the input event is a MotionEvent, check its hat axis values.
        if (event instanceof MotionEvent) {

            // Use the hat axis value to find the D-pad direction
            MotionEvent motionEvent = (MotionEvent) event;

//            CommonUtils.showMessage("Axis: Report", motionEvent.getX() + "|" + motionEvent.getY(), this.getContext());
            double angle = CommonUtils.getAngleByXYAxis(motionEvent.getX(), motionEvent.getY() *
                    -1);
            double power = CommonUtils.getPowerPressByXYAxis(motionEvent.getX(), motionEvent.getY
                    () *
                    -1);

//            Log.d("Angle and power ", String.valueOf(angle) + "|" + String.valueOf(power));

            if (mTcpClient != null && mTcpClient.isConnected()) {
//            if (true) {
                power = power * 100;
                if (power > 100)
                    power = 100;

                double originalAngle = angle;

                if (angle <= 180) {

                    if (angle > 135)
                        angle = 135;
                    if (angle < 45)
                        angle = 45;
                    if (angle >= 45 && angle <= 90) {
                        angle -= 45;
                        angle = 45 - angle;
                        angle = angle * 100 / 45;
                    } else if (angle > 90 && angle <= 135) {
                        angle -= 90;
                        angle = angle * 100 / 45;
                        angle *= -1;
                    }

                } else {
                    power *= -1;

                    angle -= 180;

                    if (angle > 135)
                        angle = 135;
                    if (angle < 45)
                        angle = 45;
                    if (angle >= 45 && angle <= 90) {
                        angle -= 45;
                        angle = 45 - angle;
                        angle = angle * 100 / 45;
                        angle *= -1;

                    } else if (angle > 90 && angle <= 135) {
                        angle -= 90;
                        angle = angle * 100 / 45;
                    }
                }


                Movement movement = new Movement();
                movement.angle = (int) angle;
                movement.power = (int) power;

                String message = movement.toJson();
//                Log.d("sending", message);

//                sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message + "@");

//                    new CountDownTimer(500, 500) {
//
//                        public void onTick(long millisUntilFinished) {
//                        }
//
//                        public void onFinish() {
//                            processing = false;
//                        }
//                    }.start();
                }
            }
        }

        return 0; //directionPressed;
    }

    private void initViews() {

        mVideoView1 = (MyMjpegSurfaceView) findViewById(R.id.video_view1);
        mVideoView2 = (MyMjpegSurfaceView) findViewById(R.id.video_view2);

        int TIMEOUT = 5;


        Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
//                    Log.e("Error",throwable.getMessage());
            }
        };

        Observable<MjpegInputStream> toSubscribe = Mjpeg.newInstance()
                .open("http://192.168.1.15:8090/?action=stream", TIMEOUT);

        toSubscribe.subscribe(new Action1<MjpegInputStream>() {

            @Override
            public void call(MjpegInputStream inputStream) {

                mVideoView1.setSource(inputStream);
                mVideoView1.setDisplayMode(DisplayMode.BEST_FIT);
                mVideoView1.showFps(true);
            }
        }, onError);


        Mjpeg.newInstance()
                .open("http://192.168.1.15:8090/?action=stream", TIMEOUT)
                .subscribe(new Action1<MjpegInputStream>() {
                    @Override
                    public void call(MjpegInputStream inputStream) {
                        mVideoView2.setSource(inputStream);
                        mVideoView2.setDisplayMode(DisplayMode.BEST_FIT);
                        mVideoView2.showFps(true);
                    }
                }, onError);


    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {


            //we create a TCPClient object and
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    Log.d("TCP", message);


                    //this method calls the onProgressUpdate
                    publishProgress(message);

                    String[] messages = message.split("@");

//                    String msg = "{\n" +
//                            " messageType:7,\n" +
//                            " batteryLevel:90\n" +
//                            "}";
                    for (String msg :
                            messages) {
                        if (msg.equals(""))
                            continue;

                        Ping ping = Ping.fromJson(msg, Ping.class);
                        switch (ping.messageType.value) {
                            case 7:
                                BatteryLevel batteryLevel = BatteryLevel.fromJson(msg, BatteryLevel.class);
                                //todo some <code></code>
                                break;
                            case 6:
                                ObstacleDetected obstacle = ObstacleDetected.fromJson(msg,
                                        ObstacleDetected.class);

                                if (obstacle.side == 1) {
                                    showPanel(panel1, panel1Timer);
                                }
                                if (obstacle.side == 2) {
                                    showPanel(panel2, panel2Timer);
                                }
                                if (obstacle.side == 3) {
                                    showPanel(panel3, panel3Timer);
                                }
                                if (obstacle.side == 4) {
                                    showPanel(panel4, panel4Timer);
                                }
                                break;

                        }
                    }


                }

                @Override
                public void connectionStablished() {
                    Log.d("TCP", "Connection stablished");
                }
            });

            try {
                mTcpClient.run();
            } catch (Exception ex) {
                Toast.makeText(context, "Error al conectar al vehiculo", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        CountDownTimer panel1Timer;
        CountDownTimer panel2Timer;
        CountDownTimer panel3Timer;
        CountDownTimer panel4Timer;

        public void showPanel(final Button button, CountDownTimer timer) {

            if (button.getVisibility() == View.VISIBLE) {
//                timer.cancel();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setVisibility(View.VISIBLE);
                }

            });

            setTimeout(new Runnable() {
                @Override
                public void run() {
                    Log.d("PANELLL", "HACETE INVISIBLE MIERDAAAA");
                    button.setVisibility(View.INVISIBLE);
                }
            }, 3000);
//
//            timer = new CountDownTimer(3000, 3000) {
//
//                public void onTick(long millisUntilFinished) {
//                }
//
//                public void onFinish() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            button.setVisibility(View.INVISIBLE);
//
//                        }
//                    });
//                }
//            }.start();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }
    }

}
