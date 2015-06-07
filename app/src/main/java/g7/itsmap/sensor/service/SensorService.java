package g7.itsmap.sensor.service;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import g7.itsmap.sensor.R;

public class SensorService extends IntentService implements ISensorService {

    public static final String ERROR_SENSOR_SERVICE = "ERROR_SENSOR_SERVICE";
    public static final String SUCCESS_SENSOR_SERVICE = "SUCCESS_SENSOR_SERVICE";
    public static final String ERROR_MESSAGE_INTENT = "ERROR_MESSAGE_INTENT";
    public static final String TEMPERATURE_INTENT = "TEMPERATURE_INTENT";
    public static final String HUMIDITY_INTENT = "HUMIDITY_INTENT";
    private static final String SENSOR_REQUEST = "SENSOR_REQUEST";

    private Socket socket;
    private PrintWriter outServer;
    private BufferedReader inServer;

    public SensorService() {
        super("SensorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.connect();
        if(this.isConnectionOpened()) {

            this.getValuesFromSensor();
        }
    }

    @Override
    public void onDestroy() {

        try {

            if(this.inServer != null) {

                this.inServer.close();
            }

            if(this.outServer != null) {

                this.outServer.close();
            }

            if(this.socket != null) {

                this.socket.close();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void connect() {

        try {

            final String SERVER_PORT = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString("pref_sensor_port", "8000");
            final String SERVER_IP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString("pref_sensor_ip_address", "192.168.43.125");

            this.socket = new Socket(SERVER_IP, Integer.valueOf(SERVER_PORT));
            this.outServer = new PrintWriter(this.socket.getOutputStream(), true);
            this.inServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        } catch (IOException e) {

            this.updateActivityWithErrorMessage(getString(R.string.error_connection_sensor));
        }
    }

    private boolean isConnectionOpened() {

        if(this.socket != null) {

            return this.socket.isConnected();
        }

        return false;
    }


    private void updateActivityWithErrorMessage(String message) {

        Intent intent = new Intent(this.ERROR_SENSOR_SERVICE);
        intent.putExtra(this.ERROR_MESSAGE_INTENT, message);
        this.sendLocalBroadcast(intent);
    }

    private void updateActivityWithSuccess(double temperature, double humidity) {

        Intent intent = new Intent(this.SUCCESS_SENSOR_SERVICE);
        intent.putExtra(this.TEMPERATURE_INTENT, temperature);
        intent.putExtra(this.HUMIDITY_INTENT, humidity);
        this.sendLocalBroadcast(intent);
    }

    private void sendLocalBroadcast(Intent intent) {

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void getValuesFromSensor() {

        this.outServer.write(this.SENSOR_REQUEST);

        String responseLine;
        ArrayList<String> results = new ArrayList<>();
        try {

            while ((responseLine = this.inServer.readLine()) != null) {

                results.add(responseLine);
            }

        } catch (IOException e1) {

            this.updateActivityWithErrorMessage(getString(R.string.error_connection_sensor));
            return;
        }

        if(results.size() == 2) {

            String strTemperature = results.get(0);
            String strHumidity = results.get(1);
            double temperature = Double.valueOf(strTemperature);
            double humidity = Double.valueOf(strHumidity);

            this.updateActivityWithSuccess(temperature, humidity);

        } else {

            this.updateActivityWithErrorMessage(getString(R.string.error_connection_sensor));
        }
    }
}
