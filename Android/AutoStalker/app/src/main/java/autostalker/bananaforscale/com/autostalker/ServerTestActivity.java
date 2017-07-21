package autostalker.bananaforscale.com.autostalker;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;

import TCP.TcpServer;

/**
 * Created by Emiliano on 12/07/2017.
 */

public class ServerTestActivity extends Activity {

    TcpServer mTcpServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_test);

        String ip = getLocalIpAddress();
        Log.d("localip",ip);
        Button btnListen = (Button) findViewById(R.id.btn_listen);
        Button btnStop = (Button) findViewById(R.id.btn_stop);
        EditText txtLog = (EditText) findViewById(R.id.log_text);

//        Handler mHandler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Notification.MessagingStyle.Message message) {
//                // This is where you do your work in the UI thread.
//                // Your worker tells you in the message what to do.
//            }
//        };

        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testlog","testlog");
                mTcpServer = new TcpServer(new TcpServer.OnMessageReceived() {
                    @Override
                    public void messageReceived(final String message) {
                        Log.d("mensaje",message);


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                mTcpServer.start();
            }
        });
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        //return inetAddress.getHostAddress().toString();
                        String ip = inetAddress.getHostAddress().toString();
                        Log.d("localip",ip);
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("logueando", ex.toString());
        }
        return "";
    }
}
