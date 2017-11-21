package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import TCP.TcpClient;
import autostalker.bananaforscale.com.autostalker.Protocol.Movement;
import autostalker.bananaforscale.com.autostalker.R;
import autostalker.bananaforscale.com.autostalker.Utils.CommonUtils;

//import com.example.android.apis.R;


public class LastDriveActivity extends Activity   {

    private Uri path;
    private VrVideoView mVrVideoView;
    private static final String TAG = "GameControllerInput";
    //private InputManager mInputManager;
    private TcpClient mTcpClient;

    String lastDriveFileName = "final.mp4";
    String ip_raspberry = "192.168.1.15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.last_drive_mode);

        //new ConnectTask().execute("");

//        initViews();
        try {
            downloadAndSaveFile(ip_raspberry, "pi", "raspberry");



            initViews();
        }catch (Exception ex) {
        }
    }

    private void initViews() {
        mVrVideoView = (VrVideoView) findViewById(R.id.video_view);

        //String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        String vidAddress = path.toString();//getFilesDir().toString() + "/Autostalker/" +
        // lastDriveFileName;
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


    public File createImageFile(String suffix) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = suffix + "_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                "." + suffix,         /* suffix */
                storageDir      /* directory */
        );

        return image;
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

                File fileContent = createImageFile("mp4");

//                String intStorageDirectory = getFilesDir().toString();
//
//                File public_file = new File(intStorageDirectory /*Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)*/, "Autostalker");
//                boolean result = public_file.mkdir();


//                File fileContent = new File(public_file.getAbsolutePath() + "/" + lastDriveFileName);

                FileOutputStream out= new FileOutputStream(fileContent);
                boolean created = fileContent.createNewFile();
//                String path = fileContent.getAbsolutePath();
                FileOutputStream fileOutputStream = new FileOutputStream(fileContent);
                outputStream = new BufferedOutputStream(fileOutputStream);

                success = ftp.retrieveFile("lastdrive/" + lastDriveFileName, outputStream);

                Uri uri = Uri.fromFile(fileContent);
                path = uri;
//                String path = fileContent.getPath();
//                String absolutePath = fileContent.getAbsolutePath();
//                String canonicalPath = fileContent.getCanonicalPath();
//                Log.d("path",path);
//                Log.d("absolutePath",absolutePath);
//                Log.d("canonicalPath",canonicalPath);
//                Log.d("uri",uri.toString());
//
//                Intent shareIntent = new Intent(
//                        android.content.Intent.ACTION_SEND);
//                shareIntent.setType("video/*");
//                shareIntent.putExtra(
//                        android.content.Intent.EXTRA_SUBJECT, "Autostalker");
//                shareIntent.putExtra(
//                        android.content.Intent.EXTRA_TITLE, "Autostalker");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                shareIntent
//                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                this.startActivity(Intent.createChooser(shareIntent,
//                        uri.toString()));


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
