package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import autostalker.bananaforscale.com.autostalker.Constants;
import autostalker.bananaforscale.com.autostalker.Interfaces.InputManagerCompat;
import autostalker.bananaforscale.com.autostalker.Protocol.BaseProtocol;
import autostalker.bananaforscale.com.autostalker.Protocol.BatteryLevel;
import autostalker.bananaforscale.com.autostalker.Protocol.Ping;
import autostalker.bananaforscale.com.autostalker.R;
import autostalker.bananaforscale.com.autostalker.Utils.CommonUtils;

/**
 * Created by Emiliano on 20/07/2017.
 */

public class SettingsActivity extends Activity implements InputManagerCompat.InputDeviceListener  {
    private InputManagerCompat mInputManager;

    private final int WAITING_UP = 1;
    private final int WAITING_DOWN = 2;
    private final int WAITING_LEFT = 3;
    private final int WAITING_RIGHT = 4;
    private final int WAITING_RETURN = 5;

    private int waiting = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputManager = InputManagerCompat.Factory.getInputManager(this);
        mInputManager.registerInputDeviceListener(this, null);

        setContentView(R.layout.settings_main);

        ImageView imgWifi = (ImageView) findViewById(R.id.imgWifi);




        imgWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        ImageView imgController = (ImageView) findViewById(R.id.imgController);

        imgController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "{\n" +
                        " messageType:7,\n" +
                        " batteryLevel:90\n" +
                        "}";

                Ping ping = Ping.fromJson(msg,Ping.class);
                switch(ping.messageType.value){
                    case 7:
                        BatteryLevel batteryLevel = BatteryLevel.fromJson(msg,BatteryLevel.class);
                        //todo some <code></code>
                        break;
                    case 8:

                }


            }
        });

        RadioButton radioButton640 = (RadioButton) findViewById(R.id.radioButton640);
        RadioButton radioButton320 = (RadioButton) findViewById(R.id.radioButton320);

        radioButton320.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(Constants.VIDEO_SETTINGS_KEY,320);
                editor.commit();
//                Toast.makeText(v.getContext(),"320",Toast.LENGTH_LONG).show();
            }
        });


        radioButton640.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(Constants.VIDEO_SETTINGS_KEY,640);
                editor.commit();

//                Toast.makeText(v.getContext(),"640",Toast.LENGTH_LONG).show();
            }
        });


        SharedPreferences settings = getPreferences(0);
        Integer videoResolution = settings.getInt(Constants.VIDEO_SETTINGS_KEY,640);
        if(videoResolution == 640)
            radioButton640.setChecked(true);
        else
            radioButton320.setChecked(true);


    }



    private void ProcessKeyDown(int keyCode) {
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();

        switch (waiting) {
            case WAITING_UP:
                editor.putInt(Constants.CONTROLLER_SETTINGS_UP,keyCode);
                editor.commit();
                waiting = WAITING_DOWN;
                CommonUtils.showMessage("Presione abajo","Presione abajo",this);
                break;
            case WAITING_DOWN:
                editor.putInt(Constants.CONTROLLER_SETTINGS_DOWN,keyCode);
                editor.commit();
                waiting = WAITING_LEFT;
                CommonUtils.showMessage("Presione izquierda","Presione izquierda",this);
                break;
            case WAITING_LEFT:
                editor.putInt(Constants.CONTROLLER_SETTINGS_LEFT,keyCode);
                editor.commit();
                waiting = WAITING_RIGHT;
                CommonUtils.showMessage("Presione derecha","Presione derecha",this);
                break;
            case WAITING_RIGHT:
                editor.putInt(Constants.CONTROLLER_SETTINGS_RIGHT,keyCode);
                editor.commit();
                waiting = WAITING_RETURN;
                CommonUtils.showMessage("Presione el boton de retorno","Presione el boton de retorno",this);
                break;
            case WAITING_RETURN:
                editor.putInt(Constants.CONTROLLER_SETTINGS_RETURN,keyCode);
                editor.commit();
                waiting = 0;
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Handle DPad keys and fire button on initial down but not on
        // auto-repeat.
        Log.d("aver","onKeyDown");

//        int deviceId = event.getDeviceId();
//        if (deviceId != -1) {

        Log.d("aver","event.getRepeatCount() " + String.valueOf(event.getRepeatCount()));

        boolean handled = false;
        if (event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    ProcessKeyDown(keyCode);

                    break;
                case KeyEvent.KEYCODE_BACK:
                    super.onBackPressed();
                    break;
                default:
//                    Toast.makeText(this,"otra cosa: " + String.valueOf(keyCode),Toast.LENGTH_SHORT).show();

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


    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        Log.d("aver","onGenericMotionEvent");

        mInputManager.onGenericMotionEvent(event);

        // Check that the event came from a joystick or gamepad since a generic
        // motion event could be almost anything. API level 18 adds the useful
        // event.isFromSource() helper function.
        int eventSource = event.getSource();
        if ((((eventSource & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
                ((eventSource & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK))
                && event.getAction() == MotionEvent.ACTION_MOVE) {
            int id = event.getDeviceId();
            if (-1 != id) {
//                Ship curShip = getShipForId(id);
//                if (curShip.onGenericMotionEvent(event)) {
//                    return true;
//                }
            }
        }
        return super.onGenericMotionEvent(event);
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // Turn on and off animations based on the window focus.
        // Alternately, we could update the game state using the Activity
        // onResume()
        // and onPause() lifecycle events.


        if (hasWindowFocus) {
//            mLastStepTime = SystemClock.uptimeMillis();
            mInputManager.onResume();
        } else {
//            int numShips = mShips.size();
//            for (int i = 0; i < numShips; i++) {
//                Ship currentShip = mShips.valueAt(i);
//                if (currentShip != null) {
//                    currentShip.setHeading(0, 0);
//                    currentShip.setVelocity(0, 0);
//                    currentShip.mDPadState = 0;
//                }
//            }
            mInputManager.onPause();
        }

        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {

    }

    @Override
    public void onInputDeviceChanged(int deviceId) {
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {

    }


}
