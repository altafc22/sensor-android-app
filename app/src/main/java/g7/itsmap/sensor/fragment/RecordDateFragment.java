package g7.itsmap.sensor.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import g7.itsmap.sensor.R;

public class RecordDateFragment extends Fragment {

    public interface RecordDateFragmentListener {

        public Date needsDate();
    }

    private RecordDateFragmentListener listener;
    private TextView textDate;

    public RecordDateFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_record_date, container, false);
        this.textDate = (TextView) v.findViewById(R.id.textRecordDateFragment);

        this.needsUpdate();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (RecordDateFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement RecordDateFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void needsUpdate() {

        Date date = this.listener.needsDate();
        String formattedString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(date);
        this.textDate.setText(formattedString);
    }
}
