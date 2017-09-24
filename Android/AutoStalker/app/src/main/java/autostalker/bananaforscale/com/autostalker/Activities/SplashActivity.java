package autostalker.bananaforscale.com.autostalker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;

import autostalker.bananaforscale.com.autostalker.MainActivity;
import autostalker.bananaforscale.com.autostalker.R;

/**
 * Created by Emiliano on 25/07/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        showDelay();
    }

    private void showDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2*1000);
    }
}
