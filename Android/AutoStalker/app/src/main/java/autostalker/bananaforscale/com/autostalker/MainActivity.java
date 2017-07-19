package autostalker.bananaforscale.com.autostalker;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
