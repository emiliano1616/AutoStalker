package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.google.vr.sdk.widgets.video.VrVideoView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import TCP.TcpClient;
import autostalker.bananaforscale.com.autostalker.Protocol.Movement;
import autostalker.bananaforscale.com.autostalker.R;
import autostalker.bananaforscale.com.autostalker.Utils.CommonUtils;

//import com.example.android.apis.R;


public class LastDriveActivity extends Activity implements InputManager.InputDeviceListener  {

    private VrVideoView mVrVideoView;
    private static final String TAG = "GameControllerInput";
    //private InputManager mInputManager;
    private TcpClient mTcpClient;

    String lastDriveFileName = "final.mp4";
    String ip_raspberry = "192.168.1.108";

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

        //new ConnectTask().execute("");

        //initJoystick();
        //initViews();
        try {
            downloadAndSaveFile(ip_raspberry, "pi", "raspberry");
            initViews();
        }catch (Exception ex) {
        }
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

        //String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        String vidAddress = getFilesDir().toString() + "/Autostalker/" + lastDriveFileName;
        //String vidAddress = "http://192.168.0.114:8200/MediaItems/21.mp4";
        //String vidAddress = "http://192.168.0.15:8090/?action=stream";
        //String vidAddress = "http://192.168.0.114:8081";
        Uri vidUri = Uri.parse(vidAddress);

        try {
            VrVideoView.Options options = new VrVideoView.Options();
            options.inputType = VrVideoView.Options.TYPE_MONO;
            mVrVideoView.loadVideo(vidUri, options);
            //mVrVideoView.loadVideoFromAsset("seaturtle.mp4", options);
        } catch( Exception /*IOException */ e ) {
            //Handle exception
            String ex = e.getMessage();
        }

        String ex = "lala";

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
                    Log.d("TCP","Connection established");
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

    private Boolean downloadAndSaveFile(String server, /*int portNumber,*/
                                        String user, String password/*, String filename, File localFile*/)
            throws IOException {
        FTPClient ftp = null;

        try {
            ftp = new FTPClient();
            ftp.connect(server/*, portNumber*/);
            File localFile;
            //Log.d(LOG_TAG, "Connected. Reply: " + ftp.getReplyString());

            ftp.login(user, password);
            //Log.d(LOG_TAG, "Logged in");
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //Log.d(LOG_TAG, "Downloading");
            ftp.enterLocalPassiveMode();

            OutputStream outputStream = null;
            boolean success = false;

            try {
                //String lastDriveFile = Environment.getExternalStorageDirectory()+ "/lastAutoStalkerDrive.mp4";

                String intStorageDirectory = getFilesDir().toString();

                File public_file = new File(intStorageDirectory /*Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)*/, "Autostalker");
                boolean result = public_file.mkdir();

                //File file = new File(lastDriveFile);

                File fileContent = new File(public_file.getAbsolutePath() + "/" + lastDriveFileName);
                //fileContent.delete();
                FileOutputStream out= new FileOutputStream(fileContent);
                boolean created = fileContent.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(fileContent);
                outputStream = new BufferedOutputStream(fileOutputStream);

                boolean fileexists3= fileContent.exists();
                success = ftp.retrieveFile("Downloads/" + lastDriveFileName, outputStream);

                boolean fileexists= fileContent.exists();
                boolean fileexists2= fileContent.exists();

            } catch(Exception ex1) {
                String exMessage = ex1.getMessage();
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return success;
        } catch(Exception ex1) {
            String exMessage = ex1.getMessage();
            String exMessage2 = ex1.getMessage();
        }
        finally {
            if (ftp != null) {
                ftp.logout();
                ftp.disconnect();
            }
        }

        return false;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
