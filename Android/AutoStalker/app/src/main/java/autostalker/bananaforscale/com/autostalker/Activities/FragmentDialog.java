package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Emiliano on 19/11/2017.
 */

public class FragmentDialog extends android.support.v4.app.DialogFragment {

    String lastDriveFileName = "final.mp4";
    String ip_raspberry = "192.168.0.14";
    private Uri path;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Quiere ver el video o compartirlo?")
                .setPositiveButton("Ver", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Abriendo ultimo viaje", Toast.LENGTH_SHORT)
                                .show();
                        Intent myIntent = new Intent(getContext(), LastDriveActivity
                                .class);
                        getContext().startActivity(myIntent);
                    }
                })
                .setNegativeButton("Compartir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            downloadAndSaveFile(ip_raspberry, "pi", "raspberry");
                        }catch (IOException ex) {

                        }
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
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
                String path = fileContent.getPath();
                String absolutePath = fileContent.getAbsolutePath();
                String canonicalPath = fileContent.getCanonicalPath();
                Log.d("path",path);
                Log.d("absolutePath",absolutePath);
                Log.d("canonicalPath",canonicalPath);
                Log.d("uri",uri.toString());

                Intent shareIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                shareIntent.setType("video/*");
                shareIntent.putExtra(
                        android.content.Intent.EXTRA_SUBJECT, "Autostalker");
                shareIntent.putExtra(
                        android.content.Intent.EXTRA_TITLE, "Autostalker");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                this.startActivity(Intent.createChooser(shareIntent,
                        uri.toString()));


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
}
