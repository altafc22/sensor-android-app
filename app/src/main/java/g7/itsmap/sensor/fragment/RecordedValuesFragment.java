package g7.itsmap.sensor.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import g7.itsmap.sensor.R;
import g7.itsmap.sensor.model.Record;

public class RecordedValuesFragment extends Fragment {

    public interface RecordedValuesFragmentListener {

        Record needRecord();
    }

    private RecordedValuesFragmentListener listener;
    private Record record;
    private TextView textTemperature;
    private TextView textHumidity;
    private TextView textInfo;

    public RecordedValuesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.record = this.listener.needRecord();
        View v = inflater.inflate(R.layout.fragment_recorded_values, container, false);
        this.textTemperature = (TextView) v.findViewById(R.id.textTemperature);
        this.textHumidity = (TextView) v.findViewById(R.id.textHumidity);
        this.textInfo = (TextView) v.findViewById(R.id.textStatus);

        this.displayRecord();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RecordedValuesFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement RecordedValuesFragmentListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        listener = null;
    }

    public void needsUpdate() {

        this.record = this.listener.needRecord();
        this.displayRecord();
    }

    private void displayRecord() {

        this.displayTemperature();
        this.displayHumidity();
        this.displayInfo();
    }
    private void displayTemperature() {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        this.textTemperature.setText(df.format(this.record.getTemperature()) + "°");
    }

    private void displayHumidity() {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        this.textHumidity.setText(df.format(this.record.getHumidity()) + "%");
    }

    private void displayInfo() {

        final double MAX_TEMPERATURE = Double.valueOf(PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("pref_doctor_max_temperature", "37"));

        final double MAX_HUMIDITY = Double.valueOf(PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("pref_doctor_max_humidity", "50"));

        if(this.record.getTemperature() > MAX_TEMPERATURE && this.record.getHumidity() > MAX_HUMIDITY) {

            this.textInfo.setText(R.string.recorded_values_both);

        } else if(this.record.getTemperature() > MAX_TEMPERATURE) {

            this.textInfo.setText(R.string.recorded_values_temperature);

        } else if(this.record.getHumidity() > MAX_HUMIDITY) {

            this.textInfo.setText(R.string.recorded_values_humidity);

        } else {

            this.textInfo.setText(R.string.recorded_values_ok);
        }
    }
}
