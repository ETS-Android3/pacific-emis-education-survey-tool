package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import io.reactivex.schedulers.Schedulers;

public class Transporter {

    private static final String TAG = Transporter.class.getName();
    private static final String MARK_END = "MSG_END";

    private final Listener listener;
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private ConnectionState connectionState;

    public Transporter(BluetoothSocket socket, Listener listener) {
        this.bluetoothSocket = socket;
        this.listener = listener;
        InputStream tempInputStream = null;
        OutputStream tempOutputStream = null;

        try {
            tempInputStream = socket.getInputStream();
            tempOutputStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp streams not created", e);
        }

        inputStream = tempInputStream;
        outputStream = tempOutputStream;
    }

    public void start() {
        Schedulers.newThread().scheduleDirect(() -> {
            Scanner inputScanner = new Scanner(inputStream).useDelimiter(MARK_END);
            Runnable disconnectRunnable = listener::onConnectionLost;
            while (connectionState == ConnectionState.CONNECTED) {
                if (!bluetoothSocket.isConnected()) {
                    disconnectRunnable.run();
                    break;
                }

                try {
                    String message = inputScanner.next();
                    Log.d(TAG, "<===\n" + message + "\n<===");
                    listener.onMessageObtain(message);
                } catch (NoSuchElementException noElementException) {
                    Log.d(TAG, "<===\n" + "NoSuchElementException" + "\n<===");
                    disconnectRunnable.run();
                } catch (IllegalStateException illegalStateException) {
                    Log.d(TAG, "<===\n" + "IllegalStateException" + "\n<===");
                    disconnectRunnable.run();
                }
            }
        });
    }

    public void write(String message) {
        Schedulers.newThread().scheduleDirect(() -> {
            try {
                String formattedMessage = message + MARK_END;
                Log.d(TAG, "===>\n" + formattedMessage + "\n===>");
                outputStream.write(formattedMessage.getBytes());
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        });
    }

    public void end() {
        try {
            inputStream.close();
            outputStream.close();
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

    public synchronized void setState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public interface Listener {
        void onMessageObtain(String message);

        void onConnectionLost();
    }
}
