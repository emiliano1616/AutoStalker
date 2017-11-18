package TCP;

import android.util.Log;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by etortorelli on 5/31/2017.
 */

public class TcpClient {

    public static final String SERVER_IP = "192.168.1.15"; //your computer IP address
    //    public static final String SERVER_IP = "10.0.2.2"; //your computer IP address
    public static final int SERVER_PORT = 5555;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    private boolean mConnectionStablished = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    public boolean isConnected() {
        return mConnectionStablished && mRun;
    }

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        // send mesage that we are closing the connection
        sendMessage(Constants.CLOSED_CONNECTION + "Kazy");

        mRun = false;
        mConnectionStablished = false;
        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = null;

            try {
                socket = new Socket(serverAddr, SERVER_PORT);

                mConnectionStablished = true;
                mMessageListener.connectionStablished();
                Log.d("TCP Client", "connection successfull");

                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                Log.d("TCP Client", "2");

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // send login name
                Log.d("TCP Client", "3");
                //sendMessage(Constants.LOGIN_NAME+"Kazy");

                //in this while the client listens for the messages sent by the server
                while (mRun) {

                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.d("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                if (socket != null)
                    socket.close();
            }

        } catch (Exception e) {

            Log.d("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);

        public void connectionStablished();
    }
}
