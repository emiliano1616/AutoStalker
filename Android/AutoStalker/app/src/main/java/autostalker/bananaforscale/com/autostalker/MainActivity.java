package autostalker.bananaforscale.com.autostalker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import autostalker.bananaforscale.com.autostalker.Activities.DriveModeActivity;
import autostalker.bananaforscale.com.autostalker.Activities.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        ImageView imgCar = (ImageView) findViewById(R.id.imgCar);


        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Abriendo modo conduccion",Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(v.getContext(),DriveModeActivity.class);
//        myIntent.putExtra("key", value); //Optional parameters
                v.getContext().startActivity(myIntent);
            }
        });


        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);

        ImageView imgLastTravel = (ImageView) findViewById(R.id.imgLastTravel);

        imgLastTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to read the contacts
                    }

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique

                    return;
                }

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),1);



//                Toast.makeText(v.getContext(),"Abriendo chat",Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(v.getContext(),ChatClientActivity.class);
//                v.getContext().startActivity(myIntent);
            }
        });



        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettings);

        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(),SettingsActivity.class);
                v.getContext().startActivity(myIntent);
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = newFacebookIntent(v.getContext().getPackageManager(),"https://www.facebook.com/proyectoautostalker/");
                startActivity(browserIntent);
            }
        });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();

                // MEDIA GALLERY
//                 String selectedImagePath = getPath(selectedImageUri);

                Log.d("stringgg",selectedImageUri.toString());


                Intent shareIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                shareIntent.setType("video/*");
                shareIntent.putExtra(
                        android.content.Intent.EXTRA_SUBJECT, "Autostalker");
                shareIntent.putExtra(
                        android.content.Intent.EXTRA_TITLE, "Autostalker");
                shareIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
                shareIntent
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                this.startActivity(Intent.createChooser(shareIntent,
                        selectedImageUri.toString()));


                // MEDIA GALLERY
//                String selectedImagePath = getPath(selectedImageUri);
//                Log.d("selectedImagePath",selectedImagePath);

//                if (selectedImagePath != null) {
//
//
//                }
            }
        }
    }


    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }



    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_test_bluetooth){
            Toast.makeText(this,"Abriendo bluetooth",Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(this,TestBluetoothActivity.class);
//        myIntent.putExtra("key", value); //Optional parameters
            this.startActivity(myIntent);
        }else if (id == R.id.action_chat) {
            Toast.makeText(this,"Abriendo chat",Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(this,ChatClientActivity.class);
//        myIntent.putExtra("key", value); //Optional parameters
            this.startActivity(myIntent);
        }else if (id == R.id.action_server) {
        Toast.makeText(this,"Abriendo chat",Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this,ServerTestActivity.class);
//        myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(myIntent);
    }


        return super.onOptionsItemSelected(item);
    }

}
