package autostalker.bananaforscale.com.autostalker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import TCP.TcpClient;
import autostalker.bananaforscale.com.autostalker.Protocol.Movement;
import autostalker.bananaforscale.com.autostalker.Protocol.Ping;
import autostalker.bananaforscale.com.autostalker.Protocol.ReturnCommand;
import autostalker.bananaforscale.com.autostalker.Protocol.Settings;

import java.util.ArrayList;

public class ChatClientActivity extends Activity {

    private ListView mList;
    private ArrayList<String> arrayList;
    private ChatClientListAdapter mAdapter;
    private TcpClient mTcpClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_client_main);

        arrayList = new ArrayList<String>();

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button) findViewById(R.id.send_button);
        final Button btnConnect = (Button) findViewById(R.id.connect_button);
        Button btnSendMovement = (Button) findViewById(R.id.btnSendMovement);
        Button btnSendPing = (Button) findViewById(R.id.btnSendPing);
        Button btnSendReturn = (Button) findViewById(R.id.btnSendReturn);
        Button btnSendSettings = (Button) findViewById(R.id.btnSendSettings);

        btnSendMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Movement movement = new Movement();
                movement.angle = 1;
                movement.power = 22;
                String message = movement.toJson();

                //add the text in the arrayList
//                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
//                mAdapter.notifyDataSetChanged();
            }
        });

        btnSendPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ping movement = new Ping();
                String message = movement.toJson();

                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
            }
        });

        btnSendReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReturnCommand returnCmd = new ReturnCommand();
                String message = returnCmd.toJson();

                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
            }
        });

        btnSendSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Settings settings = new Settings();
                String message = settings.toJson();

                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
            }
        });

        //relate the listView from java to the one created in xml
        //mList = (ListView) findViewById(R.id.list);
        mAdapter = new ChatClientListAdapter(this, arrayList);
        //mList.setAdapter(mAdapter);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnConnect.getText().equals("Conectar")){
                    new ConnectTask().execute("");
                    btnConnect.setText("Desconectar");
                } else {
                    mTcpClient.stopClient();
                    mTcpClient = null;
                    // clear the data set
                    arrayList.clear();
                    // notify the adapter that the data set has changed.
                    mAdapter.notifyDataSetChanged();
                    btnConnect.setText("Conectar");

                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString();

                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // disconnect
        mTcpClient.stopClient();
        mTcpClient = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mTcpClient != null) {
            // if the client is connected, enable the connect button and disable the disconnect one
            menu.getItem(1).setEnabled(true);
            menu.getItem(0).setEnabled(false);
        } else {
            // if the client is disconnected, enable the disconnect button and disable the connect one
            menu.getItem(1).setEnabled(false);
            menu.getItem(0).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.connect:
                // connect to the server
                new ConnectTask().execute("");
                return true;
            case R.id.disconnect:
                // disconnect
                mTcpClient.stopClient();
                mTcpClient = null;
                // clear the data set
                arrayList.clear();
                // notify the adapter that the data set has changed.
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
                    Log.d("TCP","Connection stablished");
                }
            });


            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }
}