package autostalker.bananaforscale.com.autostalker;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import autostalker.bananaforscale.com.autostalker.Interfaces.InputManagerCompat;

/**
 * Created by etortorelli on 5/17/2017.
 */

public class BluetoothTestView extends View implements InputManagerCompat.InputDeviceListener {

    private final InputManagerCompat mInputManager;


    public BluetoothTestView(Context context, AttributeSet attrs) {
        super(context,attrs);

        setFocusable(true);
        setFocusableInTouchMode(true);


        mInputManager = InputManagerCompat.Factory.getInputManager(this.getContext());
        mInputManager.registerInputDeviceListener(this, null);

        Log.d("aver","constructor");

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

                        Toast.makeText(getContext(),"IZQUIERDA",Toast.LENGTH_SHORT).show();
    //                    setHeadingX(-1);
    //                    mDPadState |= DPAD_STATE_LEFT;
    //                    handled = true;
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        Toast.makeText(getContext(),"Derecha",Toast.LENGTH_SHORT).show();


                        //                    setHeadingX(1);
    //                    mDPadState |= DPAD_STATE_RIGHT;
    //                    handled = true;
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        Toast.makeText(getContext(),"arriba",Toast.LENGTH_SHORT).show();


                        //                    setHeadingY(-1);
    //                    mDPadState |= DPAD_STATE_UP;
    //                    handled = true;
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        Toast.makeText(getContext(),"abajo",Toast.LENGTH_SHORT).show();


                        //                    setHeadingY(1);
    //                    mDPadState |= DPAD_STATE_DOWN;
    //                    handled = true;
                        break;
                    default:
                        Toast.makeText(getContext(),"otra cosa",Toast.LENGTH_SHORT).show();


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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Reset the game when the view changes size.
//        reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Update the animation
//        animateFrame();

        // Draw the ships.
//        int numShips = mShips.size();
//        for (int i = 0; i < numShips; i++) {
//            Ship currentShip = mShips.valueAt(i);
//            if (currentShip != null) {
//                currentShip.draw(canvas);
//            }
//        }
//
//        // Draw bullets.
//        int numBullets = mBullets.size();
//        for (int i = 0; i < numBullets; i++) {
//            final Bullet bullet = mBullets.get(i);
//            bullet.draw(canvas);
//        }
//
//        // Draw obstacles.
//        int numObstacles = mObstacles.size();
//        for (int i = 0; i < numObstacles; i++) {
//            final Obstacle obstacle = mObstacles.get(i);
//            obstacle.draw(canvas);
//        }
    }


    /*
   * When an input device is added, we add a ship based upon the device.
   * @see
   * com.example.inputmanagercompat.InputManagerCompat.InputDeviceListener
   * #onInputDeviceAdded(int)
   */
    @Override
    public void onInputDeviceAdded(int deviceId) {

//        getShipForId(deviceId);
    }

    /*
     * This is an unusual case. Input devices don't typically change, but they
     * certainly can --- for example a device may have different modes. We use
     * this to make sure that the ship has an up-to-date InputDevice.
     * @see
     * com.example.inputmanagercompat.InputManagerCompat.InputDeviceListener
     * #onInputDeviceChanged(int)
     */
    @Override
    public void onInputDeviceChanged(int deviceId) {
//        Ship ship = getShipForId(deviceId);
//        ship.setInputDevice(InputDevice.getDevice(deviceId));
    }

    /*
     * Remove any ship associated with the ID.
     * @see
     * com.example.inputmanagercompat.InputManagerCompat.InputDeviceListener
     * #onInputDeviceRemoved(int)
     */
    @Override
    public void onInputDeviceRemoved(int deviceId) {

//        removeShipForID(deviceId);
    }

}
