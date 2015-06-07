package g7.itsmap.sensor.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import g7.itsmap.sensor.R;
import g7.itsmap.sensor.database.DatabaseRecordHelper;
import g7.itsmap.sensor.fragment.GoButtonFragment;
import g7.itsmap.sensor.fragment.InfoRecordFragment;
import g7.itsmap.sensor.fragment.LabelResultFragment;
import g7.itsmap.sensor.fragment.BackButtonFragment;
import g7.itsmap.sensor.fragment.RecordSpinnerFragment;
import g7.itsmap.sensor.fragment.RecordedValuesFragment;
import g7.itsmap.sensor.model.Record;
import g7.itsmap.sensor.notification.ReminderManager;
import g7.itsmap.sensor.service.SensorService;

public class RecordActivity extends AppCompatActivity implements GoButtonFragment.GoButtonFragmentListener, BackButtonFragment.BackButtonFragmentListener, RecordedValuesFragment.RecordedValuesFragmentListener {

    private GoButtonFragment goButtonFragment;
    private InfoRecordFragment infoRecordFragment;
    private RecordSpinnerFragment recordSpinnerFragment;
    private RecordedValuesFragment recordedValuesFragment;
    private LabelResultFragment labelResultFragment;
    private BackButtonFragment newRecordButtonFragment;
    private DatabaseRecordHelper database;
    private Record newRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(45, 43, 56)));

        this.database = new DatabaseRecordHelper(this);

        this.goButtonFragment = new GoButtonFragment();
        this.infoRecordFragment = new InfoRecordFragment();
        this.recordSpinnerFragment = new RecordSpinnerFragment();
       // this.recordedValuesFragment = new RecordedValuesFragment();
       // this.labelResultFragment = new LabelResultFragment();
       // this.newRecordButtonFragment = new BackButtonFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.go_button_container, this.goButtonFragment)
                .add(R.id.info_spin_container, this.infoRecordFragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Log.d("tag", "Go settings");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;

        } else if (id == R.id.action_history) {

            Log.d("tag", "Go history");
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickRecordButton(GoButtonFragment.GoButtonMode mode) {

        if(mode == GoButtonFragment.GoButtonMode.RECORDING) {

            //ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //NetworkInfo wifiInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            String ip = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("pref_sensor_ip_address", null);
            String port = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("pref_sensor_port", null);

            if(ip == null || port == null) {

                this.showDialogWithMessage(getString(R.string.error_no_sensor_configuration));
                this.goButtonFragment.reset();

            } else {

                this.startRecording();
            }
            /*if (wifiInfo.isConnected()) {



            } else {

                this.showRecord();
                this.showDialogWithMessage(getString(R.string.wifi_message));
            }*/

        } else {

            this.showRecord();
        }
    }

    @Override
    public void onClickBackButton() {

        this.backToRecordFragment();
    }

    @Override
    public Record needRecord() {

        return this.newRecord;
    }

    private void startRecording() {

        this.registerBroadcasts();

        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_plus_notification_forgot", false)) {

            ReminderManager.getInstance(getApplicationContext()).startReminded();
        }

        Intent i = new Intent(getApplicationContext(), SensorService.class);
        startService(i);
        this.showSpin();
    }

    private void registerBroadcasts() {

        LocalBroadcastManager.getInstance(this).registerReceiver(
                this.receiverErrorSensorService, new IntentFilter(SensorService.ERROR_SENSOR_SERVICE));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                this.receiverSuccessSensorService, new IntentFilter(SensorService.SUCCESS_SENSOR_SERVICE));
    }

    private void unregisterBroadcasts() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiverErrorSensorService);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiverSuccessSensorService);
    }

    private void showDialogWithMessage(String message) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title)
                .setMessage(message)
                .setNeutralButton(android.R.string.yes, null)
                .show();
    }

    private void backToRecordFragment() {

        getFragmentManager().beginTransaction()
                .remove(this.labelResultFragment)
                .remove(this.recordedValuesFragment)
                .remove(this.newRecordButtonFragment)
                .replace(R.id.go_button_container, this.goButtonFragment)
                .replace(R.id.info_spin_container, this.infoRecordFragment)
                .commit();
    }

    private void showRecord() {

        this.goButtonFragment.reset();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.info_spin_container, this.infoRecordFragment)
                .commit();
    }

    private void showSpin() {

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.info_spin_container, this.recordSpinnerFragment)
                .commit();
    }

    private void showResultRecord() {

        this.recordedValuesFragment = new RecordedValuesFragment();
        this.labelResultFragment = new LabelResultFragment();
        this.newRecordButtonFragment = new BackButtonFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.test, labelResultFragment)
                .replace(R.id.go_button_container, recordedValuesFragment)
                .replace(R.id.info_spin_container, newRecordButtonFragment)
                .commit();
    }

    private void saveRecord(double temperature, double humidity) {

        this.newRecord = this.database.insertRecord(temperature, humidity);
    }

    private BroadcastReceiver receiverErrorSensorService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            runOnUiThread(new Runnable(){
                public void run() {

                    unregisterBroadcasts();
                    showRecord();
                    String messageError = intent.getStringExtra(SensorService.ERROR_MESSAGE_INTENT);
                    showDialogWithMessage(messageError);
                }
            });
        }
    };

    private BroadcastReceiver receiverSuccessSensorService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            runOnUiThread(new Runnable(){
                public void run() {

                    double temperature = intent.getDoubleExtra(SensorService.TEMPERATURE_INTENT, 0);
                    double humidity = intent.getDoubleExtra(SensorService.HUMIDITY_INTENT, 0);
                    saveRecord(temperature, humidity);
                    showResultRecord();
                }
            });
        }
    };
}
